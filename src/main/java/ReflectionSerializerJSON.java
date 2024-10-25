import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.Collection;

public class ReflectionSerializerJSON {

    public static String toJson(Object obj) throws IllegalAccessException {
        if (obj == null) {
            return "null";
        }

        Class<?> clazz = obj.getClass();
        StringBuilder jsonBuilder = new StringBuilder();
        jsonBuilder.append("{\n\t");
        boolean first = true;
        for (Field field : clazz.getDeclaredFields()) {

            if (!first) {
                jsonBuilder.append(",\n\t");
            }
            first = false;

            field.setAccessible(true);
            jsonBuilder.append("\"").append(field.getName()).append("\": ");
            Object value = field.get(obj);

            if (field.get(obj).getClass().isArray()) {
                appendArray(value, jsonBuilder);
            }
            else if (value instanceof Collection) {
                appendCollection((  Collection) value, jsonBuilder);
            }
            else {
                appendPrimitive(value, jsonBuilder);
            }
        }
        jsonBuilder.append("\n}");
        return jsonBuilder.toString();
    }

    private static void appendCollection(Collection value, StringBuilder jsonBuilder) {
        jsonBuilder.append("[");
        boolean firstInCol = true;
        for (Object element : value) {
            if (!firstInCol) {
                jsonBuilder.append(", ");
            }
            firstInCol = false;
            appendPrimitive(element, jsonBuilder);
        }
        jsonBuilder.append("]");
    }

    private static void appendArray(Object value, StringBuilder jsonBuilder) {
        jsonBuilder.append("[");
        for (int i = 0; i < Array.getLength(value); i++) {
            if (i > 0) {
                jsonBuilder.append(", ");
            }
            appendPrimitive(Array.get(value, i), jsonBuilder);
        }
        jsonBuilder.append("]");
    }

    private static void appendPrimitive(Object value, StringBuilder jsonBuilder) {
        if (value instanceof String) {
            jsonBuilder.append("\"").append(value).append("\"");
        } else {
            jsonBuilder.append(value);
        }
    }
}
