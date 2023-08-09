package com.example.water.domain.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.UniqueElements;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.util.*;

@Entity
@Table(name="USER")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="userId")
    private Long userId;

    @Column(name="email",unique = true, nullable = false)
    private String email;

    @NotEmpty
    @Column(name="nickname")
    private String nickname;

    @NotEmpty
    @Column(name="image")
    private String image;
//
//    @OneToMany(mappedBy = "userId")
//    private List<Comment> comments = new ArrayList();
//
//    @OneToMany(mappedBy = "userId")
//    private List<Crystal> crystals = new ArrayList();


}