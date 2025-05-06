package BLMS.service.impl;

import BLMS.model.Book;
import BLMS.model.BorrowRecord;
import BLMS.model.Student;
import BLMS.repository.BookRepository;
import BLMS.repository.BorrowRecordRepository;
import BLMS.repository.UserRepository;
import BLMS.service.BorrowRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class BorrowRecordServiceImpl implements BorrowRecordService {

    @Autowired
    private BorrowRecordRepository borrowRecordRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BookRepository bookRepository;

    @Override
    public BorrowRecord createBorrowRecord(String studentId, String bookId) {
        if (bookId == null || studentId == null) {
            throw new IllegalArgumentException("Book ID and Student ID are required");
        }

        Student student = userRepository.findById(studentId)
                .orElseThrow(() -> new NoSuchElementException("Student not found with ID: " + studentId));
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new NoSuchElementException("Book not found with ID: " + bookId));

        if (book.getAvailableCopies() <= 0) {
            throw new IllegalStateException("No available copies of the book");
        }

        BorrowRecord newBorrowRecord = new BorrowRecord();
        newBorrowRecord.setBookId(bookId);
        newBorrowRecord.setStudentId(studentId);
        newBorrowRecord.setBorrowDate(LocalDate.now());
        newBorrowRecord.setDueDate(LocalDate.now().plusDays(7));
        newBorrowRecord.setReturned(false);
        newBorrowRecord.setFine(0.0);

        newBorrowRecord = borrowRecordRepository.save(newBorrowRecord);

        book.setAvailableCopies(book.getAvailableCopies() - 1);
        if (student.getBorrowRecordIds() == null) {
            student.setBorrowRecordIds(new java.util.ArrayList<>());
        }
        student.getBorrowRecordIds().add(newBorrowRecord.getBorrowedId());

        bookRepository.save(book);
        userRepository.save(student);

        return newBorrowRecord;
    }

    @Override
    public Optional<BorrowRecord> findById(String id) {
        if (id == null || id.trim().isEmpty()) {
            throw new IllegalArgumentException("ID cannot be null or empty");
        }
        return borrowRecordRepository.findById(id);
    }

    @Override
    public List<BorrowRecord> findAllBorrowRecords() {
        return borrowRecordRepository.findAll();
    }

    @Override
    public BorrowRecord updateBorrowRecord(String id, BorrowRecord borrowRecord) {
        if (id == null || id.trim().isEmpty()) {
            throw new IllegalArgumentException("ID cannot be null or empty");
        }
        if (borrowRecord == null) {
            throw new IllegalArgumentException("Borrow record cannot be null");
        }

        // Find existing borrow record
        BorrowRecord existingRecord = borrowRecordRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Borrow record not found with ID: " + id));

        // Validate student and book existence
        if (!existingRecord.getStudentId().equals(borrowRecord.getStudentId())) {
            userRepository.findById(borrowRecord.getStudentId())
                    .orElseThrow(() -> new NoSuchElementException("Student not found with ID: " + borrowRecord.getStudentId()));
        }
        if (!existingRecord.getBookId().equals(borrowRecord.getBookId())) {
            bookRepository.findById(borrowRecord.getBookId())
                    .orElseThrow(() -> new NoSuchElementException("Book not found with ID: " + borrowRecord.getBookId()));
        }

        // Update fields
        existingRecord.setBookId(borrowRecord.getBookId());
        existingRecord.setStudentId(borrowRecord.getStudentId());
        existingRecord.setBorrowDate(borrowRecord.getBorrowDate() != null ? borrowRecord.getBorrowDate() : existingRecord.getBorrowDate());
        existingRecord.setDueDate(borrowRecord.getDueDate() != null ? borrowRecord.getDueDate() : existingRecord.getDueDate());
        existingRecord.setReturnDate(borrowRecord.getReturnDate());
        existingRecord.setReturned(borrowRecord.isReturned()); // Use setReturned
        existingRecord.setFine(borrowRecord.getFine());

        // If book is returned, update book and student
        if (borrowRecord.isReturned() && !existingRecord.isReturned()) {
            Book book = bookRepository.findById(existingRecord.getBookId())
                    .orElseThrow(() -> new NoSuchElementException("Book not found with ID: " + existingRecord.getBookId()));
            book.setAvailableCopies(book.getAvailableCopies() + 1);
            Student student = userRepository.findById(existingRecord.getStudentId())
                    .orElseThrow(() -> new NoSuchElementException("Student not found with ID: " + existingRecord.getStudentId()));
            student.getBorrowRecordIds().remove(existingRecord.getBorrowedId());
            bookRepository.save(book);
            userRepository.save(student);
        }

        return borrowRecordRepository.save(existingRecord);
    }

    @Override
    public void deleteBorrowRecord(String id) {
        if (id == null || id.trim().isEmpty()) {
            throw new IllegalArgumentException("ID cannot be null or empty");
        }
        BorrowRecord record = borrowRecordRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Borrow record not found with ID: " + id));
        if (!record.isReturned()) {
            Book book = bookRepository.findById(record.getBookId())
                    .orElseThrow(() -> new NoSuchElementException("Book not found with ID: " + record.getBookId()));
            book.setAvailableCopies(book.getAvailableCopies() + 1);
            Student student = userRepository.findById(record.getStudentId())
                    .orElseThrow(() -> new NoSuchElementException("Student not found with ID: " + record.getStudentId()));
            student.getBorrowRecordIds().remove(record.getBorrowedId());
            bookRepository.save(book);
            userRepository.save(student);
        }
        borrowRecordRepository.deleteById(id);
    }

    @Override
    public List<BorrowRecord> findByStudentId(String studentId) {
        if (studentId == null || studentId.trim().isEmpty()) {
            throw new IllegalArgumentException("Student ID cannot be null or empty");
        }
        return borrowRecordRepository.findByStudentId(studentId);
    }

    @Override
    public List<BorrowRecord> findByBookId(String bookId) {
        if (bookId == null || bookId.trim().isEmpty()) {
            throw new IllegalArgumentException("Book ID cannot be null or empty");
        }
        return borrowRecordRepository.findByBookId(bookId);
    }
}