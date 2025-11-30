package by.staz.services;

import by.staz.components.TransactionHelper;
import by.staz.entity.Coupon;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Service;

@Service
public class CouponService {
    private final SessionFactory sessionFactory;
    private final TransactionHelper transactionHelper;

    public CouponService(SessionFactory sessionFactory, TransactionHelper transactionHelper) {
        this.sessionFactory = sessionFactory;
        this.transactionHelper = transactionHelper;
    }

    public Coupon saveCoupon(Coupon coupon){
        return transactionHelper.executeInTransaction(session -> {
            session.persist(coupon);
            return coupon;
        });
    }

    public void updateCoupon(Long id, String code, Double discount){
        transactionHelper.executeInTransaction(session -> {
            Coupon coupon = session.get(Coupon.class, id);
            if (coupon != null){
                coupon.setCode(code);
                coupon.setDiscount(discount);
            }
        });
    }
    public Coupon findCoupon(Long id){
        return transactionHelper.executeInTransaction(session -> {
            return session.get(Coupon.class, id);
        });
    }
}
