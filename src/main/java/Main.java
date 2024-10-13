import net.openhft.compiler.CompilerUtils;

import java.io.File;
import java.io.IOException;
import java.util.List;
import net.openhft.compiler.CachedCompiler;
import net.openhft.compiler.CompilerUtils;

public class Main {
    public static void main(String[] args) throws Exception {
        final Book book = new Book(
                "Effective Java",
                "Joshua Bloch",
                416,
                List.of("Programming", "Java"),
                new String[]{"Best Practices", "Java"}
        );


        String generatedCode = ClassLoaderGeneratorJSON.generateSerializer(book);
        System.out.println(generatedCode);

        String className = "GeneratedSerializerJSON";

//        Class<?> aClass = CompilerUtils.CACHED_COMPILER.loadFromJava(className, generatedCode);
//        Object js = aClass.getDeclaredConstructor().newInstance();
//        aClass.getMethod("toJson").invoke(book);

        // Компилируем и загружаем класс
        Class<?> serializerClass = ClassLoaderGeneratorJSON.compileAndLoadClass(className, generatedCode);
        // Создаем экземпляр сгенерированного класса
        Object serializerInstance = serializerClass.getDeclaredConstructor().newInstance();
        // Вызываем метод сериализации
        String json = (String) serializerClass.getMethod("toJson", Book.class).invoke(serializerInstance, book);
        // Печатаем результат
        System.out.println(json);
    }
}