import java.util.List;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;


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
        final String json = ReflectionSerializerJSON.toJson(book);
        final String expected =
                """
                {
                \t"title": "Effective Java",
                \t"author": "Joshua Bloch",
                \t"pages": 416,
                \t"genres": ["Programming", "Java"],
                \t"tags": ["Best Practices", "Java"]
                }""";
        assertEquals(expected, json);
    }

    @Test
    void toJsonNULL() throws IllegalAccessException{
        final Book book = null;
        final String json = ReflectionSerializerJSON.toJson(book);
        final String expected = "null";
        assertEquals(expected, json);
    }
}