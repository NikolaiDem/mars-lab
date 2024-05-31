package ru.mars.mappers;

import org.mapstruct.Builder;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import ru.mars.dto.ReportInput;
import ru.mars.dto.ReportResponse;
import ru.mars.entities.Report;

@Mapper(componentModel = "spring", builder = @Builder(disableBuilder = true), unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ReportMapper {

    ReportResponse toReportResponse(Report report);

    Report toReport(ReportInput reportInput);

}
