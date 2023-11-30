package com.ssf.baseprogram.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ssf.baseprogram.constant.BaseProgramConst;
import com.ssf.baseprogram.dtos.AuditDto;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.List;

@Data
@SuperBuilder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor
@Document(collection = BaseProgramConst.BASE_PROGRAM_COLLECTION_NAME)
public class BaseProgram extends AuditDto {
    @Id
    private String id;
    private String programType;
    private String activityName;
    public List<String> eventTopics;
    public Schedule schedule;
    public List<Recipient> recipients;
    public List<Invitees> invitees;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME, pattern = BaseProgramConst.ISO_DATE_TIME_FORMAT)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = BaseProgramConst.ISO_DATE_TIME_FORMAT)
    public LocalDateTime meetingDate;
    public String minutes;
    public List<Outcome> outcomes;
}

