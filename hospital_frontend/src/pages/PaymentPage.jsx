import React, { useState, useEffect, useRef } from "react";
import { useParams, useNavigate } from 'react-router-dom';
import { motion } from 'framer-motion';
import Layout from '../layout/Layout';
import Card from '../components/common/Card';
import Button from '../components/common/Button';
import Spinner from '../components/common/Spinner';
import { patientApi } from '../api/patientApi';
import { toast } from 'react-toastify';
import { loadStripe } from '@stripe/stripe-js';
import {
  Elements,
  CardElement,
  useStripe,
  useElements,
} from '@stripe/react-stripe-js';
import {
  CreditCard, Lock, CheckCircle, Clock,
  AlertCircle, ArrowLeft, Shield
} from 'lucide-react';

// Stripe publishable key (TEST MODE)
// TODO: Apni Stripe test publishable key yahan daalo
const stripePromise = loadStripe(
  import.meta.env.VITE_STRIPE_PUBLISHABLE_KEY || 'pk_test_YOUR_PUBLISHABLE_KEY'
);

// =====================================================
// Payment Form Component (inside Elements)
// =====================================================
const PaymentForm = ({ appointment, payment, onSuccess, onBack }) => {
  const stripe = useStripe();
  const elements = useElements();
  const [processing, setProcessing] = useState(false);
  const [error, setError] = useState(null);
  const [timeLeft, setTimeLeft] = useState(600); // 10 minutes


  

  // Countdown timer
  useEffect(() => {
    const timer = setInterval(() => {
      setTimeLeft(prev => {
        if (prev <= 1) {
          clearInterval(timer);
          toast.error('Payment time expired!');
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
    return `${mins}:${secs.toString().padStart(2, '0')}`;
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    
    if (!stripe || !elements) {
      return;
    }

    setProcessing(true);
    setError(null);

    try {
      const cardElement = elements.getElement(CardElement);

      // Confirm payment with Stripe
      const { error: stripeError, paymentIntent } = await stripe.confirmCardPayment(
        payment.clientSecret,
        {
          payment_method: {
            card: cardElement,
          },
        }
      );

      if (stripeError) {
        setError(stripeError.message);
        toast.error(stripeError.message);
        setProcessing(false);
        return;
      }

      if (paymentIntent.status === 'succeeded') {
        toast.success('Payment successful! 🎉');
        onSuccess(paymentIntent);
      }
    } catch (err) {
      console.error('Payment error:', err);
      setError(err.message);
      toast.error('Payment failed. Please try again.');
    } finally {
      setProcessing(false);
    }
  };

  const cardElementOptions = {
    style: {
      base: {
        fontSize: '16px',
        color: '#424770',
        '::placeholder': {
          color: '#aab7c4',
        },
        fontFamily: 'system-ui, -apple-system, sans-serif',
      },
      invalid: {
        color: '#ef4444',
      },
    },
  };

  return (
    <motion.div
      initial={{ opacity: 0, y: 20 }}
      animate={{ opacity: 1, y: 0 }}
    >
      <Card>
        {/* Timer Alert */}
        <div className="bg-yellow-50 border border-yellow-200 rounded-lg p-3 mb-6 flex items-center justify-between">
          <div className="flex items-center gap-2 text-yellow-800">
            <Clock size={18} />
            <span className="text-sm font-medium">Payment expires in</span>
          </div>
          <span className="text-lg font-bold text-yellow-800">{formatTime(timeLeft)}</span>
        </div>

        {/* Test Card Info */}
        <div className="bg-blue-50 border border-blue-200 rounded-lg p-4 mb-6">
          <div className="flex items-start gap-2">
            <AlertCircle size={18} className="text-blue-600 mt-0.5 flex-shrink-0" />
            <div className="text-sm text-blue-800">
              <p className="font-semibold mb-1">🧪 Test Mode - Use test cards:</p>
              <ul className="space-y-1 text-xs">
                <li>✅ <strong>Success:</strong> 4242 4242 4242 4242</li>
                <li>❌ <strong>Decline:</strong> 4000 0000 0000 0002</li>
                <li>🔐 <strong>CVV:</strong> Any 3 digits</li>
                <li>📅 <strong>Expiry:</strong> Any future date</li>
              </ul>
            </div>
          </div>
        </div>

        <form onSubmit={handleSubmit}>
          {/* Card Input */}
          <div className="mb-6">
            <label className="block text-sm font-medium text-gray-700 mb-2">
              Card Details
            </label>
            <div className="border border-gray-300 rounded-lg p-4 focus-within:ring-2 focus-within:ring-primary-500 focus-within:border-primary-500 transition-all bg-white">
              <CardElement options={cardElementOptions} />
            </div>
          </div>

          {/* Error */}
          {error && (
            <div className="bg-red-50 border border-red-200 rounded-lg p-3 mb-4 flex items-center gap-2">
              <AlertCircle size={18} className="text-red-600" />
              <span className="text-sm text-red-800">{error}</span>
            </div>
          )}

          {/* Secure Badge */}
          <div className="flex items-center justify-center gap-2 text-xs text-gray-500 mb-4">
            <Shield size={14} />
            <span>Secured by Stripe • Your card info is encrypted</span>
          </div>

          {/* Buttons */}
          <div className="flex flex-col sm:flex-row gap-3">
            <Button
              type="button"
              variant="outline"
              onClick={onBack}
              className="w-full sm:w-auto"
              disabled={processing}
            >
              <ArrowLeft size={18} />
              Cancel
            </Button>
            <Button
              type="submit"
              className="flex-1"
              isLoading={processing}
              disabled={!stripe || processing}
            >
              <Lock size={18} />
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
      // 1. Get appointment details
      const appointmentResponse = await patientApi.getMyAppointmentById(appointmentId);
      const appointmentData = appointmentResponse.data.data;
      setAppointment(appointmentData);

      // Check if already paid
      if (appointmentData.status === 'BOOKED' || appointmentData.status === 'CONFIRMED') {
        toast.info('This appointment is already paid');
        navigate('/my-appointments');
        return;
      }

      // 2. Create payment intent
      const paymentResponse = await patientApi.createPayment(parseInt(appointmentId));
      setPayment(paymentResponse.data.data);
      
    } catch (err) {
      console.error('Error initializing payment:', err);
      setError(err.response?.data?.message || 'Failed to initialize payment');
      toast.error('Failed to load payment details');
    } finally {
      setLoading(false);
    }
  };

  const handlePaymentSuccess = (paymentIntent) => {
    // Redirect to success page or appointments
    setTimeout(() => {
      navigate('/my-appointments', { 
        state: { paymentSuccess: true, appointmentId } 
      });
    }, 1500);
  };

  const handleBack = () => {
    // Cancel appointment if user goes back
    if (window.confirm('Are you sure? Your appointment will be cancelled.')) {
      patientApi.cancelMyAppointment(appointmentId)
        .then(() => {
          toast.info('Appointment cancelled');
          navigate('/dashboard');
        })
        .catch(() => navigate('/dashboard'));
    }
  };

  if (loading) {
    return (
      <Layout>
        <div className="flex flex-col items-center justify-center h-96">
          <Spinner size="lg" />
          <p className="mt-4 text-gray-500">Preparing payment...</p>
        </div>
      </Layout>
    );
  }

  if (error || !payment) {
    return (
      <Layout>
        <Card className="text-center py-12 max-w-md mx-auto">
          <AlertCircle className="w-16 h-16 text-red-500 mx-auto mb-4" />
          <h3 className="text-lg font-semibold text-gray-800">Payment Failed to Load</h3>
          <p className="text-gray-500 mt-2 mb-4">{error || 'Something went wrong'}</p>
          <Button onClick={() => navigate('/dashboard')}>
            <ArrowLeft size={18} />
            Back to Dashboard
          </Button>
        </Card>
      </Layout>
    );
  }

  return (
    <Layout>
      <div className="max-w-2xl mx-auto space-y-6">
        {/* Header */}
        <motion.div
          initial={{ opacity: 0, y: -20 }}
          animate={{ opacity: 1, y: 0 }}
        >
          <h1 className="text-3xl font-bold flex items-center gap-3">
            <CreditCard className="w-8 h-8 text-primary-500" />
            Complete Payment
          </h1>
          <p className="text-gray-500 mt-1">Secure payment powered by Stripe</p>
        </motion.div>

        {/* Appointment Summary */}
        <Card>
          <h3 className="text-sm font-semibold text-gray-500 uppercase tracking-wider mb-4">
            Appointment Details
          </h3>
          <div className="space-y-3">
            <div className="flex justify-between text-sm">
              <span className="text-gray-500">Appointment Code</span>
              <span className="font-medium">{appointment?.appointmentCode}</span>
            </div>
            <div className="flex justify-between text-sm">
              <span className="text-gray-500">Doctor</span>
              <span className="font-medium">{appointment?.doctorName || 'N/A'}</span>
            </div>
            <div className="flex justify-between text-sm">
              <span className="text-gray-500">Date</span>
              <span className="font-medium">{appointment?.appointmentDate}</span>
            </div>
            <div className="flex justify-between text-sm">
              <span className="text-gray-500">Time</span>
              <span className="font-medium">{appointment?.appointmentTime}</span>
            </div>
            <div className="flex justify-between text-sm">
              <span className="text-gray-500">Visit Type</span>
              <span className="font-medium">{appointment?.visitType}</span>
            </div>
            <hr className="my-3" />
            <div className="flex justify-between">
              <span className="font-semibold text-gray-700">Total Amount</span>
              <span className="text-2xl font-bold text-primary-600">
                ₹{payment.amount}
              </span>
            </div>
          </div>
        </Card>

        {/* Stripe Payment Form */}
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