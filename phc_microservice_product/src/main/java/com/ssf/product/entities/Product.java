package com.ssf.product.entities;

import com.ssf.product.constant.ProductConst;
import com.ssf.product.dtos.AuditDto;
import com.ssf.product.dtos.UomDto;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@SuperBuilder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor
@Document(collection = ProductConst.PRODUCT_COLLECTION_NAME)
public class Product extends AuditDto {
    private String id;
    private String name;
    private String group;
    private String code;
    private String classification;
    private String hsnCode;
    private String expiryDateFormat;
    private String primaryUOM;
    private List<Uom> umo;
    private String status;
}
