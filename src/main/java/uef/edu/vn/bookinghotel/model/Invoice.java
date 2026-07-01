package uef.edu.vn.bookinghotel.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class Invoice {

    private int bookingId;
    private int id;
    private int nights;
    private String customerName;
    private String paymentMethod;
    private String paymentStatus;
    private String roomNumber;
    private String roomType;
    private BigDecimal amount;
    private BigDecimal roomPricePerNight;
    private LocalDate checkInDate;
    private LocalDate checkOutDate;
    private LocalDateTime checkInAt;
    private LocalDateTime checkOutAt;
    private LocalDateTime issuedAt;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getBookingId() {
        return bookingId;
    }

    public void setBookingId(int bookingId) {
        this.bookingId = bookingId;
    }

    public int getNights() {
        return nights;
    }

    public void setNights(int nights) {
        this.nights = nights;
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

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public BigDecimal getRoomPricePerNight() {
        return roomPricePerNight;
    }

    public void setRoomPricePerNight(BigDecimal roomPricePerNight) {
        this.roomPricePerNight = roomPricePerNight;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
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

    public LocalDateTime getIssuedAt() {
        return issuedAt;
    }

    public void setIssuedAt(LocalDateTime issuedAt) {
        this.issuedAt = issuedAt;
    }
}
