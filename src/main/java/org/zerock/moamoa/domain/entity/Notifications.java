package org.zerock.moamoa.domain.entity;

import lombok.ToString;
import javax.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name= "notifications")
@ToString
public class Notifications {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 11, nullable = false)
    private Long sender_id;
    @Column(length = 11, nullable = false)
    private Long receiver_id;

    @Column(length = 254, nullable = false)
    private String message;

    @Column(length = 2, nullable = false)
    private boolean read_or_not;

    @Column(length = 32, nullable = false)
    private String type;

    @Column(length = 11, nullable = false)
    private Long reference_id;


    @Column(name = "send_date", nullable = false)
    private LocalDateTime sendDate;

    @PrePersist
    protected void onCreate() {
        this.sendDate = LocalDateTime.now();
    }
}