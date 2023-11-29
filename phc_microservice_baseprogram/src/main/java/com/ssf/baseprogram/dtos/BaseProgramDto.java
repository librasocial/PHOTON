package com.ssf.baseprogram.dtos;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ssf.baseprogram.constant.BaseProgramConst;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor
public class BaseProgramDto {
    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    private String id;
    private String programType;
    private String activityName;
    public List<String> eventTopics;
    public ScheduleDto schedule;
    public List<RecipientDto> recipients;
    public List<InviteesDto> invitees;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME, pattern = BaseProgramConst.ISO_DATE_TIME_FORMAT)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = BaseProgramConst.ISO_DATE_TIME_FORMAT)
    public LocalDateTime meetingDate;
    public String minutes;
    public List<OutcomeDto> outcomes;
    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    public AuditDto audit;
}

