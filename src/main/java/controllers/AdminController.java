package controllers;

import entities.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
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
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        loadMember();
    }

    @FXML
    private TableView<User> tbvMember;

    @FXML
    public TableColumn<User, String> firstName;

    @FXML
    public TableColumn<User, String> lastName;

    @FXML
    public TableColumn<User, String> phone;

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
}
