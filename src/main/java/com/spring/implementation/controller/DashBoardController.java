package com.spring.implementation.controller;


import com.spring.implementation.model.AutomationWorkflowsEntity;
import com.spring.implementation.model.TenantCostSavings;
import com.spring.implementation.service.DashBoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/cloudseal/v1/api")
public class DashBoardController {

    private final DashBoardService dashBoardService;

    @GetMapping("/cost_savings")
    public ResponseEntity<List<TenantCostSavings>> fetchAllTenantCostSavings() {

        return ResponseEntity.ok(dashBoardService.getAllTenantCostSavings());
    }

    @GetMapping("/work_flows")
    public ResponseEntity<List<AutomationWorkflowsEntity>> fetchAllWorkFlows() {

        return ResponseEntity.ok(dashBoardService.getAllWorkFlow());
    }

}