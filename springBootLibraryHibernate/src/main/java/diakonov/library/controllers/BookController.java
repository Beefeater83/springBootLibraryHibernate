package diakonov.library.controllers;

import diakonov.library.models.Book;
import diakonov.library.models.Person;
import diakonov.library.services.BookService;
import diakonov.library.services.PeopleService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/books")
public class BookController {

    private final BookService bookService;
    private final PeopleService peopleService;

    @Autowired
    public BookController(BookService bookService, PeopleService peopleService) {
        this.bookService = bookService;
        this.peopleService=peopleService;
    }

    @GetMapping()
    public String index(@RequestParam(value = "page",required = false, defaultValue = "0") int page,
                        @RequestParam(value = "books_per_page", required = false, defaultValue = "0") int books_per_page,
                        @RequestParam(value = "sort_by_year", required = false) String sortByYear,
                        Model model) {
        boolean sortByYearValue = sortByYear != null && sortByYear.equals("true");
        Sort sort = sortByYearValue ? Sort.by("yearOfIssue") : Sort.unsorted();

        if (page==0 && books_per_page ==0) {
            if(sortByYearValue){
                model.addAttribute("books", bookService.findAll(sort));
            }
            else {
                model.addAttribute("books", bookService.findAll());
            }
            return "books/index";
        }else {
            Pageable pageable = PageRequest.of(page, books_per_page);
            Page<Book> bookPage = bookService.findAll(pageable);
            model.addAttribute("booksWithPages", bookPage.getContent());
            model.addAttribute("currentPage", bookPage.getNumber());
            model.addAttribute("totalPages", bookPage.getTotalPages());
            model.addAttribute("booksPerPage", books_per_page);
        }
        return "books/index";
    }

    @GetMapping("/new")
    public String newBook(@ModelAttribute("book") Book book) {

        return "books/new";
    }

    @GetMapping("/{id}")
    public String show(@PathVariable("id") int id, Model model, @ModelAttribute("selectedPerson") Person selectedPerson) {
        model.addAttribute("book", bookService.findOne(id));
        Optional<Person> bookOwner = bookService.findReaderByBookId(id);
        if (bookOwner.isPresent())
            model.addAttribute("owner", bookOwner.get());
         else
            model.addAttribute("people", peopleService.findAll());

        return "books/show";
    }

    @PostMapping()
    public String create(@ModelAttribute("book") @Valid Book book, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) return "books/new";
        bookService.save(book);
        return "redirect:/books";
    }

    @GetMapping("/{id}/edit")
    public String edit(Model model, @PathVariable("id") int id) {
        model.addAttribute("book", bookService.findOne(id));
        return "books/edit";
    }

    @PatchMapping("/{id}")
    public String update(@ModelAttribute("book") @Valid Book book, BindingResult bindingResult, @PathVariable("id") int id) {
        if (bindingResult.hasErrors()) return "books/edit";
        bookService.update(id, book);
        return "redirect:/books";
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable("id") int id) {
        bookService.delete(id);
        return "redirect:/books";
    }

    @PatchMapping("/{id}/add")
    public String addBookReader(@PathVariable("id") int bookId, @ModelAttribute("selectedPerson") Person selectedPerson) {
        bookService.addReaderForBook(bookId, selectedPerson);
        return "redirect:/books/" + bookId;
    }

    @PatchMapping("/{id}/releaseBook")
    public String releaseBook(@PathVariable("id") int bookId) {
        bookService.releaseBook(bookId);
        return "redirect:/books/" + bookId;
    }
    @GetMapping("/search")
    public String searchBook() { return "books/search";
    }
    @GetMapping("/search-result")
    public String searchResult(@RequestParam(name = "nameofbook") String nameofbook, Model model) {
        Book foundbook=bookService.findBookByBookNameStartingWith(nameofbook);
        Person readeroffoundbook;
        if(foundbook !=null){
            model.addAttribute("foundbook",foundbook);
            Optional<Person>reader=bookService.findReaderByBookId(foundbook.getId());
            if(reader.isPresent()){
                readeroffoundbook=reader.get();
                model.addAttribute("readeroffoundbook", readeroffoundbook);
            }
        }

        return "books/search";
    }
}
