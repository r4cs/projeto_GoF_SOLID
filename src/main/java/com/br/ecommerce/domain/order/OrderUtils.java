// src/main/java/com/br/ecommerce/domain/order/OrderUtils.java
package com.br.ecommerce.domain.order;

import com.br.ecommerce.domain.OrderStatus;

public class OrderUtils {
    public String getStatusClass(OrderStatus status) {
        if (status == OrderStatus.DELIVERED) return "bg-success";
        if (status == OrderStatus.CANCELED) return "bg-danger";
        return "bg-primary";
    }
}