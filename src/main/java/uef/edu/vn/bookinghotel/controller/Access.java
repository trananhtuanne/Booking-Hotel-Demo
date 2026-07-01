package uef.edu.vn.bookinghotel.controller;

import jakarta.servlet.http.HttpSession;
import java.util.Arrays;
import uef.edu.vn.bookinghotel.model.User;

final class Access {

    private Access() {
    }

    static User user(HttpSession session) {
        return session == null ? null : (User) session.getAttribute("currentUser");
    }

    static boolean loggedIn(HttpSession session) {
        return user(session) != null;
    }

    static boolean has(HttpSession session, String... roles) {
        User user = user(session);
        return user != null && Arrays.stream(roles).anyMatch(role -> role.equals(user.getRole()));
    }

    static String requireLogin(HttpSession session) {
        return loggedIn(session) ? null : "redirect:/login";
    }

    static String requireRole(HttpSession session, String... roles) {
        if (!loggedIn(session)) {
            return "redirect:/login";
        }
        return has(session, roles) ? null : "redirect:/access-denied";
    }
}
