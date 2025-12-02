
import by.staz.entity.Order;
import by.staz.services.ClientService;
import by.staz.services.CouponService;
import by.staz.services.OrderService;
import by.staz.services.ProfileService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.math.BigDecimal;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        // 1. Инициализация Spring Context
        // Убедитесь, что "by.staz" - это корневой пакет, где лежат ваши конфиги и сервисы
        ApplicationContext context = new AnnotationConfigApplicationContext("by.staz");

        // 2. Получение бинов
        ClientService clientService = context.getBean(ClientService.class);
        OrderService orderService = context.getBean(OrderService.class);
        CouponService couponService = context.getBean(CouponService.class);
        ProfileService profileService = context.getBean(ProfileService.class);

        Scanner scanner = new Scanner(System.in);
        System.out.println("Приложение успешно запущено!");

        // 3. Главный цикл приложения
        while (true) {
            printMenu();
            System.out.print("Введите номер команды: ");
            String command = scanner.nextLine();

            try {
                switch (command) {
                    case "1" -> handleAddClient(scanner, clientService);
                    case "2" -> handleDeleteClient(scanner, clientService);
                    case "3" -> handleEditProfile(scanner, profileService);
                    case "4" -> handleAddOrder(scanner, orderService);
                    case "5" -> handleEditCoupon(scanner, couponService);
                    case "6" -> handleSearchOrders(scanner, orderService);
                    case "7" -> {
                        System.out.println("Завершение работы...");
                        return; // Выход из программы
                    }
                    default -> System.out.println("⚠ Неверная команда, попробуйте снова.");
                }
            } catch (NumberFormatException e) {
                System.out.println("⚠ Ошибка ввода: введите корректное число.");
            } catch (Exception e) {
                System.out.println("⚠ Произошла ошибка: " + e.getMessage());
                e.printStackTrace(); // Можно убрать, если не нужен стектрейс
            }
            System.out.println("--------------------------------------------------");
        }
    }

    private static void printMenu() {
        System.out.println("\n=== МЕНЮ УПРАВЛЕНИЯ ===");
        System.out.println("1. Добавить клиента");
        System.out.println("2. Удалить клиента");
        System.out.println("3. Редактировать профиль");
        System.out.println("4. Добавить заказ");
        System.out.println("5. Редактировать купоны");
        System.out.println("6. Найти заказы (фильтр)");
        System.out.println("7. Выход");
    }

    // --- 1. Добавление клиента ---
    private static void handleAddClient(Scanner scanner, ClientService service) {
        System.out.println("\n--- Добавление клиента ---");
        System.out.print("Введите имя: ");
        String name = scanner.nextLine();

        System.out.print("Введите email: ");
        String email = scanner.nextLine();

        System.out.print("Введите адрес: ");
        String address = scanner.nextLine();

        System.out.print("Введите телефон: ");
        String phone = scanner.nextLine();

        // Вызываем ваш метод (передаем null/false для опциональных параметров, чтобы упростить ввод)
        // Если хотите добавить ввод купонов сразу - можно расширить этот метод
        Long id = service.addClient(name, email, address, phone, false, null, null, null);

        System.out.println("✅ Клиент успешно создан. ID: " + id);
    }

    // --- 2. Удаление клиента ---
    private static void handleDeleteClient(Scanner scanner, ClientService service) {
        System.out.println("\n--- Удаление клиента ---");
        System.out.print("Введите ID клиента для удаления: ");
        Long id = Long.parseLong(scanner.nextLine());

        service.deleteClient(id);
        // Сообщение об успехе уже есть внутри сервиса (или можно добавить тут)
    }

    // --- 3. Редактирование профиля ---
    private static void handleEditProfile(Scanner scanner, ProfileService service) {
        System.out.println("\n--- Редактирование профиля ---");
        System.out.print("Введите ID клиента: ");
        Long clientId = Long.parseLong(scanner.nextLine());

        System.out.print("Новый адрес: ");
        String address = scanner.nextLine();

        System.out.print("Новый телефон: ");
        String phone = scanner.nextLine();

        service.updateProfileByClientId(clientId, address, phone);
    }

    // --- 4. Добавление заказа ---
    private static void handleAddOrder(Scanner scanner, OrderService service) {
        System.out.println("\n--- Добавление заказа ---");
        System.out.print("Введите ID клиента: ");
        Long clientId = Long.parseLong(scanner.nextLine());

        System.out.print("Сумма заказа: ");
        BigDecimal amount = new BigDecimal(scanner.nextLine());

        System.out.print("Статус заказа (NEW, PAID, SHIPPED): ");
        String status = scanner.nextLine();

        service.addOrderToClient(clientId, java.time.LocalDateTime.now(), amount, status);
    }

    // --- 5. Редактирование купона ---
    private static void handleEditCoupon(Scanner scanner, CouponService service) {
        System.out.println("\n--- Редактирование купона ---");
        System.out.print("Введите ID купона: ");
        Long id = Long.parseLong(scanner.nextLine());

        System.out.print("Новый код купона: ");
        String code = scanner.nextLine();

        System.out.print("Новая скидка (например 0.15): ");
        Double discount = Double.parseDouble(scanner.nextLine());

        service.editCoupon(id, code, discount);
    }

    // --- 6. Поиск заказов ---
    private static void handleSearchOrders(Scanner scanner, OrderService service) {
        System.out.println("\n--- Поиск заказов ---");
        System.out.println("(Нажмите Enter, чтобы пропустить фильтр)");

        System.out.print("Минимальная сумма: ");
        String amountStr = scanner.nextLine();
        BigDecimal minAmount = amountStr.isBlank() ? null : new BigDecimal(amountStr);

        System.out.print("Статус: ");
        String status = scanner.nextLine();
        if (status.isBlank()) status = null;

        // Тут можно добавить ввод даты, если нужно

        System.out.println("Выполняю поиск...");
        List<Order> orders = service.searchOrders(null, minAmount, status);

        if (orders.isEmpty()) {
            System.out.println("ℹ Заказы не найдены.");
        } else {
            // Красивый вывод таблицы
            System.out.printf("%-5s | %-16s | %-10s | %-10s | %-20s%n", "ID", "Дата", "Сумма", "Статус", "Клиент");
            System.out.println("-------------------------------------------------------------------------");

            for (Order o : orders) {
                // Если Fetch Join настроен правильно, этот вызов не сделает SQL-запрос
                String clientName = (o.getClient() != null) ? o.getClient().getName() : "Unknown";

                System.out.printf("%-5d | %-16s | %-10s | %-10s | %-20s%n",
                        o.getId(),
                        o.getOrderDate().toString().substring(0, 16).replace("T", " "),
                        o.getTotalAmount(),
                        o.getStatus(),
                        clientName
                );
            }
            System.out.println("Всего найдено: " + orders.size());
        }
    }
}