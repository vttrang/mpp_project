package entities;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;


@Entity
@Table(name = "book",
  uniqueConstraints = { @UniqueConstraint(columnNames = {"isbn" }) })
public class Book implements Serializable{

	@ManyToMany
	@JoinTable(name = "book_author",
    joinColumns = { @JoinColumn(name = "isbn") },
    inverseJoinColumns = { @JoinColumn(name = "author_id") })
    private Set<Author> products = new HashSet<Author>();

	@Id
	@Column(name = "isbn")
	private Integer isbn;

	@Column(name = "title")
	private String title;

	public Integer getIsbn() {
		return isbn;
	}

	public void setIsbn(Integer isbn) {
		this.isbn = isbn;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

}