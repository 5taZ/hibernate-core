package by.staz.services;

import by.staz.components.TransactionHelper;
import by.staz.entity.Coupon;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Service;

@Service
public class CouponService {
    private final TransactionHelper transactionHelper;

    public CouponService(SessionFactory sessionFactory, TransactionHelper transactionHelper) {
        this.transactionHelper = transactionHelper;
    }

    public void editCoupon(Long id, String newCode, Double newDiscount) {
        transactionHelper.executeInTransaction(session -> {
            Coupon coupon = session.get(Coupon.class, id);

            if (coupon != null) {
                coupon.setCode(newCode);
                coupon.setDiscount(newDiscount);

                System.out.println("Купон с ID " + id + " успешно обновлен.");
            } else {
                System.out.println("Купон с ID " + id + " не найден.");
            }
        });
    }

    public Long createCoupon(String code, Double discount) {
        return transactionHelper.executeInTransaction(session -> {
            Coupon coupon = new Coupon();
            coupon.setCode(code);
            coupon.setDiscount(discount);
            session.persist(coupon);
            System.out.println("ℹ Создан новый купон: " + code);
            return coupon.getId();
        });
    }
}
