package ru.mars.repositories;

import java.util.List;
import org.springframework.data.domain.PageRequest;
import ru.mars.entities.Report;
import ru.mars.entities.User;
import ru.mars.enums.ReportFilter;
import ru.mars.enums.ReportSort;

public interface ReportSearchRepository {

    List<Report> findBy(User user, List<ReportSort> sortList, List<ReportFilter> filterList, PageRequest pageRequest);
}
