package org.zerock.moamoa.domain.entity;


import lombok.*;

import javax.persistence.*;

@Table(name = "my_categories")
@Entity
@Data
public class MyCategory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name="user_id", nullable = false)
    private User users;
}
