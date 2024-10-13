import javax.tools.*;
import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.io.StringWriter;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.security.SecureClassLoader;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class ClassLoaderGeneratorJSON {
    public static String generateSerializer(Object obj) throws IllegalAccessException {

        Class<?> clazz = obj.getClass();
        StringBuilder sb = new StringBuilder();

        sb.append("public class GeneratedSerializerJSON {\n")
          .append("\tpublic static String toJson(").append(clazz.getName()).append(" obj) throws IllegalAccessException {\n")
          .append("\tif (obj == null) {\n")
          .append("\t\treturn \"null\";\n")
          .append("\t}\n")
          .append("\tStringBuilder jsonBuilder = new StringBuilder();\n")
          .append("\tjsonBuilder.append(\"{\\n\\t\");\n");
        boolean first = true;
        for (Field field : clazz.getDeclaredFields()) {

            if (!first) {
                sb.append("\tjsonBuilder.append(\",\\n\\t\");\n");
            }
            first = false;

            field.setAccessible(true);
            field.getType();
            Object value = field.get(obj);
            String fieldName = field.getName();

            sb.append("\tjsonBuilder.append(\"\\\"" + fieldName + "\\\": \");\n");
            if (field.get(obj).getClass().isArray()) {
                appendArray(fieldName, sb);
            }
            else if (value instanceof Collection) {
                appendCollection(fieldName, sb);
            } else {
                sb.append("\tappendPrimitive(obj.").append(fieldName).append("(), jsonBuilder);\n");
            }
        }

        sb.append("\tjsonBuilder.append(\"\\n}\");\n");
        sb.append("\treturn jsonBuilder.toString();\n");
        sb.append("\t}\n");

        sb.append("\tprivate static void appendPrimitive(Object value, StringBuilder jsonBuilder) {\n")
          .append("\tif (value instanceof String) {\n")
          .append("\t\tjsonBuilder.append(\"\\\"\").append(value).append(\"\\\"\");\n")
          .append("\t} else {\n")
          .append("\t\tjsonBuilder.append(value);\n")
          .append("\t}\n")
          .append("\t}\n");

        sb.append("}\n");
        return sb.toString();
    }

    private static void appendCollection(String fieldName, StringBuilder sb) {
        sb.append("\tjsonBuilder.append(\"[\");\n")
          .append("\tboolean firstInCol = true;\n")
          .append("\tfor (Object element: obj.").append(fieldName).append("()) {\n")
          .append("\t\tif (!firstInCol) {\n")
          .append("\t\t\tjsonBuilder.append(\", \");\n")
          .append("\t\t}\n")
          .append("\t\tfirstInCol = false;\n")
          .append("\t\tappendPrimitive(element, jsonBuilder);\n")
          .append("\t}\n")
          .append("\tjsonBuilder.append(\"]\");\n");
    }

    private static void appendArray(String fieldName, StringBuilder sb) {
        sb.append("\tjsonBuilder.append(\"[\");\n")
          .append("\tfor (int i = 0; i < obj.").append(fieldName).append("().length; i++) {\n")
          .append("\t\tif (i > 0) {\n")
          .append("\t\t\tjsonBuilder.append(\", \");\n")
          .append("\t\t}\n")
          .append("\t\tappendPrimitive(obj.").append(fieldName).append("()[i], jsonBuilder);\n")
          .append("\t}\n")
          .append("\tjsonBuilder.append(\"]\");\n");
    }

    // Метод для компиляции и загрузки класса в рантайме
    public static Class<?> compileAndLoadClass(String className, String sourceCode) throws Exception {
        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        DiagnosticCollector<JavaFileObject> diagnostics = new DiagnosticCollector<>();
        JavaFileManager fileManager = new ClassFileManager(compiler.getStandardFileManager(diagnostics, null, null));

        // Создаем исходный файл в памяти
        JavaFileObject javaFileObject = new CharSequenceJavaFileObject(className, sourceCode);
        Iterable<? extends JavaFileObject> compilationUnits = java.util.Collections.singletonList(javaFileObject);
        JavaCompiler.CompilationTask task = compiler.getTask(null, fileManager, diagnostics, null, null, compilationUnits);

        // Компиляция
        if (!task.call()) {
            StringWriter error = new StringWriter();
            for (Diagnostic<? extends JavaFileObject> diagnostic : diagnostics.getDiagnostics()) {
                error.append(diagnostic.toString()).append("\n");
            }
            throw new RuntimeException("Compilation failed:\n" + error.toString());
        }

        // Загрузка скомпилированного класса
        return fileManager.getClassLoader(null).loadClass(className);
    }
}

// Класс для хранения исходного кода в памяти
class CharSequenceJavaFileObject extends SimpleJavaFileObject {
    private final String code;

    public CharSequenceJavaFileObject(String name, String code) {
        super(java.net.URI.create("string:///" + name.replace('.', '/') + JavaFileObject.Kind.SOURCE.extension), JavaFileObject.Kind.SOURCE);
        this.code = code;
    }

    @Override
    public CharSequence getCharContent(boolean ignoreEncodingErrors) {
        return code;
    }
}

// Класс для управления байт-кодом скомпилированных файлов в памяти
class ClassFileManager extends ForwardingJavaFileManager<JavaFileManager> {
    private final Map<String, ByteArrayJavaFileObject> classObjects = new HashMap<>();

    protected ClassFileManager(JavaFileManager fileManager) {
        super(fileManager);
    }

    @Override
    public JavaFileObject getJavaFileForOutput(JavaFileManager.Location location, String className, JavaFileObject.Kind kind, FileObject sibling) {
        ByteArrayJavaFileObject fileObject = new ByteArrayJavaFileObject(className, kind);
        classObjects.put(className, fileObject);
        return fileObject;
    }

    @Override
    public ClassLoader getClassLoader(JavaFileManager.Location location) {
        return new SecureClassLoader() {
            @Override
            protected Class<?> findClass(String name) throws ClassNotFoundException {
                ByteArrayJavaFileObject fileObject = classObjects.get(name);
                if (fileObject == null) {
                    throw new ClassNotFoundException(name);
                }
                byte[] bytes = fileObject.getBytes();
                return defineClass(name, bytes, 0, bytes.length);
            }
        };
    }
}

// Класс для хранения байт-кода в памяти
class ByteArrayJavaFileObject extends SimpleJavaFileObject {
    private final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

    protected ByteArrayJavaFileObject(String name, JavaFileObject.Kind kind) {
        super(java.net.URI.create("bytes:///" + name.replace('.', '/') + kind.extension), kind);
    }

    @Override
    public OutputStream openOutputStream() {
        return byteArrayOutputStream;
    }

    public byte[] getBytes() {
        return byteArrayOutputStream.toByteArray();
    }
}

