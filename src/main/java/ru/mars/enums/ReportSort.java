package ru.mars.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class ReportSort {

    public static final ReportSort EMPTY = new ReportSort();
    private ReportField reportField;
    private Direction direction;

    public static ReportSort from(String reportSort) {
        if (StringUtils.isEmpty(reportSort)) {
            return EMPTY;
        }
        String[] filedAndDirection = reportSort.split(",");
        var direction = filedAndDirection.length < 2 ? Direction.ASC : Direction.fromString(filedAndDirection[1]);
        return new ReportSort(ReportField.fromString(filedAndDirection[0]), direction);
    }

    @Getter
    @AllArgsConstructor
    public enum Direction implements EnumDeserializable {
        ASC, DESC;

        public static Direction fromString(String name) {
            return EnumDeserializable.from(Direction.class, name);
        }
    }
}
