package org.ptithcm2021.hr_management.enums;

public enum ContractStatusEnum {
    PENDING,
    EXPIRED,
    TERMINATED,
    RENEWED,
    SIGNED_PENDING_EFFECTIVE, // Hợp đồng đã ký nhưng chưa có hiệu lực, sẽ có hiệu lực từ tháng sau
    ACTIVE, // Hợp đồng đang có hiệu lực
}
