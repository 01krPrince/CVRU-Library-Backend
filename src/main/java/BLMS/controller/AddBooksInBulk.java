package BLMS.controller;

import BLMS.model.request.AddBookRequest;
import BLMS.service.BookService;
import BLMS.service.impl.BookServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@CrossOrigin("*")
public class AddBooksInBulk {

    @Autowired
    private BookService bookService;

    public static List<AddBookRequest> getBooks() {
        List<AddBookRequest> books = new ArrayList<>();

        books.add(new AddBookRequest(
                "Data Structures Through C",
                "Yashavant Kanetkar",
                "Data Structures",
                "https://m.media-amazon.com/images/I/61m9FWxpYyL._SY425_.jpg",
                "BPB Publications",
                2018,
                "ENGLISH",
                5
        ));

        books.add(new AddBookRequest(
                "Computer Programming in C",
                "V. Rajaraman",
                "Programming",
                "https://m.media-amazon.com/images/I/41dkuJSx29L._AC_UF1000,1000_QL80_.jpg",
                "PHI Learning",
                2013,
                "ENGLISH",
                5
        ));

        books.add(new AddBookRequest(
                "Introduction to Algorithms",
                "Thomas H. Cormen",
                "Algorithms",
                "",
                "PHI Learning",
                2015,
                "ENGLISH",
                4
        ));
        books.add(new AddBookRequest(
                "Operating Systems: Concepts and Design",
                "Milan Milenkovic",
                "Operating Systems",
                "",
                "McGraw Hill India",
                2017,
                "ENGLISH",
                3
        ));
        books.add(new AddBookRequest(
                "Database Management Systems",
                "Raghu Ramakrishnan",
                "Databases",
                "",
                "McGraw Hill India",
                2014,
                "ENGLISH",
                4
        ));
        books.add(new AddBookRequest(
                "Computer Networks",
                "Andrew S. Tanenbaum",
                "Computer Networks",
                "https://encrypted-tbn1.gstatic.com/images?q=tbn:ANd9GcSc_IaZNjqH6zD5zPi0A0zlhOrKyONJk0nCIS5M6CQ9D2uqUylZpadyXHj3bul7TSNUOsGOhirhtuO4ux_GU4KLQxwTTCMGEVxUAuWz9g",
                "Pearson India",
                2016,
                "ENGLISH",
                5
        ));
        books.add(new AddBookRequest(
                "Programming with Java",
                "E. Balagurusamy",
                "Programming",
                "https://m.media-amazon.com/images/I/51xiwRkS2oL._AC_SX296_SY426_FMwebp_QL65_.jpg",
                "McGraw Hill India",
                2019,
                "ENGLISH",
                6
        ));
        books.add(new AddBookRequest(
                "Software Engineering",
                "K.K. Aggarwal",
                "Software Engineering",
                "data:image/webp;base64,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",
                "New Age International",
                2018,
                "ENGLISH",
                3
        ));
        books.add(new AddBookRequest(
                "Artificial Intelligence",
                "Saroj Kaushik",
                "Artificial Intelligence",
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSrY_8zSKnEUqc7cFGkv_VTcLep8QqoXYMrCeaPYYlucYKIXWC-yy9ReeqN8U-W9vuaz-m9Y3VvgX5VlUIyjLGX09pmqqqo89G7Up3VcpG1nQ",
                "Cengage India",
                2020,
                "ENGLISH",
                4
        ));

        books.add(new AddBookRequest(
                "Object-Oriented Programming with C++",
                "E. Balagurusamy",
                "Programming",
                "https://m.media-amazon.com/images/I/71SEZqzWmGL._SY425_.jpg",
                "McGraw Hill India",
                2017,
                "ENGLISH",
                5
        ));

        books.add(new AddBookRequest(
                "Theory of Computation",
                "K.L.P. Mishra",
                "Theory of Computation",
                "https://m.media-amazon.com/images/I/615NvJhKfRL._SY342_.jpg",
                "PHI Learning",
                2015,
                "ENGLISH",
                3
        ));

        books.add(new AddBookRequest(
                "Computer Organization and Architecture",
                "William Stallings",
                "Computer Architecture",
                "https://m.media-amazon.com/images/I/51d7z7z7z7L.jpg",
                "Pearson India",
                2019,
                "ENGLISH",
                4
        ));

        books.add(new AddBookRequest(
                "Data Communications and Networking",
                "Behrouz A. Forouzan",
                "Computer Networks",
                "https://m.media-amazon.com/images/I/41e7z7z7z7L.jpg",
                "McGraw Hill India",
                2016,
                "ENGLISH",
                5
        ));

        books.add(new AddBookRequest(
                "Design and Analysis of Algorithms",
                "S. Sridhar",
                "Algorithms",
                "https://m.media-amazon.com/images/I/51f7z7z7z7L.jpg",
                "Oxford University Press",
                2014,
                "ENGLISH",
                3
        ));

        books.add(new AddBookRequest(
                "Discrete Mathematics and Its Applications",
                "Kenneth H. Rosen",
                "Discrete Mathematics",
                "https://m.media-amazon.com/images/I/41g7z7z7z7L.jpg",
                "McGraw Hill India",
                2018,
                "ENGLISH",
                4
        ));

        books.add(new AddBookRequest(
                "Compiler Design",
                "A.A. Puntambekar",
                "Compiler Design",
                "https://m.media-amazon.com/images/I/51h7z7z7z7L.jpg",
                "Technical Publications",
                2017,
                "ENGLISH",
                3
        ));

        books.add(new AddBookRequest(
                "Web Technologies",
                "Uttam K. Roy",
                "Web Development",
                "https://m.media-amazon.com/images/I/41i7z7z7z7L.jpg",
                "Oxford University Press",
                2016,
                "ENGLISH",
                4
        ));

        books.add(new AddBookRequest(
                "Python Programming",
                "Reema Thareja",
                "Programming",
                "https://m.media-amazon.com/images/I/51j7z7z7z7L.jpg",
                "Oxford University Press",
                2019,
                "ENGLISH",
                5
        ));

        books.add(new AddBookRequest(
                "Computer Graphics",
                "Amarendra N. Sinha",
                "Computer Graphics",
                "https://m.media-amazon.com/images/I/41k7z7z7z7L.jpg",
                "McGraw Hill India",
                2015,
                "ENGLISH",
                3
        ));

        books.add(new AddBookRequest(
                "Machine Learning",
                "Saikat Dutt",
                "Machine Learning",
                "https://m.media-amazon.com/images/I/51l7z7z7z7L.jpg",
                "Pearson India",
                2020,
                "ENGLISH",
                4
        ));

        books.add(new AddBookRequest(
                "Cryptography and Network Security",
                "Atul Kahate",
                "Network Security",
                "https://m.media-amazon.com/images/I/41m7z7z7z7L.jpg",
                "McGraw Hill India",
                2017,
                "ENGLISH",
                3
        ));

        books.add(new AddBookRequest(
                "Digital Logic and Computer Design",
                "M. Morris Mano",
                "Digital Electronics",
                "https://m.media-amazon.com/images/I/51n7z7z7z7L.jpg",
                "Pearson India",
                2016,
                "ENGLISH",
                4
        ));

        books.add(new AddBookRequest(
                "Data Warehousing and Data Mining",
                "Reema Thareja",
                "Data Mining",
                "https://m.media-amazon.com/images/I/41n7z7z7z7L.jpg",
                "Oxford University Press",
                2018,
                "ENGLISH",
                3
        ));

        books.add(new AddBookRequest(
                "Embedded Systems",
                "Raj Kamal",
                "Embedded Systems",
                "https://m.media-amazon.com/images/I/51o7z7z7z7L.jpg",
                "McGraw Hill India",
                2015,
                "ENGLISH",
                4
        ));

        books.add(new AddBookRequest(
                "Cloud Computing",
                "Saurabh Kumar",
                "Cloud Computing",
                "https://m.media-amazon.com/images/I/41p7z7z7z7L.jpg",
                "BPB Publications",
                2019,
                "ENGLISH",
                3
        ));

        books.add(new AddBookRequest(
                "Programming in ANSI C",
                "E. Balagurusamy",
                "Programming",
                "https://m.media-amazon.com/images/I/51q7z7z7z7L.jpg",
                "McGraw Hill India",
                2017,
                "ENGLISH",
                5
        ));

        books.add(new AddBookRequest(
                "Automata Theory and Formal Languages",
                "Shyamalendu Kandar",
                "Theory of Computation",
                "https://m.media-amazon.com/images/I/41r7z7z7z7L.jpg",
                "Pearson India",
                2016,
                "ENGLISH",
                3
        ));

        books.add(new AddBookRequest(
                "Microprocessor Architecture and Programming",
                "Ramesh Gaonkar",
                "Microprocessors",
                "https://m.media-amazon.com/images/I/51s7z7z7z7L.jpg",
                "Penram International",
                2018,
                "ENGLISH",
                4
        ));

        books.add(new AddBookRequest(
                "Software Testing",
                "Yogesh Singh",
                "Software Engineering",
                "https://m.media-amazon.com/images/I/41t7z7z7z7L.jpg",
                "Cambridge University Press",
                2017,
                "ENGLISH",
                3
        ));

        books.add(new AddBookRequest(
                "Big Data Analytics",
                "Parag Kulkarni",
                "Big Data",
                "https://m.media-amazon.com/images/I/51u7z7z7z7L.jpg",
                "PHI Learning",
                2019,
                "ENGLISH",
                4
        ));

        books.add(new AddBookRequest(
                "Internet of Things",
                "Arshdeep Bahga",
                "IoT",
                "https://m.media-amazon.com/images/I/41v7z7z7z7L.jpg",
                "Universities Press",
                2020,
                "ENGLISH",
                3
        ));

        books.add(new AddBookRequest(
                "Computer System Architecture",
                "M. Morris Mano",
                "Computer Architecture",
                "https://m.media-amazon.com/images/I/51w7z7z7z7L.jpg",
                "Pearson India",
                2016,
                "ENGLISH",
                4
        ));

        books.add(new AddBookRequest(
                "Data Structures and Algorithms in Python",
                "Rance D. Necaise",
                "Data Structures",
                "https://m.media-amazon.com/images/I/41x7z7z7z7L.jpg",
                "Wiley India",
                2018,
                "ENGLISH",
                3
        ));

        books.add(new AddBookRequest(
                "Operating System Concepts",
                "Abraham Silberschatz",
                "Operating Systems",
                "https://m.media-amazon.com/images/I/51x7z7z7z7L.jpg",
                "Wiley India",
                2017,
                "ENGLISH",
                4
        ));

        books.add(new AddBookRequest(
                "Advanced Database Management Systems",
                "Rini Chakrabarti",
                "Databases",
                "https://m.media-amazon.com/images/I/41y7z7z7z7L.jpg",
                "Dreamtech Press",
                2016,
                "ENGLISH",
                3
        ));

        books.add(new AddBookRequest(
                "Mobile Computing",
                "Raj Kamal",
                "Mobile Computing",
                "https://m.media-amazon.com/images/I/51y7z7z7z7L.jpg",
                "Oxford University Press",
                2018,
                "ENGLISH",
                4
        ));

        books.add(new AddBookRequest(
                "Parallel Computing",
                "V. Rajaraman",
                "Parallel Computing",
                "https://m.media-amazon.com/images/I/41z7z7z7z7L.jpg",
                "PHI Learning",
                2017,
                "ENGLISH",
                3
        ));

        books.add(new AddBookRequest(
                "Computer Vision",
                "Sunil Kumar",
                "Computer Vision",
                "https://m.media-amazon.com/images/I/51z7z7z7z7L.jpg",
                "BPB Publications",
                2019,
                "ENGLISH",
                4
        ));

        books.add(new AddBookRequest(
                "Distributed Systems",
                "Sukumar Ghosh",
                "Distributed Systems",
                "https://m.media-amazon.com/images/I/41a7z7z7z7L.jpg",
                "CRC Press",
                2016,
                "ENGLISH",
                3
        ));

        books.add(new AddBookRequest(
                "Network Programming",
                "Katta G. Murty",
                "Computer Networks",
                "https://m.media-amazon.com/images/I/51b7z7z7z7L.jpg",
                "PHI Learning",
                2018,
                "ENGLISH",
                4
        ));

        books.add(new AddBookRequest(
                "Software Project Management",
                "Bob Hughes",
                "Software Engineering",
                "https://m.media-amazon.com/images/I/41c7z7z7z7L.jpg",
                "McGraw Hill India",
                2017,
                "ENGLISH",
                3
        ));

        books.add(new AddBookRequest(
                "Deep Learning",
                "Anurag Sharma",
                "Deep Learning",
                "https://m.media-amazon.com/images/I/51d7z7z7z7L.jpg",
                "BPB Publications",
                2020,
                "ENGLISH",
                4
        ));

        books.add(new AddBookRequest(
                "Pattern Recognition",
                "Sankar K. Pal",
                "Pattern Recognition",
                "https://m.media-amazon.com/images/I/41e7z7z7z7L.jpg",
                "World Scientific",
                2016,
                "ENGLISH",
                3
        ));

        books.add(new AddBookRequest(
                "Graph Theory with Applications",
                "Narsingh Deo",
                "Graph Theory",
                "https://m.media-amazon.com/images/I/51f7z7z7z7L.jpg",
                "PHI Learning",
                2018,
                "ENGLISH",
                4
        ));

        books.add(new AddBookRequest(
                "Information Security",
                "Dhiren R. Patel",
                "Cybersecurity",
                "https://m.media-amazon.com/images/I/41g7z7z7z7L.jpg",
                "PHI Learning",
                2017,
                "ENGLISH",
                3
        ));

        books.add(new AddBookRequest(
                "Human-Computer Interaction",
                "K. Meena",
                "HCI",
                "https://m.media-amazon.com/images/I/51h7z7z7z7L.jpg",
                "PHI Learning",
                2019,
                "ENGLISH",
                4
        ));

        books.add(new AddBookRequest(
                "Numerical Methods for Engineers",
                "S.K. Gupta",
                "Numerical Methods",
                "https://m.media-amazon.com/images/I/41i7z7z7z7L.jpg",
                "New Age International",
                2016,
                "ENGLISH",
                3
        ));

        books.add(new AddBookRequest(
                "Bioinformatics",
                "Harshawardhan P. Bal",
                "Bioinformatics",
                "https://m.media-amazon.com/images/I/51j7z7z7z7L.jpg",
                "Oxford University Press",
                2018,
                "ENGLISH",
                4
        ));

        books.add(new AddBookRequest(
                "Natural Language Processing",
                "Tanweer Alam",
                "NLP",
                "https://m.media-amazon.com/images/I/41k7z7z7z7L.jpg",
                "BPB Publications",
                2020,
                "ENGLISH",
                3
        ));

        books.add(new AddBookRequest(
                "Blockchain Technology",
                "Chandramouli Subramanian",
                "Blockchain",
                "https://m.media-amazon.com/images/I/51l7z7z7z7L.jpg",
                "Universities Press",
                2019,
                "ENGLISH",
                4
        ));

        return books;
    }

    @PostMapping("/AddBulkBook")
    public int addBooksInBulk(){
        int count = 0;
        List<AddBookRequest> bookList = getBooks();
        for(AddBookRequest i : bookList) {
            bookService.addBook(i, "6804da3b6755f6426c607317");
            count++;
        }
        return count;
    }



}
