import React, { useState, useEffect, useRef } from "react";
import { useParams, useNavigate } from "react-router-dom";
import { motion } from "framer-motion";
import Layout from "../layout/Layout";
import Card from "../components/common/Card";
import Button from "../components/common/Button";
import { patientApi } from "../api/patientApi";
import { toast } from "react-toastify";
import { loadStripe } from "@stripe/stripe-js";
import {
  Elements,
  CardElement,
  useStripe,
  useElements,
} from "@stripe/react-stripe-js";
import {
  CreditCard,
  Lock,
  Clock,
  AlertCircle,
  ArrowLeft,
  Shield,
} from "lucide-react";

// Stripe publishable key
const stripePromise = loadStripe(
  import.meta.env.VITE_STRIPE_PUBLISHABLE_KEY || "pk_test_YOUR_PUBLISHABLE_KEY"
);

// =====================================================
// Payment Form Component
// =====================================================
const PaymentForm = ({ appointment, payment, onSuccess, onBack }) => {
  const stripe = useStripe();
  const elements = useElements();
  const [processing, setProcessing] = useState(false);
  const [error, setError] = useState(null);
  const [timeLeft, setTimeLeft] = useState(600);

  // Countdown timer
  useEffect(() => {
    const timer = setInterval(() => {
      setTimeLeft((prev) => {
        if (prev <= 1) {
          clearInterval(timer);
          toast.error("Payment session expired");
          onBack();
          return 0;
        }
        return prev - 1;
      });
    }, 1000);
    return () => clearInterval(timer);
  }, [onBack]);

  const formatTime = (seconds) => {
    const mins = Math.floor(seconds / 60);
    const secs = seconds % 60;
    return `${mins}:${secs.toString().padStart(2, "0")}`;
  };

  const isExpiringSoon = timeLeft < 60; // Under 1 minute

  const handleSubmit = async (e) => {
    e.preventDefault();

    if (!stripe || !elements) return;

    setProcessing(true);
    setError(null);

    try {
      const cardElement = elements.getElement(CardElement);

      const { error: stripeError, paymentIntent } =
        await stripe.confirmCardPayment(payment.clientSecret, {
          payment_method: {
            card: cardElement,
          },
        });

      if (stripeError) {
        setError(stripeError.message);
        setProcessing(false);
        return;
      }

      if (paymentIntent.status === "succeeded") {
        // Confirm with backend
        try {
          await patientApi.confirmPayment(paymentIntent.id);
        } catch (err) {
          // Continue even if backend sync fails
          // Webhook will handle it eventually
        }

        onSuccess(paymentIntent);
      }
    } catch (err) {
      setError(err.userMessage || err.message || "Payment failed");
    } finally {
      setProcessing(false);
    }
  };

  const cardElementOptions = {
    style: {
      base: {
        fontSize: "15px",
        color: "#1e293b",
        "::placeholder": { color: "#94a3b8" },
        fontFamily: "system-ui, -apple-system, sans-serif",
      },
      invalid: { color: "#dc2626" },
    },
  };

  return (
    <motion.div initial={{ opacity: 0, y: 20 }} animate={{ opacity: 1, y: 0 }}>
      <Card>
        {/* Timer */}
        <div
          className={`rounded-lg p-3 mb-6 flex items-center justify-between border ${
            isExpiringSoon
              ? "bg-red-50 border-red-200"
              : "bg-amber-50 border-amber-200"
          }`}
        >
          <div
            className={`flex items-center gap-2 ${
              isExpiringSoon ? "text-red-800" : "text-amber-800"
            }`}
          >
            <Clock size={16} />
            <span className="text-sm font-medium">
              {isExpiringSoon ? "Expiring soon" : "Payment expires in"}
            </span>
          </div>
          <span
            className={`text-base font-semibold tabular-nums ${
              isExpiringSoon ? "text-red-800" : "text-amber-800"
            }`}
          >
            {formatTime(timeLeft)}
          </span>
        </div>

        {/* Test Mode Info */}
        <div className="bg-slate-50 border border-slate-200 rounded-lg p-4 mb-6">
          <div className="flex items-start gap-3">
            <AlertCircle
              size={16}
              className="text-slate-600 mt-0.5 shrink-0"
            />
            <div className="text-sm">
              <p className="font-medium text-slate-800 mb-2">
                Test mode active
              </p>
              <div className="space-y-1 text-xs text-slate-600">
                <p>
                  <span className="font-medium text-slate-700">Success:</span>{" "}
                  4242 4242 4242 4242
                </p>
                <p>
                  <span className="font-medium text-slate-700">Decline:</span>{" "}
                  4000 0000 0000 0002
                </p>
                <p>
                  <span className="font-medium text-slate-700">CVV:</span> Any
                  3 digits
                </p>
                <p>
                  <span className="font-medium text-slate-700">Expiry:</span>{" "}
                  Any future date
                </p>
              </div>
            </div>
          </div>
        </div>

        <form onSubmit={handleSubmit}>
          <div className="mb-6">
            <label className="block text-sm font-medium text-slate-700 mb-2">
              Card Details
            </label>
            <div className="border border-slate-300 rounded-lg p-4 focus-within:border-teal-600 focus-within:ring-2 focus-within:ring-teal-600/10 transition-colors bg-white">
              <CardElement options={cardElementOptions} />
            </div>
          </div>

          {error && (
            <div className="bg-red-50 border border-red-200 rounded-lg p-3 mb-4 flex items-start gap-2">
              <AlertCircle
                size={16}
                className="text-red-600 mt-0.5 shrink-0"
              />
              <span className="text-sm text-red-800">{error}</span>
            </div>
          )}

          <div className="flex items-center justify-center gap-2 text-xs text-slate-500 mb-5">
            <Shield size={14} />
            <span>Secured by Stripe. Card information is encrypted.</span>
          </div>

          <div className="flex flex-col sm:flex-row gap-3">
            <Button
              type="button"
              variant="outline"
              onClick={onBack}
              className="w-full sm:w-auto"
              disabled={processing}
            >
              <ArrowLeft size={16} />
              Cancel
            </Button>
            <Button
              type="submit"
              className="flex-1"
              isLoading={processing}
              disabled={!stripe || processing}
            >
              <Lock size={16} />
              Pay ₹{payment.amount}
            </Button>
          </div>
        </form>
      </Card>
    </motion.div>
  );
};

