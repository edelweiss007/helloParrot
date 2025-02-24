package com.study.board.enums;

public enum LoginStatus { //로그인 상태
    NOT_AVAILABLE(0),
    SUCCESS(1);

    private final int status;

    LoginStatus(int i) {
        this.status = i;
    }

    public int getStatus() {
        return status;
    }
}
