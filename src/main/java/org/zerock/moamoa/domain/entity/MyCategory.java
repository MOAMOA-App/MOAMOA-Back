package org.zerock.moamoa.domain.entity;


import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "my_categories")
@Data
public class MyCategory {
    @Id
    @GeneratedValue
    private Long id;

    @OneToOne
    @JoinColumn(name="user_id", nullable = false)
    private User users;

    @ManyToOne
    @JoinColumn(name="cate_id", nullable = true)
    private Category categories;
}
