package ru.mars.dto;

import java.time.LocalDateTime;

public record ReportResponse(int id, String title, LocalDateTime lastUpdated, UserResponse author, String state, String file, String comment) {

}
