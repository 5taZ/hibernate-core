import by.staz.components.TransactionHelper;
import by.staz.entity.Client;
import by.staz.entity.Order;
import by.staz.entity.Profile;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import by.staz.services.ClientService;
import by.staz.services.OrderService;
import org.hibernate.SessionFactory;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        AnnotationConfigApplicationContext context =
                new AnnotationConfigApplicationContext("by/staz/");

        SessionFactory sessionFactory = context.getBean(SessionFactory.class);
        TransactionHelper transactionHelper = context.getBean(TransactionHelper.class);

        ClientService clientService = context.getBean(ClientService.class);
        OrderService orderService = context.getBean(OrderService.class);

        createTestData(clientService);

        System.out.println("\n--- ТЕСТ 1: Поиск всех заказов (фильтры null) ---");
        List<Order> allOrders = orderService.searchOrders(null, null, null);
        printOrders(allOrders);

        System.out.println("\n--- ТЕСТ 2: Поиск дорогих заказов (> 2000) ---");
        List<Order> expensiveOrders = orderService.searchOrders(null, new BigDecimal("2000"), null);
        printOrders(expensiveOrders);

        System.out.println("\n--- ТЕСТ 3: Поиск по статусу 'COMPLETED' ---");
        List<Order> completedOrders = orderService.searchOrders(null, null, "COMPLETED");
        printOrders(completedOrders);

        System.out.println("\n--- ТЕСТ 4: Комбинированный (Дороже 2000 И статус NEW) ---");
        List<Order> comboOrders = orderService.searchOrders(null, new BigDecimal("2000"), "NEW");
        printOrders(comboOrders);

    }

    private static void printOrders(List<Order> orders) {
        if (orders.isEmpty()) {
            System.out.println("Заказы не найдены.");
        } else {
            for (Order o : orders) {
                System.out.printf("Order ID: %d | Сумма: %s | Статус: %s | Дата: %s\n",
                        o.getId(), o.getTotalAmount(), o.getStatus(), o.getOrderDate());
            }
        }
    }

    private static void createTestData(ClientService service) {
        Client client = new Client();
        client.setName("Test User");
        client.setEmail("test@user.com");
        client.setRegistrationDate(LocalDateTime.now());
        client.setProfile(new Profile(null, "Address", "123", null));

        // Заказ 1: Дешевый, выполнен
        Order o1 = new Order();
        o1.setTotalAmount(new BigDecimal("500.00"));
        o1.setStatus("COMPLETED");
        o1.setOrderDate(LocalDateTime.now().minusDays(5));
        client.addOrder(o1);

        // Заказ 2: Средний, новый
        Order o2 = new Order();
        o2.setTotalAmount(new BigDecimal("1500.00"));
        o2.setStatus("NEW");
        o2.setOrderDate(LocalDateTime.now().minusDays(2));
        client.addOrder(o2);

        // Заказ 3: Дорогой, новый
        Order o3 = new Order();
        o3.setTotalAmount(new BigDecimal("5000.00"));
        o3.setStatus("NEW");
        o3.setOrderDate(LocalDateTime.now());
        client.addOrder(o3);

        service.saveClient(client);
        System.out.println("Тестовые данные созданы.");
    }
}