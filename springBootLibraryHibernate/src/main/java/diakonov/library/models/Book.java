package diakonov.library.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

import java.util.Date;

@Entity
@Table(name = "Book")
public class Book {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @NotEmpty(message = "Название не должно быть пустым")
    @Size(min=2, max = 30, message = "Название должно содержать от 2 до 30 символов")
    @Column(name = "book_name")
    private String bookName;
    @NotEmpty(message = "Название не должно быть пустым")
    @Column(name = "author")
    private String author;
    @Min(value = 1900, message = "Введите корректный год")
    @Column(name = "year_of_issue")
    private int yearOfIssue;
    @ManyToOne
    @JoinColumn(name = "person_id", referencedColumnName = "id")
    private Person reader;
    @Column(name = "date_book_taken")
    @Temporal(TemporalType.DATE)
    private Date dateBookTaken;
    @Transient
    private boolean overDueBookReturn;

    public boolean isOverDueBookReturn() {
        return overDueBookReturn;
    }

    public void setOverDueBookReturn(boolean overDueBookReturn) {
        this.overDueBookReturn = overDueBookReturn;
    }

    public Person getReader() {
        return reader;
    }

    public void setReader(Person reader) {
        this.reader = reader;
    }

    public String getBookName() {
        return bookName;
    }

    public void setBookName(String bookName) {
        this.bookName = bookName;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public int getYearOfIssue() {
        return yearOfIssue;
    }

    public void setYearOfIssue(int yearOfIssue) {
        this.yearOfIssue = yearOfIssue;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
    public Book() { }

    public Date getDateBookTaken() {
        return dateBookTaken;
    }


    public void setDateBookTaken(Date dateBookTaken) {
        this.dateBookTaken = dateBookTaken;
    }

    public Book(int id, String bookName, String author, int yearOfIssue, boolean overDueBookReturn) {
        this.bookName = bookName;
        this.author = author;
        this.yearOfIssue = yearOfIssue;
        this.overDueBookReturn = overDueBookReturn;
    }

}

