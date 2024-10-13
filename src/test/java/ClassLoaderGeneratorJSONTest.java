import net.openhft.compiler.CompilerUtils;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ClassLoaderGeneratorJSONTest {

    @Test
    void generateSerializer() throws Exception {
        final Book book = new Book(
                "Effective Java",
                "Joshua Bloch",
                416,
                List.of("Programming", "Java"),
                new String[]{"Best Practices", "Java"}
        );

        String generatedCode = ClassLoaderGeneratorJSON.generateSerializer(Book.class);
        String className = "GeneratedSerializerJSON";
        Class<?> generatedSerializerJSON = CompilerUtils.CACHED_COMPILER.loadFromJava(className, generatedCode);

        final String json = (String) generatedSerializerJSON.getMethod("toJson", Book.class).invoke(generatedSerializerJSON, book);
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
    void generateSerializerNULL() throws Exception {
        Book book = null;

        String generatedCode = ClassLoaderGeneratorJSON.generateSerializer(Book.class);
        String className = "GeneratedSerializerJSON";
        Class<?> generatedSerializerJSON = CompilerUtils.CACHED_COMPILER.loadFromJava(className, generatedCode);

        final String json = (String) generatedSerializerJSON.getMethod("toJson", Book.class).invoke(generatedSerializerJSON, book);
        final String expected = "null";
        assertEquals(expected, json);
    }
}