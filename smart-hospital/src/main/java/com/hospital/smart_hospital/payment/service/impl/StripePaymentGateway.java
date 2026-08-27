package com.hospital.smart_hospital.payment.service.impl;



import com.hospital.smart_hospital.exception.BadRequestException;

import com.hospital.smart_hospital.payment.dto.response.PaymentGatewayResponse;
import com.hospital.smart_hospital.payment.service.PaymentGateway;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import com.stripe.param.PaymentIntentCreateParams;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@Slf4j
public class StripePaymentGateway
        implements PaymentGateway {

    public StripePaymentGateway(
            @Value("${stripe.secret-key}")
            String secretKey) {

        Stripe.apiKey = secretKey;
    }

    @Override
    public PaymentGatewayResponse createPayment(
            BigDecimal amount,
            String currency,
            String paymentReference) {

        try {

            long amountInSmallestUnit =
                    amount
                            .movePointRight(2)
                            .longValueExact();

            PaymentIntentCreateParams params =
                    PaymentIntentCreateParams.builder()
                            .setAmount(amountInSmallestUnit)
                            .setCurrency(
                                    currency.toLowerCase()
                            )
                            .putMetadata(
                                    "payment_reference",
                                    paymentReference
                            )
                            .build();

            PaymentIntent paymentIntent =
                    PaymentIntent.create(params);

            log.info(
                    "Stripe PaymentIntent created. id={}, reference={}",
                    paymentIntent.getId(),
                    paymentReference
            );

            return new PaymentGatewayResponse(
                    paymentIntent.getId(),
                    paymentIntent.getClientSecret()
            );

        } catch (StripeException | ArithmeticException e) {

            log.error(
                    "Failed to create Stripe PaymentIntent for {}",
                    paymentReference,
                    e
            );

            throw new BadRequestException(
                    "Unable to create payment."
            );
        }
    }
}