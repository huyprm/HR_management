package org.ptithcm2021.hr_management.enums;
public enum RoleEnum {
    ADMIN("người quản lí toàn bộ", 1),
    STAFF("nhân viên nhập dữ liệu", 3),
    MANAGER("người duyệt đơn từ chứng từ trong khoa tương đương với trưởng khoa, phó khoa", 2),
    USER("giảng viên", 3);

    private final String description;
    private final int level;

    RoleEnum(String description, int level) {
        this.description = description;
        this.level = level;
    }

    public String getDescription() {
        return description;
    }

    public int getLevel() {
        return level;
    }
}
