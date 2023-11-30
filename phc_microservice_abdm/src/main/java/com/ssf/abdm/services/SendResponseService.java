package com.ssf.abdm.services;

import com.ssf.abdm.constant.ABDMServiceConstant;
import com.ssf.abdm.constant.ABDMUrlConstant;
import com.ssf.abdm.model.sendResponse.carecontext.CareContexts;
import com.ssf.abdm.model.sendResponse.carecontext.Discover;
import com.ssf.abdm.model.sendResponse.common.Patient;
import com.ssf.abdm.model.sendResponse.common.Resp;
import com.ssf.abdm.model.sendResponse.link.Confirm;
import com.ssf.abdm.model.sendResponse.link.Init;
import com.ssf.abdm.model.sendResponse.link.Link;
import com.ssf.abdm.model.sendResponse.link.Meta;
import com.ssf.abdm.model.sendResponse.shareprofile.Acknowledgement;
import com.ssf.abdm.model.sendResponse.shareprofile.Share;
import com.ssf.abdm.utils.ABDMUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Map;
import java.util.UUID;

@Service
public class SendResponseService implements ISendResponseService {

    @Autowired
    private ABDMUtils abdmUtils;

    @Override
    public void sendCareContextDiscoverResponse(Map<String, Object> data) {
        Discover discover = new Discover();
        discover.setTimestamp(LocalDateTime.now(ZoneOffset.UTC).format(DateTimeFormatter.ofPattern(ABDMServiceConstant.ISO_DATE_TIME_FORMAT)));
        discover.setRequestId(UUID.randomUUID().toString());
//        TODO Fetch the data from DB
        discover.setPatient(Patient.builder().matchedBy(Arrays.asList("MOBILE")).display("Akash Godre").referenceNumber("pt-001")
                .careContexts(Arrays.asList(CareContexts.builder().display("visit-001").referenceNumber("v1").build(), CareContexts.builder().display("visit-002").referenceNumber("v2").build())).build());
        discover.setResp(Resp.builder().requestId(data.get("requestId").toString()).build());
        abdmUtils.makeCall(discover, ABDMUrlConstant.CARE_CONTEXT_DISCOVER);
    }

    @Override
    public void sendLinkInItResponse(Map<String, Object> data) {
        Init init = new Init();
        init.setTimestamp(LocalDateTime.now(ZoneOffset.UTC).format(DateTimeFormatter.ofPattern(ABDMServiceConstant.ISO_DATE_TIME_FORMAT)));
        init.setRequestId(UUID.randomUUID().toString());
        init.setTransactionId(data.get("transactionId").toString());
//        TODO Fetch the data from DB
        init.setLink(Link.builder()
                .authenticationType("DIRECT")
                .meta(Meta.builder()
                        .communicationExpiry(LocalDateTime.now(ZoneOffset.UTC).plusDays(10).format(DateTimeFormatter.ofPattern(ABDMServiceConstant.ISO_DATE_TIME_FORMAT)))
                        .communicationHint("+9195XXXX5558").communicationMedium("MOBILE").build())
                .referenceNumber(UUID.randomUUID().toString()).build());
        init.setResp(Resp.builder().requestId(data.get("requestId").toString()).build());
        abdmUtils.makeCall(init, ABDMUrlConstant.CARE_CONTEXT_LINK_INIT);
    }

    @Override
    public void sendLinkConfirmResponse(Map<String, Object> data) {
        Confirm confirm = new Confirm();
        confirm.setTimestamp(LocalDateTime.now(ZoneOffset.UTC).format(DateTimeFormatter.ofPattern(ABDMServiceConstant.ISO_DATE_TIME_FORMAT)));
        confirm.setRequestId(UUID.randomUUID().toString());
//        TODO Fetch the data from DB
        confirm.setPatient(Patient.builder().display("Akash Godre").referenceNumber("pt-001")
                .careContexts(Arrays.asList(CareContexts.builder().display("visit-001").referenceNumber("v1").build(), CareContexts.builder().display("visit-002").referenceNumber("v2").build())).build());
        confirm.setResp(Resp.builder().requestId(data.get("requestId").toString()).build());
        abdmUtils.makeCall(confirm, ABDMUrlConstant.CARE_CONTEXT_LINK_CONFIRM);
    }

    @Override
    public void sendShareProfileResponse(Map<String, Object> data) {
        Share share = new Share();
        share.setTimestamp(LocalDateTime.now(ZoneOffset.UTC).format(DateTimeFormatter.ofPattern(ABDMServiceConstant.ISO_DATE_TIME_FORMAT)));
        share.setRequestId(UUID.randomUUID().toString());
        //        TODO Fetch the data from DB
        share.setAcknowledgement(Acknowledgement.builder().tokenNumber("VISIT-001").healthId("kausthub15629@sbx").status("SUCCESS").build());
        share.setResp(Resp.builder().requestId(data.get("requestId").toString()).build());
        abdmUtils.makeCall(share, ABDMUrlConstant.USER_SHARE_PROFILE);
    }

}
