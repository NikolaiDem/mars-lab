package ru.mars.services;

import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.mars.dto.PeriodDto;
import ru.mars.entities.DeliveryPeriod;
import ru.mars.entities.PeriodKey;
import ru.mars.repositories.TimeRepository;

@RequiredArgsConstructor
@Service
public class TimerService {

    private final TimeRepository timeRepository;

    public void addTime(List<PeriodDto> periodDtos) {
        List<DeliveryPeriod> times = periodDtos.stream()
            .map(period -> {
                DeliveryPeriod timeEntity = new DeliveryPeriod();
                PeriodKey periodKey = new PeriodKey();
                periodKey.setFromDate(period.getFrom());
                periodKey.setToDate(period.getTo());
                timeEntity.setPeriodKey(periodKey);

                return timeEntity;
            })
            .collect(Collectors.toList());
        timeRepository.saveAll(times);
    }
}
