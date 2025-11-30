package services;

import components.TransactionHelper;
import entity.Client;
import entity.Order;
import entity.Profile;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Service;

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
}
