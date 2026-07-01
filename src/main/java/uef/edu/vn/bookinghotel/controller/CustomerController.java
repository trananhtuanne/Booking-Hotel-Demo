package uef.edu.vn.bookinghotel.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import uef.edu.vn.bookinghotel.authorization.RoleRequired;
import uef.edu.vn.bookinghotel.model.Customer;
import uef.edu.vn.bookinghotel.model.User;
import uef.edu.vn.bookinghotel.service.BookingService;
import uef.edu.vn.bookinghotel.service.CustomerService;
import uef.edu.vn.bookinghotel.service.InvoiceService;

@Controller
public class CustomerController {

    private final BookingService bookingService;
    private final CustomerService customerService;
    private final InvoiceService invoiceService;

    public CustomerController(
            BookingService bookingService,
            CustomerService customerService,
            InvoiceService invoiceService) {
        this.bookingService = bookingService;
        this.customerService = customerService;
        this.invoiceService = invoiceService;
    }

    @GetMapping("/customers")
    @RoleRequired({"RECEPTIONIST", "MANAGER", "ADMIN"})
    public String list(Model model, HttpSession session) {
        String guard = Access.requireRole(session, "RECEPTIONIST", "MANAGER", "ADMIN");
        if (guard != null) {
            return guard;
        }

        model.addAttribute("customers", customerService.customers());
        return "receptionist/customers/list";
    }

    @PostMapping("/customers")
    @RoleRequired({"RECEPTIONIST", "ADMIN"})
    public String save(@ModelAttribute Customer customer, HttpSession session) {
        String guard = Access.requireRole(session, "RECEPTIONIST", "ADMIN");
        if (guard != null) {
            return guard;
        }

        customerService.saveCustomer(customer);
        return "redirect:/customers";
    }

    @PostMapping("/customers/delete")
    @RoleRequired({"RECEPTIONIST", "ADMIN"})
    public String delete(@RequestParam("id") int id, HttpSession session) {
        String guard = Access.requireRole(session, "RECEPTIONIST", "ADMIN");
        if (guard != null) {
            return guard;
        }

        customerService.deleteCustomer(id);
        return "redirect:/customers";
    }

    @GetMapping("/profile")
    @RoleRequired("CUSTOMER")
    public String profile(HttpSession session, Model model) {
        User user = (User) session.getAttribute("currentUser");
        if (user == null) {
            return "redirect:/login";
        }

        Customer customer = customerService.customerOf(user);
        if (customer == null) {
            customer = new Customer();
            customer.setUserId(user.getId());
            customer.setFullName(user.getFullName());
            customer.setEmail(user.getEmail());
            customer.setPhone(user.getPhone());
        }

        model.addAttribute("customer", customer);
        model.addAttribute("bookings", bookingService.bookingsOfUser(user.getId()));
        model.addAttribute("invoices", invoiceService.invoicesOfUser(user.getId()));
        return "customer/profile/index";
    }

    @PostMapping("/profile")
    @RoleRequired("CUSTOMER")
    public String updateProfile(@ModelAttribute Customer customer, HttpSession session) {
        User user = (User) session.getAttribute("currentUser");
        if (user == null) {
            return "redirect:/login";
        }

        customer.setUserId(user.getId());
        customerService.updateProfile(user, customer);
        user.setFullName(customer.getFullName());
        user.setEmail(customer.getEmail());
        user.setPhone(customer.getPhone());
        session.setAttribute("currentUser", user);
        return "redirect:/profile";
    }
}
