package com.br.RestAll.dashboard.controller;

import com.br.RestAll.dashboard.dto.DashboardResponse;
import com.br.RestAll.dashboard.dto.PeriodoDashboard;
import com.br.RestAll.dashboard.service.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/dashboard")
@RequiredArgsConstructor
@PreAuthorize("hasAnyRole('ADMINISTRADOR', 'DONO', 'GERENTE')")
public class DashboardController {

    private final DashboardService dashboardService;

    @GetMapping
    public DashboardResponse getDashboard(@RequestParam(defaultValue = "DIA") PeriodoDashboard periodo) {
        return dashboardService.getDashboardData(periodo);
    }
}
