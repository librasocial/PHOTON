package com.ssf.laborders.service;

import com.ssf.laborders.dtos.AuditDTO;
import com.ssf.laborders.dtos.GenderCountDTO;
import com.ssf.laborders.dtos.LabOrdersPageResponse;
import com.ssf.laborders.entities.LabOrder;
import com.ssf.laborders.entities.OrdersSummary;
import com.ssf.laborders.repository.LabOrdersRepository;
import com.ssf.laborders.repository.OrdersSummaryRepository;
import com.ssf.laborders.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class OrdersSummaryService implements IOrdersSummaryService {

    @Autowired
    private OrdersSummaryRepository ordersSummaryRepository;

    @Autowired
    private LabOrdersRepository labOrdersRepository;

    @Override
    public LabOrdersPageResponse getOrdersSummaryByFilter(String startDate, String endDate, Integer page, Integer size) {
        Page<OrdersSummary> labOrderPage;
        if (startDate != null && endDate != null) {
            labOrderPage = ordersSummaryRepository.findAllByOrderDateBetween(Utils.stringToLocalDate(startDate), Utils.stringToLocalDate(endDate), PageRequest.of(page, size));
        } else {
            labOrderPage = ordersSummaryRepository.findAll(PageRequest.of(page, size));
        }
        return LabOrdersPageResponse.builder().totalPages(Long.valueOf(labOrderPage.getTotalPages())).totalElements(labOrderPage.getTotalElements()).content(labOrderPage.getContent()).build();
    }

    @Override
    public OrdersSummary updateOrdersSummary(LabOrder order) {
        OrdersSummary summary;

        Optional<OrdersSummary> summaryOptional = ordersSummaryRepository.findByWorkingDay(order.getOrderDate().toLocalDate());
        if (summaryOptional.isPresent()) {
            summary = summaryOptional.get();
            AuditDTO auditDTO = summary.getAudit();
            auditDTO.setModifiedBy(order.getAudit().getModifiedBy());
            summary.setAudit(auditDTO);
        } else {
            summary = new OrdersSummary();
            summary.setAudit(AuditDTO.builder().createdBy(order.getAudit().getModifiedBy())
                    .modifiedBy(order.getAudit().getModifiedBy())
                    .build());
            summary.setWorkingDay(order.getOrderDate().toLocalDate());
        }

        List<GenderCountDTO> genderCounts = labOrdersRepository.findOrdersCountOfGenders(order.getOrderDate());

        Long orderCount = 0L;

        for (GenderCountDTO genderCountDTO : genderCounts) {
            orderCount = orderCount + genderCountDTO.getCount();
            switch (genderCountDTO.getId()) {
                case "Male":
                    summary.setMalePatientCount(genderCountDTO.getCount());
                    break;
                case "Female":
                    summary.setFemalePatientCount(genderCountDTO.getCount());
                    break;
                case "Transgender":
                    summary.setTransgenderPatientCount(genderCountDTO.getCount());
                    break;
                default:
                    break;
            }
        }

        summary.setOrdersCount(orderCount);

        return ordersSummaryRepository.save(summary);
    }

}
