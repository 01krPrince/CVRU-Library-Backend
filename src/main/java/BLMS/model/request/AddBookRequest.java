package BLMS.model.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "books")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AddBookRequest {

    @NotBlank(message = "Book title is required")
    private String title;

    @NotBlank(message = "Author name is required")
    private String author;

    @NotBlank(message = "Genre is required")
    private String genre;

    @NotBlank(message = "Image URL is required")
    private String imgUrl;

    @NotBlank(message = "Publisher name is required")
    private String publisher;

    @NotNull(message = "Publication year is required")
    private Integer publicationYear;

    @NotNull(message = "Language (e.g., ENGLISH, HINDI) is required")
    private String language;

    @NotNull(message = "Total number of copies is required")
    @Min(value = 1, message = "Total copies must be at least 1")
    private int totalNumberOfBooks;
}
