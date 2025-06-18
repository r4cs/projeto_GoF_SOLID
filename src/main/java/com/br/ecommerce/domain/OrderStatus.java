package com.br.ecommerce.domain;

public enum OrderStatus {
    NEW {
        @Override
        public OrderStatus next() {
            return PROCESSING;
        }
    },
    PROCESSING {
        @Override
        public OrderStatus next() {
            return SHIPPED;
        }
    },
    SHIPPED {
        @Override
        public OrderStatus next() {
            return DELIVERED;
        }
    },
    DELIVERED {
        @Override
        public OrderStatus next() {
            return this; // Não avança mais
        }
    };

    public abstract OrderStatus next();

    public boolean canAdvance() {
        return next() != this;
    }
}