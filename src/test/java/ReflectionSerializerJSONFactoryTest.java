import java.util.List;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;


class ReflectionSerializerJSONFactoryTest {
    @Test
    void toJson() {
        final Book book = new Book(
                "Effective Java",
                "Joshua Bloch",
                416,
                List.of("Programming", "Java"),
                new String[]{"Best Practices", "Java"}
        );
        SerializerJSON<Book> reflectionSerializerJSON = (SerializerJSON<Book>) ReflectionSerializerJSONFactory.generateReflectionSerializerJSON(Book.class);
        final String json = reflectionSerializerJSON.toJson(book);
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
    void toJsonNULL() {
        final Book book = null;
        SerializerJSON<Book> reflectionSerializerJSON = (ReflectionSerializerJSON<Book>) ReflectionSerializerJSONFactory.generateReflectionSerializerJSON(Book.class);
        final String json = reflectionSerializerJSON.toJson(book);
        final String expected = "null";
        assertEquals(expected, json);
    }

    @Test
    void toJsonNull() {
        final Book book = new Book(
                "Effective Java",
                null,
                416,
                List.of("Programming", "Java"),
                new String[]{"Best Practices", "Java"}
        );
        SerializerJSON<Book> reflectionSerializerJSON = (ReflectionSerializerJSON<Book>) ReflectionSerializerJSONFactory.generateReflectionSerializerJSON(Book.class);
        final String json = reflectionSerializerJSON.toJson(book);
        final String expected =
                """
                {
                \t"title": "Effective Java",
                \t"author": null,
                \t"pages": 416,
                \t"genres": ["Programming", "Java"],
                \t"tags": ["Best Practices", "Java"]
                }""";
        assertEquals(expected, json);
    }
}