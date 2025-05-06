package BLMS.controller;

import BLMS.model.BorrowRecord;
import BLMS.service.BorrowRecordService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/borrow")
@CrossOrigin("*")
public class BorrowRecordController {

    @Autowired
    private BorrowRecordService borrowRecordService;

    @PostMapping("BorrowBook")
    public ResponseEntity<BorrowRecord> createBorrowRecord(@RequestParam String studentId, @RequestParam String bookId) {
        BorrowRecord createdRecord = borrowRecordService.createBorrowRecord(studentId, bookId);
        return new ResponseEntity<>(createdRecord, HttpStatus.CREATED);
    }

    @GetMapping("getRecordById")
    public ResponseEntity<BorrowRecord> getBorrowRecord(@PathVariable String id) {
        Optional<BorrowRecord> borrowRecord = borrowRecordService.findById(id);
        return borrowRecord.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping("getAll")
    public ResponseEntity<List<BorrowRecord>> getAllBorrowRecords() {
        List<BorrowRecord> borrowRecords = borrowRecordService.findAllBorrowRecords();
        return new ResponseEntity<>(borrowRecords, HttpStatus.OK);
    }

    @PutMapping("/updateBy{id}")
    public ResponseEntity<BorrowRecord> updateBorrowRecord(@PathVariable String id, @Valid @RequestBody BorrowRecord borrowRecord) {
        BorrowRecord updatedRecord = borrowRecordService.updateBorrowRecord(id, borrowRecord);
        return new ResponseEntity<>(updatedRecord, HttpStatus.OK);
    }

    @DeleteMapping("/deleteBy{id}")
    public ResponseEntity<Void> deleteBorrowRecord(@PathVariable String id) {
        borrowRecordService.deleteBorrowRecord(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/student/{studentId}")
    public ResponseEntity<List<BorrowRecord>> getBorrowRecordsByStudent(@PathVariable String studentId) {
        List<BorrowRecord> borrowRecords = borrowRecordService.findByStudentId(studentId);
        return new ResponseEntity<>(borrowRecords, HttpStatus.OK);
    }

    @GetMapping("/book/{bookId}")
    public ResponseEntity<List<BorrowRecord>> getBorrowRecordsByBook(@PathVariable String bookId) {
        List<BorrowRecord> borrowRecords = borrowRecordService.findByBookId(bookId);
        return new ResponseEntity<>(borrowRecords, HttpStatus.OK);
    }

    @GetMapping("/returnBook")
    public ResponseEntity<BorrowRecord> returnBook(@RequestParam String studentId, @RequestParam String bookId) {
        BorrowRecord borrowRecords = borrowRecordService.returnBook(studentId, bookId);
        return new ResponseEntity<>(borrowRecords, HttpStatus.OK);
    }
}