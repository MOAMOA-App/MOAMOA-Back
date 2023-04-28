package org.zerock.moamoa.domain.entity;

import lombok.ToString;
import javax.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name= "category")
@ToString
public class Category {
        @Id
        @GeneratedValue
        private long id;

        @Column(length = 20, nullable = false)
        private String name;

        @ManyToMany
        @JoinTable(name = "cate_mycate",
                joinColumns = @JoinColumn(name = "cate_id"),
                inverseJoinColumns = @JoinColumn(name = "mycate_id"))
        private List<MyCategory> my_categories = new ArrayList<>();
    }

