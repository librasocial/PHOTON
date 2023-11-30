package com.ssf.product.entities;

import com.ssf.product.constant.ProductConst;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor
@Document(collection = ProductConst.PRODUCT_COLLECTION_NAME)
public class Uom {
    private String alternateUOM;
    private String alternateUOMUnit;
    private String equivelentPrimaryUOMUnit;

}
