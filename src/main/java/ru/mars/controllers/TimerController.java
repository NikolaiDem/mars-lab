package ru.mars.controllers;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import ru.mars.dto.PeriodDto;
import ru.mars.entities.Report;
import ru.mars.services.TimerService;

@RestController
@RequestMapping
@AllArgsConstructor
public class TimerController {

    private ObjectMapper objectMapper;
    private TimerService timerService;

    @PostMapping(path = "/times")
    public ResponseEntity<Report> addTime(@RequestPart("file") MultipartFile file) throws IOException {
        List<PeriodDto> periodDtos = objectMapper.readValue(new String(file.getBytes(), StandardCharsets.UTF_8), new TypeReference<>() {});
        timerService.addTime(periodDtos);

        return ResponseEntity.ok().build();
    }
}
