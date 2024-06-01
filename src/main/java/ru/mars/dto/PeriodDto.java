package ru.mars.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.mars.utils.IsoLocalDateTimeDeserializer;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class PeriodDto {

    @JsonDeserialize(using = IsoLocalDateTimeDeserializer.class)
    private LocalDateTime from;
    @JsonDeserialize(using = IsoLocalDateTimeDeserializer.class)
    private LocalDateTime to;
}
