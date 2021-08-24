package org.aleks4ay.hotel.service;

import org.aleks4ay.hotel.exception.AlreadyException;
import org.aleks4ay.hotel.exception.NotFoundException;
import org.aleks4ay.hotel.model.Invoice;
import org.aleks4ay.hotel.model.Order;
import org.aleks4ay.hotel.repository.InvoiceRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class InvoiceService {
    private InvoiceRepo invoiceRepo;

    @Autowired
    public InvoiceService(InvoiceRepo invoiceRepo) {
        this.invoiceRepo = invoiceRepo;
    }

    public Optional<Invoice> getById(Long id) {
        return invoiceRepo.findById(id);
    }

    public Invoice save(Invoice invoice) {
        if (invoiceRepo.findByOrderId(invoice.getOrder().getId()) != null) {
            throw new AlreadyException("Invoice for Order '" + invoice.getOrder().getId() + "' already exists");
        }
        return invoiceRepo.save(invoice);
    }

    public Page<Invoice> findAll(int numPage, int rowsOnPage, String sortMethod) {
        Pageable sortedPage = PageRequest.of(numPage, rowsOnPage, Sort.by(sortMethod));
        return invoiceRepo.findAll(sortedPage);
    }

    public Optional<Invoice> getByOrderId(Long id) {
        return Optional.of(invoiceRepo.findByOrderId(id));
//                .orElseThrow(() -> new NotFoundException("Invoice for order #" + id + "not found"));
    }


    public Invoice create(Invoice.Status aNew, Order order) {
        Invoice invoice = new Invoice(LocalDateTime.now(), Invoice.Status.NEW, order);
        order.setInvoice(invoice);
        return invoice;
    }
}
