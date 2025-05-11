package com.nvc.notification_service.enums;

import lombok.Getter;

@Getter
public enum TemplateEnum {
    WELCOME("welcome", "Chào mừng bạn đến với CAHOUSE!"),
    CREATE_MOTEL_SUCCESS("create-motel", "Bạn đã tạo thành công một phòng trọ trên CAHOUSE!"),
    APPOINTMENT("appointment", " Thông báo: Có người đặt lịch xem phòng trọ của bạn");

    private final String value;
    private final String object;

    TemplateEnum(String value, String object) {
        this.value = value;
        this.object = object;
    }
}
