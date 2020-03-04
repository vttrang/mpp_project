package controllers;

import java.awt.Checkbox;
import java.net.URL;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.List;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import org.hibernate.query.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import libs.HibernateUtils;
import entities.Address;
import entities.Credential;
import entities.Role;
import entities.User;
import entities.Book;
import entities.BookCopy;
import entities.Checkout;
import libs.PasswordMD5;
import libs.Utils;


public class CheckoutController implements Initializable {
	@FXML
	private Button submitCheckout;
	
	@FXML
	private TextField isbn;
	
	@FXML
	private TextField userId;
	
	@FXML
	private Text errorMessage;
	
	@FXML
	public void submitCheckout(ActionEvent event) {
		String message = "";
		
		if(!libs.Utils.isNumeric(isbn.getText())) {
			message += "ibsn must be number!\n";
		}
		
		if(!libs.Utils.isNumeric(userId.getText())) {
			message += "User Id must be number!";
		}
		
		if(message != "") {
			errorMessage.setText(message);
			return;
		}
		
		SessionFactory factory = HibernateUtils.getSessionFactory();
		Session session = factory.getCurrentSession();
		User user = null;
		try {
			session.getTransaction().begin();
			
			//load user
			user = session.get(User.class, Integer.parseInt(userId.getText()));
		
			if(user == null || !user.getRole().getId().equals(libs.Role.MEMBER.getCode())) {
				errorMessage.setText("User id did not found or User is not member!");
				session.close();
				return;
			}
			
			Book book = session.get(Book.class, Integer.parseInt(isbn.getText()));
			
			if(book == null) {
				errorMessage.setText("ISBN did not found!");
				session.close();
				return;
			}
			
			 String sql = "Select bc from " + BookCopy.class.getName() + 
					 " bc WHERE isbn = " + isbn.getText() +
					 " AND availability = " + "1";
			 Query<BookCopy> query = session.createQuery(sql);
			 List<BookCopy> bookCopys = query.getResultList();
			 
			 if(bookCopys.size() == 0) {
				errorMessage.setText("Book is not available!");
				session.close();
				return;
			 }
			 
			 BookCopy bookCopy = bookCopys.get(0);
			 
			 Checkout checkout = new Checkout();
			 checkout.setBookCopy(bookCopy);
			 checkout.setUser(user);
			 
			 Timestamp currentTime = new Timestamp(System.currentTimeMillis());
			 checkout.setCheckoutDate(currentTime);
			 
			 Calendar cal = Calendar.getInstance();
			 cal.setTime(currentTime);
			 cal.add(Calendar.DAY_OF_WEEK, bookCopy.getLendableDay());
			 Timestamp duteDate = new Timestamp(cal.getTime().getTime());
			 checkout.setDueDate(duteDate);
			 
			 session.persist(checkout);
			 
			 bookCopy = session.load(BookCopy.class, bookCopy.getCopyId());
			 bookCopy.setAvailability(0);
			 session.persist(bookCopy);
			 
			 session.getTransaction().commit();
			 session.close();
		} catch (Exception e) {
			e.printStackTrace();
			session.getTransaction().rollback();
		}
	}
	
	


	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
		
	}
}