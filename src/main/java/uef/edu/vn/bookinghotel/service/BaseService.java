package uef.edu.vn.bookinghotel.service;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.List;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import uef.edu.vn.bookinghotel.model.Booking;
import uef.edu.vn.bookinghotel.model.Customer;
import uef.edu.vn.bookinghotel.model.Invoice;
import uef.edu.vn.bookinghotel.model.MaintenanceRequest;
import uef.edu.vn.bookinghotel.model.Payment;
import uef.edu.vn.bookinghotel.model.Room;
import uef.edu.vn.bookinghotel.model.RoomType;
import uef.edu.vn.bookinghotel.model.User;

abstract class BaseService {

    protected final JdbcTemplate jdbc;

    protected BaseService(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    protected final RowMapper<User> userMapper = (rs, rowNum) -> {
        User user = new User();
        user.setId(rs.getInt("id"));
        user.setUsername(rs.getString("username"));
        user.setPasswordHash(rs.getString("password_hash"));
        user.setFullName(rs.getString("full_name"));
        user.setEmail(rs.getString("email"));
        user.setPhone(rs.getString("phone"));
        user.setRole(rs.getString("role"));
        user.setStatus(rs.getString("status"));
        return user;
    };

    protected final RowMapper<Customer> customerMapper = (rs, rowNum) -> {
        Customer customer = new Customer();
        customer.setId(rs.getInt("id"));
        int userId = rs.getInt("user_id");
        customer.setUserId(rs.wasNull() ? null : userId);
        customer.setFullName(rs.getString("full_name"));
        customer.setEmail(rs.getString("email"));
        customer.setPhone(rs.getString("phone"));
        customer.setIdentityNumber(rs.getString("identity_number"));
        customer.setAddress(rs.getString("address"));
        return customer;
    };

    protected final RowMapper<RoomType> roomTypeMapper = (rs, rowNum) -> {
        RoomType roomType = new RoomType();
        roomType.setId(rs.getInt("id"));
        roomType.setName(rs.getString("name"));
        roomType.setDescription(rs.getString("description"));
        roomType.setPricePerNight(rs.getBigDecimal("price_per_night"));
        roomType.setCapacity(rs.getInt("capacity"));
        return roomType;
    };

    protected final RowMapper<Room> roomMapper = (rs, rowNum) -> {
        Room room = new Room();
        room.setId(rs.getInt("id"));
        room.setRoomTypeId(rs.getInt("room_type_id"));
        room.setRoomNumber(rs.getString("room_number"));
        room.setStatus(rs.getString("status"));
        room.setImageUrl(rs.getString("image_url"));
        room.setDescription(rs.getString("description"));
        try {
            room.setTypeName(rs.getString("type_name"));
        } catch (Exception ignored) {
        }
        try {
            room.setPricePerNight(rs.getBigDecimal("price_per_night"));
        } catch (Exception ignored) {
        }
        return room;
    };

    protected final RowMapper<Booking> bookingMapper = (rs, rowNum) -> {
        Booking booking = new Booking();
        booking.setId(rs.getInt("id"));
        booking.setCustomerId(rs.getInt("customer_id"));
        booking.setRoomId(rs.getInt("room_id"));
        Timestamp checkInAt = timestamp(rs, "check_in_at");
        Timestamp checkOutAt = timestamp(rs, "check_out_at");
        Date checkIn = rs.getDate("check_in_date");
        Date checkOut = rs.getDate("check_out_date");
        if (checkInAt != null) {
            booking.setCheckInAt(checkInAt.toLocalDateTime());
        } else {
            booking.setCheckInDate(checkIn == null ? null : checkIn.toLocalDate());
        }
        if (checkOutAt != null) {
            booking.setCheckOutAt(checkOutAt.toLocalDateTime());
        } else {
            booking.setCheckOutDate(checkOut == null ? null : checkOut.toLocalDate());
        }
        booking.setStatus(rs.getString("status"));
        booking.setTotalAmount(rs.getBigDecimal("total_amount"));
        try {
            booking.setCustomerName(rs.getString("customer_name"));
        } catch (Exception ignored) {
        }
        try {
            booking.setRoomNumber(rs.getString("room_number"));
        } catch (Exception ignored) {
        }
        try {
            booking.setRoomType(rs.getString("room_type"));
        } catch (Exception ignored) {
        }
        return booking;
    };

    protected final RowMapper<Invoice> invoiceMapper = (rs, rowNum) -> {
        Invoice invoice = new Invoice();
        invoice.setId(rs.getInt("id"));
        invoice.setBookingId(rs.getInt("booking_id"));
        invoice.setCustomerName(rs.getString("customer_name"));
        invoice.setRoomNumber(rs.getString("room_number"));
        invoice.setRoomType(rs.getString("room_type"));
        Timestamp checkInAt = timestamp(rs, "check_in_at");
        Timestamp checkOutAt = timestamp(rs, "check_out_at");
        Date checkIn = rs.getDate("check_in_date");
        Date checkOut = rs.getDate("check_out_date");
        if (checkInAt != null) {
            invoice.setCheckInAt(checkInAt.toLocalDateTime());
        } else {
            invoice.setCheckInDate(checkIn == null ? null : checkIn.toLocalDate());
        }
        if (checkOutAt != null) {
            invoice.setCheckOutAt(checkOutAt.toLocalDateTime());
        } else {
            invoice.setCheckOutDate(checkOut == null ? null : checkOut.toLocalDate());
        }
        invoice.setNights(rs.getInt("nights"));
        invoice.setRoomPricePerNight(rs.getBigDecimal("room_price_per_night"));
        invoice.setAmount(rs.getBigDecimal("amount"));
        invoice.setPaymentMethod(rs.getString("payment_method"));
        invoice.setPaymentStatus(rs.getString("payment_status"));
        Timestamp issuedAt = rs.getTimestamp("issued_at");
        invoice.setIssuedAt(issuedAt == null ? null : issuedAt.toLocalDateTime());
        return invoice;
    };

    protected final RowMapper<Payment> paymentMapper = (rs, rowNum) -> {
        Payment payment = new Payment();
        payment.setId(rs.getInt("id"));
        payment.setInvoiceId(rs.getInt("invoice_id"));
        payment.setCustomerName(rs.getString("customer_name"));
        payment.setMethod(rs.getString("method"));
        payment.setStatus(rs.getString("status"));
        payment.setAmount(rs.getBigDecimal("amount"));
        Timestamp paidAt = rs.getTimestamp("paid_at");
        payment.setPaidAt(paidAt == null ? null : paidAt.toLocalDateTime());
        return payment;
    };

    protected final RowMapper<MaintenanceRequest> maintenanceRequestMapper = (rs, rowNum) -> {
        MaintenanceRequest request = new MaintenanceRequest();
        request.setId(rs.getInt("id"));
        request.setRoomId(rs.getInt("room_id"));
        request.setRoomNumber(rs.getString("room_number"));
        request.setTitle(rs.getString("title"));
        request.setDescription(rs.getString("description"));
        request.setStatus(rs.getString("status"));
        Timestamp createdAt = rs.getTimestamp("created_at");
        Timestamp updatedAt = rs.getTimestamp("updated_at");
        request.setCreatedAt(createdAt == null ? null : createdAt.toLocalDateTime());
        request.setUpdatedAt(updatedAt == null ? null : updatedAt.toLocalDateTime());
        return request;
    };

    protected <T> T first(List<T> rows) {
        return rows.isEmpty() ? null : rows.get(0);
    }

    protected boolean blank(String value) {
        return value == null || value.isBlank();
    }

    private Timestamp timestamp(java.sql.ResultSet rs, String column) {
        try {
            return rs.getTimestamp(column);
        } catch (Exception ignored) {
            return null;
        }
    }
}
