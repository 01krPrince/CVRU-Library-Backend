package BLMS.service;

import BLMS.model.Book;
import BLMS.model.request.AddBookRequest;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface BookService {
    Book addBook(AddBookRequest addBookRequest, String adminId);
    Optional<Book> findById(String id);
    List<Book> findAllBooks();
    Book updateBook(String isbn, AddBookRequest updateBookRequest, String adminId);
    void deleteBook(String isbn, String adminId);
    List<Book> searchBooks(String searchEle);

    Optional<Book> searchByISBN(String isbn);

    long totleBookCount();

    long totleIssued();

    Map<String, Long> adminDashboard(String adminId);

    Map<String, Long> studentDashboard(String studentId);
}
