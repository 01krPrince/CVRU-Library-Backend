package BLMS.service.impl;

import BLMS.model.Book;
import BLMS.model.request.AddBookRequest;
import BLMS.repository.BookRepository;
import BLMS.service.AdminService;
import BLMS.service.BookService;
import BLMS.service.BorrowRecordService;
import BLMS.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.*;

@Service
public class BookServiceImpl implements BookService {

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private AdminService adminService;

    @Autowired
    private StudentService studentService;

    @Autowired
    private BorrowRecordService borrowRecordService;

    private void validateAdmin(String adminId) {
        if (adminId == null || !adminService.findById(adminId).isPresent()) {
            throw new IllegalArgumentException("Unauthorized admin");
        }
    }

    // Generates a unique ISBN starting with "978" and 10 random digits
    private String isbnGenerator() {
        Random random = new Random();
        String isbn;
        int attempts = 0;
        do {
            isbn = "978" + String.format("%010d", Math.abs(random.nextLong()) % 1_000_000_0000L);
            attempts++;
            if (attempts > 100) throw new RuntimeException("Unable to generate unique ISBN");
        } while (bookRepository.findByIsbn(isbn).isPresent());
        return isbn;
    }

    @Override
    @Transactional
    public Book addBook(AddBookRequest addBookRequest, String adminId) {
        validateAdmin(adminId);

        if (addBookRequest == null) {
            throw new IllegalArgumentException("Book request cannot be null");
        }

        String isbn = isbnGenerator();
        int totalCopies = addBookRequest.getTotalNumberOfBooks();

        Book newBook = new Book();
        newBook.setTitle(addBookRequest.getTitle());
        newBook.setAuthor(addBookRequest.getAuthor());
        newBook.setGenre(addBookRequest.getGenre());
        newBook.setImgUrl(addBookRequest.getImgUrl());
        newBook.setPublisher(addBookRequest.getPublisher());
        newBook.setPublicationYear(addBookRequest.getPublicationYear());
        newBook.setLanguage(addBookRequest.getLanguage());
        newBook.setIsbn(isbn);
        newBook.setTotalCopies(totalCopies);
        newBook.setAvailableCopies(totalCopies);
        newBook.setStatus("AVAILABLE");
        newBook.setAddedDate(LocalDate.now());

        List<Boolean> availabilityList = new ArrayList<>();
        for (int i = 0; i < totalCopies; i++) {
            availabilityList.add(true);
        }
        newBook.setCopyAvailability(availabilityList);

        return bookRepository.save(newBook);
    }

    @Override
    @Transactional
    public Book updateBook(String isbn, AddBookRequest updateBookRequest, String adminId) {
        validateAdmin(adminId);

        if (isbn == null || isbn.trim().isEmpty()) {
            throw new IllegalArgumentException("ISBN cannot be null or empty");
        }

        if (updateBookRequest == null) {
            throw new IllegalArgumentException("Book request cannot be null");
        }

        Book existingBook = bookRepository.findByIsbn(isbn)
                .orElseThrow(() -> new NoSuchElementException("Book not found with ISBN: " + isbn));

        int newTotalCopies = updateBookRequest.getTotalNumberOfBooks();
        int currentTotalCopies = existingBook.getTotalCopies();

        List<Boolean> availabilityList = new ArrayList<>(existingBook.getCopyAvailability());
        if (newTotalCopies > currentTotalCopies) {
            for (int i = currentTotalCopies; i < newTotalCopies; i++) {
                availabilityList.add(true);
            }
        } else if (newTotalCopies < currentTotalCopies) {
            availabilityList = availabilityList.subList(0, newTotalCopies);
        }

        int updatedAvailableCopies = (int) availabilityList.stream().filter(a -> a).count();

        existingBook.setTitle(updateBookRequest.getTitle());
        existingBook.setAuthor(updateBookRequest.getAuthor());
        existingBook.setGenre(updateBookRequest.getGenre());
        existingBook.setPublisher(updateBookRequest.getPublisher());
        existingBook.setImgUrl(updateBookRequest.getImgUrl());
        existingBook.setPublicationYear(updateBookRequest.getPublicationYear());
        existingBook.setLanguage(updateBookRequest.getLanguage());
        existingBook.setTotalCopies(newTotalCopies);
        existingBook.setAvailableCopies(updatedAvailableCopies);
        existingBook.setStatus("AVAILABLE");
        existingBook.setCopyAvailability(availabilityList);

        return bookRepository.save(existingBook);
    }

    @Override
    @Transactional
    public void deleteBook(String isbn, String adminId) {
        validateAdmin(adminId);

        if (isbn == null || isbn.trim().isEmpty()) {
            throw new IllegalArgumentException("ISBN cannot be null or empty");
        }

        if (bookRepository.findByIsbn(isbn).isEmpty()) {
            throw new NoSuchElementException("Book not found with ISBN: " + isbn);
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

        return bookRepository.findByTitleContainingIgnoreCaseOrAuthorContainingIgnoreCaseOrIsbnContainingIgnoreCase(
                searchEle, searchEle, searchEle
        );
    }

    @Override
    public Optional<Book> searchByISBN(String isbn) {
        if (isbn == null || isbn.trim().isEmpty()) {
            throw new IllegalArgumentException("ISBN cannot be null or empty");
        }
        return bookRepository.findByIsbn(isbn);
    }

    @Override
    public Long totalBookCount() {
        return bookRepository.findAll().stream()
                .mapToLong(Book::getTotalCopies)
                .sum();
    }

    @Override
    public Long totalBookIssuedCount() {
        return bookRepository.findAll().stream()
                .mapToLong(book -> book.getTotalCopies() - book.getAvailableCopies())
                .sum();
    }
}
