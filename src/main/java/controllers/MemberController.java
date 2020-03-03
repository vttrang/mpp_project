package controllers;

import entities.Address;
import entities.Role;
import entities.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import libs.HibernateUtils;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import java.net.URL;
import java.util.ResourceBundle;

public class MemberController implements Initializable {
    @FXML
    private TextField firstName;
    @FXML
    private TextField lastName;
    @FXML
    private TextField street;
    @FXML
    private TextField city;
    @FXML
    private TextField state;
    @FXML
    private TextField zip;
    @FXML
    private TextField phone;

    @FXML
    private Button close;

    @FXML
    private Button save;

    Session session;

    @FXML
    public void save(ActionEvent event) {
        SessionFactory factory = HibernateUtils.getSessionFactory();
        session = factory.getCurrentSession();
        try {
            session.getTransaction().begin();

            Address address = this.newAddress();
            Role role = session.load(Role.class, models.Role.MEMBER.getCode());
            User user = this.newUser(address, role);

            session.getTransaction().commit();
            session.close();
            if(null != user) {
                save.setDisable(true);
            }

        }  catch (Exception e) {
            e.printStackTrace();
            session.getTransaction().rollback();
        }
    }

    @FXML
    public void close(ActionEvent event) {
        Stage stage = (Stage) close.getScene().getWindow();
        stage.close();
    }

    public void initialize(URL location, ResourceBundle resources) {
        // TODO Auto-generated method stub

    }

    private Address newAddress() {
        if (this.street.getText().isEmpty() || this.city.getText().isEmpty() ||
                this.state.getText().isEmpty() || this.zip.getText().isEmpty()) {
            return null;
        }

        Address add = new Address();
        add.setStreet(this.street.getText());
        add.setCity(this.city.getText());
        add.setState(this.state.getText());
        add.setZip(Integer.parseInt(this.zip.getText()));

        session.persist(add);

        return add;
    }

    private User newUser(Address add, Role role) {
        if (this.firstName.getText().isEmpty() || this.lastName.getText().isEmpty() ||
                this.phone.getText().isEmpty()) {
            return null;
        }
        User user = new User();
        user.setFirstName(this.firstName.getText());
        user.setLastName(this.lastName.getText());
        user.setPhone(this.phone.getText());
        user.setAddress(add);
        user.setRole(role);

        this.session.persist(user);

        return user;
    }
}
