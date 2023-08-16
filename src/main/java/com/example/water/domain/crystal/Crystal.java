package com.example.water.domain.crystal;

import com.example.water.domain.comment.Comment;
import com.example.water.domain.user.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.*;

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

    @Column(name="myCrystalCount")
    private Long myCrystalCount;

    @Column(name="red")
    private Long red;

    @Column(name="green")
    private Long green;

    @Column(name="blue")
    private Long blue;

    public static Crystal of(User userId, Long myCrystalCount, Long red, Long green, Long blue) {
        return Crystal.builder()
                .userId(userId)
                .myCrystalCount(myCrystalCount)
                .red(red)
                .green(green)
                .blue(blue)
                .build();
    }
}
