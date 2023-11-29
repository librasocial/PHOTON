package com.ssf.baseprogram.dtos;

import lombok.*;

@Data
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor
public class InviteesDto {
	private String name;
	private String email;
	private String phone;
}
