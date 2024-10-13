import java.util.List;
import org.junit.jupiter.api.Test;

class ReflectionSerializerJSONTest {
    @Test
    void toJson() throws IllegalAccessException{
        final Book book = new Book(
                "Effective Java",
                "Joshua Bloch",
                416,
                List.of("Programming", "Java"),
                new String[]{"Best Practices", "Java"}
        );
        System.out.println(ReflectionSerializerJSON.toJson(book));
    }
}