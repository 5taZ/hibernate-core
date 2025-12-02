package by.staz.services;

import by.staz.components.TransactionHelper;
import by.staz.entity.Client;
import by.staz.entity.Profile;
import org.springframework.stereotype.Service;

@Service
public class ProfileService {
    private final TransactionHelper transactionHelper;

    public ProfileService(TransactionHelper transactionHelper) {
        this.transactionHelper = transactionHelper;
    }
    public void updateProfileByClientId(Long clientId, String newAddress, String newPhoneNumber) {
        transactionHelper.executeInTransaction(session -> {
            Client client = session.get(Client.class, clientId);
            if (client != null) {
                Profile profile = client.getProfile();

                if (profile != null) {
                    profile.setAddress(newAddress);
                    profile.setPhone(newPhoneNumber);

                    System.out.println("Профиль для клиента с ID " + clientId + " успешно обновлен.");
                } else {
                    System.out.println("Ошибка: У клиента с ID " + clientId + " профиль отсутствует.");
                }
            } else {
                System.out.println("Ошибка: Клиент с ID " + clientId + " не найден.");
            }
        });
    }
}
