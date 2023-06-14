package top.offsetmonkey538.offsetconfig538.serialization;

import java.lang.reflect.ParameterizedType;
import java.util.Map;
import top.offsetmonkey538.offsetconfig538.exception.OffsetConfigException;

public interface OffsetConfigSerializer<T> {

    T deserialize(Map<String, Object> entries) throws OffsetConfigException;

    default String getType() {
        return getTypeClass().getName();
    }

    default Class<?> getTypeClass() {
        return (Class<?>) ((ParameterizedType) getClass().getGenericInterfaces()[0]).getActualTypeArguments()[0];
    }
}
