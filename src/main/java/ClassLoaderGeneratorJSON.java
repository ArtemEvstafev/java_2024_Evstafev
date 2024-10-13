import java.lang.reflect.Field;
import java.util.Collection;

public class ClassLoaderGeneratorJSON {
    public static String generateSerializer(Class<?> clazz) {

        StringBuilder sb = new StringBuilder();

        sb.append("public class GeneratedSerializerJSON {\n")
          .append("\tpublic static String toJson(").append(clazz.getName()).append(" obj) throws IllegalAccessException {\n")
          .append("\tif (obj == null) {\n")
          .append("\t\treturn \"null\";\n")
          .append("\t}\n")
          .append("\tStringBuilder jsonBuilder = new StringBuilder();\n")
          .append("\tjsonBuilder.append(\"{\\n\\t\");\n\n");
        boolean first = true;
        for (Field field : clazz.getDeclaredFields()) {

            if (!first) {
                sb.append("\tjsonBuilder.append(\",\\n\\t\");\n\n");
            }
            first = false;

            field.setAccessible(true);
            String fieldName = field.getName();

            sb.append("\tjsonBuilder.append(\"\\\"").append(fieldName).append("\\\": \");\n");
            if (field.getType().isArray()) {
                appendArray(fieldName, sb);
            }
            else if (Collection.class.isAssignableFrom(field.getType())) {
                appendCollection(fieldName, sb);
            } else {
                sb.append("\tappendPrimitive(obj.").append(fieldName).append("(), jsonBuilder);\n");
            }
        }

        sb.append("\tjsonBuilder.append(\"\\n}\");\n");
        sb.append("\treturn jsonBuilder.toString();\n");
        sb.append("\t}\n\n");

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

}