package BLMS.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;

@Document(collection = "borrowRecords")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BorrowRecord {

    @Id
    private String borrowedId;

    @NotBlank(message = "Book ID is required")
    private String bookId;

    @NotBlank(message = "Student ID is required")
    private String studentId;

    @NotNull(message = "Borrow date is required")
    private LocalDate borrowDate;

    @NotNull(message = "Due date is required")
    private LocalDate dueDate;

    private LocalDate returnDate;

    private boolean isReturned = false;

    private double fine;
}