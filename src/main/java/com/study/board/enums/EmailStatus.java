package com.study.board.enums;

public enum EmailStatus {
    ALREADY_EXIST(0),
    SUCCESS(1),
    DOESNT_EXIST(2);

    private final int status;

    EmailStatus(int i) {
        this.status = i;
    }

    public int getStatus() {
        return status;
    }
}
