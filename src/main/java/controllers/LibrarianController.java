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
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
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

public class LibrarianController implements Initializable {
	
	@FXML
	Button buttonOpenStageCheckout;
	
	@FXML
	Button btnSearch;
	
	@FXML
	Button btnLoadAll;
	
	@FXML
	TextField inputSearchUserId;
	
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
	
	public void setUser(User user){
	   System.out.println(user.getFirstName());
	}
}
