package by.staz.services;

import by.staz.components.TransactionHelper;
import by.staz.entity.Order;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class OrderService {
    private final TransactionHelper transactionHelper;

    public OrderService(TransactionHelper transactionHelper) {
        this.transactionHelper = transactionHelper;
    }

    public List<Order> searchOrders(LocalDateTime dateAfter, BigDecimal minAmount, String status) {
        return transactionHelper.executeInTransaction(session -> {
            StringBuilder hql = new StringBuilder("FROM Order o WHERE 1=1");

            if (dateAfter != null) {
                hql.append(" AND o.orderDate >= :dateParam");
            }
            if (minAmount != null) {
                hql.append(" AND o.totalAmount >= :amountParam");
            }
            if (status != null && !status.isBlank()) {
                hql.append(" AND o.status = :statusParam");
            }
            hql.append(" ORDER BY o.orderDate DESC");
            var query = session.createQuery(hql.toString(), Order.class);
            if (dateAfter != null) {
                query.setParameter("dateParam", dateAfter);
            }
            if (minAmount != null) {
                query.setParameter("amountParam", minAmount);
            }
            if (status != null && !status.isBlank()) {
                query.setParameter("statusParam", status);
            }
            return query.getResultList();
        });
    }
}
