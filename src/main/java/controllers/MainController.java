package controllers;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Callback;

import org.hibernate.query.Query;

import com.google.protobuf.Timestamp;

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

public class MainController implements Initializable {
	
	@FXML
	Button buttonOpenStageCheckout;
	
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
	Button btnSearch;
	
	@FXML
	Button btnLoadAll;
	
	@FXML
	Button newMember;
	
	@FXML
	Button addBook;
	
	@FXML
	Button addBookCopy;
	
	@FXML
	TextField inputSearchUserId;
	
	@FXML
	Text managerName;
	
	@FXML
	Pane librarianPane;
	
	@FXML
	TabPane AdminTabPane;
	
    @FXML
    public TableColumn<Book, String> title;
    
    @FXML
    public TableColumn<Book, Integer> isbnBook;

    @FXML
    private TableView<BookCopy> tbvBookCopy;

    @FXML
    public TableColumn<BookCopy, Integer> isbnCopy;

    @FXML
    public TableColumn<BookCopy, String> availability;

    @FXML
    public TableColumn<BookCopy, String> lendableDay;
	
	@FXML
	private TableView<Checkout> tbvCheckout;
	
	@FXML
    public TableColumn<Checkout, String> isbn;

    @FXML
    public TableColumn<Checkout, String> bookTitle;

    @FXML
    public TableColumn<Checkout, String> userName;
    
    @FXML
    public TableColumn<Checkout, Timestamp> checkoutDate;
    
    @FXML
    public TableColumn<Checkout, Timestamp> dueDate;
    
    @FXML
    public User user;
	
	private SessionFactory factory = HibernateUtils.getSessionFactory();
    private Session session = factory.getCurrentSession();
	
	@FXML
	public void openStageCheckout(ActionEvent event) {
	
		try {
	        BorderPane root = (BorderPane) FXMLLoader.load(getClass().getResource("/views/Checkout.fxml"));
	        Scene scene = new Scene(root,600,220);
	        scene.getStylesheets().add(getClass().getResource("/assets/css/application.css").toExternalForm());
	        Stage stage = new Stage();
	        stage.setScene(scene);
	        stage.show();
	    } catch(Exception e) {
	        e.printStackTrace();
	    }
	}
	
	@FXML
    public void reloadBookCopy(ActionEvent event) {
        loadBookCopy();
    }

	public void initialize(URL location, ResourceBundle resources) {
		loadCheckout();
	}
	
	private void loadCheckout() {
        try {
        	if(!session.isOpen()) {
                session = factory.getCurrentSession();
                session.getTransaction().begin();
            }
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<Checkout> cq = cb.createQuery(Checkout.class);
            Root<Checkout> rootEntry = cq.from(Checkout.class);
            CriteriaQuery<Checkout> all = cq.select(rootEntry);

            TypedQuery<Checkout> allQuery = session.createQuery(all);
            ObservableList<Checkout> checkouts = FXCollections.observableArrayList(allQuery.getResultList());
        	this.loadCheckoutWithData(checkouts);
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        session.close();
    }
	
	private void loadCheckoutWithData(ObservableList<Checkout> checkouts) {
		isbn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Checkout,String>, ObservableValue<String>>() {
            public ObservableValue<String> call(CellDataFeatures<Checkout, String> param) {
            	BookCopy bc = param.getValue().getBookCopy();
                return new SimpleStringProperty(bc.getBook().getIsbn().toString());
            }
        });
        
        bookTitle.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Checkout,String>, ObservableValue<String>>() {
            public ObservableValue<String> call(CellDataFeatures<Checkout, String> param) {
            	BookCopy bc = param.getValue().getBookCopy();
                return new SimpleStringProperty(bc.getBook().getTitle());
            }
        });
        
        userName.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Checkout,String>, ObservableValue<String>>() {
            public ObservableValue<String> call(CellDataFeatures<Checkout, String> param) {
            	User user = param.getValue().getUser();
                return new SimpleStringProperty(user.getFirstName() + " " + user.getLastName());
            }
        });
        
        checkoutDate.setCellValueFactory(new PropertyValueFactory<Checkout, Timestamp>("checkoutDate"));
        dueDate.setCellValueFactory(new PropertyValueFactory<Checkout, Timestamp>("dueDate"));
        
        tbvCheckout.setItems(checkouts);
	}
	
	@FXML
	public void searchCheckout(ActionEvent event) {
		
		String userId = inputSearchUserId.getText();
		if(userId.equals("")) {
			this.loadCheckout();
		} else  {
			if(!session.isOpen()) {
                session = factory.getCurrentSession();
                session.getTransaction().begin();
            }
	        
			String sql = "Select ck from " + Checkout.class.getName() + 
					 " ck WHERE user_id = " + userId;
			
			 Query<Checkout> query = session.createQuery(sql);
			 List<Checkout> Checkout21 = query.getResultList();
			 for(Checkout checkout : Checkout21) {
				 System.out.println(checkout.getId());
			 }
			 
			 
			 ObservableList<Checkout> checkouts = FXCollections.observableArrayList(query.getResultList());
			 
			 this.loadCheckoutWithData(checkouts);
		}
		session.close();
	}
	
	@FXML
	public void loadAllCheckout(ActionEvent event) {
		this.loadCheckout();
	}
	
	@FXML
    public void reloadBook(ActionEvent event) {
        loadBook();
    }
	
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
	
	public void setUser(User user){
	   this.user = user;
	   managerName.setText(this.user.getFirstName() + " " + this.user.getLastName());
	   
	   if(this.user.getRole().getId().equals(libs.Role.LIBRARIAN.getCode())) {
		   addBook.setVisible(false);
		   addBookCopy.setVisible(false);
		   newMember.setVisible(false);
		   AdminTabPane.setVisible(false);
	   } else if(this.user.getRole().getId().equals(libs.Role.ADMIN.getCode())) {
		   buttonOpenStageCheckout.setVisible(false);
		   librarianPane.setVisible(false);
	   }
	   
	}
	
	@FXML
    public void reloadMember(ActionEvent event) {
        loadMember();
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

            isbnBook.setCellValueFactory(new PropertyValueFactory<Book, Integer>("isbn"));
            title.setCellValueFactory(new PropertyValueFactory<Book, String>("title"));
            tbvBook.setItems(books);
        } catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            session.close();
        }
    }
	
	@FXML
    public void newBookCopy(ActionEvent event) {
        try {
            BorderPane root = (BorderPane) FXMLLoader.load(getClass().getResource("/views/AddBookCopy.fxml"));
            Scene scene = new Scene(root,450,340);
            scene.getStylesheets().add(getClass().getResource("/assets/css/application.css").toExternalForm());
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.show();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
}
