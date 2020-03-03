package entities;

import java.io.Serializable;
import java.security.NoSuchAlgorithmException;
import java.util.HashSet;
import java.util.Set;
 
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import libs.PasswordMD5;

@Entity
@Table(name = "credential",
  uniqueConstraints = { @UniqueConstraint(columnNames = { "user_id" }) })
public class Credential implements Serializable{
	
	@Id
	@OneToOne
    @JoinColumn(name = "user_id")
    public User user;
	
	@Column(name = "password")
	private String password;

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = PasswordMD5.generate(password);
	}
}
