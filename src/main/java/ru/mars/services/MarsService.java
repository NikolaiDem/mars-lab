package ru.mars.services;

import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.mars.commands.ChangeStateCommand;
import ru.mars.commands.ChangeStateContext;
import ru.mars.commands.ReportUpdateDto;
import ru.mars.dto.ReportInput;
import ru.mars.entities.Report;
import ru.mars.entities.User;
import ru.mars.enums.ReportFilter;
import ru.mars.enums.ReportSort;
import ru.mars.enums.State;
import ru.mars.exceptions.EntityNotFoundException;
import ru.mars.repositories.ReportRepository;
import ru.mars.repositories.UserRepository;

@AllArgsConstructor
@Service
@Slf4j
public class MarsService {

    private ReportRepository reportRepository;
    private UserRepository userRepository;
    private FileServiceMock fileService;
    private List<ChangeStateCommand> changeStateCommands;

    private void addFile(Report report, MultipartFile multipartFile) {
        if (multipartFile != null) {
            String fileUuid = fileService.saveFile(multipartFile);
            report.setFileUuid(fileUuid);
            report.setFileName(multipartFile.getName());
        }
    }

    public Report addReport(Report report, String username, MultipartFile multipartFile) {
        User user = userRepository.findByUsername(username)
            .orElseThrow(() -> new EntityNotFoundException(STR."User with name \{username} not found"));

        addFile(report, multipartFile);
        report.setLastUpdated(LocalDateTime.now());
        report.setState(State.CREATED);
        report.setAuthor(user);

        return reportRepository.save(report);
    }

    public Report editReport(ReportInput reportInput, MultipartFile multipartFile) {
        var report = reportRepository.findById(reportInput.id())
            .filter(r -> State.CREATED == r.getState() || State.REJECTED == r.getState())
            .orElseThrow(() -> new EntityNotFoundException(STR."Report with id \{reportInput.id()} is not editable"));

        addFile(report, multipartFile);
        report.setLastUpdated(LocalDateTime.now());
        report.setState(State.CREATED);
        report.setTitle(reportInput.title());

        return reportRepository.save(report);
    }

    public Report getReportById(Integer id) {
        return reportRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException(STR."Report with id \{id} is not found"));
    }


    public Report completeReport(ReportUpdateDto reportUpdateDto, State state) {
        var id = reportUpdateDto.id();

        var changeStateContext = new ChangeStateContext(reportUpdateDto);
        changeStateCommands.stream()
            .filter(command -> command.availableFor(state))
            .findFirst()
            .orElseThrow(() -> new IllegalStateException(STR."Change state to \{state} is not allowed"))
            .execute(changeStateContext);

        return reportRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException(STR."Report by id \{id} is not found"));
    }

    public List<Report> findBy(String username, List<ReportSort> sortList, List<ReportFilter> reportFilterList, PageRequest pageRequest) {
        User user = userRepository.findByUsername(username)
            .orElseThrow(() -> new EntityNotFoundException(STR."User with name \{username} not found"));

        return reportRepository.findBy(user, sortList, reportFilterList, pageRequest);
    }

    public byte[] download(Integer id) {
        Report report = getReportById(id);

        return fileService.download(report.getFileUuid());
    }
}
