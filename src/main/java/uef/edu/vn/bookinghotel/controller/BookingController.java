package uef.edu.vn.bookinghotel.controller;

import jakarta.servlet.http.HttpSession;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import uef.edu.vn.bookinghotel.authorization.RoleRequired;
import uef.edu.vn.bookinghotel.model.Booking;
import uef.edu.vn.bookinghotel.model.Customer;
import uef.edu.vn.bookinghotel.model.Invoice;
import uef.edu.vn.bookinghotel.model.Room;
import uef.edu.vn.bookinghotel.model.User;
import uef.edu.vn.bookinghotel.service.BookingService;
import uef.edu.vn.bookinghotel.service.CustomerService;
import uef.edu.vn.bookinghotel.service.InvoiceService;
import uef.edu.vn.bookinghotel.service.PaymentService;
import uef.edu.vn.bookinghotel.service.RoomService;
import uef.edu.vn.bookinghotel.service.RoomTypeService;

@Controller
public class BookingController {

    private final BookingService bookingService;
    private final CustomerService customerService;
    private final InvoiceService invoiceService;
    private final PaymentService paymentService;
    private final RoomService roomService;
    private final RoomTypeService roomTypeService;

    public BookingController(
            BookingService bookingService,
            CustomerService customerService,
            InvoiceService invoiceService,
            PaymentService paymentService,
            RoomService roomService,
            RoomTypeService roomTypeService) {
        this.bookingService = bookingService;
        this.customerService = customerService;
        this.invoiceService = invoiceService;
        this.paymentService = paymentService;
        this.roomService = roomService;
        this.roomTypeService = roomTypeService;
    }

    @GetMapping("/booking/search")
    public String search(
            @RequestParam(value = "checkIn", required = false) String checkIn,
            @RequestParam(value = "checkOut", required = false) String checkOut,
            @RequestParam(value = "roomTypeId", required = false) Integer roomTypeId,
            Model model,
            HttpSession session) {
        LocalDateTime in = parseDateTime(checkIn, defaultCheckIn());
        LocalDateTime out = parseDateTime(checkOut, in.plusHours(12));
        if (!out.isAfter(in)) {
            out = in.plusHours(12);
        }

        model.addAttribute("rooms", roomService.availableRooms(in, out, roomTypeId));
        model.addAttribute("roomTypes", roomTypeService.roomTypes());
        if (Access.has(session, "RECEPTIONIST", "ADMIN")) {
            model.addAttribute("customers", customerService.customers());
        }
        model.addAttribute("checkIn", in);
        model.addAttribute("checkOut", out);
        return "public/booking/search";
    }

    @PostMapping({"/booking/create", "/booking/confirm"})
    @RoleRequired({"CUSTOMER", "RECEPTIONIST", "ADMIN"})
    public String confirmBookingRequest(
            @RequestParam("roomId") int roomId,
            @RequestParam("checkIn") String checkIn,
            @RequestParam("checkOut") String checkOut,
            @RequestParam(value = "customerId", required = false) Integer customerId,
            Model model,
            HttpSession session) {
        String guard = requireBookingAccess(session);
        if (guard != null) {
            return guard;
        }

        LocalDateTime in = parseDateTime(checkIn, defaultCheckIn());
        LocalDateTime out = parseDateTime(checkOut, in.plusHours(12));
        if (!out.isAfter(in)) {
            out = in.plusHours(12);
        }

        Customer customer = bookingCustomer(session, customerId);
        Room room = roomService.room(roomId);
        if (customer == null) {
            return Access.has(session, "CUSTOMER") ? "redirect:/profile" : "redirect:/customers";
        }
        if (room == null) {
            return redirectToSearch(in, out);
        }
        if (!bookingService.canBook(roomId, in, out)) {
            addBookingStepModel(model, room, customer, in, out);
            model.addAttribute("dateError", "This room is already booked for the selected date and time. Please choose another check-in or check-out time.");
            return "public/booking/confirm";
        }

        addBookingStepModel(model, room, customer, in, out);
        return "public/booking/confirm";
    }

    @PostMapping("/booking/payment")
    @RoleRequired({"CUSTOMER", "RECEPTIONIST", "ADMIN"})
    public String payment(
            @RequestParam("roomId") int roomId,
            @RequestParam("checkIn") String checkIn,
            @RequestParam("checkOut") String checkOut,
            @RequestParam(value = "customerId", required = false) Integer customerId,
            @RequestParam("fullName") String fullName,
            @RequestParam("email") String email,
            @RequestParam("phone") String phone,
            Model model,
            HttpSession session) {
        String guard = requireBookingAccess(session);
        if (guard != null) {
            return guard;
        }

        LocalDateTime in = parseDateTime(checkIn, defaultCheckIn());
        LocalDateTime out = parseDateTime(checkOut, in.plusHours(12));
        if (!out.isAfter(in)) {
            out = in.plusHours(12);
        }

        Customer customer = bookingCustomer(session, customerId);
        Room room = roomService.room(roomId);
        if (customer == null) {
            return Access.has(session, "CUSTOMER") ? "redirect:/profile" : "redirect:/customers";
        }
        if (room == null) {
            return redirectToSearch(in, out);
        }

        customer.setFullName(fullName);
        customer.setEmail(email);
        customer.setPhone(phone);

        if (!bookingService.canBook(roomId, in, out)) {
            addBookingStepModel(model, room, customer, in, out);
            model.addAttribute("dateError", "This room is already booked for the selected date and time. Please choose another check-in or check-out time.");
            return "public/booking/confirm";
        }

        addBookingStepModel(model, room, customer, in, out);
        return "public/booking/payment";
    }

