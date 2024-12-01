import net.openhft.compiler.CompilerUtils;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ClassLoaderSerializerJSON {

    Collection<Field> fields;

    private static final Map<Class<?>, SerializerJSON<?>> hash = new HashMap<>();

    public static SerializerJSON<?> generateClassLoadeSerializerJSON(Class<?> clazz) {
        return hash.computeIfAbsent(clazz, k -> {
            String generatedHumanCode = ClassLoaderSerializerJSON.generateSerializer(Book.class);
            Class<?> generatedSerializerJSON = null;
            try {
                generatedSerializerJSON = CompilerUtils.CACHED_COMPILER.loadFromJava("GeneratedSerializerJSON", generatedHumanCode);
            } catch (ClassNotFoundException e) {
                throw new RuntimeException("Failed to load the generated class", e);
            }
            try {
                return (SerializerJSON<?>) generatedSerializerJSON.getDeclaredConstructor().newInstance();
            } catch (ReflectiveOperationException e) {
                throw new RuntimeException("Failed to create an instance of the generated class", e);
            }
        });
    }

    public ClassLoaderSerializerJSON(Class<?> clazz) {
        if (clazz == null) {
            return;
        }
        fields = List.of(clazz.getFields());
    }

    public static String generateSerializer(Class<?> clazz) {

        StringBuilder sb = new StringBuilder();

        sb.append("public class GeneratedSerializerJSON implements SerializerJSON<").append(clazz.getName()).append("> {\n")
          .append("\tpublic GeneratedSerializerJSON() {}\n")
          .append("\tpublic String toJson(").append(clazz.getName()).append(" obj) {\n")
          .append("\tif (obj == null) {\n")
          .append("\t\treturn \"null\";\n")
          .append("\t}\n")
          .append("\tStringBuilder jsonBuilder = new StringBuilder();\n")
          .append("\tjsonBuilder.append(\"{\\n\\t\");\n\n");
        boolean first = true;
        for (Field field : clazz.getFields()) {

            if (!first) {
                sb.append("\tjsonBuilder.append(\",\\n\\t\");\n\n");
            }
            first = false;

            String fieldName = field.getName();

            sb.append("\tjsonBuilder.append(\"\\\"").append(fieldName).append("\\\": \");\n");
            var fieldType = field.getType();
            if (fieldType.isArray()) {
                appendArray(fieldName, sb);
            } else if (Collection.class.isAssignableFrom(fieldType)) {
                appendCollection(fieldName, sb);
            } else {
                sb.append("\tappendPrimitive(obj.").append(fieldName).append(", jsonBuilder);\n");
            }
        }

        sb.append("\tjsonBuilder.append(\"\\n}\");\n");
        sb.append("\treturn jsonBuilder.toString();\n");
        sb.append("\t}\n\n");

        appendPrimitive(sb);

        sb.append("}\n");
        return sb.toString();
    }

    private static void appendPrimitive(StringBuilder sb) {
        sb.append("\tprivate void appendPrimitive(Object value, StringBuilder jsonBuilder) {\n")
          .append("\tif (value == null) {\n")
          .append("\t\tjsonBuilder.append(\"null\");\n")
          .append("\t} else if (value instanceof Number || value instanceof Boolean) {\n")
          .append("\t\tjsonBuilder.append(value);\n")
          .append("\t} else {\n")
          .append("\t\tjsonBuilder.append(String.format(\"\\\"%s\\\"\", value.toString()));\n")
          .append("\t}\n")
          .append("\t}\n");
    }

    private static void appendCollection(String fieldName, StringBuilder sb) {
        sb.append("\tjsonBuilder.append(\"[\");\n")
          .append("\tboolean firstInCol = true;\n")
          .append("\tfor (Object element: obj.").append(fieldName).append(") {\n")
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
          .append("\tfor (int i = 0; i < obj.").append(fieldName).append(".length; i++) {\n")
          .append("\t\tif (i > 0) {\n")
          .append("\t\t\tjsonBuilder.append(\", \");\n")
          .append("\t\t}\n")
          .append("\t\tappendPrimitive(obj.").append(fieldName).append("[i], jsonBuilder);\n")
          .append("\t}\n")
          .append("\tjsonBuilder.append(\"]\");\n");
    }

}