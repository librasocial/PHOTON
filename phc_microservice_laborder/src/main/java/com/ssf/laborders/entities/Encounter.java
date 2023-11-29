package com.ssf.laborders.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ssf.laborders.constants.Constant;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Encounter {
    private String encounterId;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Constant.ISO_DATE_TIME_FORMAT)
    private LocalDateTime encounterDateTime;

    private String staffId;

    private String staffName;
}
