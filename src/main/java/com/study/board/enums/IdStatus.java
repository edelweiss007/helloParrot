package com.study.board.enums;

public enum IdStatus {
    ALREADY_EXIST(0),
    SUCCESS(1);

    private final int status;

    IdStatus(int i) {
        this.status = i;
    }

    public int getStatus() {
        return status;
    }

}
