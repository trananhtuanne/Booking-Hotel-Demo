package uef.edu.vn.bookinghotel.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import uef.edu.vn.bookinghotel.authorization.RoleRequired;
import uef.edu.vn.bookinghotel.model.User;
import uef.edu.vn.bookinghotel.service.CustomerService;
import uef.edu.vn.bookinghotel.service.InvoiceService;
import uef.edu.vn.bookinghotel.service.PaymentService;

@Controller
public class InvoiceController {

    private final CustomerService customerService;
    private final InvoiceService invoiceService;
    private final PaymentService paymentService;

    public InvoiceController(
            CustomerService customerService,
            InvoiceService invoiceService,
            PaymentService paymentService) {
        this.customerService = customerService;
        this.invoiceService = invoiceService;
        this.paymentService = paymentService;
    }

    @GetMapping("/invoices")
    @RoleRequired({"RECEPTIONIST", "MANAGER", "ADMIN"})
    public String list(Model model, HttpSession session) {
        String guard = Access.requireRole(session, "RECEPTIONIST", "MANAGER", "ADMIN");
        if (guard != null) {
            return guard;
        }

        model.addAttribute("invoices", invoiceService.invoices());
        return "receptionist/invoices/list";
    }

    @GetMapping("/invoices/view")
    @RoleRequired({"CUSTOMER", "RECEPTIONIST", "MANAGER", "ADMIN"})
    public String view(@RequestParam("id") int id, Model model, HttpSession session) {
        String guard = Access.requireRole(session, "CUSTOMER", "RECEPTIONIST", "MANAGER", "ADMIN");
        if (guard != null) {
            return guard;
        }

        if (Access.has(session, "CUSTOMER") && !customerOwnsInvoice(session, id)) {
            return "redirect:/access-denied";
        }

        model.addAttribute("invoice", invoiceService.invoice(id));
        return "shared/invoices/view";
    }

    @GetMapping("/invoices/print")
    @RoleRequired({"RECEPTIONIST", "ADMIN"})
    public String print(@RequestParam("id") int id, Model model, HttpSession session) {
        String guard = Access.requireRole(session, "RECEPTIONIST", "ADMIN");
        if (guard != null) {
            return guard;
        }

        model.addAttribute("invoice", invoiceService.invoice(id));
        return "receptionist/invoices/print";
    }

    @GetMapping("/invoices/download")
    @RoleRequired({"CUSTOMER", "RECEPTIONIST", "MANAGER", "ADMIN"})
    public String download(@RequestParam("id") int id, Model model, HttpSession session) {
        String guard = Access.requireRole(session, "CUSTOMER", "RECEPTIONIST", "MANAGER", "ADMIN");
        if (guard != null) {
            return guard;
        }

        if (Access.has(session, "CUSTOMER") && !customerOwnsInvoice(session, id)) {
            return "redirect:/access-denied";
        }

        model.addAttribute("invoice", invoiceService.invoice(id));
        return "shared/invoices/download";
    }

    @PostMapping("/invoices/pay")
    @RoleRequired({"RECEPTIONIST", "ADMIN"})
    public String pay(
            @RequestParam("id") int id,
            @RequestParam(value = "method", defaultValue = "CASH") String method,
            HttpSession session) {
        String guard = Access.requireRole(session, "RECEPTIONIST", "ADMIN");
        if (guard != null) {
            return guard;
        }

        paymentService.pay(id, method);
        return "redirect:/invoices";
    }

    @GetMapping("/payments")
    @RoleRequired({"CUSTOMER", "RECEPTIONIST", "MANAGER", "ADMIN"})
    public String payments(Model model, HttpSession session) {
        String guard = Access.requireRole(session, "CUSTOMER", "RECEPTIONIST", "MANAGER", "ADMIN");
        if (guard != null) {
            return guard;
        }

        if (Access.has(session, "CUSTOMER")) {
            User user = Access.user(session);
            model.addAttribute("payments", paymentService.paymentsOfUser(user.getId()));
        } else {
            model.addAttribute("payments", paymentService.payments());
        }

        return "shared/payments/list";
    }

    private boolean customerOwnsInvoice(HttpSession session, int invoiceId) {
        User user = Access.user(session);
        return user != null && invoiceService.userOwnsInvoice(user.getId(), invoiceId);
    }
}
