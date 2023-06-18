package top.offsetmonkey538.offsetconfig538.exampleclasses;

import java.util.Map;
import top.offsetmonkey538.offsetconfig538.exception.OffsetConfigException;
import top.offsetmonkey538.offsetconfig538.serialization.OffsetConfigSerializer;

public record VeryCoolObjectWithObject(int anInteger, VeryCoolObjectWithArray coolObject) {

    public static class VeryCoolObjectSerializer implements OffsetConfigSerializer<VeryCoolObjectWithObject> {
        @Override
        public VeryCoolObjectWithObject deserialize(Map<String, Object> entries) {
            return new VeryCoolObjectWithObject(
                    (Integer) entries.get("anInteger"),
                    (VeryCoolObjectWithArray) entries.get("coolObject")
            );
        }

        @Override
        public void serialize(Map<String, Object> entries, VeryCoolObjectWithObject value) throws OffsetConfigException {
            entries.put("anInteger", value.anInteger());
            entries.put("coolObject", value.coolObject());
        }
    }
}
