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
@Table(name = "book_copy",
  uniqueConstraints = { @UniqueConstraint(columnNames = {"copy_id" }) })
public class BookCopy implements Serializable{

	@ManyToOne
    @JoinColumn(name = "isbn")
    private Book book;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "copy_id")
	private Integer copy_id;

	@Column(name = "availability")
	private boolean availability;

	@Column(name = "lendable_day")
	private Integer lendable_day;

	public Integer getCopyID() {
		return copy_id;
	}

	public boolean getAvailability() {
		return availability;
	}

	public void setAvailability(boolean availability) {
		this.availability = availability;
	}

	public Integer getLendableDay() {
		return lendable_day;
	}

	public void setLendableDay(Integer lendable_day) {
		this.lendable_day = lendable_day;
	}

}
