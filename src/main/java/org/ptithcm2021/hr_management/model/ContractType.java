package org.ptithcm2021.hr_management.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "contractTypes")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ContractType {
    @Id
    private String id;

    @Column(unique = true, nullable = false)
    private String name;

    private String duration;

    @Column(nullable = false)
    private boolean isPolicy; // với hợp đồng thỉnh giảng
}
