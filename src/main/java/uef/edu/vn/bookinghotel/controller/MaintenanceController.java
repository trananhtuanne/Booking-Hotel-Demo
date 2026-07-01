package uef.edu.vn.bookinghotel.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import uef.edu.vn.bookinghotel.authorization.RoleRequired;
import uef.edu.vn.bookinghotel.model.MaintenanceRequest;
import uef.edu.vn.bookinghotel.service.MaintenanceRequestService;
import uef.edu.vn.bookinghotel.service.RoomService;

@Controller
@RoleRequired({"STAFF", "ADMIN"})
public class MaintenanceController {

    private final MaintenanceRequestService maintenanceRequestService;
    private final RoomService roomService;

    public MaintenanceController(
            MaintenanceRequestService maintenanceRequestService,
            RoomService roomService) {
        this.maintenanceRequestService = maintenanceRequestService;
        this.roomService = roomService;
    }

    @GetMapping("/maintenance")
    public String list(Model model, HttpSession session) {
        String guard = Access.requireRole(session, "STAFF", "ADMIN");
        if (guard != null) {
            return guard;
        }

        model.addAttribute("requests", maintenanceRequestService.maintenanceRequests());
        model.addAttribute("rooms", roomService.rooms(null, null, null));
        return "staff/maintenance/list";
    }

    @PostMapping("/maintenance")
    public String create(@ModelAttribute MaintenanceRequest request, HttpSession session) {
        String guard = Access.requireRole(session, "STAFF", "ADMIN");
        if (guard != null) {
            return guard;
        }

        maintenanceRequestService.saveMaintenance(request);
        return "redirect:/maintenance";
    }

    @PostMapping("/maintenance/status")
    public String status(
            @RequestParam("id") int id,
            @RequestParam("status") String status,
            HttpSession session) {
        String guard = Access.requireRole(session, "STAFF", "ADMIN");
        if (guard != null) {
            return guard;
        }

        maintenanceRequestService.updateMaintenanceStatus(id, status);
        return "redirect:/maintenance";
    }
}
