package uef.edu.vn.bookinghotel.service;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Service;
import uef.edu.vn.bookinghotel.model.Booking;
import uef.edu.vn.bookinghotel.model.Room;

@Service
public class BookingService extends BaseService {

    private final InvoiceService invoiceService;
    private final RoomService roomService;

    public BookingService(JdbcTemplate jdbc, InvoiceService invoiceService, RoomService roomService) {
        super(jdbc);
        this.invoiceService = invoiceService;
        this.roomService = roomService;
    }

    public Booking book(int customerId, int roomId, LocalDateTime in, LocalDateTime out) {
        prepareBookingAuditStorage();
        Room room = roomService.room(roomId);
        if (room == null || !canBook(roomId, in, out)) {
            return null;
        }

        BigDecimal total = totalAmount(room, in, out);
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbc.update(connection -> {
            PreparedStatement statement = connection.prepareStatement(
                    "INSERT INTO bookings "
                    + "(customer_id, room_id, check_in_date, check_out_date, check_in_at, check_out_at, "
                    + "status, total_amount, total_price, booked_at) "
                    + "VALUES (?, ?, ?, ?, ?, ?, 'BOOKED', ?, ?, ?)",
                    Statement.RETURN_GENERATED_KEYS);
            statement.setInt(1, customerId);
            statement.setInt(2, roomId);
            statement.setDate(3, Date.valueOf(in.toLocalDate()));
            statement.setDate(4, Date.valueOf(out.toLocalDate()));
            statement.setTimestamp(5, Timestamp.valueOf(in));
            statement.setTimestamp(6, Timestamp.valueOf(out));
            statement.setBigDecimal(7, total);
            statement.setBigDecimal(8, total);
            statement.setTimestamp(9, Timestamp.valueOf(LocalDateTime.now()));
            return statement;
        }, keyHolder);

        Number bookingId = keyHolder.getKey();
        if (bookingId == null) {
            return null;
        }
        jdbc.update("UPDATE rooms SET status = 'BOOKED' WHERE id = ?", roomId);
        return booking(bookingId.intValue());
    }

    public boolean canBook(int roomId, LocalDateTime in, LocalDateTime out) {
        prepareBookingAuditStorage();
        Room room = roomService.room(roomId);
        return room != null && !hasOverlap(roomId, in, out);
    }

    public BigDecimal quoteTotal(int roomId, LocalDateTime in, LocalDateTime out) {
        Room room = roomService.room(roomId);
        return room == null ? BigDecimal.ZERO : totalAmount(room, in, out);
    }

    public long billableDays(LocalDateTime in, LocalDateTime out) {
        if (in == null || out == null || !out.isAfter(in)) {
            return 1;
        }

        long minutes = ChronoUnit.MINUTES.between(in, out);
        return Math.max(1, (minutes + 719) / 720);
    }

    public Booking booking(int id) {
        return first(jdbc.query(bookingSql() + " WHERE b.id = ?", bookingMapper, id));
    }

    public List<Booking> bookings() {
        return jdbc.query(bookingSql() + " ORDER BY b.id DESC", bookingMapper);
    }

    public List<Booking> bookingsOf(int customerId) {
        return jdbc.query(
                bookingSql() + " WHERE b.customer_id = ? ORDER BY b.id DESC",
                bookingMapper,
                customerId);
    }

    public List<Booking> bookingsOfUser(int userId) {
        return jdbc.query(
                bookingSql() + " WHERE c.user_id = ? ORDER BY b.id DESC",
                bookingMapper,
                userId);
    }

    public List<Booking> activeBookingsForRoom(int roomId) {
        return jdbc.query(
                bookingSql()
                + " WHERE b.room_id = ? AND b.status IN ('BOOKED','CHECK_IN') "
                + "ORDER BY COALESCE(b.check_in_at, TIMESTAMP(b.check_in_date)), b.id",
                bookingMapper,
                roomId);
    }

    public boolean customerOwnsBooking(int customerId, int bookingId) {
        Integer count = jdbc.queryForObject(
                "SELECT COUNT(*) FROM bookings WHERE id = ? AND customer_id = ?",
                Integer.class,
                bookingId,
                customerId);
        return count != null && count > 0;
    }

    public boolean userOwnsBooking(int userId, int bookingId) {
        Integer count = jdbc.queryForObject(
                "SELECT COUNT(*) FROM bookings b JOIN customers c ON c.id = b.customer_id "
                + "WHERE b.id = ? AND c.user_id = ?",
                Integer.class,
                bookingId,
                userId);
        return count != null && count > 0;
    }

