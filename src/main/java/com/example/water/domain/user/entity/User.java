package com.example.water.domain.user.entity;

import com.example.water.domain.comment.entity.Comment;
import com.example.water.domain.crystal.entity.Crystal;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
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

    @OneToMany(mappedBy = "userId", cascade = CascadeType.REMOVE)
    private List<Comment> comments = new ArrayList<>();

    @OneToMany(mappedBy = "userId", cascade = CascadeType.REMOVE)
    private List<Crystal> crystals = new ArrayList<>();

    public void setNickname(String newNickname) {
        this.nickname=newNickname;
    }

    public void setImage(String newImage) { this.image=newImage; }
}
