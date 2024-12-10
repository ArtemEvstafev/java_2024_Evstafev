import java.util.HashMap;
import java.util.Map;

public class ReflectionSerializerJSONFactory {
    private static final Map<Class<?>, ReflectionSerializerJSON<?>> hash = new HashMap<>();

    public static SerializerJSON<?> generateReflectionSerializerJSON(Class<?> clazz){
        return hash.computeIfAbsent(clazz, ReflectionSerializerJSON::new);
    }
}
