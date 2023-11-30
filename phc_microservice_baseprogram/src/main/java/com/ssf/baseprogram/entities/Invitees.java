package com.ssf.baseprogram.entities;

import org.springframework.data.mongodb.core.mapping.Document;

import com.ssf.baseprogram.constant.BaseProgramConst;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor
@Document(collection = BaseProgramConst.BASE_PROGRAM_COLLECTION_NAME)
public class Invitees {
	private String name;
	private String email;
	private String phone;
}
