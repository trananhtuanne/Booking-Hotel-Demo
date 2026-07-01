package uef.edu.vn.bookinghotel.service;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import uef.edu.vn.bookinghotel.model.Booking;
import uef.edu.vn.bookinghotel.model.Invoice;
import uef.edu.vn.bookinghotel.model.Room;

@Service
public class InvoiceService extends BaseService {

    public InvoiceService(JdbcTemplate jdbc) {
        super(jdbc);
    }

    public List<Invoice> invoices() {
        return jdbc.query("SELECT * FROM invoices ORDER BY id DESC", invoiceMapper);
    }

    public Invoice invoice(int id) {
        return first(jdbc.query("SELECT * FROM invoices WHERE id = ?", invoiceMapper, id));
    }

    public Invoice invoiceByBooking(int bookingId) {
        return first(jdbc.query(
                "SELECT * FROM invoices WHERE booking_id = ?",
                invoiceMapper,
                bookingId));
    }

    public List<Invoice> invoicesOf(int customerId) {
        return jdbc.query(
                "SELECT i.* FROM invoices i JOIN bookings b ON b.id = i.booking_id "
                + "WHERE b.customer_id = ? ORDER BY i.id DESC",
                invoiceMapper,
                customerId);
    }

    public List<Invoice> invoicesOfUser(int userId) {
        return jdbc.query(
                "SELECT i.* FROM invoices i "
                + "JOIN bookings b ON b.id = i.booking_id "
                + "JOIN customers c ON c.id = b.customer_id "
                + "WHERE c.user_id = ? ORDER BY i.id DESC",
                invoiceMapper,
                userId);
    }

    public boolean customerOwnsInvoice(int customerId, int invoiceId) {
        Integer count = jdbc.queryForObject(
                "SELECT COUNT(*) FROM invoices i JOIN bookings b ON b.id = i.booking_id "
                + "WHERE i.id = ? AND b.customer_id = ?",
                Integer.class,
                invoiceId,
                customerId);
        return count != null && count > 0;
    }

    public boolean userOwnsInvoice(int userId, int invoiceId) {
        Integer count = jdbc.queryForObject(
                "SELECT COUNT(*) FROM invoices i "
                + "JOIN bookings b ON b.id = i.booking_id "
                + "JOIN customers c ON c.id = b.customer_id "
                + "WHERE i.id = ? AND c.user_id = ?",
                Integer.class,
                invoiceId,
                userId);
        return count != null && count > 0;
    }

    public Invoice createInvoice(Booking booking, Room room) {
        return createInvoice(booking, room, "CASH", "PENDING");
    }

    public Invoice createInvoice(Booking booking, Room room, String paymentMethod, String paymentStatus) {
        if (booking == null) {
            return null;
        }
        ensureInvoiceDateTimeColumns();

        Invoice existing = invoiceByBooking(booking.getId());
        if (existing != null) {
            return existing;
        }

        BigDecimal price = room == null ? BigDecimal.ZERO : room.getPricePerNight();
        LocalDateTime checkInAt = booking.getCheckInAt();
        LocalDateTime checkOutAt = booking.getCheckOutAt();
        long nights = billableDays(checkInAt, checkOutAt);
        jdbc.update(
                "INSERT INTO invoices (booking_id, customer_name, room_number, room_type, check_in_date, check_out_date, "
                + "check_in_at, check_out_at, nights, room_price_per_night, amount, payment_method, payment_status, issued_at) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)",
                booking.getId(),
                booking.getCustomerName(),
                booking.getRoomNumber(),
                booking.getRoomType(),
                Date.valueOf(booking.getCheckInDate()),
                Date.valueOf(booking.getCheckOutDate()),
                Timestamp.valueOf(checkInAt),
                Timestamp.valueOf(checkOutAt),
                (int) nights,
                price,
                booking.getTotalAmount(),
                paymentMethod,
                paymentStatus,
                Timestamp.valueOf(LocalDateTime.now()));
        return invoiceByBooking(booking.getId());
    }

    private long billableDays(LocalDateTime checkInAt, LocalDateTime checkOutAt) {
        if (checkInAt == null || checkOutAt == null || !checkOutAt.isAfter(checkInAt)) {
            return 1;
        }

        long minutes = ChronoUnit.MINUTES.between(checkInAt, checkOutAt);
        return Math.max(1, (minutes + 719) / 720);
    }

    private void ensureInvoiceDateTimeColumns() {
        ensureColumn("invoices", "check_in_at", "DATETIME NULL");
        ensureColumn("invoices", "check_out_at", "DATETIME NULL");
        jdbc.update("UPDATE invoices SET check_in_at = TIMESTAMP(check_in_date) "
                + "WHERE check_in_at IS NULL AND check_in_date IS NOT NULL");
        jdbc.update("UPDATE invoices SET check_out_at = TIMESTAMP(check_out_date) "
                + "WHERE check_out_at IS NULL AND check_out_date IS NOT NULL");
    }

    private void ensureColumn(String tableName, String columnName, String definition) {
        Integer count = jdbc.queryForObject(
                "SELECT COUNT(*) FROM INFORMATION_SCHEMA.COLUMNS "
                + "WHERE TABLE_SCHEMA = DATABASE() AND TABLE_NAME = ? AND COLUMN_NAME = ?",
                Integer.class,
                tableName,
                columnName);
        if (count == null || count == 0) {
            jdbc.execute("ALTER TABLE " + tableName + " ADD COLUMN " + columnName + " " + definition);
        }
    }
}
