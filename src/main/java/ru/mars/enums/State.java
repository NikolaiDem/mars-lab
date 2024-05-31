package ru.mars.enums;

public enum State implements EnumDeserializable {
    CREATED,
    APPROVED,
    REJECTED,
    SENT;

    public static State fromString(String stateStr) {
        return EnumDeserializable.from(State.class, stateStr);
    }
}
