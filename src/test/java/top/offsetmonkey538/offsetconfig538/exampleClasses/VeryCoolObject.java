package top.offsetmonkey538.offsetconfig538.exampleClasses;

import java.util.Map;
import top.offsetmonkey538.offsetconfig538.exception.OffsetConfigException;
import top.offsetmonkey538.offsetconfig538.serialization.OffsetConfigSerializer;

public record VeryCoolObject(int anInteger, float aFloat, boolean aTrueBoolean, boolean aFalseBoolean, String aString) {

    public static class VeryCoolObjectSerializer implements OffsetConfigSerializer<VeryCoolObject> {
        @Override
        public VeryCoolObject deserialize(Map<String, Object> entries) {
            return new VeryCoolObject(
                    (Integer) entries.get("anInteger"),
                    (Float) entries.get("aFloat"),
                    (Boolean) entries.get("aTrueBoolean"),
                    (Boolean) entries.get("aFalseBoolean"),
                    (String) entries.get("aString")
            );
        }

        @Override
        public void serialize(Map<String, Object> entries, VeryCoolObject value) throws OffsetConfigException {
            entries.put("anInteger", value.anInteger());
            entries.put("aFloat", value.aFloat());
            entries.put("aTrueBoolean", value.aTrueBoolean());
            entries.put("aFalseBoolean", value.aFalseBoolean());
            entries.put("aString", value.aString());
        }
    }
}
