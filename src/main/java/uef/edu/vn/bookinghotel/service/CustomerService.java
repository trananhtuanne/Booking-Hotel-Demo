package uef.edu.vn.bookinghotel.service;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Service;
import uef.edu.vn.bookinghotel.model.Customer;
import uef.edu.vn.bookinghotel.model.User;

@Service
public class CustomerService extends BaseService {

    private final UserService userService;

    public CustomerService(JdbcTemplate jdbc, UserService userService) {
        super(jdbc);
        this.userService = userService;
    }

    public List<Customer> customers() {
        return jdbc.query("SELECT * FROM customers ORDER BY id", customerMapper);
    }

    public Customer customer(int id) {
        return first(jdbc.query("SELECT * FROM customers WHERE id = ?", customerMapper, id));
    }

    public Customer defaultCustomer() {
        return first(jdbc.query("SELECT * FROM customers ORDER BY id LIMIT 1", customerMapper));
    }

    public Customer customerOf(User user) {
        if (user == null) {
            return null;
        }
        return first(jdbc.query(
                "SELECT * FROM customers WHERE user_id = ? ORDER BY id LIMIT 1",
                customerMapper,
                user.getId()));
    }

    public void saveCustomer(Customer customer) {
        if (customer.getId() == 0) {
            insertCustomer(customer);
            return;
        }

        jdbc.update(
                "UPDATE customers SET user_id=?, full_name=?, email=?, phone=?, identity_number=?, address=? WHERE id=?",
                customer.getUserId(),
                customer.getFullName(),
                customer.getEmail(),
                customer.getPhone(),
                customer.getIdentityNumber(),
                customer.getAddress(),
                customer.getId());
    }

    public Customer createBookingCustomer(Customer source, String fullName, String email, String phone) {
        Customer customer = new Customer();
        if (source != null) {
            customer.setUserId(source.getUserId());
            customer.setIdentityNumber(source.getIdentityNumber());
            customer.setAddress(source.getAddress());
        }

        customer.setFullName(blank(fullName) && source != null ? source.getFullName() : fullName);
        customer.setEmail(blank(email) && source != null ? source.getEmail() : email);
        customer.setPhone(blank(phone) && source != null ? source.getPhone() : phone);
        return insertCustomer(customer);
    }

    private Customer insertCustomer(Customer customer) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbc.update(connection -> {
            PreparedStatement statement = connection.prepareStatement(
                    "INSERT INTO customers (user_id, full_name, email, phone, identity_number, address) "
                    + "VALUES (?, ?, ?, ?, ?, ?)",
                    Statement.RETURN_GENERATED_KEYS);
            statement.setObject(1, customer.getUserId());
            statement.setString(2, customer.getFullName());
            statement.setString(3, customer.getEmail());
            statement.setString(4, customer.getPhone());
            statement.setString(5, customer.getIdentityNumber());
            statement.setString(6, customer.getAddress());
            return statement;
        }, keyHolder);

        Number id = keyHolder.getKey();
        if (id != null) {
            customer.setId(id.intValue());
        }
        return customer;
    }

    public void deleteCustomer(int id) {
        jdbc.update(
                "DELETE FROM customers WHERE id = ? AND NOT EXISTS (SELECT 1 FROM bookings WHERE customer_id = ? AND status <> 'CANCELLED')",
                id,
                id);
    }

    public void updateProfile(User user, Customer customer) {
        userService.updateProfile(user, customer);
        saveCustomer(customer);
    }
}
