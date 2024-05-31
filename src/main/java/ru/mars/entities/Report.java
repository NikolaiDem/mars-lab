package ru.mars.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.mars.enums.State;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "reports")
public class Report {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(columnDefinition = "serial")
    private int id;
    private String title;
    private LocalDateTime lastUpdated;
    @ManyToOne
    @JoinColumn(name = "author_id")
    private User author;
    @Enumerated(EnumType.STRING)
    private State state;
    private String fileUuid;
    private String fileName;
    private String comment;
}
