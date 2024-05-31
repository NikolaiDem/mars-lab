package ru.mars.controllers;

import jakarta.annotation.security.RolesAllowed;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import lombok.AllArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import ru.mars.commands.ReportUpdateDto;
import ru.mars.dto.ReportInput;
import ru.mars.dto.ReportResponse;
import ru.mars.entities.Report;
import ru.mars.enums.ReportField;
import ru.mars.enums.ReportFilter;
import ru.mars.enums.ReportSort;
import ru.mars.enums.State;
import ru.mars.mappers.ReportMapper;
import ru.mars.services.MarsService;

@RestController
@RequestMapping("/reports")
@AllArgsConstructor
public class MarsController {

    private MarsService marsService;
    private ReportMapper reportMapper;

    @PostMapping(path = "")
    public ResponseEntity<ReportResponse> add(Authentication authentication,
                                              @RequestPart(value = "report") ReportInput reportInput,
                                              @RequestPart(value = "file", required = false) MultipartFile file) {
        var newReport = reportMapper.toReport(reportInput);
        var savedReport = marsService.addReport(newReport, authentication.getName(), file);

        return ResponseEntity.ok(reportMapper.toReportResponse(savedReport));
    }

    @PutMapping(path = "/{id}")
    public ResponseEntity<ReportResponse> edit(@PathVariable("id") Integer id,
                                               @RequestPart(value = "report") ReportInput reportInput,
                                               @RequestPart(value = "file", required = false) MultipartFile file) {
        ReportInput enrichedReportInput = new ReportInput(id, reportInput.title(), reportInput.userId(), reportInput.state());
        var editedReport = marsService.editReport(enrichedReportInput, file);

        return ResponseEntity.ok().body(reportMapper.toReportResponse(editedReport));
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<ReportResponse> get(@PathVariable("id") Integer id) {
        return ResponseEntity.ok(reportMapper.toReportResponse(marsService.getReportById(id)));
    }

    @GetMapping(path = "/list")
    public ResponseEntity<List<ReportResponse>> list(Authentication authentication,
                                                     @RequestParam(value = "state", required = false) String state,
                                                     @RequestParam(value = "author_id", required = false) String authorId,
                                                     @RequestParam(value = "state_sort", required = false) String stateSort,
                                                     @RequestParam(value = "date_sort", required = false) String dateSort,
                                                     @RequestParam(defaultValue = "0") int page,
                                                     @RequestParam(defaultValue = "10") int size) {
        ReportSort stateSorting = ReportSort.from(stateSort);
        ReportSort dateSorting = ReportSort.from(dateSort);
        List<ReportSort> sortList = Stream.of(stateSorting, dateSorting)
            .filter(r -> r != ReportSort.EMPTY)
            .toList();

        ReportFilter stateFilter = ReportFilter.from(ReportField.STATE, state);
        ReportFilter authorIdFilter = ReportFilter.from(ReportField.AUTHOR_ID, authorId);
        List<ReportFilter> filterList = Stream.of(stateFilter, authorIdFilter)
            .filter(r -> r != ReportFilter.EMPTY)
            .toList();;

        PageRequest pageable = PageRequest.of(page, size);

        List<Report> reports = marsService.findBy(authentication.getName(), sortList, filterList, pageable);

        return ResponseEntity.ok(reports.stream()
            .map(reportMapper::toReportResponse)
            .collect(Collectors.toList()));
    }

    @RolesAllowed("INSPECTOR")
    @PutMapping(path = "/{id}/{state}")
    public ResponseEntity<ReportResponse> complete(@PathVariable("id") Integer id, @PathVariable String state, @RequestBody String comment) {
        var newState = State.fromString(state);
        var reportInfo = new ReportUpdateDto(id, comment);
        Report report = marsService.completeReport(reportInfo, newState);

        return ResponseEntity.ok(reportMapper.toReportResponse(report));
    }

    @GetMapping(path = "/{id}/file")
    public ResponseEntity<Resource> download(@PathVariable("id") Integer id) {
        byte[] file = marsService.download(id);
        Report report = marsService.getReportById(id);
        ByteArrayResource resource = new ByteArrayResource(file);

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .header("Content-Disposition", "attachment; filename=" + report.getFileName())
                .body(resource);
    }
}
