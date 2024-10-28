package org.example.enums;

public enum ColumnEnum {
    Column1(0),
    Column2(1),
    Column3(2),
    Column4(3);



    private final int code;

    ColumnEnum(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}