// =====================================================
// Main Payment Page
// =====================================================
const PaymentPage = () => {
  const { appointmentId } = useParams();
  const navigate = useNavigate();
  const [loading, setLoading] = useState(true);
  const [appointment, setAppointment] = useState(null);
  const [payment, setPayment] = useState(null);
  const [error, setError] = useState(null);

  const initRef = useRef(false);

  useEffect(() => {
    if (initRef.current) return;
    initRef.current = true;
    initializePayment();
  }, [appointmentId]);

  const initializePayment = async () => {
    setLoading(true);
    try {
      const appointmentResponse =
        await patientApi.getMyAppointmentById(appointmentId);
      const appointmentData = appointmentResponse.data.data;
      setAppointment(appointmentData);

      if (
        appointmentData.status === "BOOKED" ||
        appointmentData.status === "CONFIRMED"
      ) {
        toast.info("This appointment is already paid");
        navigate("/my-appointments");
        return;
      }

      const paymentResponse = await patientApi.createPayment(
        parseInt(appointmentId)
      );
      setPayment(paymentResponse.data.data);
    } catch (err) {
      setError(err.userMessage || "Failed to initialize payment");
    } finally {
      setLoading(false);
    }
  };

  const handlePaymentSuccess = async (paymentIntent) => {
    toast.success("Payment successful. Verifying appointment...");

    const maxAttempts = 10;
    const pollInterval = 1000;

    for (let i = 0; i < maxAttempts; i++) {
      try {
        await new Promise((resolve) => setTimeout(resolve, pollInterval));

        const response = await patientApi.getMyAppointmentById(appointmentId);
        const apt = response.data.data;

        if (apt.status === "BOOKED" || apt.status === "CONFIRMED") {
          toast.success("Appointment confirmed");
          navigate("/my-appointments", {
            state: { paymentSuccess: true, appointmentId },
          });
          return;
        }
      } catch (err) {
        // Continue polling
      }
    }

    toast.info("Payment received. Verification pending.");
    navigate("/my-appointments");
  };

  const handleBack = () => {
    if (
      !window.confirm(
        "Cancel this appointment? You will need to book again."
      )
    ) {
      return;
    }

    patientApi
      .cancelMyAppointment(appointmentId)
      .then(() => {
        toast.info("Appointment cancelled");
        navigate("/dashboard");
      })
      .catch(() => navigate("/dashboard"));
  };

  // Loading state with skeleton
  if (loading) {
    return (
      <Layout>
        <div className="max-w-2xl mx-auto space-y-6">
          <div className="h-8 w-64 bg-slate-200 rounded animate-pulse" />
          <Card>
            <div className="space-y-3">
              {[1, 2, 3, 4, 5].map((i) => (
                <div key={i} className="flex justify-between">
                  <div className="h-4 w-32 bg-slate-200 rounded animate-pulse" />
                  <div className="h-4 w-24 bg-slate-200 rounded animate-pulse" />
                </div>
              ))}
            </div>
          </Card>
          <Card>
            <div className="h-12 bg-slate-200 rounded animate-pulse mb-4" />
            <div className="h-32 bg-slate-200 rounded animate-pulse" />
          </Card>
        </div>
      </Layout>
    );
  }

  if (error || !payment) {
    return (
      <Layout>
        <div className="max-w-md mx-auto">
          <Card className="text-center py-10">
            <div className="w-12 h-12 rounded-full bg-red-50 flex items-center justify-center mx-auto mb-4">
              <AlertCircle className="w-6 h-6 text-red-600" />
            </div>
            <h3 className="text-lg font-semibold text-slate-800">
              Unable to load payment
            </h3>
            <p className="text-sm text-slate-500 mt-2 mb-6">
              {error || "The payment details could not be retrieved."}
            </p>
            <Button onClick={() => navigate("/dashboard")}>
              <ArrowLeft size={16} />
              Back to Dashboard
            </Button>
          </Card>
        </div>
      </Layout>
    );
  }

  return (
    <Layout>
      <div className="max-w-2xl mx-auto space-y-6">
        <motion.div
          initial={{ opacity: 0, y: -20 }}
          animate={{ opacity: 1, y: 0 }}
        >
          <h1 className="text-2xl font-semibold text-slate-800 flex items-center gap-3">
            <CreditCard className="w-7 h-7 text-teal-700" />
            Complete Payment
          </h1>
          <p className="text-sm text-slate-500 mt-1">
            Secure payment processed by Stripe
          </p>
        </motion.div>

        {/* Appointment Summary */}
        <Card>
          <h3 className="text-xs font-semibold text-slate-500 uppercase tracking-wider mb-4">
            Appointment Details
          </h3>
          <div className="space-y-3">
            <div className="flex justify-between text-sm">
              <span className="text-slate-500">Reference</span>
              <span className="font-medium text-slate-800 tabular-nums">
                {appointment?.appointmentCode}
              </span>
            </div>
            <div className="flex justify-between text-sm">
              <span className="text-slate-500">Doctor</span>
              <span className="font-medium text-slate-800">
                {appointment?.doctorName || "Not assigned"}
              </span>
            </div>
            <div className="flex justify-between text-sm">
              <span className="text-slate-500">Date</span>
              <span className="font-medium text-slate-800">
                {appointment?.appointmentDate}
              </span>
            </div>
            <div className="flex justify-between text-sm">
              <span className="text-slate-500">Time</span>
              <span className="font-medium text-slate-800">
                {appointment?.appointmentTime}
              </span>
            </div>
            <div className="flex justify-between text-sm">
              <span className="text-slate-500">Visit Type</span>
              <span className="font-medium text-slate-800">
                {appointment?.visitType}
              </span>
            </div>

            <div className="border-t border-slate-100 pt-3 mt-3">
              <div className="flex justify-between items-baseline">
                <span className="text-sm font-medium text-slate-600">
                  Total Amount
                </span>
                <span className="text-xl font-semibold text-slate-800 tabular-nums">
                  ₹{payment.amount}
                </span>
              </div>
            </div>
          </div>
        </Card>

        <Elements stripe={stripePromise}>
          <PaymentForm
            appointment={appointment}
            payment={payment}
            onSuccess={handlePaymentSuccess}
            onBack={handleBack}
          />
        </Elements>
      </div>
    </Layout>
  );
};

export default PaymentPage;