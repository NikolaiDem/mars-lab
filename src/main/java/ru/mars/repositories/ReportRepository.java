package ru.mars.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.mars.entities.Report;
import ru.mars.enums.State;

import java.util.List;

public interface ReportRepository extends JpaRepository<Report, Integer>, ReportSearchRepository {

    List<Report> findByState(State state);
}
