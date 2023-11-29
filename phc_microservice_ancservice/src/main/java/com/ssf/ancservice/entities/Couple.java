package com.ssf.ancservice.entities;

import lombok.*;

@Data
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Couple {
    private String wifeId;
    private String husbandId;
}
