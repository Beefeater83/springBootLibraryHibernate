package diakonov.library.repositories;

import diakonov.library.models.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import diakonov.library.models.Person;
import java.util.List;
import java.util.Optional;

@Repository
public interface BookRepository extends JpaRepository<Book, Integer> {

    List<Book> findBooksByReaderId(int id);

    Optional<Book>findBookByBookNameStartingWith(String startingWith);

}
