package uef.edu.vn.bookinghotel.service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import uef.edu.vn.bookinghotel.model.Invoice;
import uef.edu.vn.bookinghotel.model.Payment;

@Service
public class PaymentService extends BaseService {

    private final InvoiceService invoiceService;

    public PaymentService(JdbcTemplate jdbc, InvoiceService invoiceService) {
        super(jdbc);
        this.invoiceService = invoiceService;
    }

    public void pay(int id, String method) {
        Invoice invoice = invoiceService.invoice(id);
        if (invoice == null || "PAID".equals(invoice.getPaymentStatus())) {
            return;
        }

        jdbc.update(
                "UPDATE invoices SET payment_method = ?, payment_status = 'PAID' WHERE id = ?",
                method,
                id);
        recordPayment(invoice, method);
    }

    public void recordPayment(Invoice invoice, String method) {
        if (invoice == null) {
            return;
        }

        Integer exists = jdbc.queryForObject(
                "SELECT COUNT(*) FROM payments WHERE invoice_id = ?",
                Integer.class,
                invoice.getId());
        if (exists != null && exists > 0) {
            return;
        }

        jdbc.update(
                "INSERT INTO payments (invoice_id, customer_name, method, status, amount, paid_at) "
                + "VALUES (?, ?, ?, 'PAID', ?, ?)",
                invoice.getId(),
                invoice.getCustomerName(),
                method,
                invoice.getAmount(),
                Timestamp.valueOf(LocalDateTime.now()));
    }

    public List<Payment> payments() {
        return jdbc.query("SELECT * FROM payments ORDER BY id DESC", paymentMapper);
    }

    public List<Payment> paymentsOf(String customerName) {
        return jdbc.query(
                "SELECT * FROM payments WHERE customer_name = ? ORDER BY id DESC",
                paymentMapper,
                customerName);
    }

    public List<Payment> paymentsOfCustomer(int customerId) {
        return jdbc.query(
                "SELECT p.* FROM payments p "
                + "JOIN invoices i ON i.id = p.invoice_id "
                + "JOIN bookings b ON b.id = i.booking_id "
                + "WHERE b.customer_id = ? ORDER BY p.id DESC",
                paymentMapper,
                customerId);
    }

    public List<Payment> paymentsOfUser(int userId) {
        return jdbc.query(
                "SELECT p.* FROM payments p "
                + "JOIN invoices i ON i.id = p.invoice_id "
                + "JOIN bookings b ON b.id = i.booking_id "
                + "JOIN customers c ON c.id = b.customer_id "
                + "WHERE c.user_id = ? ORDER BY p.id DESC",
                paymentMapper,
                userId);
    }
}
