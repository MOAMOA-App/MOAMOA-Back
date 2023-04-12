package org.zerock.moamoa.entity;

import lombok.ToString;
import javax.persistence.*;
@Entity
@Table(name= "User")
@ToString
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long UID;

    @Column(length = 200, nullable = false)
    private String Nickname;
}