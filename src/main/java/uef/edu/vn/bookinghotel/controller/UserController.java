package uef.edu.vn.bookinghotel.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import uef.edu.vn.bookinghotel.authorization.RoleRequired;
import uef.edu.vn.bookinghotel.model.User;
import uef.edu.vn.bookinghotel.service.UserService;

@Controller
@RoleRequired("ADMIN")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/users")
    public String users(Model model, HttpSession session) {
        String guard = Access.requireRole(session, "ADMIN");
        if (guard != null) {
            return guard;
        }

        model.addAttribute("users", userService.users());
        return "admin/users/list";
    }

    @PostMapping("/users")
    public String save(@ModelAttribute User user, HttpSession session) {
        String guard = Access.requireRole(session, "ADMIN");
        if (guard != null) {
            return guard;
        }

        userService.saveUser(user);
        return "redirect:/users";
    }

    @PostMapping("/users/delete")
    public String delete(@RequestParam("id") int id, HttpSession session) {
        String guard = Access.requireRole(session, "ADMIN");
        if (guard != null) {
            return guard;
        }

        userService.deleteUser(id);
        return "redirect:/users";
    }

    @PostMapping("/users/status")
    public String status(
            @RequestParam("id") int id,
            @RequestParam("status") String status,
            HttpSession session) {
        String guard = Access.requireRole(session, "ADMIN");
        if (guard != null) {
            return guard;
        }

        userService.setUserStatus(id, status);
        return "redirect:/users";
    }
}
