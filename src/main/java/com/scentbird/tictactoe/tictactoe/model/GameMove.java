package com.scentbird.tictactoe.tictactoe.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "game_move")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GameMove {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private UUID gameId;

    @Column(length = 9)
    private String prevBoardState;

    @Column(nullable = false, length = 9)
    private String currentBoardState;

    @Column(nullable = false)
    private Integer moveNumber;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 1)
    private PlayerRole currentPlayerRole;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 1)
    private PlayerRole nextMoveRole;

    @Column(nullable = false)
    private String currentInstance;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(nullable = false)
    private LocalDateTime updatedAt;
}

