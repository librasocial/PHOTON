package com.ssf.childcareimmunization.dtos;

import java.util.List;

import org.springframework.data.mongodb.core.mapping.Document;

import com.ssf.childcareimmunization.constants.Constants;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor
@Document(collection = Constants.CHILD_CARE_RECOMMENDATION_SERVICE_COLLECTION_NAME)
public class RecommendationsDetailsDto {
	private List<VaccinesDto> vaccines;
	private String targetDiseaseName;
	private String description;
	private String series;
	private String doseNumber;
}
