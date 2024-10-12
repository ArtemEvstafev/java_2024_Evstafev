import java.util.List;

public record Book (String title, String author, int pages, List<String> genres, String[] tags) {}
