package com.ssf.membership.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.google.gson.Gson;
import com.ssf.membership.dtos.Member;
import com.ssf.membership.model.MemberNode;
import com.ssf.membership.model.MemberType;
import com.ssf.membership.utils.Utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MemberFactory {
    Member member;
    MemberNode node;
    Gson gson = new Gson();
    List<Object> list = new ArrayList<>();

    private ObjectMapper mapper = JsonMapper.builder()
            .addModule(new JavaTimeModule())
            .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
            .build();

    public MemberFactory(MemberNode node) {
        this.member = new Member();
        this.node = node;
    }


    public List<Object> getMemberDto() {
        String s;
        if (this.node == null || this.node.getLabels().size() == 0) {
            return new ArrayList<>();
        }
        switch (this.node.getLabels().get(0)) {
            case "AshaWorker":
                s = Utils.joltTransform(gson.toJson(mapper.convertValue(node.getProperties(), Map.class)), MemberType.AshaWorker.toString());
                list.add(gson.fromJson(s, Map.class));
                return this.list;
            case "Citizen":
                s = Utils.joltTransform(gson.toJson(mapper.convertValue(node.getProperties(), Map.class)), MemberType.Citizen.toString());
                list.add(gson.fromJson(s, Map.class));
                return this.list;
            case "MedicalOfficer":
                s = Utils.joltTransform(gson.toJson(mapper.convertValue(node.getProperties(), Map.class)), MemberType.MedicalOfficer.toString());
                list.add(gson.fromJson(s, Map.class));
                return this.list;
            case "JuniorHealthAssistantFemale":
                s = Utils.joltTransform(gson.toJson(mapper.convertValue(node.getProperties(), Map.class)), MemberType.JuniorHealthAssistantFemale.toString());
                list.add(gson.fromJson(s, Map.class));
                return this.list;
            case "JuniorHealthAssistantMale":
                s = Utils.joltTransform(gson.toJson(mapper.convertValue(node.getProperties(), Map.class)), MemberType.JuniorHealthAssistantMale.toString());
                list.add(gson.fromJson(s, Map.class));
                return this.list;
            case "JuniorHealthAssistant":
                s = Utils.joltTransform(gson.toJson(mapper.convertValue(node.getProperties(), Map.class)), MemberType.JuniorHealthAssistant.toString());
                list.add(gson.fromJson(s, Map.class));
                return this.list;
            case "PhcAdministrator":
                s = Utils.joltTransform(gson.toJson(mapper.convertValue(node.getProperties(), Map.class)), MemberType.PhcAdministrator.toString());
                list.add(gson.fromJson(s, Map.class));
                return this.list;
            case "JuniorLabTechnician":
                s = Utils.joltTransform(gson.toJson(mapper.convertValue(node.getProperties(), Map.class)), MemberType.JuniorLabTechnician.toString());
                list.add(gson.fromJson(s, Map.class));
                return this.list;

            default:
                return null;
        }
    }
}
