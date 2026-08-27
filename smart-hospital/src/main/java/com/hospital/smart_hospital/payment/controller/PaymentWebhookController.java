package com.hospital.smart_hospital.payment.controller;

import com.hospital.smart_hospital.payment.service.PaymentService;
import com.stripe.exception.SignatureVerificationException;
import com.stripe.model.Event;
import com.stripe.model.PaymentIntent;
import com.stripe.net.Webhook;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/payments/webhook")
@RequiredArgsConstructor
@Slf4j
public class PaymentWebhookController {

    private final PaymentService paymentService;

    @Value("${stripe.webhook-secret}")
    private String webhookSecret;

    @PostMapping("/stripe")
    public ResponseEntity<String> handleStripeWebhook(
            @RequestBody String payload,
            @RequestHeader("Stripe-Signature")
            String signature) {

        final Event event;

        try {

            event = Webhook.constructEvent(
                    payload,
                    signature,
                    webhookSecret
            );

        } catch (SignatureVerificationException e) {

            log.warn("Invalid Stripe webhook signature.");

            return ResponseEntity
                    .badRequest()
                    .body("Invalid signature.");
        }

        switch (event.getType()) {

            case "payment_intent.succeeded" -> {

                PaymentIntent paymentIntent =
                        (PaymentIntent) event
                                .getDataObjectDeserializer()
                                .getObject()
                                .orElseThrow();

                paymentService.handleStripePaymentSuccess(
                        paymentIntent.getId()
                );
            }

            case "payment_intent.payment_failed" -> {

                PaymentIntent paymentIntent =
                        (PaymentIntent) event
                                .getDataObjectDeserializer()
                                .getObject()
                                .orElseThrow();

                String failureReason = null;

                if (paymentIntent.getLastPaymentError() != null) {

                    failureReason =
                            paymentIntent
                                    .getLastPaymentError()
                                    .getMessage();
                }

                paymentService.handleStripePaymentFailure(
                        paymentIntent.getId(),
                        failureReason
                );
            }

            default -> log.info(
                    "Ignoring Stripe event: {}",
                    event.getType()
            );
        }

        return ResponseEntity.ok("Webhook received.");
    }
}