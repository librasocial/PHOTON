package com.ssf.product.dtos;

import com.ssf.product.constant.StatusEnum;
import com.ssf.product.entities.Inventory;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.util.List;

@Data
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor
public class ProductDto {
    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    private String id;
    private String name;
    private String group;
    private String code;
    private String classification;
    private String hsnCode;
    private String expiryDateFormat;
    private String primaryUOM;
    private List<UomDto> umo;
    private StatusEnum status;
    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    private AuditDto audit;

}
