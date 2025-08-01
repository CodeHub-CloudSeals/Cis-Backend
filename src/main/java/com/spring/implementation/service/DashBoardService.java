package com.spring.implementation.service;
import com.spring.implementation.model.AutomationWorkflowsEntity;
import com.spring.implementation.model.TenantCostSavings;
import com.spring.implementation.repository.TenantCostSavingRepository;
import com.spring.implementation.repository.WorkFlowRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;


@Service
@RequiredArgsConstructor
public class DashBoardService {

    private final TenantCostSavingRepository tenantCostSavingRepository;

    private final WorkFlowRepository workFlowRepository;

    public List<TenantCostSavings> getAllTenantCostSavings() {
        return tenantCostSavingRepository.findAll();
    }

    public List<AutomationWorkflowsEntity> getAllWorkFlow() {
        return workFlowRepository.findAll();
    }


}