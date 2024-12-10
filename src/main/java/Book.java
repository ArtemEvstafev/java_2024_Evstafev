import java.util.List;

public class Book {
    public String title;
    public String author;
    public int pages;
    public List<String> genres;
    public String[] tags;

    public Book(String title, String author, int pages, List<String> genres, String[] tags) {
        this.title = title;
        this.author = author;
        this.pages = pages;
        this.genres = genres;
        this.tags = tags;
    }
}
