package org.zerock.moamoa.domain.entity;

import lombok.*;
import lombok.ToString;
import javax.persistence.*;


import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Table(name= "categories")
@Entity
@Data
public class Category {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private long id;

        @Column(name = "name", length = 20, nullable = false)
        private String name;

//        @ManyToMany
//        @JoinTable(name = "cate_mycate",
//                joinColumns = @JoinColumn(name = "cate_id"),
//                inverseJoinColumns = @JoinColumn(name = "mycate_id"))
//        private List<MyCategory> my_categories = new ArrayList<>();
}

