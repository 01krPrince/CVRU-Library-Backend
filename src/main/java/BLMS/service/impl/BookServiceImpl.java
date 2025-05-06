package BLMS.service.impl;

import BLMS.model.Book;
import BLMS.model.request.AddBookRequest;
import BLMS.repository.BookRepository;
import BLMS.service.AdminService;
import BLMS.service.BookService;
import BLMS.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class BookServiceImpl implements BookService {

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private AdminService adminService;

    @Autowired
    private StudentService studentService;

    private void validateAdmin(String adminId) {
        if (adminId == null || !adminService.findById(adminId).isPresent()) {
            throw new IllegalArgumentException("Un-Authorize user");
        }
    }

    private String isbnGenerator() {
        Random random = new Random();
        String isbn;
        do {
            // Generate a 13-digit ISBN-like string
            isbn = "978" + String.format("%010d", Math.abs(random.nextLong()) % 1_000_000_0000L);
        } while (bookRepository.findByIsbn(isbn).isPresent());
        return isbn;
    }


    @Override
    public Book addBook(AddBookRequest addBookRequest, String adminId) {
        validateAdmin(adminId);

        if (addBookRequest == null) {
            throw new IllegalArgumentException("Book request cannot be null");
        }
        String isbn = isbnGenerator();
        Book newBook = new Book();
        newBook.setTitle(addBookRequest.getTitle());
        newBook.setAuthor(addBookRequest.getAuthor());
        newBook.setGenre(addBookRequest.getGenre());
        newBook.setImgUrl(addBookRequest.getImgUrl());
        newBook.setPublisher(addBookRequest.getPublisher());
        newBook.setPublicationYear(addBookRequest.getPublicationYear());
        newBook.setLanguage(addBookRequest.getLanguage());
        newBook.setTotalCopies(addBookRequest.getTotalNumberOfBooks());
        newBook.setIsbn(isbn);
        newBook.setStatus("AVAILABLE");

        return bookRepository.save(newBook);
    }

    @Override
    public Book updateBook(String isbn, AddBookRequest updateBookRequest, String adminId) {
        validateAdmin(adminId);

        if (isbn == null || isbn.trim().isEmpty()) {
            throw new IllegalArgumentException("ID cannot be null or empty");
        }
        if (updateBookRequest == null) {
            throw new IllegalArgumentException("Book request cannot be null");
        }

        Book existingBook = bookRepository.findByIsbn(isbn)
                .orElseThrow(() -> new NoSuchElementException("Book not found with ISBM: " + isbn));

        int totalCopies = updateBookRequest.getTotalNumberOfBooks();
        int availableCopies = Math.min(existingBook.getAvailableCopies(), totalCopies);

        existingBook.setTitle(updateBookRequest.getTitle());
        existingBook.setAuthor(updateBookRequest.getAuthor());
        existingBook.setGenre(updateBookRequest.getGenre());
        existingBook.setPublisher(updateBookRequest.getPublisher());
        existingBook.setImgUrl(updateBookRequest.getImgUrl());
        existingBook.setPublicationYear(updateBookRequest.getPublicationYear());
        existingBook.setLanguage(updateBookRequest.getLanguage());
        existingBook.setTotalCopies(totalCopies);
        existingBook.setAvailableCopies(availableCopies);
        existingBook.setStatus("AVAILABLE");

        return bookRepository.save(existingBook);
    }

    @Override
    public void deleteBook(String isbn, String adminId) {
        validateAdmin(adminId);

        if (isbn == null || isbn.trim().isEmpty()) {
            throw new IllegalArgumentException("ID cannot be null or empty");
        }

        if (bookRepository.findByIsbn(isbn).isEmpty()) {
            throw new NoSuchElementException("Book not found with ID: " + isbn);
        }

        bookRepository.deleteByIsbn(isbn);
    }

    @Override
    public Optional<Book> findById(String id) {
        if (id == null || id.trim().isEmpty()) {
            throw new IllegalArgumentException("ID cannot be null or empty");
        }
        return bookRepository.findById(id);
    }

    @Override
    public List<Book> findAllBooks() {
        return bookRepository.findAll();
    }

    @Override
    public List<Book> searchBooks(String searchEle) {
        if (searchEle == null || searchEle.isBlank()) {
            return Collections.emptyList();
        }
        System.out.println("Hitting the api sucessfully");
        return bookRepository.findByTitleContainingIgnoreCaseOrAuthorContainingIgnoreCaseOrIsbnContainingIgnoreCase(
                searchEle, searchEle, searchEle
        );
    }


    @Override
    public Optional<Book> searchByISBN(String isbn) {
        return bookRepository.findByIsbn(isbn);
    }

    public long totleBookCount() {
        return bookRepository.count();
    }

    public long totleIssued() {
        long issuedBookCount = 0;
        List<Book> books = bookRepository.findAll();
        for (Book i :  books) {
            if (i.getTotalCopies() != i.getAvailableCopies()) {
                issuedBookCount += (i.getTotalCopies() - i.getAvailableCopies());
            }
        }
        return issuedBookCount;
    }


    @Override
    public Map<String, Long> adminDashboard(String adminId) {
        if (adminService.findById(adminId).isPresent()) {
            long totalBookCount = totleBookCount();
            long totalBookIssued = totleIssued();
            long totalRegisteredStudents = studentService.totleStudentCount();

            return Map.of(
                    "totalBookCount", totalBookCount,
                    "totalBookIssued", totalBookIssued,
                    "totalRegisteredStudents", totalRegisteredStudents
            );
        }

        return Map.of();
    }

    @Override
    public Map<String, Long> studentDashboard(String studentId) {
        if (studentService.findById(studentId).isPresent()) {
//            long bookBorrowed
//            long dueDate
//            long availableBooks
        }
        return Map.of();
    }

}
