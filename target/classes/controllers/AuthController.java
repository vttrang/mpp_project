package controllers;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

import org.hibernate.query.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import libs.HibernateUtils;
import entities.User;

public class AuthController implements Initializable {
	@FXML
   private Button submitLogin;
	
	
	@FXML
	public void submitLogin(ActionEvent event) {
		System.out.println("Button Clickgggged!");
		System.out.println("asdasd");
		SessionFactory factory = HibernateUtils.getSessionFactory();
		Session session = factory.getCurrentSession();
		User user = null;
		try {
			session.getTransaction().begin();
			
			//load user
			user =  session.load(User.class, 1);
			System.out.print(user.getId());
			System.out.print(user.getFirstName());
			
			//edit user
			user.setFirstName("Jacky");
			session.persist(user);
//			session.getTransaction().commit();
			
			//create New User
			User user2 = new User();
			user2.setFirstName("David");
			user2.setLastName("Tran");
			session.persist(user2);
			session.getTransaction().commit();
			
 
		} catch (Exception e) {
			e.printStackTrace();
			session.getTransaction().rollback();
		}
	}


	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
		
	}
}
