package ru.mars.repositories;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;
import ru.mars.entities.Report;
import ru.mars.entities.User;
import ru.mars.enums.ReportField;
import ru.mars.enums.ReportFilter;
import ru.mars.enums.ReportSort;
import ru.mars.enums.ReportSort.Direction;
import ru.mars.enums.Role;
import ru.mars.enums.State;

@Repository
@AllArgsConstructor
public class CriteriaReportSearchRepository implements ReportSearchRepository {

    private EntityManager em;

    @Override
    public List<Report> findBy(User user, List<ReportSort> sortList, List<ReportFilter> filterList, PageRequest pageRequest) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Report> cq = cb.createQuery(Report.class);

        Root<Report> book = cq.from(Report.class);
        List<Predicate> predicates = new ArrayList<>();

        if (filterList.isEmpty() && sortList.isEmpty()) {
            predicates.add(cb.notEqual(book.get("state"), State.SENT.name()));
            cq.orderBy(cb.desc(book.get("lastUpdated")));
        }
        if (user.getRole() != Role.INSPECTOR) {
            predicates.add(cb.equal(book.get("author").get("id"), user.getId()));
        }
        filterList.forEach(filterField -> {
            var field = fromField(filterField.getField());
            predicates.add(cb.equal(book.get(field), filterField.getValue()));
        });
        sortList.forEach(sort -> {
            String name = fromField(sort.getReportField());
            if (sort.getDirection() == Direction.ASC) {
                cq.orderBy(cb.asc(book.get(name)));
            } else {
                cq.orderBy(cb.desc(book.get(name)));
            }
        });
        cq.where(predicates.toArray(new Predicate[0]));

        TypedQuery<Report> query = em.createQuery(cq);
        query.setFirstResult(Math.toIntExact(pageRequest.getOffset()));
        query.setMaxResults(pageRequest.getPageSize());

        return query.getResultList();
    }

    private String fromField(ReportField reportField) {
        return switch (reportField) {
            case DATE -> "lastUpdated";
            case AUTHOR_ID -> "author_id";
            case STATE -> "state";
        };
    }
}
