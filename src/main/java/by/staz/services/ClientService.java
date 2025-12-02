package by.staz.services;

import by.staz.components.TransactionHelper;
import by.staz.entity.Client;
import by.staz.entity.Coupon;
import by.staz.entity.Order;
import by.staz.entity.Profile;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.cglib.core.Local;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class ClientService {
    private final TransactionHelper transactionHelper;

    public ClientService(SessionFactory sessionFactory, TransactionHelper transactionHelper) {
        this.transactionHelper = transactionHelper;
    }


    public Long addClient(
            String name,
            String email,
            String address,
            String phone,
            boolean withInitialOrder,
            BigDecimal orderAmount,
            String orderStatus,
            List<Long> couponIds
    )
    {
        return transactionHelper.executeInTransaction(session -> {
            Client client = new Client();
            client.setName(name);
            client.setEmail(email);
            client.setRegistrationDate(LocalDateTime.now());
            Profile profile = new Profile();
            profile.setAddress(address);
            profile.setPhone(phone);
            client.setProfile(profile);
            if (withInitialOrder){
                Order order = new Order();
                order.setOrderDate(LocalDateTime.now());
                order.setTotalAmount(orderAmount);
                order.setStatus(orderStatus);
                client.addOrder(order);

            }
            if(couponIds != null){
                for (Long id : couponIds){
                    Coupon coupon = session.get(Coupon.class, id);
                    if (coupon != null){
                        client.addCoupon(coupon);
                    }
                }
            }
            session.persist(client);
            return client.getId();
        });
    }
    public void deleteClient(Long id) {
        transactionHelper.executeInTransaction(session -> {
            Client client = session.get(Client.class, id);

            if (client != null) {
                session.remove(client);
                System.out.println("Клиент с ID " + id + " и все связанные данные удалены.");
            } else {
                System.out.println("Клиент с ID " + id + " не найден.");
            }
        });
    }
}
