package services;

import components.TransactionHelper;
import entity.Client;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
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
}