    public void updateBooking(int id, LocalDateTime in, LocalDateTime out) {
        prepareBookingAuditStorage();
        Booking booking = booking(id);
        if (booking == null) {
            return;
        }

        Room room = roomService.room(booking.getRoomId());
        if (room == null) {
            return;
        }
        if (hasOverlap(booking.getRoomId(), in, out, id)) {
            return;
        }

        BigDecimal total = totalAmount(room, in, out);
        jdbc.update(
                "UPDATE bookings SET check_in_date=?, check_out_date=?, check_in_at=?, check_out_at=?, "
                + "total_amount=?, total_price=? WHERE id=?",
                Date.valueOf(in.toLocalDate()),
                Date.valueOf(out.toLocalDate()),
                Timestamp.valueOf(in),
                Timestamp.valueOf(out),
                total,
                total,
                id);
    }

    public void confirmBooking(int id) {
        jdbc.update("UPDATE bookings SET status = 'BOOKED' WHERE id = ? AND status = 'BOOKED'", id);
    }

    public void checkIn(int id) {
        changeStatus(id, "BOOKED", "CHECK_IN", "CHECK_IN");
    }

    public void checkOut(int id) {
        Booking booking = changeStatus(id, "CHECK_IN", "CHECK_OUT", "AVAILABLE");
        if (booking == null) {
            return;
        }
        invoiceService.createInvoice(booking, roomService.room(booking.getRoomId()));
    }

    public void cancel(int id) {
        changeStatus(id, "BOOKED", "CANCELLED", "AVAILABLE");
    }

    private Booking changeStatus(
            int id,
            String expectedBookingStatus,
            String bookingStatus,
            String roomStatus) {
        Booking booking = booking(id);
        if (booking == null || !expectedBookingStatus.equals(booking.getStatus())) {
            return null;
        }

        jdbc.update("UPDATE bookings SET status = ? WHERE id = ?", bookingStatus, id);
        jdbc.update("UPDATE rooms SET status = ? WHERE id = ?", roomStatus, booking.getRoomId());
        return booking;
    }

    private boolean hasOverlap(int roomId, LocalDateTime in, LocalDateTime out) {
        return hasOverlap(roomId, in, out, null);
    }

    private boolean hasOverlap(int roomId, LocalDateTime in, LocalDateTime out, Integer excludedBookingId) {
        StringBuilder sql = new StringBuilder(
                "SELECT COUNT(*) FROM bookings WHERE room_id = ? AND status IN ('BOOKED','CHECK_IN') "
                + "AND COALESCE(check_in_at, TIMESTAMP(check_in_date)) < ? "
                + "AND COALESCE(check_out_at, TIMESTAMP(check_out_date)) > ?");
        List<Object> params = new java.util.ArrayList<>();
        params.add(roomId);
        params.add(Timestamp.valueOf(out));
        params.add(Timestamp.valueOf(in));
        if (excludedBookingId != null) {
            sql.append(" AND id <> ?");
            params.add(excludedBookingId);
        }

        Integer overlaps = jdbc.queryForObject(
                sql.toString(),
                Integer.class,
                params.toArray());
        return overlaps != null && overlaps > 0;
    }

    private BigDecimal totalAmount(Room room, LocalDateTime in, LocalDateTime out) {
        return room.getPricePerNight().multiply(BigDecimal.valueOf(billableDays(in, out)));
    }

    private String bookingSql() {
        prepareBookingAuditStorage();
        return "SELECT b.*, c.full_name AS customer_name, "
                + "COALESCE(rh.old_room_number, r.room_number) AS room_number, "
                + "COALESCE(rh.old_type_name, rt.name) AS room_type "
                + "FROM bookings b "
                + "JOIN customers c ON c.id = b.customer_id "
                + "JOIN rooms r ON r.id = b.room_id "
                + "JOIN room_types rt ON rt.id = r.room_type_id "
                + "LEFT JOIN room_change_history rh ON rh.id = ("
                + "SELECT h.id FROM room_change_history h "
                + "WHERE h.room_id = b.room_id "
                + "AND COALESCE(b.booked_at, b.check_in_at, TIMESTAMP(b.check_in_date)) < h.changed_at "
                + "ORDER BY h.changed_at ASC, h.id ASC LIMIT 1)";
    }

    private void prepareBookingAuditStorage() {
        roomService.ensureRoomChangeHistoryTable();
        ensureColumn("bookings", "check_in_at", "DATETIME NULL");
        ensureColumn("bookings", "check_out_at", "DATETIME NULL");
        ensureColumn("bookings", "total_price", "DECIMAL(12,2) NULL");
        ensureColumn("bookings", "booked_at", "DATETIME NULL");
        jdbc.update("UPDATE bookings SET check_in_at = TIMESTAMP(check_in_date) "
                + "WHERE check_in_at IS NULL AND check_in_date IS NOT NULL");
        jdbc.update("UPDATE bookings SET check_out_at = TIMESTAMP(check_out_date) "
                + "WHERE check_out_at IS NULL AND check_out_date IS NOT NULL");
        jdbc.update("UPDATE bookings SET total_price = total_amount "
                + "WHERE total_price IS NULL AND total_amount IS NOT NULL");
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
