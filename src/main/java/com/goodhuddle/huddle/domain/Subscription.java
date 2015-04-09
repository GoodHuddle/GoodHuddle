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
@Table(name = "subscription")
@SequenceGenerator(name = "sequence_generator", sequenceName = "subscription_id_seq")
public class Subscription extends AbstractHuddleObject<Long> {

    @Column(name = "frequency")
    @Enumerated(EnumType.STRING)
    private SubscriptionFrequency frequency;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private SubscriptionStatus status;

    @Column(name = "payment_type")
    @Enumerated(EnumType.STRING)
    private PaymentType paymentType;

    @Column(name = "stripe_customer_id")
    private String stripeCustomerId;

    @Column(name = "stripe_subscription_id")
    private String stripeSubscriptionId;

    @Column(name = "amount_in_cents")
    private int amountInCents;

    @Column(name = "currency")
    private String currency;

    @Column(name = "description")
    private String description;

    @ManyToOne
    @JoinColumn(name="member_id")
    private Member member;

    @Column(name="next_payment_due")
    @Type(type="org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    private DateTime nextPaymentDue;

    @Column(name="created_on")
    @Type(type="org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    private DateTime createdOn;

    @Column(name="cancelled_on")
    @Type(type="org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    private DateTime cancelledOn;

    @ManyToOne
    @JoinColumn(name="cancelled_by")
    private Member cancelledBy;

    public Subscription() {
    }

    public Subscription(Huddle huddle, SubscriptionFrequency frequency, PaymentType paymentType,
                        String stripeCustomerId, String stripeSubscriptionId,
                        int amountInCents, String currency, String description, Member member) {
        super(huddle);
        this.frequency = frequency;
        this.status = SubscriptionStatus.active;
        this.paymentType = paymentType;
        this.stripeCustomerId = stripeCustomerId;
        this.stripeSubscriptionId = stripeSubscriptionId;
        this.amountInCents = amountInCents;
        this.currency = currency;
        this.description = description;
        this.member = member;
        this.createdOn = new DateTime();
    }

    public SubscriptionFrequency getFrequency() {
        return frequency;
    }

    public void setFrequency(SubscriptionFrequency frequency) {
        this.frequency = frequency;
    }

    public SubscriptionStatus getStatus() {
        return status;
    }

    public void setStatus(SubscriptionStatus status) {
        this.status = status;
    }

    public PaymentType getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(PaymentType paymentType) {
        this.paymentType = paymentType;
    }

    public String getStripeCustomerId() {
        return stripeCustomerId;
    }

    public void setStripeCustomerId(String stripeCustomerId) {
        this.stripeCustomerId = stripeCustomerId;
    }

    public String getStripeSubscriptionId() {
        return stripeSubscriptionId;
    }

    public void setStripeSubscriptionId(String stripeSubscriptionId) {
        this.stripeSubscriptionId = stripeSubscriptionId;
    }

    public int getAmountInCents() {
        return amountInCents;
    }

    public void setAmountInCents(int amountInCents) {
        this.amountInCents = amountInCents;
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

    public Member getMember() {
        return member;
    }

    public void setMember(Member member) {
        this.member = member;
    }

    public DateTime getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(DateTime createdOn) {
        this.createdOn = createdOn;
    }

    public void setCancelled(Member cancelledBy) {
        this.status = SubscriptionStatus.cancelled;
        this.cancelledBy = cancelledBy;
        this.cancelledOn = DateTime.now();
    }

    public DateTime getCancelledOn() {
        return cancelledOn;
    }

    public void setCancelledOn(DateTime cancelledOn) {
        this.cancelledOn = cancelledOn;
    }

    public Member getCancelledBy() {
        return cancelledBy;
    }

    public void setCancelledBy(Member cancelledBy) {
        this.cancelledBy = cancelledBy;
    }

    public DateTime getNextPaymentDue() {
        return nextPaymentDue;
    }

    public void setNextPaymentDue(DateTime nextPaymentDue) {
        this.nextPaymentDue = nextPaymentDue;
    }
}
