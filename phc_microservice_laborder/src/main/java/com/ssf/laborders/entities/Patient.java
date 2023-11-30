package com.ssf.laborders.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ssf.laborders.constants.Constant;
import com.ssf.laborders.constants.Genders;
import lombok.Data;

import java.time.LocalDate;

@Data
public class Patient {
    private String patientId;

    private String citizenId;

    private String healthId;

    private String uhid;

    private String name;

    private Genders gender;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Constant.ISO_DATE_FORMAT)
    private LocalDate dob;

    private String phone;
}
