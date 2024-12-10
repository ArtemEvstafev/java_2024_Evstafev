import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ReflectionSerializerJSON<T> implements SerializerJSON<T> {

    private Collection<Field> fields;

    public ReflectionSerializerJSON(Class<T> clazz) {
        if (clazz == null) {
            return;
        }
        fields = List.of(clazz.getFields());
    }

    public String toJson(T obj) {
        if (obj == null) {
            return "null";
        }

        StringBuilder jsonBuilder = new StringBuilder();

        jsonBuilder.append("{\n\t");
        boolean first = true;
        for (Field field : fields) {

            if (!first) {
                jsonBuilder.append(",\n\t");
            }
            first = false;
            jsonBuilder.append("\"").append(field.getName()).append("\": ");
            Object value;
            value = getValueIfValid(obj, field, jsonBuilder);
            if (value == null) continue;
            appendValue(value, jsonBuilder);
        }
        jsonBuilder.append("\n}");
        return jsonBuilder.toString();
    }

    private void appendValue(Object value, StringBuilder jsonBuilder) {
        if (value.getClass().isArray()) {
            appendArray(value, jsonBuilder);
        }
        else if (value instanceof Collection) {
            appendCollection((Collection<?>) value, jsonBuilder);
        }
        else {
            appendPrimitive(value, jsonBuilder);
        }
    }

    private <T> Object getValueIfValid(T obj, Field field, StringBuilder jsonBuilder) {
        Object value;
        try {
            value = field.get(obj);
        } catch (IllegalAccessException e) {
            throw new RuntimeException("object isn't an instance of the class or interface declaring the underlying field or Field is private", e);
        }
        if (value == null) {
            jsonBuilder.append("null");
            return null;
        }
        return value;
    }

    private void appendCollection(Collection<?> value, StringBuilder jsonBuilder) {
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

    private void appendArray(Object value, StringBuilder jsonBuilder) {
        jsonBuilder.append("[");
        for (int i = 0; i < Array.getLength(value); i++) {
            if (i > 0) {
                jsonBuilder.append(", ");
            }
            appendPrimitive(Array.get(value, i), jsonBuilder);
        }
        jsonBuilder.append("]");
    }

    private void appendPrimitive(Object value, StringBuilder jsonBuilder) {
        if (value instanceof Number || value instanceof Boolean) {
            jsonBuilder.append(value);
        } else {
            jsonBuilder.append(String.format("\"%s\"", value.toString()));
        }
    }
}
