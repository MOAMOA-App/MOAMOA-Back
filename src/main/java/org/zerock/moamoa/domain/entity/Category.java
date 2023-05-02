package org.zerock.moamoa.domain.entity;

import lombok.*;
import lombok.ToString;
import javax.persistence.*;


import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name= "categories")
@ToString
public class Category {
        @Id
        @GeneratedValue
        private long id;

        @Column(length = 20, nullable = false)
        private String name;

//        @ManyToMany
//        @JoinTable(name = "cate_mycate",
//                joinColumns = @JoinColumn(name = "cate_id"),
//                inverseJoinColumns = @JoinColumn(name = "mycate_id"))
//        private List<MyCategory> my_categories = new ArrayList<>();
    }

