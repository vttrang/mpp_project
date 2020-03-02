package entities;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
 
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
 
@Entity
@Table(name = "user",
  uniqueConstraints = { @UniqueConstraint(columnNames = { "id" }) })
public class User implements Serializable{
 
	private Integer id;
	
	private String firstName;
	
	private String lastName;
	 
	public User() {
	}

	public User(Integer id) {
	   this.id = id;
	}
	 
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	public Integer getId() {
	   return id;
	}
	 
	public void setId(Integer id) {
	   this.id = id;
	}
	
	@Column(name = "first_name")
	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	@Column(name = "last_name")
	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
}