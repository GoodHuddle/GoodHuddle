/*
 * (C) Copyright ${year} Nuxeo SA (http://nuxeo.com/) and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser General Public License
 * (LGPL) version 2.1 which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/lgpl-2.1.html
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 */

package com.goodhuddle.huddle.service.impl;

import com.goodhuddle.huddle.domain.*;
import com.goodhuddle.huddle.domain.Subscription;
import com.goodhuddle.huddle.repository.*;
import com.goodhuddle.huddle.service.HuddleService;
import com.goodhuddle.huddle.service.MemberService;
import com.goodhuddle.huddle.service.PaymentService;
import com.goodhuddle.huddle.service.exception.*;
import com.goodhuddle.huddle.service.request.payment.CreateSubscriptionRequest;
import com.goodhuddle.huddle.service.request.payment.ProcessPaymentRequest;
import com.goodhuddle.huddle.service.request.payment.SearchPaymentsRequest;
import com.goodhuddle.huddle.service.request.payment.SearchSubscriptionsRequest;
import com.goodhuddle.huddle.service.request.payment.settings.UpdatePaymentSettingsRequest;
import com.stripe.exception.*;
import com.stripe.model.*;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class PaymentServiceImpl implements PaymentService {

    private static final Logger log = LoggerFactory.getLogger(PaymentServiceImpl.class);

    public static final String META_HUDDLE = "gh-huddle";

    private final PaymentRepository paymentRepository;
    private final SubscriptionRepository subscriptionRepository;
    private final PaymentSettingsRepository paymentSettingsRepository;
    private final MemberService memberService;
    private final HuddleService huddleService;

    @Autowired
    public PaymentServiceImpl(PaymentRepository paymentRepository,
                              SubscriptionRepository subscriptionRepository,
                              PaymentSettingsRepository paymentSettingsRepository,
                              MemberService memberService,
                              HuddleService huddleService) {

        this.paymentRepository = paymentRepository;
        this.subscriptionRepository = subscriptionRepository;
        this.paymentSettingsRepository = paymentSettingsRepository;
        this.memberService = memberService;
        this.huddleService = huddleService;
    }

    @Override
    public PaymentSettings getPaymentSettings() {
        List<PaymentSettings> settings = paymentSettingsRepository.findByHuddle(huddleService.getHuddle());
        return settings.size() > 0 ? settings.get(0) : new PaymentSettings(huddleService.getHuddle());
    }

    @Override
    public String getHuddlePublishableKey() {
        PaymentSettings settings = getPaymentSettings();
        return settings.getPublishableKey();
    }

    @Override
    public void checkChangeSettingsAllowed() throws SubscriptionsExistException {
        if (subscriptionRepository.countByHuddleAndStatus(huddleService.getHuddle(), SubscriptionStatus.active) > 0) {
            throw new SubscriptionsExistException(
                    "You must cancel all active subscriptions before changing your payment settings");
        }
    }

    @Override
    @Transactional(readOnly = false)
    public PaymentSettings updatePaymentSettings(UpdatePaymentSettingsRequest request) throws SubscriptionsExistException {
        checkChangeSettingsAllowed();
        PaymentSettings settings = getPaymentSettings();
        settings.setSecretKey(request.getSecretKey());
        settings.setPublishableKey(request.getPublishableKey());
        paymentSettingsRepository.save(settings);
        return settings;
    }

    @Override
    public Payment getPayment(long id) {
        return paymentRepository.findByHuddleAndId(huddleService.getHuddle(), id);
    }

    @Override
    public Page<? extends Payment> searchPayments(SearchPaymentsRequest request) {
        return paymentRepository.findAll(PaymentSpecification.search(huddleService.getHuddle(), request),
                new PageRequest(request.getPage(), request.getSize(), Sort.Direction.DESC, "paidOn"));
    }


    @Override
    @Transactional(readOnly = false)
    public Payment processPayment(ProcessPaymentRequest request) throws PaymentFailedException, PaymentsNotSetupException {

        try {

            Member payee = memberService.getOrCreateMemberByEmail(request.getEmail());
            PaymentSettings settings = getPaymentSettings();

            log.info("Making one-off Stripe payment of ${} by ", request.getAmountInCents() / 100, request.getEmail());
            final Map<String, Object> chargeParams = new HashMap<>();
            chargeParams.put("amount", request.getAmountInCents());
            chargeParams.put("currency", request.getCurrency());
            chargeParams.put("card", request.getToken());

            Map<String, String> metaData = new HashMap<>();
            metaData.put("huddle", huddleService.getHuddle().getSlug());
            chargeParams.put("metadata", metaData);

            final Charge charge = Charge.create(chargeParams, settings.getSecretKey());
            log.debug("Stripe payment completed with success=", charge.getPaid());

            if (charge.getPaid()) {

                DateTime paymentDate = new DateTime(charge.getCreated() * 1000);
                Payment payment = paymentRepository.save(new Payment(
                        huddleService.getHuddle(),
                        request.getType(),
                        charge.getId(),
                        charge.getBalanceTransaction(),
                        request.getAmountInCents(),
                        charge.getCurrency(),
                        request.getDescription(),
                        request.getEmail(),
                        payee,
                        paymentDate
                ));

                payment = postProcessPayment(payment);

                log.info("New payment '{}' processed with ID '{}'", request.getDescription(), payment.getId());
                return payment;
            } else {
                log.warn("Payment failed: {} ({})", charge.getFailureMessage(), charge.getFailureCode());
                throw new PaymentFailedException("Payment failed: " + charge.getFailureMessage(), null);
            }


        } catch (AuthenticationException | APIConnectionException | CardException | InvalidRequestException | APIException e) {
            log.error("Payment failed", e);
            throw new PaymentFailedException("Payment failed: " + e, null);
        }
    }

    @Override
    public Subscription getSubscription(long id) {
        return subscriptionRepository.findByHuddleAndId(huddleService.getHuddle(), id);
    }

    @Override
    public Subscription getSubscriptionByStripeCustomerId(String stripeCustomerId) {
        return subscriptionRepository.findByStripeCustomerId(stripeCustomerId);
    }

    @Override
    public Page<? extends Subscription> searchSubscriptions(SearchSubscriptionsRequest request) {
        return subscriptionRepository.findAll(SubscriptionSpecification.search(huddleService.getHuddle(), request),
                new PageRequest(request.getPage(), request.getSize(), Sort.Direction.DESC, "createdOn"));
    }

    @Override
    @Transactional(readOnly = false)
    public Subscription createSubscription(CreateSubscriptionRequest request)
            throws PaymentFailedException, PaymentsNotSetupException {

        try {

            Member payee = memberService.getOrCreateMemberByEmail(request.getEmail());
            PaymentSettings settings = getPaymentSettings();

            log.info("Making recurring Stripe payment of ${} by {}", request.getAmountInCents() / 100, request.getEmail());

            String planId = createPaymentPlanId(request);
            Plan plan;
            try {
                plan = Plan.retrieve(planId, settings.getSecretKey());
            } catch (InvalidRequestException e) {
                // plan does not exist, create plan
                Map<String, Object> planParams = new HashMap<>();
                planParams.put("id", planId);
                planParams.put("name", createPaymentPlanName(request));
                planParams.put("amount", request.getAmountInCents());
                planParams.put("currency", request.getCurrency());
                planParams.put("interval", request.getFrequency().name());

                Map<String, String> metaData = new HashMap<>();
                metaData.put(META_HUDDLE, huddleService.getHuddle().getSlug());
                planParams.put("metadata", metaData);

                plan = Plan.create(planParams, settings.getSecretKey());
                log.debug("New Stripe payment plan created with ID '{}'", plan.getId());
            }

            // create customer / subscription

            final Map<String, Object> customerParams = new HashMap<>();
            customerParams.put("email", request.getEmail());
            customerParams.put("plan", plan.getId());
            customerParams.put("card", request.getToken());

            Map<String, String> metaData = new HashMap<>();
            metaData.put(META_HUDDLE, huddleService.getHuddle().getSlug());
            customerParams.put("metadata", metaData);

            final Customer customer = Customer.create(customerParams, settings.getSecretKey());

            String stripeSubscriptionId = customer.getSubscriptions().getData().get(0).getId();

            Subscription subscription = subscriptionRepository.save(new Subscription(huddleService.getHuddle(),
                    request.getFrequency(), request.getType(), customer.getId(), stripeSubscriptionId, 
                    request.getAmountInCents(), request.getCurrency(), request.getDescription(), payee));

            subscriptionRepository.save(subscription);
            log.debug("Stripe recurring payment setup with subscription ID {}", stripeSubscriptionId);

            return subscription;

        } catch (AuthenticationException | APIConnectionException | CardException | InvalidRequestException | APIException e) {
            log.error("Payment failed", e);
            throw new PaymentFailedException("Payment failed: " + e, null);
        }
    }

    @Override
    @Transactional(readOnly = false)
    public Subscription cancelSubscription(long id) throws SubscriptionErrorException {
        try {

            Subscription subscription = subscriptionRepository.findByHuddleAndId(huddleService.getHuddle(), id);
            PaymentSettings settings = getPaymentSettings();

            String activeStripeApiKey = settings.getSecretKey();
            Customer stripeCustomer = Customer.retrieve(subscription.getStripeCustomerId(), activeStripeApiKey);
            boolean cancelled = false;
            for(com.stripe.model.Subscription stripeSubscription : stripeCustomer.getSubscriptions().getData()){
                if (stripeSubscription.getId().equals(subscription.getStripeSubscriptionId())){
                    log.debug("Cancelling Stripe subscription with ID {}", stripeSubscription.getId());
                    stripeSubscription.cancel(null, activeStripeApiKey);
                    cancelled = true;
                    break;
                }
            }

            if (!cancelled) {
                throw new SubscriptionErrorException("Unable to cancel Stripe Subscription with ID "
                        + subscription.getStripeSubscriptionId() + " because it could not be found");
            }

            subscription.setCancelled(memberService.getLoggedInMember());
            subscription = subscriptionRepository.save(subscription);
            log.info("Subscription with ID {} (for {}) was cancelled",
                    subscription.getId(), subscription.getMember().getDisplayName());

            return subscription;

        } catch (AuthenticationException | InvalidRequestException | APIConnectionException | CardException | APIException e) {
            log.error("Payment failed", e);
            throw new SubscriptionErrorException("Payment failed: " + e, null);
        }
    }

    @Override
    @Transactional(readOnly = false)
    public void handleStripeEvent(Event event) throws WebHookException {
        switch(event.getType()) {

            case "charge.succeeded":
                Charge charge = (Charge) event.getData().getObject();
                processSubscriptionPayment(charge);
                break;

            default:
                log.debug("Ignoring unknown Stripe event: {}", event.getType());
        }
    }

    private void processSubscriptionPayment(Charge charge) throws WebHookException {
        Subscription subscription = getSubscriptionByStripeCustomerId(charge.getCustomer());
        if (subscription != null) {
            log.info("Processing new payment of {} for subscription with ID {}", charge.getAmount(), subscription.getId());

            DateTime paymentDate = new DateTime(charge.getCreated() *  1000);
            Payment payment = paymentRepository.save(new Payment(
                    subscription,
                    charge.getId(),
                    charge.getBalanceTransaction(),
                    charge.getAmount(),
                    charge.getCurrency(),
                    subscription.getDescription(),
                    paymentDate
            ));

            try {
                postProcessPayment(payment);
            } catch (CardException | APIException | AuthenticationException
                    | InvalidRequestException | APIConnectionException e) {
                log.error("Error post processing Stripe payment", e);
                throw new WebHookException("Error post processing Stripe payment", e);
            }

        } else {
            log.debug("Ignoring payment as it is not linked to a GoodHuddle subscription: customerId = {}", charge.getCustomer());
        }
    }

    private Payment postProcessPayment(Payment payment)
            throws CardException, APIException, AuthenticationException, InvalidRequestException, APIConnectionException {

        // get the fee information
        PaymentSettings settings = getPaymentSettings();
        BalanceTransaction transaction = BalanceTransaction.retrieve(
                payment.getStripeBalanceTransactionId(), settings.getSecretKey());
        payment.setFeesInCents(transaction.getFee());
        paymentRepository.save(payment);

        if (payment.getSubscription() != null) {
            Subscription subscription = payment.getSubscription();
            DateTime nextPaymentDue = new DateTime(payment.getPaidOn());
            switch (subscription.getFrequency()) {
                case week:
                    nextPaymentDue = nextPaymentDue.plusWeeks(1);
                    break;
                case month:
                    nextPaymentDue = nextPaymentDue.plusMonths(1);
                    break;
                case year:
                    nextPaymentDue = nextPaymentDue.plusYears(1);
                    break;
                default:
                    throw new IllegalArgumentException("Unsupported frequency: " + subscription.getFrequency());
            }
            subscription.setNextPaymentDue(nextPaymentDue);
            subscriptionRepository.save(subscription);
        }

        return payment;
    }

    private String createPaymentPlanId(CreateSubscriptionRequest request) {
        return request.getType().name() + "_" + request.getFrequency().name()
                + "_" + (request.getAmountInCents() / 100) + request.getCurrency();
    }

    private String createPaymentPlanName(CreateSubscriptionRequest request) {
        return "Recurring " + request.getFrequency().name() + " " + request.getType().name()
                + " of " + (request.getAmountInCents() / 100) + request.getCurrency();
    }
}
