package entities;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;


@Entity
@Table(name = "author",
  uniqueConstraints = { @UniqueConstraint(columnNames = {"author_id" }) })
public class Author implements Serializable{

	@Id
	@Column(name = "author_id")
	private Integer author_id;

	@Column(name = "fname")
	private String fname;

	@Column(name = "lname")
	private String lname;

	@Column(name = "credentials")
	private String credentials;

	@Column(name = "bio")
	private String bio;

	public Integer getId() {
		return author_id;
	}

	public void setId(Integer author_id) {
		this.author_id = author_id;
	}

	public String getFName() {
		return fname;
	}

	public void setFName(String fname) {
		this.fname = fname;
	}

	public String getLName() {
		return lname;
	}

	public void setLName(String lname) {
		this.lname = lname;
	}

	public String getCredentials() {
		return credentials;
	}

	public void setCredentials(String credentials) {
		this.credentials = credentials;
	}

	public String getBio() {
		return bio;
	}

	public void setBio(String bio) {
		this.bio = bio;
	}
}
