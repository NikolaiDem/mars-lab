package ru.mars.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ReportField implements EnumDeserializable {
    DATE, STATE, AUTHOR_ID;

    public static ReportField fromString(String name) {
        return EnumDeserializable.from(ReportField.class, name);
    }
}
