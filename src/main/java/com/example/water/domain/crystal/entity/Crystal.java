package com.example.water.domain.crystal.entity;

import com.example.water.domain.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name="CRYSTAL")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor

public class Crystal {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="crystalId")
    private Long crystalId;

    @ManyToOne
    @JoinColumn(name="userId")
    private User userId;

    @Column(name="red")
    private Long red;

    @Column(name="green")
    private Long green;

    @Column(name="blue")
    private Long blue;

    public static Crystal of(User userId, Long red, Long green, Long blue) {
        return Crystal.builder()
                .userId(userId)
                .red(red)
                .green(green)
                .blue(blue)
                .build();
    }
}
