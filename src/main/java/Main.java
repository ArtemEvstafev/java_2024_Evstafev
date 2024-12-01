import java.util.List;

public class Main {
    public static void main(String[] args) {
        final Book book = new Book(
                "Effective Java",
                "Joshua Bloch",
                416,
                List.of("Programming", "Java"),
                new String[]{"Best Practices", "Java"}
        );


        String generatedCode = ClassLoaderSerializerJSON.generateSerializer(Book.class);
        System.out.println(generatedCode);
    }
}