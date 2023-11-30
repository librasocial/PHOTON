package com.ssf.surveycube.repository;


import com.ssf.surveycube.dtos.FilterDto;
import com.ssf.surveycube.dtos.PageDto;
import com.ssf.surveycube.dtos.SurveyCubePageResponse;
import com.ssf.surveycube.entities.SurveyCube;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;
import org.springframework.util.MultiValueMap;

import java.util.ArrayList;
import java.util.List;

@Repository
public class SurveyCubeQueryRepository {

    private static Long LIMIT = 200L;
    @Autowired
    private MongoTemplate template;

    public SurveyCubePageResponse getAllSurveyCube(FilterDto filterDto) {
        List<SurveyCube> list = new ArrayList<>();
        Query query = new Query();

        query.addCriteria(Criteria.where("context.surveyType").in(filterDto.getSurveyType()));

        if (filterDto.getHouseHoldId() != null && !filterDto.getHouseHoldId().isEmpty()) {
            query.addCriteria(Criteria.where("context.household").in(filterDto.getHouseHoldId()));
        }
        if (filterDto.getVillageId() != null && !filterDto.getVillageId().isEmpty()) {
            query.addCriteria(Criteria.where("context.village").in(filterDto.getVillageId()));
        }
        if (filterDto.getCitizenId() != null && !filterDto.getCitizenId().isEmpty()) {
            query.addCriteria(Criteria.where("context.citizen").in(filterDto.getCitizenId()));
        }
        for (MultiValueMap.Entry<String, List<String>> entry : filterDto.getFilters().entrySet()) {
            List<Object> values = new ArrayList<>();
            for (String str : entry.getValue()) {
                if (str.matches("^-?\\d+$")) {
                    values.add(Integer.parseInt(str));
                } else {
                    values.add(str);
                }
            }
            query.addCriteria(Criteria.where(filterDto.getTopics().getTopic() + "." + entry.getKey()).in(values));
        }
        query.fields().include("context");
        Long count = template.count(query, SurveyCube.class);
        Long page = count % LIMIT == 0 ? count / LIMIT : (count / LIMIT) + 1;
        for (long i = 0; i < page; i++) {
            list.addAll(template.find(query.skip(LIMIT * i).limit(Math.toIntExact(LIMIT)), SurveyCube.class));
        }
        return SurveyCubePageResponse.builder().data(list).meta(PageDto.builder().totalPages(count > 0 ? 1L : 0L).totalElements(count).build()).build();
    }


}