    @PostMapping("/booking/complete")
    @RoleRequired({"CUSTOMER", "RECEPTIONIST", "ADMIN"})
    public String completeBooking(
            @RequestParam("roomId") int roomId,
            @RequestParam("checkIn") String checkIn,
            @RequestParam("checkOut") String checkOut,
            @RequestParam(value = "customerId", required = false) Integer customerId,
            @RequestParam("fullName") String fullName,
            @RequestParam("email") String email,
            @RequestParam("phone") String phone,
            @RequestParam(value = "method", defaultValue = "CASH") String method,
            HttpSession session) {
        String guard = requireBookingAccess(session);
        if (guard != null) {
            return guard;
        }

        LocalDateTime in = parseDateTime(checkIn, defaultCheckIn());
        LocalDateTime out = parseDateTime(checkOut, in.plusHours(12));
        if (!out.isAfter(in)) {
            out = in.plusHours(12);
        }

        Customer customer = bookingCustomer(session, customerId);
        if (customer == null) {
            return Access.has(session, "CUSTOMER") ? "redirect:/profile" : "redirect:/customers";
        }

        Room room = roomService.room(roomId);
        if (room == null || !bookingService.canBook(roomId, in, out)) {
            return redirectToSearch(in, out);
        }

        Customer bookingCustomer = customerService.createBookingCustomer(customer, fullName, email, phone);
        Booking booking = bookingService.book(bookingCustomer.getId(), roomId, in, out);
        if (booking == null) {
            return redirectToSearch(in, out);
        }

        String paymentMethod = normalizePaymentMethod(method);
        Invoice invoice = invoiceService.createInvoice(booking, room, paymentMethod, "PAID");
        paymentService.recordPayment(invoice, paymentMethod);
        return Access.has(session, "CUSTOMER") ? "redirect:/my-bookings" : "redirect:/bookings";
    }

    private String requireBookingAccess(HttpSession session) {
        User user = Access.user(session);
        if (user == null) {
            return "redirect:/login";
        }
        return Access.has(session, "CUSTOMER", "RECEPTIONIST", "ADMIN") ? null : "redirect:/access-denied";
    }

    private Customer bookingCustomer(HttpSession session, Integer customerId) {
        if (Access.has(session, "CUSTOMER")) {
            return customerService.customerOf(Access.user(session));
        }
        if (customerId != null) {
            return customerService.customer(customerId);
        }
        return customerService.defaultCustomer();
    }

    private LocalDateTime defaultCheckIn() {
        return LocalDateTime.now().withMinute(0).withSecond(0).withNano(0);
    }

    private LocalDateTime parseDateTime(String value, LocalDateTime fallback) {
        if (value == null || value.isBlank()) {
            return fallback;
        }
        if (value.length() == 10) {
            return LocalDate.parse(value).atStartOfDay();
        }
        return LocalDateTime.parse(value);
    }

    private void addBookingStepModel(
            Model model,
            Room room,
            Customer customer,
            LocalDateTime checkIn,
            LocalDateTime checkOut) {
        long billableDays = bookingService.billableDays(checkIn, checkOut);
        BigDecimal totalAmount = bookingService.quoteTotal(room.getId(), checkIn, checkOut);
        model.addAttribute("room", room);
        model.addAttribute("customer", customer);
        model.addAttribute("checkIn", checkIn);
        model.addAttribute("checkOut", checkOut);
        model.addAttribute("nights", billableDays);
        model.addAttribute("billableDays", billableDays);
        model.addAttribute("totalAmount", totalAmount);
        model.addAttribute("roomBookings", bookingService.activeBookingsForRoom(room.getId()));
    }

    private String redirectToSearch(LocalDateTime checkIn, LocalDateTime checkOut) {
        return "redirect:/booking/search?checkIn=" + checkIn + "&checkOut=" + checkOut;
    }

    private String normalizePaymentMethod(String method) {
        if ("ONLINE".equalsIgnoreCase(method)) {
            return "ONLINE";
        }
        if ("QR".equalsIgnoreCase(method)) {
            return "QR";
        }
        return "CASH";
    }

