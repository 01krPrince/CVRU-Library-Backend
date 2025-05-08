package BLMS.controller;

import BLMS.model.Book;
import BLMS.model.request.AddBookRequest;
import BLMS.service.BookService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/books")
@CrossOrigin("*")
public class BookController {

    @Autowired
    private BookService bookService;

    // Add a new book
    @PostMapping("/addBook")
    public ResponseEntity<Book> addBook(@Valid @RequestBody AddBookRequest request,
                                        @RequestParam String adminId) {
        Book addedBook = bookService.addBook(request, adminId);
        return new ResponseEntity<>(addedBook, HttpStatus.CREATED);
    }

    // Get book by ID
    @GetMapping("/{id}")
    public ResponseEntity<Book> getBook(@PathVariable String id) {
        Optional<Book> book = bookService.findById(id);
        return book.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    // Get all books
    @GetMapping
    public ResponseEntity<List<Book>> getAllBooks() {
        List<Book> books = bookService.findAllBooks();
        return new ResponseEntity<>(books, HttpStatus.OK);
    }

    // Update book details
    @PutMapping("/updateByISBN")
    public ResponseEntity<Book> updateBook(@RequestParam String isbn,
                                           @Valid @RequestBody AddBookRequest updateBookRequest,
                                           @RequestParam String adminId) {
        Book updatedBook = bookService.updateBook(isbn, updateBookRequest, adminId);
        return new ResponseEntity<>(updatedBook, HttpStatus.OK);
    }

    // Delete book
    @DeleteMapping
    public ResponseEntity<Void> deleteBook(@RequestParam String isbn,
                                           @RequestParam String adminId) {
        bookService.deleteBook(isbn, adminId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    // Search books by title, author or ISBN
    @GetMapping("/search")
    public ResponseEntity<List<Book>> searchBooks(@RequestParam String searchEle) {
        List<Book> books = bookService.searchBooks(searchEle);
        return new ResponseEntity<>(books, HttpStatus.OK);
    }

    // Find book by ISBN
    @GetMapping("/searchByISBN")
    public ResponseEntity<Optional<Book>> findByISBN(@RequestParam String isbn) {
        Optional<Book> book = bookService.searchByISBN(isbn);
        return new ResponseEntity<>(book, HttpStatus.OK);
    }

    // Get total book count
    @GetMapping("/totalBookCount")
    public ResponseEntity<Long> totalBookCount() {
        return new ResponseEntity<>(bookService.totalBookCount(), HttpStatus.OK);
    }

    // Get total issued book count
    @GetMapping("/totalBookIssuedCount")
    public ResponseEntity<Long> totalBookIssuedCount() {
        return new ResponseEntity<>(bookService.totalBookIssuedCount(), HttpStatus.OK);
    }
}
