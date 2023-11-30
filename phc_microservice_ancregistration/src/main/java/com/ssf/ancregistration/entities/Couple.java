package com.ssf.ancregistration.entities;

import com.ssf.ancregistration.constant.ANCRegistrationConst;
import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Document(collection = ANCRegistrationConst.ANC_REG_COLLECTION_NAME)
public class Couple {
    public String husbandId;
    public String husbandName;
    public String husbandPhone;
    public String wifeId;
    public String wifeName;
    public String wifePhone;
    public String address;
    public Integer ecSerialNumber;
    public String registeredBy;
    public String registeredByName;
    public LocalDateTime registeredOn;
    public String rchId;
}
