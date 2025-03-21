package org.ptithcm2021.hr_management.enums;
public enum RoleEnum {
    ADMIN("người quản lí toàn bộ"),
    STAFF("nhân viên nhập dữ liệu"),
    MANAGER("người duyệt đơn từ chứng từ trong khoa tương đương với trưởng khoa, phó khoa"),
    USER("giảng viên");

    private final String description;

    RoleEnum(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
