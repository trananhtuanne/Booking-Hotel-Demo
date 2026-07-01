package uef.edu.vn.bookinghotel.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import uef.edu.vn.bookinghotel.authorization.RoleRequired;
import uef.edu.vn.bookinghotel.service.DashboardStatsService;

@Controller
@RoleRequired({"MANAGER", "ADMIN"})
public class ReportController {

    private final DashboardStatsService dashboardStatsService;

    public ReportController(DashboardStatsService dashboardStatsService) {
        this.dashboardStatsService = dashboardStatsService;
    }

    @GetMapping("/reports")
    public String reports(Model model, HttpSession session) {
        String guard = Access.requireRole(session, "MANAGER", "ADMIN");
        if (guard != null) {
            return guard;
        }

        model.addAttribute("revenue", dashboardStatsService.revenue());
        model.addAttribute("occupancy", dashboardStatsService.occupancy());
        model.addAttribute("bookingStats", dashboardStatsService.bookingStatistics());
        model.addAttribute("topCustomers", dashboardStatsService.topCustomers());
        return "manager/reports/index";
    }
}
