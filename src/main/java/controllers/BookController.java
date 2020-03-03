package controllers;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
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

public class BookController implements Initializable {
	@FXML
	private Button addBook_btn_addBook;

	@FXML
	private Button addBook_btn_addBookCopy;

	@FXML
	private Button addBook_btn_done;

	@FXML
	private TextField addBook_tf_title;

	@FXML
	private TextField addBook_tf_isbn;

	@FXML
	private TextField addBookCopy_tf_isbn;

	@FXML
	private TextField addBookCopy_tf_quantity;

//	@FXML
//	private TableView addBook_lv_authorList;

	@FXML
	private RadioButton addBookCopy_rbtn_7d;

	@FXML
	private RadioButton addBookCopy_rbtn_21d;

	Session session;

	@FXML
	public void addBook(ActionEvent event) {
		SessionFactory factory = HibernateUtils.getSessionFactory();
		Session session = factory.getCurrentSession();
		Book aBook = new Book();
		List<BookCopy> listcopies = new ArrayList<BookCopy>();

		try {
			session.getTransaction().begin();

			BorderPane addCopy = (BorderPane) FXMLLoader.load(getClass().getResource("/views/AddBookCopy.fxml"));
			Scene scene = new Scene(addCopy,1049,588);
	        scene.getStylesheets().add(getClass().getResource("/assets/css/application.css").toExternalForm());
	        Stage stage = new Stage();
	        stage.setScene(scene);
	        stage.show();

			addBookCopy_rbtn_7d.setUserData("7");
			addBookCopy_rbtn_21d.setUserData("21");

			aBook = newBook();
			listcopies = newBookCopies();
			session.getTransaction().commit();
	        session.close();
		}	catch(Exception e) {
	        e.printStackTrace();
	        session.getTransaction().rollback();
	    }
	}

	public void addBookCopy(ActionEvent event) {
		addBook(event);
	}

	public void done(ActionEvent event) {
        Stage stage = (Stage) addBook_btn_done.getScene().getWindow();
        stage.close();
	}

	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub

	}

	private Book newBook() {
        if (this.addBook_tf_title.getText().isEmpty() || this.addBook_tf_isbn.getText().isEmpty()) {
            return null;
        }

        Book book = new Book();
        book.setTitle(addBook_tf_title.getText());
		book.setIsbn(Integer.valueOf(addBook_tf_isbn.getText()));

        this.session.persist(book);

        return book;
    }

    private ArrayList<BookCopy> newBookCopies() {
        if (this.addBookCopy_tf_quantity.getText().isEmpty()) {
            return null;
        }

        ArrayList<BookCopy> bcl = new ArrayList<BookCopy>();
		ToggleGroup lendable_day = new ToggleGroup();
		addBookCopy_rbtn_7d.setToggleGroup(lendable_day);
		addBookCopy_rbtn_7d.setSelected(true);
		addBookCopy_rbtn_21d.setToggleGroup(lendable_day);

        for (int i = 1; i < Integer.valueOf(addBookCopy_tf_quantity.getText()); i++){
			//start Add Book Copy view
			BookCopy bc = new BookCopy();
			bc.setAvailability(true);
			if (lendable_day.getSelectedToggle().getUserData().toString().equals("7")){
				bc.setLendableDay(7);
			}
			else if (lendable_day.getSelectedToggle().getUserData().toString().equals("21")){
				bc.setLendableDay(21);
			}
			bcl.add(bc);
        }

        this.session.persist(bcl);

        return bcl;

    }
}