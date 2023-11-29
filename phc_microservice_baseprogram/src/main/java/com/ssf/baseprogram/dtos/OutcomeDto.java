package com.ssf.baseprogram.dtos;

import lombok.*;

@Data
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor
public class OutcomeDto {
    private String title;
    private String imgUrl;
}
