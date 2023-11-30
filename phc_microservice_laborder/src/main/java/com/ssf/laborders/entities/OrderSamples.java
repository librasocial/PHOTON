package com.ssf.laborders.entities;


import com.ssf.laborders.constants.Constant;
import com.ssf.laborders.dtos.AuditDTO;
import lombok.Data;
import nonapi.io.github.classgraph.json.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = Constant.ORDER_SAMPLES_COLLECTION)
public class OrderSamples {

    @Id
    private String id;

    private String orderId;

    private Sample sample;

    private AuditDTO audit;

}
