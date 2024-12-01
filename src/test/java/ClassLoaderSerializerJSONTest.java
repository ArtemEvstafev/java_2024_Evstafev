import net.openhft.compiler.CompilerUtils;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ClassLoaderSerializerJSONTest {

    @Test
    void generateSerializer() {
        final Book book = new Book(
                "Effective Java",
                "Joshua Bloch",
                416,
                List.of("Programming", "Java"),
                new String[]{"Best Practices", "Java"}
        );

//        String generatedCode = ClassLoaderSerializerJSON.generateSerializer(Book.class);
//        Class<?> generatedSerializerJSON = CompilerUtils.CACHED_COMPILER.loadFromJava("GeneratedSerializerJSON", generatedCode);
//        final String json = (String) generatedSerializerJSON.getMethod("toJson", Book.class).invoke(generatedSerializerJSON, book);

        SerializerJSON<Book> classLoadeSerializerJSON = (SerializerJSON<Book>) ClassLoaderSerializerJSON.generateClassLoadeSerializerJSON(Book.class);
        final String json = classLoadeSerializerJSON.toJson(book);

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
    void generateSerializerNULL() {
        Book book = null;

        SerializerJSON<Book> classLoadeSerializerJSON = (SerializerJSON<Book>) ClassLoaderSerializerJSON.generateClassLoadeSerializerJSON(Book.class);
        final String json = classLoadeSerializerJSON.toJson(book);

        final String expected = "null";
        assertEquals(expected, json);
    }
}