package com.ssf.baseprogram.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ssf.baseprogram.constant.BaseProgramConst;
import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor
@Document(collection = BaseProgramConst.BASE_PROGRAM_COLLECTION_NAME)
public class Schedule {
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE, pattern = BaseProgramConst.ISO_DATE_FORMAT)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = BaseProgramConst.ISO_DATE_FORMAT)
    private LocalDate eventDate;
    private String startTime;
    private String location;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME, pattern = BaseProgramConst.ISO_DATE_TIME_FORMAT)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = BaseProgramConst.ISO_DATE_TIME_FORMAT)
    private LocalDateTime createdDate;
    private String recipientMsgTemplate;
    private String inviteesMsgTemplate;
    private String status;
    private String village;
}
