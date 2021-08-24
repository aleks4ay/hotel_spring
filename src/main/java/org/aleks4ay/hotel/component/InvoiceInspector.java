package org.aleks4ay.hotel.component;

import org.aleks4ay.hotel.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class InvoiceInspector {

    private final OrderService orderService;

    @Autowired
    public InvoiceInspector(OrderService orderService) {
        this.orderService = orderService;
    }

    @Scheduled(fixedRate = 15 * 60 * 1000)
    public void startInvoiceInspector() {
        System.out.println("Task doing");
        orderService.setCancelInvoice();
    }
}

