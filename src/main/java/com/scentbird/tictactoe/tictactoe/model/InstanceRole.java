package com.scentbird.tictactoe.tictactoe.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "instance_role")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InstanceRole {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String instance;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 1)
    private PlayerRole role;
}