    @GetMapping("/bookings")
    @RoleRequired({"RECEPTIONIST", "MANAGER", "ADMIN"})
    public String all(Model model, HttpSession session) {
        String guard = Access.requireRole(session, "RECEPTIONIST", "MANAGER", "ADMIN");
        if (guard != null) {
            return guard;
        }

        model.addAttribute("bookings", bookingService.bookings());
        return "receptionist/bookings/list";
    }

    @GetMapping("/my-bookings")
    @RoleRequired("CUSTOMER")
    public String mine(HttpSession session, Model model) {
        User user = Access.user(session);
        if (user == null) {
            return "redirect:/login";
        }

        model.addAttribute(
                "bookings",
                bookingService.bookingsOfUser(user.getId()));
        return "customer/bookings/list";
    }

    @GetMapping("/bookings/detail")
    @RoleRequired({"CUSTOMER", "RECEPTIONIST", "MANAGER", "ADMIN"})
    public String detail(@RequestParam("id") int id, Model model, HttpSession session) {
        String guard = Access.requireRole(session, "CUSTOMER", "RECEPTIONIST", "MANAGER", "ADMIN");
        if (guard != null) {
            return guard;
        }

        if (Access.has(session, "CUSTOMER")) {
            User user = Access.user(session);
            if (user == null || !bookingService.userOwnsBooking(user.getId(), id)) {
                return "redirect:/access-denied";
            }
        }

        model.addAttribute("booking", bookingService.booking(id));
        return "shared/bookings/detail";
    }

    @PostMapping("/bookings/update")
    @RoleRequired({"RECEPTIONIST", "ADMIN"})
    public String update(
            @RequestParam("id") int id,
            @RequestParam("checkIn") String checkIn,
            @RequestParam("checkOut") String checkOut,
            HttpSession session) {
        String guard = Access.requireRole(session, "RECEPTIONIST", "ADMIN");
        if (guard != null) {
            return guard;
        }

        LocalDateTime in = parseDateTime(checkIn, defaultCheckIn());
        LocalDateTime out = parseDateTime(checkOut, in.plusHours(12));
        if (out.isAfter(in)) {
            bookingService.updateBooking(id, in, out);
        }
        return "redirect:/bookings";
    }

    @PostMapping("/bookings/confirm")
    @RoleRequired({"RECEPTIONIST", "ADMIN"})
    public String confirm(@RequestParam("id") int id, HttpSession session) {
        String guard = Access.requireRole(session, "RECEPTIONIST", "ADMIN");
        if (guard != null) {
            return guard;
        }

        bookingService.confirmBooking(id);
        return "redirect:/bookings";
    }

    @PostMapping("/bookings/check-in")
    @RoleRequired({"CUSTOMER", "RECEPTIONIST", "ADMIN"})
    public String checkIn(@RequestParam("id") int id, HttpSession session) {
        String guard = Access.requireRole(session, "CUSTOMER", "RECEPTIONIST", "ADMIN");
        if (guard != null) {
            return guard;
        }

        if (Access.has(session, "CUSTOMER")) {
            User user = Access.user(session);
            if (user == null || !bookingService.userOwnsBooking(user.getId(), id)) {
                return "redirect:/access-denied";
            }
        }

        bookingService.checkIn(id);
        return Access.has(session, "CUSTOMER") ? "redirect:/my-bookings" : "redirect:/bookings";
    }

    @PostMapping("/bookings/check-out")
    @RoleRequired({"CUSTOMER", "RECEPTIONIST", "ADMIN"})
    public String checkOut(@RequestParam("id") int id, HttpSession session) {
        String guard = Access.requireRole(session, "CUSTOMER", "RECEPTIONIST", "ADMIN");
        if (guard != null) {
            return guard;
        }

        if (Access.has(session, "CUSTOMER")) {
            User user = Access.user(session);
            if (user == null || !bookingService.userOwnsBooking(user.getId(), id)) {
                return "redirect:/access-denied";
            }
        }

        bookingService.checkOut(id);
        return Access.has(session, "CUSTOMER") ? "redirect:/my-bookings" : "redirect:/bookings";
    }

    @PostMapping("/bookings/cancel")
    @RoleRequired({"CUSTOMER", "RECEPTIONIST", "ADMIN"})
    public String cancel(@RequestParam("id") int id, HttpSession session) {
        String guard = Access.requireRole(session, "CUSTOMER", "RECEPTIONIST", "ADMIN");
        if (guard != null) {
            return guard;
        }

        if (Access.has(session, "CUSTOMER")) {
            User user = Access.user(session);
            if (user == null || !bookingService.userOwnsBooking(user.getId(), id)) {
                return "redirect:/access-denied";
            }
        }

        bookingService.cancel(id);
        return Access.has(session, "CUSTOMER") ? "redirect:/my-bookings" : "redirect:/bookings";
    }
}
