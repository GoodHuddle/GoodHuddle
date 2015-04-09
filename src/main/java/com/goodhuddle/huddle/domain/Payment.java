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

package com.goodhuddle.huddle.domain;

import org.hibernate.annotations.Type;
import org.joda.time.DateTime;

import javax.persistence.*;

@Entity
@Table(name = "payment")
@SequenceGenerator(name = "sequence_generator", sequenceName = "payment_id_seq")
public class Payment extends AbstractHuddleObject<Long> {

    @Column(name = "type")
    @Enumerated(EnumType.STRING)
    private PaymentType type;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private PaymentStatus status;

    @Column(name = "stripe_payment_id")
    private String stripePaymentId;

    @Column(name = "stripe_balance_trans_id")
    private String stripeBalanceTransactionId;

    @ManyToOne
    @JoinColumn(name="subscription_id")
    private Subscription subscription;

    @Column(name = "amount_in_cents")
    private int amountInCents;

    @Column(name = "fees_in_cents")
    private int feesInCents;

    @Column(name = "currency")
    private String currency;

    @Column(name = "description")
    private String description;

    @Column(name = "paid_by_email")
    private String paidByEmail;

    @ManyToOne
    @JoinColumn(name="paid_by")
    private Member paidBy;

    @Column(name="paid_on")
    @Type(type="org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    private DateTime paidOn;

    public Payment() {
    }


    public Payment(Subscription subscription, String stripePaymentId, String stripeBalanceTransactionId,
                   int amountInCents, String currency, String description, DateTime paidOn) {
        super(subscription.getHuddle());
        this.type = subscription.getPaymentType();
        this.status = PaymentStatus.paid;
        this.subscription = subscription;
        this.stripePaymentId = stripePaymentId;
        this.stripeBalanceTransactionId = stripeBalanceTransactionId;
        this.amountInCents = amountInCents;
        this.currency = currency;
        this.description = description;
        this.paidBy = subscription.getMember();
        this.paidByEmail = paidBy.getEmail();
        this.paidOn = paidOn;
    }

    public Payment(Huddle huddle, PaymentType type, String stripePaymentId, String stripeBalanceTransactionId,
                   int amountInCents, String currency, String description,
                   String paidByEmail, Member paidBy, DateTime paidOn) {
        super(huddle);
        this.type = type;
        this.status = PaymentStatus.paid;
        this.stripePaymentId = stripePaymentId;
        this.stripeBalanceTransactionId = stripeBalanceTransactionId;
        this.amountInCents = amountInCents;
        this.currency = currency;
        this.description = description;
        this.paidByEmail = paidByEmail;
        this.paidBy = paidBy;
        this.paidOn = paidOn;
    }

    public PaymentType getType() {
        return type;
    }

    public PaymentStatus getStatus() {
        return status;
    }

    public void setType(PaymentType type) {
        this.type = type;
    }

    public String getStripePaymentId() {
        return stripePaymentId;
    }

    public void setStripePaymentId(String stripePaymentId) {
        this.stripePaymentId = stripePaymentId;
    }

    public String getStripeBalanceTransactionId() {
        return stripeBalanceTransactionId;
    }

    public void setStripeBalanceTransactionId(String stripeBalanceTransactionId) {
        this.stripeBalanceTransactionId = stripeBalanceTransactionId;
    }

    public Subscription getSubscription() {
        return subscription;
    }

    public void setSubscription(Subscription subscription) {
        this.subscription = subscription;
    }

    public int getAmountInCents() {
        return amountInCents;
    }

    public void setAmountInCents(int amountInCents) {
        this.amountInCents = amountInCents;
    }

    public int getFeesInCents() {
        return feesInCents;
    }

    public void setFeesInCents(int feesInCents) {
        this.feesInCents = feesInCents;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPaidByEmail() {
        return paidByEmail;
    }

    public void setPaidByEmail(String paidByEmail) {
        this.paidByEmail = paidByEmail;
    }

    public Member getPaidBy() {
        return paidBy;
    }

    public void setPaidBy(Member paidBy) {
        this.paidBy = paidBy;
    }

    public DateTime getPaidOn() {
        return paidOn;
    }

    public void setPaidOn(DateTime paidOn) {
        this.paidOn = paidOn;
    }
}
