package BLMS.service;

import BLMS.model.BorrowRecord;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Optional;

public interface BorrowRecordService {
    BorrowRecord createBorrowRecord(String studentId, String bookId);
    Optional<BorrowRecord> findById(String id);
    List<BorrowRecord> findAllBorrowRecords();
    BorrowRecord updateBorrowRecord(String id, BorrowRecord borrowRecord);
    void deleteBorrowRecord(String id);
    List<BorrowRecord> findByStudentId(String studentId);
    List<BorrowRecord> findByBookId(String bookId);

    BorrowRecord returnBook(String studentId, String bookId);
}