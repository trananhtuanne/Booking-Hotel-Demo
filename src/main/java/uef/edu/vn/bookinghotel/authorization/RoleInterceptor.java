package uef.edu.vn.bookinghotel.authorization;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.util.Arrays;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import uef.edu.vn.bookinghotel.model.User;

public class RoleInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        if (!(handler instanceof HandlerMethod handlerMethod)) {
            return true;
        }

        RoleRequired roleRequired = handlerMethod.getMethodAnnotation(RoleRequired.class);
        if (roleRequired == null) {
            roleRequired = handlerMethod.getBeanType().getAnnotation(RoleRequired.class);
        }
        if (roleRequired == null) {
            return true;
        }

        HttpSession session = request.getSession(false);
        User user = session == null ? null : (User) session.getAttribute("currentUser");
        if (user == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return false;
        }

        String[] allowedRoles = roleRequired.value();
        if (allowedRoles.length == 0 || Arrays.stream(allowedRoles).anyMatch(role -> role.equals(user.getRole()))) {
            return true;
        }

        response.sendRedirect(request.getContextPath() + "/access-denied");
        return false;
    }
}
