package uef.edu.vn.bookinghotel.service;

import java.sql.PreparedStatement;
import java.sql.Statement;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Service;
import uef.edu.vn.bookinghotel.model.Customer;
import uef.edu.vn.bookinghotel.model.User;

@Service
public class UserService extends BaseService {

    public UserService(JdbcTemplate jdbc) {
        super(jdbc);
    }

    public User login(String username, String password) {
        return first(jdbc.query(
                "SELECT * FROM users WHERE username = ? AND password_hash = ? AND status = 'ACTIVE'",
                userMapper,
                username,
                password));
    }

    public boolean changePassword(User user, String oldPassword, String newPassword) {
        if (user == null) {
            return false;
        }
        return jdbc.update(
                "UPDATE users SET password_hash = ? WHERE id = ? AND password_hash = ?",
                newPassword,
                user.getId(),
                oldPassword) > 0;
    }

    public boolean resetPassword(String username, String email, String newPassword) {
        return jdbc.update(
                "UPDATE users SET password_hash = ? WHERE username = ? AND email = ?",
                newPassword,
                username,
                email) > 0;
    }

    public void register(User user, String password) {
        int userId = insertUser(
                user.getUsername(),
                password,
                user.getFullName(),
                user.getEmail(),
                user.getPhone(),
                "CUSTOMER",
                "ACTIVE");
        jdbc.update(
                "INSERT INTO customers (user_id, full_name, email, phone) VALUES (?, ?, ?, ?)",
                userId,
                user.getFullName(),
                user.getEmail(),
                user.getPhone());
    }

    public java.util.List<User> users() {
        return jdbc.query("SELECT * FROM users ORDER BY id", userMapper);
    }

    public void saveUser(User user) {
        if (user.getId() == 0) {
            insertUser(
                    user.getUsername(),
                    blank(user.getPasswordHash()) ? "123456" : user.getPasswordHash(),
                    user.getFullName(),
                    user.getEmail(),
                    user.getPhone(),
                    blank(user.getRole()) ? "CUSTOMER" : user.getRole(),
                    blank(user.getStatus()) ? "ACTIVE" : user.getStatus());
            return;
        }

        if (blank(user.getPasswordHash())) {
            jdbc.update(
                    "UPDATE users SET username=?, full_name=?, email=?, phone=?, role=?, status=? WHERE id=?",
                    user.getUsername(),
                    user.getFullName(),
                    user.getEmail(),
                    user.getPhone(),
                    user.getRole(),
                    user.getStatus(),
                    user.getId());
            return;
        }

        jdbc.update(
                "UPDATE users SET username=?, password_hash=?, full_name=?, email=?, phone=?, role=?, status=? WHERE id=?",
                user.getUsername(),
                user.getPasswordHash(),
                user.getFullName(),
                user.getEmail(),
                user.getPhone(),
                user.getRole(),
                user.getStatus(),
                user.getId());
    }

    public void updateProfile(User user, Customer customer) {
        if (user == null) {
            return;
        }
        jdbc.update(
                "UPDATE users SET full_name=?, email=?, phone=? WHERE id=?",
                customer.getFullName(),
                customer.getEmail(),
                customer.getPhone(),
                user.getId());
    }

    public void deleteUser(int id) {
        jdbc.update("DELETE FROM users WHERE id = ? AND username <> 'admin'", id);
    }

    public void setUserStatus(int id, String status) {
        jdbc.update("UPDATE users SET status = ? WHERE id = ? AND username <> 'admin'", status, id);
    }

    private int insertUser(
            String username,
            String password,
            String fullName,
            String email,
            String phone,
            String role,
            String status) {
        KeyHolder key = new GeneratedKeyHolder();
        jdbc.update(con -> {
            PreparedStatement ps = con.prepareStatement(
                    "INSERT INTO users (username, password_hash, full_name, email, phone, role, status) VALUES (?, ?, ?, ?, ?, ?, ?)",
                    Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, username);
            ps.setString(2, password);
            ps.setString(3, fullName);
            ps.setString(4, email);
            ps.setString(5, phone);
            ps.setString(6, role);
            ps.setString(7, status);
            return ps;
        }, key);
        return key.getKey().intValue();
    }
}
