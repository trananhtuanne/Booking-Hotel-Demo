package uef.edu.vn.bookinghotel.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class Booking {

    private int customerId;
    private int id;
    private int roomId;
    private String customerName;
    private String roomNumber;
    private String roomType;
    private String status;
    private LocalDate checkInDate;
    private LocalDate checkOutDate;
    private LocalDateTime checkInAt;
    private LocalDateTime checkOutAt;
    private BigDecimal totalAmount;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public int getRoomId() {
        return roomId;
    }

    public void setRoomId(int roomId) {
        this.roomId = roomId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getRoomNumber() {
        return roomNumber;
    }

    public void setRoomNumber(String roomNumber) {
        this.roomNumber = roomNumber;
    }

    public String getRoomType() {
        return roomType;
    }

    public void setRoomType(String roomType) {
        this.roomType = roomType;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDate getCheckInDate() {
        return checkInAt == null ? checkInDate : checkInAt.toLocalDate();
    }

    public void setCheckInDate(LocalDate checkInDate) {
        this.checkInDate = checkInDate;
        if (checkInDate != null && checkInAt == null) {
            this.checkInAt = checkInDate.atStartOfDay();
        }
    }

    public LocalDate getCheckOutDate() {
        return checkOutAt == null ? checkOutDate : checkOutAt.toLocalDate();
    }

    public void setCheckOutDate(LocalDate checkOutDate) {
        this.checkOutDate = checkOutDate;
        if (checkOutDate != null && checkOutAt == null) {
            this.checkOutAt = checkOutDate.atStartOfDay();
        }
    }

    public LocalDateTime getCheckInAt() {
        return checkInAt;
    }

    public void setCheckInAt(LocalDateTime checkInAt) {
        this.checkInAt = checkInAt;
        this.checkInDate = checkInAt == null ? null : checkInAt.toLocalDate();
    }

    public LocalDateTime getCheckOutAt() {
        return checkOutAt;
    }

    public void setCheckOutAt(LocalDateTime checkOutAt) {
        this.checkOutAt = checkOutAt;
        this.checkOutDate = checkOutAt == null ? null : checkOutAt.toLocalDate();
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }
}
