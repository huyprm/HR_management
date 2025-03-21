package org.ptithcm2021.hr_management.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.ptithcm2021.hr_management.enums.FormStatusEnum;

import java.time.Instant;

@Entity(name = "requestForms")
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class RequestForm{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String name;
    private Instant sendDate;

    private Instant startDate;
    private Instant endDate;
    private Instant signDate;
    private FormStatusEnum formStatusEnum;
    private String content;
    private String reply;
    private String attachment;

    @ManyToOne
    @JoinColumn(name = "userId")
    private User user;

    @ManyToOne()
    @JoinColumn(name = "userSignId")
    private User userSign;

    @ManyToOne
    @JoinColumn(name = "formTypeId")
    private FormType formType;

}
