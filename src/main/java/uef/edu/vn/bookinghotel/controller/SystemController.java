package uef.edu.vn.bookinghotel.controller;

import jakarta.servlet.http.HttpSession;
import java.time.LocalDateTime;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import uef.edu.vn.bookinghotel.authorization.RoleRequired;

@Controller
@RoleRequired("ADMIN")
public class SystemController {

    @GetMapping("/system")
    public String system(Model model, HttpSession session) {
        String guard = Access.requireRole(session, "ADMIN");
        if (guard != null) {
            return guard;
        }

        model.addAttribute("backupTime", LocalDateTime.now());
        return "admin/system/index";
    }
}
