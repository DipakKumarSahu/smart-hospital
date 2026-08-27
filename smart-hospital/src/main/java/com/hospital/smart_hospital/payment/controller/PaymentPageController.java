package com.hospital.smart_hospital.payment.controller;


import com.hospital.smart_hospital.payment.dto.request.CreatePaymentRequest;
import com.hospital.smart_hospital.payment.dto.response.PaymentResponse;
import com.hospital.smart_hospital.payment.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/payment")
@RequiredArgsConstructor
public class PaymentPageController {

    private final PaymentService paymentService;

    @GetMapping("/appointment/{appointmentId}")
    public String paymentPage(
            @PathVariable Long appointmentId,
            Model model) {

        PaymentResponse payment =
                paymentService.createPayment(
                        new CreatePaymentRequest(
                                appointmentId
                        )
                );

        model.addAttribute("payment", payment);

        return "payment";
    }
}
