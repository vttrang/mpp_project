package controllers;

import java.net.URL;
import java.util.*;

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

public class BookController implements Initializable {

	@FXML
	private TextField isbn;

	@FXML
	private TextField title;

	@FXML
	private ListView lvAuthors;

	private SessionFactory factory = HibernateUtils.getSessionFactory();
	private Session session = factory.getCurrentSession();

	public void initialize(URL location, ResourceBundle resources) {
		loadAuthor();
	}

	@FXML
	public void addBook(ActionEvent event) {
		addBook();
	}

	private void loadAuthor() {
		try {
			session = factory.getCurrentSession();
			session.getTransaction().begin();

			CriteriaBuilder cb = session.getCriteriaBuilder();
			CriteriaQuery<Author> cq = cb.createQuery(Author.class);
			Root<Author> rootEntry = cq.from(Author.class);
			CriteriaQuery<Author> all = cq.select(rootEntry);
			TypedQuery<Author> allQuery = session.createQuery(all);
			List<Author> authors = allQuery.getResultList();

			List<String> temp = new ArrayList<String>();
			for (Author author: authors) {
				String s = author.getFName() + " " + author.getLName() + " - " + author.getId();
				temp.add(s);
			}
			ObservableList<String> authorFirstNames = FXCollections.observableArrayList(temp);
			lvAuthors.setItems(authorFirstNames);

		} catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			session.close();
		}
	}

	private void addBook() {
		int isbn;
		String title;

		if (this.isbn.getText().isEmpty() || this.title.getText().isEmpty()) return;

		String selectedAuthor = (String) lvAuthors.getSelectionModel().getSelectedItem();
		String[] temp = selectedAuthor.split("-");
		String authorId = temp[1];
		Author author = this.getAuthor(authorId);

		if (null == author) return;
		isbn = Integer.parseInt(this.isbn.getText());
		title = this.title.getText();
		Set<Author> authors = new HashSet<Author>();
		authors.add(author);
		try {
			session = factory.getCurrentSession();
			session.getTransaction().begin();
			Book book = new Book();
			book.setIsbn(isbn);
			book.setTitle(title);
			book.addAuthor(authors);
			session.persist(book);
			session.getTransaction().commit();
			session.close();

		}  catch (Exception e) {
			e.printStackTrace();
			session.getTransaction().rollback();
		}
		finally {
			session.close();
		}
	}

	private Author getAuthor(String authorID) {
		Author author = new Author();
		try {
			if (authorID.isEmpty()) return null;

			session = factory.getCurrentSession();
			session.getTransaction().begin();
			String sql = "Select ck from " + Author.class.getName() +
					" ck WHERE id = " + authorID;
			Query<Author> query = session.createQuery(sql);
			List<Author> authors = query.getResultList();
			if (authors.size() < 1) {
				return null;
			}
			author = authors.get(0);
		} catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			session.close();
		}
		return author;

	}
}