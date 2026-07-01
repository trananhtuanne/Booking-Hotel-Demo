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
import uef.edu.vn.bookinghotel.service.DashboardStatsService;
import uef.edu.vn.bookinghotel.service.UserService;

@Controller
public class AuthController {

    private final DashboardStatsService dashboardStatsService;
    private final UserService userService;

    public AuthController(DashboardStatsService dashboardStatsService, UserService userService) {
        this.dashboardStatsService = dashboardStatsService;
        this.userService = userService;
    }

    @GetMapping({"/", "/dashboard"})
    public String dashboard(Model model) {
        model.addAttribute("stats", dashboardStatsService.stats());
        model.addAttribute("revenue", dashboardStatsService.revenue());
        model.addAttribute("occupancy", dashboardStatsService.occupancy());
        return "public/dashboard/index";
    }

    @GetMapping("/login")
    public String loginForm() {
        return "public/auth/login";
    }

    @PostMapping("/login")
    public String login(
            @RequestParam("username") String username,
            @RequestParam("password") String password,
            HttpSession session,
            Model model) {
        User user = userService.login(username, password);
        if (user == null) {
            model.addAttribute("error", "Invalid username or password.");
            return "public/auth/login";
        }

        session.setAttribute("currentUser", user);
        return "redirect:/dashboard";
    }

    @GetMapping("/register")
    public String registerForm(Model model) {
        model.addAttribute("user", new User());
        return "public/auth/register";
    }

    @PostMapping("/register")
    public String register(@ModelAttribute User user, @RequestParam("password") String password) {
        userService.register(user, password);
        return "redirect:/login?registered=1";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }

    @GetMapping("/access-denied")
    public String denied() {
        return "public/errors/access-denied";
    }

    @GetMapping("/change-password")
    @RoleRequired
    public String changePasswordForm(HttpSession session) {
        String guard = Access.requireLogin(session);
        return guard == null ? "shared/account/change-password" : guard;
    }

    @PostMapping("/change-password")
    @RoleRequired
    public String changePassword(
            @RequestParam("oldPassword") String oldPassword,
            @RequestParam("newPassword") String newPassword,
            HttpSession session,
            Model model) {
        User user = (User) session.getAttribute("currentUser");
        if (user == null) {
            return "redirect:/login";
        }

        boolean ok = userService.changePassword(user, oldPassword, newPassword);
        model.addAttribute(
                ok ? "message" : "error",
                ok ? "Password changed successfully." : "Old password is incorrect.");
        return "shared/account/change-password";
    }

    @GetMapping("/forgot-password")
    public String forgotPasswordForm() {
        return "public/auth/forgot-password";
    }

    @PostMapping("/forgot-password")
    public String forgotPassword(
            @RequestParam("username") String username,
            @RequestParam("email") String email,
            @RequestParam("newPassword") String newPassword,
            Model model) {
        boolean ok = userService.resetPassword(username, email, newPassword);
        model.addAttribute(
                ok ? "message" : "error",
                ok ? "Password reset successfully. Please login again." : "Username and email do not match.");
        return "public/auth/forgot-password";
    }
}
