package ru.mars.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

@AllArgsConstructor
@Getter
public class ReportFilter {

    public static final ReportFilter EMPTY = new ReportFilter(null, null);
    private ReportField field;
    private String value;

    public static ReportFilter from(ReportField field, String value) {
        if (StringUtils.isEmpty(value)) {
            return EMPTY;
        }
        return new ReportFilter(field, value);
    }
}
