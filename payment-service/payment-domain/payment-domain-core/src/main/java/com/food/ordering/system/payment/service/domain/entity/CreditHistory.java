package com.food.ordering.system.payment.service.domain.entity;

import com.food.ordering.system.domain.entity.BaseEntity;
import com.food.ordering.system.domain.valueobject.CustomerId;
import com.food.ordering.system.domain.valueobject.Money;
import com.food.ordering.system.payment.service.domain.valueobject.CreditHistoryId;
import com.food.ordering.system.payment.service.domain.valueobject.TransactionalType;

public class CreditHistory extends BaseEntity<CreditHistoryId> {
    private final CustomerId customerId;
    private final Money amount;
    private final TransactionalType transactionalType;

    private CreditHistory(Builder builder) {
        setId(builder.id);
        customerId = builder.customerId;
        amount = builder.amount;
        transactionalType = builder.transactionalType;
    }

    public static Builder builder() {
        return new Builder();
    }

    public CustomerId getCustomerId() {
        return customerId;
    }

    public Money getAmount() {
        return amount;
    }

    public TransactionalType getTransactionalType() {
        return transactionalType;
    }

    public static final class Builder {
        private CreditHistoryId id;
        private CustomerId customerId;
        private Money amount;
        private TransactionalType transactionalType;

        private Builder() {
        }

        public Builder id(CreditHistoryId val) {
            id = val;
            return this;
        }

        public Builder customerId(CustomerId val) {
            customerId = val;
            return this;
        }

        public Builder amount(Money val) {
            amount = val;
            return this;
        }

        public Builder transactionalType(TransactionalType val) {
            transactionalType = val;
            return this;
        }

        public CreditHistory build() {
            return new CreditHistory(this);
        }
    }
}
