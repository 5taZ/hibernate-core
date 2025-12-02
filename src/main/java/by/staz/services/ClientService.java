package by.staz.services;

import by.staz.components.TransactionHelper;
import by.staz.entity.Client;
import by.staz.entity.Coupon;
import by.staz.entity.Order;
import by.staz.entity.Profile;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class ClientService {
    private final SessionFactory sessionFactory;
    private final TransactionHelper transactionHelper;

    public ClientService(SessionFactory sessionFactory, TransactionHelper transactionHelper) {
        this.sessionFactory = sessionFactory;
        this.transactionHelper = transactionHelper;
    }
    public Client saveClient(Client client){
        return transactionHelper.executeInTransaction(session -> {
            session.persist(client);
            return client;
        });
    }
    public void deleteClient(Long id){
        transactionHelper.executeInTransaction(session -> {
            Client removableClient = session.get(Client.class,id);
            if (removableClient != null)
                session.remove(removableClient);
        });
    }
    public void updateClientProfile(Long clientId, String address, String phone){
        transactionHelper.executeInTransaction(session -> {
            Client client = session.get(Client.class, clientId);
            if(client != null){
                Profile profile = client.getProfile();
                if(profile == null){
                    profile = new Profile();
                    client.setProfile(profile);
                }
                profile.setAddress(address);
                profile.setPhone(phone);
            }
        });
    }
    public Client findClient(Long id){
        return transactionHelper.executeInTransaction(session -> {
            return session.get(Client.class, id);
        });
    }

    public void addOrderToClient(Long clientId, Order newOrder){
        transactionHelper.executeInTransaction(session -> {
            Client client = session.get(Client.class, clientId);
            client.addOrder(newOrder);
        });
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
        Session session = sessionFactory.openSession();
        Client client = new Client();
        client.setName(name);
        client.setEmail(email);
        Profile profile = new Profile();
        profile.setAddress(address);
        profile.setPhone(phone);
        client.setProfile(profile);
        profile.setClient(client);

        if(withInitialOrder){
            Order order = new Order();
            order.setOrderDate(LocalDateTime.now());
            order.setTotalAmount(orderAmount);
            order.setStatus(orderStatus);
            client.getOrders().add(order);
            order.setClient(client);
        }

        if(couponIds != null){
            for (Long id : couponIds){
                Coupon coupon = session.get(Coupon.class, id);
                if (coupon != null){
                    client.getCoupons().add(coupon);
                    coupon.getClients().add(client);
                }
            }
        }
        return client.getId();
    }
}
