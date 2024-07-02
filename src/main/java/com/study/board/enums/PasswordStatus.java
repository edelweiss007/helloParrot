package com.study.board.enums;

public enum PasswordStatus {

    PASSWORD_MISMATCH(0),
    SUCCESS(1);

    private final int status;

    PasswordStatus(int i) {
        this.status = i;
    }

    public int getStatus() {
        return status;
    }
}
