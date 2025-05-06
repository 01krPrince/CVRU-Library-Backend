package BLMS.model;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Document(collection = "books")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Book {

    @Id
    private String id;

    @NotBlank(message = "Title is required")
    private String title;

    @NotBlank(message = "Author is required")
    private String author;

    @NotBlank(message = "ISBN is required")
    private String isbn;

    private String genre;

    private String imgUrl;

    private String publisher;
    private Integer publicationYear;
    private String language;

    @NotNull(message = "Total copies is required")
    @Min(value = 1, message = "Total copies cannot be negative")
    private int totalCopies;

    @NotNull(message = "Available copies is required")
    @Min(value = 0, message = "Available copies cannot be negative")
    private int availableCopies;

    private String status = "AVAILABLE"; // e.g., "AVAILABLE", "DISCONTINUED"

    @NotNull(message = "Added date is required")
    private LocalDate addedDate;

}