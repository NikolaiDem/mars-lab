package ru.mars.commands;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import ru.mars.entities.Report;
import ru.mars.enums.State;
import ru.mars.repositories.ReportRepository;

@AllArgsConstructor
@Component
public class RejectedCommand implements ChangeStateCommand {

    private ReportRepository reportRepository;

    @Override
    public void execute(ChangeStateContext changeStateContext) {
        var completeReportInput = changeStateContext.getReportUpdateDto();
        Report report = reportRepository.findById(completeReportInput.id())
            .orElseThrow(() -> new IllegalArgumentException(STR."No report by id \{completeReportInput.comment()}"));
        if (State.CREATED == report.getState() || State.SENT == report.getState()) {
            throw new IllegalStateException("Report is not available for rejection");
        }
        report.setState(State.REJECTED);
        report.setComment(completeReportInput.comment());
        reportRepository.save(report);
    }

    @Override
    public boolean availableFor(State state) {
        return state == State.REJECTED;
    }
}
