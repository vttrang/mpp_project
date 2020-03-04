package controllers;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import org.hibernate.query.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import libs.HibernateUtils;
import entities.Book;
import entities.BookCopy;
import entities.Role;
import entities.User;
import entities.Address;
import entities.Author;

import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

public class BookCopyController implements Initializable {

	@FXML
	private TextField isbn;
	@FXML
	private TextField quantity;

	@FXML
	private RadioButton lendable7Days;

	@FXML
	private Button done;

	private SessionFactory factory = HibernateUtils.getSessionFactory();
	private Session session = factory.getCurrentSession();

	public void initialize(URL location, ResourceBundle resources) {
	}

	@FXML
	public void addBookCopy(ActionEvent event) {
		try {
			int isbn = Integer.parseInt(this.isbn.getText());
			Book book = this.getBook(isbn);
			if (null == book) {
				System.out.println("Book is null");
				return;
			}
			int lendableDay = this.lendable7Days.isSelected() ? 7 : 21;
			
			if(!session.isOpen()) {
				session = factory.getCurrentSession();
				session.getTransaction().begin();
			}
			
			for(int i = 0; i < Integer.parseInt(this.quantity.getText()); i++){
				BookCopy bookCopy = new BookCopy();
				bookCopy.setBook(book);
				bookCopy.setAvailability(1);
				bookCopy.setLendableDay(lendableDay);
				session.persist(bookCopy);
			}
			
			session.getTransaction().commit();
			session.close();


		}  catch (Exception e) {
			e.printStackTrace();
			session.getTransaction().rollback();
		}
	}

	@FXML
	public void done(ActionEvent event) {
		Stage stage = (Stage) done.getScene().getWindow();
		stage.close();
	}

	private Book getBook (int isbn) {
		Book book = new Book();
		try {
			session = factory.getCurrentSession();
			session.getTransaction().begin();
			String sql = "Select ck from " + Book.class.getName() +
					" ck WHERE isbn = " + isbn;
			Query<Book> query = session.createQuery(sql);
			List<Book> books = query.getResultList();
			if (books.size() < 1) {
				return null;
			}
			book = books.get(0);
		} catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			session.close();
		}
		return book;
	}
}