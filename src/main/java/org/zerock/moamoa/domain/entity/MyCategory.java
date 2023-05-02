package org.zerock.moamoa.domain.entity;

import lombok.*;
import javax.persistence.*;

public class MyCategory {

    @Entity
    @Table(name = "my_categories")
    public class My_categories {
        @Id
        @GeneratedValue
        private Long id;

        @OneToOne
        @JoinColumn(name="user_id", nullable = false)
        private User users;

        @ManyToOne
        @JoinColumn(name="id2", nullable = true)
        private Category categories;

    }
}
