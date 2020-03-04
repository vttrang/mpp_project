package controllers;

import entities.Book;
import entities.BookCopy;
import entities.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.util.Callback;
import libs.HibernateUtils;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.net.URL;
import java.util.ResourceBundle;

public class AdminController implements Initializable {
    
    public void initialize(URL location, ResourceBundle resources) {
        loadMember();
        loadBook();
        loadBookCopy();
    }

    @FXML
    private TableView<User> tbvMember;

    @FXML
    public TableColumn<User, String> firstName;

    @FXML
    public TableColumn<User, String> lastName;

    @FXML
    public TableColumn<User, String> phone;

    @FXML
    private TableView<Book> tbvBook;

    @FXML
    public TableColumn<Book, Integer> isbn;

    @FXML
    public TableColumn<Book, String> title;

    @FXML
    private TableView<BookCopy> tbvBookCopy;

    @FXML
    public TableColumn<BookCopy, Integer> isbnCopy;

    @FXML
    public TableColumn<BookCopy, String> availability;

    @FXML
    public TableColumn<BookCopy, String> lendableDay;

    private SessionFactory factory = HibernateUtils.getSessionFactory();
    private Session session = factory.getCurrentSession();

    @FXML
    public void newMember(ActionEvent event) {
        try {
            BorderPane root = (BorderPane) FXMLLoader.load(getClass().getResource("/views/MemberForm.fxml"));
            Scene scene = new Scene(root,1049,588);
            scene.getStylesheets().add(getClass().getResource("/assets/css/application.css").toExternalForm());
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.show();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
    @FXML
    public void reloadMember(ActionEvent event) {
        loadMember();
    }

    @FXML
    public void reloadBook(ActionEvent event) {
        loadBook();
    }

    @FXML
    public void reloadBookCopy(ActionEvent event) {
        loadBookCopy();
    }


    private void loadMember() {
        try {
            if(!session.isOpen()) {
                session = factory.getCurrentSession();
                session.getTransaction().begin();
            }
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<User> cq = cb.createQuery(User.class);
            Root<User> rootEntry = cq.from(User.class);
            CriteriaQuery<User> all = cq.select(rootEntry);

            TypedQuery<User> allQuery = session.createQuery(all);
            ObservableList<User> users = FXCollections.observableArrayList(allQuery.getResultList());

            firstName.setCellValueFactory(new PropertyValueFactory<User, String>("firstName"));
            lastName.setCellValueFactory(new PropertyValueFactory<User, String>("lastName"));
            phone.setCellValueFactory(new PropertyValueFactory<User, String>("phone"));
            tbvMember.setItems(users);
        } catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            session.close();
        }
    }

    private void loadBook() {
        try {
            if(!session.isOpen()) {
                session = factory.getCurrentSession();
                session.getTransaction().begin();
            }
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<Book> cq = cb.createQuery(Book.class);
            Root<Book> rootEntry = cq.from(Book.class);
            CriteriaQuery<Book> all = cq.select(rootEntry);

            TypedQuery<Book> allQuery = session.createQuery(all);
            ObservableList<Book> books = FXCollections.observableArrayList(allQuery.getResultList());

            isbn.setCellValueFactory(new PropertyValueFactory<Book, Integer>("isbn"));
            title.setCellValueFactory(new PropertyValueFactory<Book, String>("title"));
            
            tbvBook.setItems(books);
        } catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            session.close();
        }
    }

    private void loadBookCopy() {
        try {
            if(!session.isOpen()) {
                session = factory.getCurrentSession();
                session.getTransaction().begin();
            }
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<BookCopy> cq = cb.createQuery(BookCopy.class);
            Root<BookCopy> rootEntry = cq.from(BookCopy.class);
            CriteriaQuery<BookCopy> all = cq.select(rootEntry);

            TypedQuery<BookCopy> allQuery = session.createQuery(all);
            ObservableList<BookCopy> books = FXCollections.observableArrayList(allQuery.getResultList());

            isbnCopy.setCellValueFactory(new PropertyValueFactory<BookCopy, Integer>("copyId"));
            availability.setCellValueFactory(new PropertyValueFactory<BookCopy, String>("availability"));
            lendableDay.setCellValueFactory(new PropertyValueFactory<BookCopy, String>("lendableDay"));
            tbvBookCopy.setItems(books);
        } catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            session.close();
        }
    }
}
