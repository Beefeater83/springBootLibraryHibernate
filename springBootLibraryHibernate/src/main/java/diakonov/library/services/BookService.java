package diakonov.library.services;

import diakonov.library.models.Book;
import diakonov.library.models.Person;
import diakonov.library.repositories.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class BookService {
    private final BookRepository bookRepository;

    @Autowired
    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    public List<Book>findAll(){
        return bookRepository.findAll();
    }
    public List<Book>findAll(Sort sorted){
        return bookRepository.findAll(sorted);
    }

    public Page<Book> findAll(Pageable pageable){
        return bookRepository.findAll(pageable);
    }

    public Book findOne(int id) {
        Optional<Book> foundPerson = bookRepository.findById(id);
        return foundPerson.orElse(null);
    }

    @Transactional
    public void save(Book book) {
        bookRepository.save(book);
    }

    @Transactional
    public void update(int id, Book updatedBook) {
        updatedBook.setId(id);
        bookRepository.save(updatedBook);
    }

    @Transactional
    public void delete(int id) {
        bookRepository.deleteById(id);
    }

    @Transactional
    public List<Book> findBooksByReader(int id) {
        List<Book> books = bookRepository.findBooksByReaderId(id);
    for (Book book:books){
    book.setOverDueBookReturn(updateOverDueStatus(book.getId()));
       }
        return books;
    }

    @Transactional
    public void addReaderForBook(int id, Person selectedPerson) {
        Optional<Book> bookFound = bookRepository.findById(id);
        bookFound.ifPresent(book -> {
            book.setDateBookTaken(new Date());
            book.setReader(selectedPerson);
        });
    }

    @Transactional
    public void releaseBook(int id) {
        Optional<Book> bookFoundForRelease = bookRepository.findById(id);
        if (bookFoundForRelease.isPresent()) {
            Book book = bookFoundForRelease.get();
            if (book.getReader() != null) {
                book.setReader(null);
                book.setDateBookTaken(null);
            }
        }
    }

    @Transactional
    public Optional<Person> findReaderByBookId(int bookId) {
        Optional<Book> book = bookRepository.findById(bookId);
        if (book.isPresent()) {
            return Optional.ofNullable(book.get().getReader());
        } else {
            return Optional.empty();
        }
    }
    public Book findBookByBookNameStartingWith(String startingWith){
        Optional<Book> book = bookRepository.findBookByBookNameStartingWith(startingWith);
        if(book.isPresent()){
            return book.get();
        }else {
            return null;
        }
    }

    public boolean updateOverDueStatus(int bookId) {
        Optional<Book> book = bookRepository.findById(bookId);
        if (book.isPresent() && book.get().getReader() != null) {
            Date currentdate = new Date();
            Date dbDate = book.get().getDateBookTaken();
            long millisecondsDifference = currentdate.getTime() - dbDate.getTime();
            int daysInUse = (int) (millisecondsDifference / (24 * 60 * 60 * 1000));
            boolean isOverDue = daysInUse >= 10;
            book.get().setOverDueBookReturn(isOverDue);
            return isOverDue;
        }return false;
    }




}
