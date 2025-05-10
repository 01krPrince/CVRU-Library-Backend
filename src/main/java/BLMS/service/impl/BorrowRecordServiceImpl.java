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
import java.time.temporal.ChronoUnit;
import java.util.*;

@Service
public class BorrowRecordServiceImpl implements BorrowRecordService {

    @Autowired
    private BorrowRecordRepository borrowRecordRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BookRepository bookRepository;

    @Override
    public BorrowRecord createBorrowRecord(String studentId, String isbn) {
        if (isbn == null || studentId == null) {
            throw new IllegalArgumentException("Book ID and Student ID are required");
        }

        Student student = userRepository.findById(studentId)
                .orElseThrow(() -> new NoSuchElementException("Student not found with ID: " + studentId));
        Book book = bookRepository.findByIsbn(isbn)
                .orElseThrow(() -> new NoSuchElementException("Book not found with ISBN: " + isbn));

        if (book.getAvailableCopies() <= 0) {
            throw new IllegalStateException("No available copies of the book");
        }

        List<BorrowRecord> borrowHistory = borrowRecordRepository.findByStudentId(studentId);
        for (BorrowRecord record : borrowHistory) {
            if (record.getIsbn().equals(isbn) && !record.isReturned()) {
                throw new IllegalStateException("Student " + studentId + " already has this book (ISBN: " + isbn + ").");
            }
        }

        List<Boolean> copyAvailability = book.getCopyAvailability();
        int copyIndex = -1;
        for (int i = 0; i < copyAvailability.size(); i++) {
            if (copyAvailability.get(i)) {
                copyAvailability.set(i, false);
                copyIndex = i;
                break;
            }
        }

        if (copyIndex == -1) {
            throw new IllegalStateException("All copies are marked as unavailable despite availableCopies > 0.");
        }

        // Create and save new borrow record
        BorrowRecord newBorrowRecord = new BorrowRecord();
        newBorrowRecord.setIsbn(isbn);
        newBorrowRecord.setStudentId(studentId);
        newBorrowRecord.setBorrowDate(LocalDate.now());
        newBorrowRecord.setDueDate(LocalDate.now().plusDays(7));
        newBorrowRecord.setReturned(false);
        newBorrowRecord.setFine(0.0);
        newBorrowRecord.setCopyIndex(copyIndex);
        newBorrowRecord.setDueDate(LocalDate.now().plusDays(7));

        newBorrowRecord = borrowRecordRepository.save(newBorrowRecord);
        student.getBorrowRecordIds().add(newBorrowRecord.getBorrowedId());

        book.setAvailableCopies(book.getAvailableCopies() - 1);
        book.setCopyAvailability(copyAvailability);
        bookRepository.save(book);

        if (student.getBorrowRecordIds() == null) {
            student.setBorrowRecordIds(new ArrayList<>());
        }
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
        return null;
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
        return List.of();
    }

    public List<BorrowRecord> findByBookISBN(String isbn) {
        if (isbn == null || isbn.trim().isEmpty()) {
            throw new IllegalArgumentException("Book ISBN cannot be null or empty");
        }
        return borrowRecordRepository.findByIsbn(isbn);
    }

    @Override
    public BorrowRecord returnBook(String studentId, String isbn) {
        if (isbn == null || studentId == null) {
            throw new IllegalArgumentException("Book ISBN and Student ID are required");
        }

        Student student = userRepository.findById(studentId)
                .orElseThrow(() -> new NoSuchElementException("Student not found with ID: " + studentId));
        Book book = bookRepository.findByIsbn(isbn)
                .orElseThrow(() -> new NoSuchElementException("Book not found with ISBN: " + isbn));

        // Find the active borrow record (not returned yet) for the given student and ISBN
        List<BorrowRecord> borrowHistory = borrowRecordRepository.findByStudentId(studentId);
        BorrowRecord activeBorrow = null;

        for (BorrowRecord record : borrowHistory) {
            if (record.getIsbn().equals(isbn) && !record.isReturned()) {
                activeBorrow = record;
                break;
            }
        }

        if (activeBorrow == null) {
            throw new IllegalStateException("Student " + studentId + " has not borrowed this book or already returned it.");
        }

        // Update return status and fine (if any)
        activeBorrow.setReturned(true);
        LocalDate today = LocalDate.now();
        if (today.isAfter(activeBorrow.getDueDate())) {
            long overdueDays = ChronoUnit.DAYS.between(activeBorrow.getDueDate(), today);
            activeBorrow.setFine(overdueDays * 2.0); // e.g., ₹2 per overdue day
        }

        // Mark the specific book copy as available
        int copyIndex = activeBorrow.getCopyIndex();
        List<Boolean> availabilityList = book.getCopyAvailability();
        if (copyIndex >= 0 && copyIndex < availabilityList.size()) {
            availabilityList.set(copyIndex, true);
        } else {
            throw new IllegalStateException("Invalid copy index found in borrow record.");
        }

        // Update book's available copies and save changes
        book.setAvailableCopies(book.getAvailableCopies() + 1);
        book.setCopyAvailability(availabilityList);
        bookRepository.save(book);

        // Save updated borrow record
        borrowRecordRepository.save(activeBorrow);

        return activeBorrow;
    }

    @Override
    public long booksBorrowedCount(String studentId) {
        return borrowRecordRepository.findByStudentId(studentId).stream()
                .filter(record -> !record.isReturned())
                .count();
    }

    @Override
    public BorrowRecord upcomingDueDate(String studentId) {
        return borrowRecordRepository.findByStudentId(studentId).stream()
                .filter(record -> !record.isReturned())                          // Not yet returned
                .filter(record -> record.getDueDate().isAfter(LocalDate.now())) // Only future due dates
                .min(Comparator.comparing(BorrowRecord::getDueDate))            // Earliest due date
                .orElse(null);                                                  // Return null if none
    }


}