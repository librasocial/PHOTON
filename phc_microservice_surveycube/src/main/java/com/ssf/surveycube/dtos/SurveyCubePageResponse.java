package com.ssf.surveycube.dtos;

import lombok.*;

@Data
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor
public class SurveyCubePageResponse {
    private PageDto meta;
    private Object data;
}
