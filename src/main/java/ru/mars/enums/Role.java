package ru.mars.enums;

import java.util.Arrays;

public enum Role {

    SCIENTIST,
    INSPECTOR;

    public static Role fromString(String roleStr) {
        return Arrays.stream(values())
            .filter(e -> e.name().equalsIgnoreCase(roleStr))
            .findFirst()
            .orElseThrow(() -> new IllegalArgumentException(STR."Invalid role: \{roleStr}"));
    }
}
