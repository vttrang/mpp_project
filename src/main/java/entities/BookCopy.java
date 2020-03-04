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

import org.hibernate.type.descriptor.sql.TinyIntTypeDescriptor;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;


@Entity
@Table(name = "book_copy",
  uniqueConstraints = { @UniqueConstraint(columnNames = {"copy_id" }) })
public class BookCopy implements Serializable{

	@OneToOne
    @JoinColumn(name = "isbn")
    private Book book;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "copy_id")
	private Integer copyId;

	@Column(name = "availability")
	private int availability;

	@Column(name = "lendable_day")
	private Integer lendableDay;

	public Book getBook() {
		return book;
	}

	public void setBook(Book book) {
		this.book = book;
	}

	public Integer getCopyId() {
		return copyId;
	}

	public void setCopyId(Integer copyId) {
		this.copyId = copyId;
	}

	public int isAvailability() {
		return availability;
	}

	public void setAvailability(int availability) {
		this.availability = availability;
	}

	public Integer getLendableDay() {
		return lendableDay;
	}

	public void setLendableDay(Integer lendableDay) {
		this.lendableDay = lendableDay;
	}

	

}
