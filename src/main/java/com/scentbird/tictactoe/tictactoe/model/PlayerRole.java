package com.scentbird.tictactoe.tictactoe.model;

public enum PlayerRole {
    X, O;

    public PlayerRole other() {
        return this == X ? O : X;
    }
}
