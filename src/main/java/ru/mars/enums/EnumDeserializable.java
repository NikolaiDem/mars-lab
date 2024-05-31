package ru.mars.enums;

import java.util.Arrays;

public interface EnumDeserializable {

    static <T extends Enum<T> & EnumDeserializable> T from(Class<T> tClass, String name) {
        return Arrays.stream(tClass.getEnumConstants())
            .filter(v -> v.name().equalsIgnoreCase(name))
            .findFirst()
            .orElseThrow(() -> new IllegalArgumentException(STR."Invalid field name: \{name}"));
    }
}
