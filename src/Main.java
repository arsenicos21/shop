import Shop.*;
import Shop.Interfaces.*;

import java.util.Scanner;
import static java.lang.System.out;

public class Main {
    private static final Scanner scanner = new Scanner(System.in);
    private static final Catalog gunCatalog = new ShopCatalog();
    private static Orders orders = new ShopOrders();
    private static Cart completedCart = null;
    private static Clients shopClients = new ShopClients();
    private static int orderNumber = 1;

    public static void main(String[] args) {
        createCatalog(gunCatalog);

        out.println("Welcome to gun-shop AMMO Nation");
        out.println();

        while (true) {
            out.println("Select a menu item:\n" +
                    "1. Viewing the catalog\n" +
                    "2. Add the product to the cart\n" +
                    "3. Empty the basket\n" +
                    "4. Place an order\n" +
                    "5. Delete an order\n" +
                    "6. Return the order\n" +
                    "7. Arrange delivery\n" +
                    "8. Exit\n");
            out.print("-> ");

            String choice = scanner.nextLine();

            switch (choice) {
                case "1": workWithCatalog();
                    break;
                case "2": completedCart = workWithCart();
                    break;
                case "3": removeItemFromCart();
                    break;
                case "4": checkout();
                    break;
                case "5": deleteOrder();
                    break;
                case "6": customerReturn();
                    break;
                case "7": arrangeDelivery();
                    break;
                case "8":
                    return;
            }
        }
    }

    public static void createCatalog(Catalog shopCatalog) {
        shopCatalog.addItem(
                new ShopProduct("AK-47", Manufacturer.IZHMASH, 1300, 200, 9));
        shopCatalog.addItem(
                new ShopProduct("AK-74", Manufacturer.IZHMASH, 1700, 80, 7));
        shopCatalog.addItem(
                new ShopProduct("AKS-74U", Manufacturer.TOZ, 2000, 20, 6));
        shopCatalog.addItem(
                new ShopProduct("Mosin Rifle", Manufacturer.TOZ, 300, 50, 6));
        shopCatalog.addItem(
                new ShopProduct("Beretta 92 Compact", Manufacturer.BERETTA, 650, 50, 8));
        shopCatalog.addItem(
                new ShopProduct("Beretta M9", Manufacturer.BERETTA, 800, 50, 7));
        shopCatalog.addItem(
                new ShopProduct("Benelli M1", Manufacturer.BENELLI, 650, 30, 7));
        shopCatalog.addItem(
                new ShopProduct("Benelli Nova", Manufacturer.BENELLI, 1500, 40, 8));
        shopCatalog.addItem(
                new ShopProduct("MP5", Manufacturer.HK, 900, 100, 7));
        shopCatalog.addItem(
                new ShopProduct("P2000", Manufacturer.HK, 600, 200, 6));
        shopCatalog.addItem(
                new ShopProduct("Mauser 98k", Manufacturer.MAUSER, 300, 60, 6));
        shopCatalog.addItem(
                new ShopProduct("MG 42", Manufacturer.MAUSER, 10_000, 10, 9));
        shopCatalog.addItem(
                new ShopProduct("M1911", Manufacturer.COLT, 250, 500, 9));
        shopCatalog.addItem(
                new ShopProduct("M16", Manufacturer.COLT, 800, 400, 7));
        shopCatalog.addItem(
                new ShopProduct("Browning Citori", Manufacturer.BROWNING, 1700, 70, 6));
        shopCatalog.addItem(
                new ShopProduct("Browning BPS", Manufacturer.BROWNING, 600, 100, 7));
    }

    public static void workWithCatalog() {
        gunCatalog.viewCatalog();

        while (true) {
            out.println("Choose filter:\n" +
                    "1. Keywords\n" +
                    "2. Price\n" +
                    "3. Manufacturer\n" +
                    "4. Exit");
            out.print("-> ");

            String filter = scanner.nextLine();
            switch (filter) {
                case "1":
                    out.print("Enter a keyword: ");
                    String keyWord = scanner.nextLine();
                    gunCatalog.keywordFilter(keyWord);
                case  "2":
                    out.print("Enter the price range separated by a space: ");
                    String priceRange = scanner.nextLine();
                    String[] parts = priceRange.split(" ");
                    int minPrice = Integer.parseInt(parts[0]);
                    int maxPrice = Integer.parseInt(parts[1]);
                    gunCatalog.priceFilter(minPrice, maxPrice);
                case  "3":
                    out.print("Enter the name of the manufacturers from the list -\n" +
                            "IZHMASH\n" +
                            "TOZ\n" +
                            "BERETTA\n" +
                            "BENELLI\n" +
                            "HK\n" +
                            "MAUSER\n" +
                            "COLT\n" +
                            "BROWNING");
                    out.print("-> ");
                    String brand = scanner.nextLine();
                    gunCatalog.brandFilter(brand);
                case "4":
                    return;
            }
        }
    }

    public static Cart workWithCart() {
        Cart newCart = new ShopCart();
        out.println("Select a gun from the catalog");
        gunCatalog.viewCatalog();

        while (true) {
            out.println("Enter the product ID or enter <exit>");
            String input = scanner.nextLine();

            if ("exit".equals(input)) {
                break;
            } else {
                int productId = Integer.parseInt(input);
                out.println("Enter the quantity of the product:");
                int productVolume = Integer.parseInt(scanner.nextLine());
                CartItem item = new ShopCartItem(gunCatalog.getItem(productId), productVolume);
                gunCatalog
                        .getItem(productId)
                        .setQuantity(gunCatalog.getItem(productId).getQuantity() - productVolume);
                gunCatalog.purchaseRecommendationSystem(gunCatalog.getItem(productId));
                newCart.addItem(item);
            }
        }
        return newCart;
    }

    public static void removeItemFromCart() {
        out.println("Do you want to empty the basket? (yes/no)");
        String answer = scanner.nextLine();

        if("yes".equals(answer)) {
            assert completedCart != null;
            for (int i = 0; i < completedCart.getSize(); i++) {
                CartItem item = completedCart.getCartItem(i);
                Product product = item.getProduct();
                gunCatalog
                        .getItem(product.getId())
                        .setQuantity(gunCatalog.getItem(product.getId()).getQuantity() + item.getCount());
            }
            completedCart.emptyCart();
            out.println("The basket is empty");
        }
    }

    public static void checkout() {
        out.println("Your basket:");
        assert completedCart != null;
        out.println(completedCart);

        out.println("Do you want to place an order? (yes/no)");
        String answer = scanner.nextLine();

        if ("yes".equals(answer)) {
            out.print("Enter your data (first name, last name) separated by a space: ");
            String name = scanner.nextLine();

            out.println("Enter your phone number: ");
            String phoneNumber = scanner.nextLine();

            out.println("Enter your address (city, street, house, apartment) separated by a space: ");
            String address = scanner.nextLine();

            Client newClient = new ShopClient(name, phoneNumber, address);

            shopClients.addClient(newClient);

            Order newOrder = new ShopOrder(orderNumber, completedCart);

            orders.addOrder(orderNumber, newOrder);
            orderNumber++;
            out.println("Your order number: " + (orderNumber - 1));
        }
    }

    public static void deleteOrder() {
        out.println("Enter your order number: ");
        int number  = Integer.parseInt(scanner.nextLine());

        out.println("Your order: \n" + orders.getOrder(number));

        out.println("Do you want to delete an order № " + number + "? (yes/no):");
        String answer = scanner.nextLine();

        if ("yes".equals(answer)) {
            orders.removeOrder(number);
            out.println("Your order has been deleted!");
        }
    }

    public static void customerReturn() {
        out.println("Enter your order number: ");
        int number = Integer.parseInt(scanner.nextLine());

        if ("SHIPPED".equals(orders.getOrder(number).getOrderStatus())) {
            out.println("Your order has been transferred to the delivery service. Refund is not possible!");
            return;
        }

        out.println("Do you want to delete an order № " + number + "? (yes/no):");
        String answer = scanner.nextLine();

        if ("yes".equals(answer)) {
            orders.returnOrder(number, gunCatalog);

            for (int i = 0; i < orders.getOrder(orderNumber).getFinalCart().getSize(); i++) {
                Product cartItem = orders
                        .getOrder(orderNumber)
                        .getFinalCart()
                        .getItem(i);

                if (gunCatalog.checkingItemExist(cartItem)) {
                    Product item = gunCatalog
                            .getItem(cartItem.getId());
                    item.setQuantity(item.getQuantity() + cartItem.getQuantity());
                }
            }
            orders.removeOrder(number);
        }
    }

    public static void arrangeDelivery() {
        out.print("Do you want to arrange delivery? (yes/no): ");
        String answer = scanner.nextLine();

        if ("yes".equals(answer)) {
            out.println("Enter your phone number: ");
            String mapKey = scanner.nextLine();

            out.println("Does the delivery address match the address in your profile? (yes/no):");
            answer = scanner.nextLine();

            if ("yes".equals(answer)) {
                out.print("Enter your order number: ");
                int number = Integer.parseInt(scanner.nextLine());

                orders.getOrder(number)
                        .setShippingDate();
                orders.getOrder(number)
                        .setShippingAddress(shopClients.getClient(mapKey).getAddress());
                orders.getOrder(number)
                        .setOrderTrackingNumber();
                orders.getOrder(number)
                        .setOrderStatus("SHIPPED");

                out.println("Your order № " + number + " has been sent " +
                        orders.getOrder(number).getSenderDate());
                out.println("Tracking number: " + orders.getOrder(number).getTrackingNumber());
                out.println();
            } else {
                out.print("Enter the delivery address (separated by a space): ");
                String address = scanner.nextLine();

                out.print("Enter your order number: ");
                int number = Integer.parseInt(scanner.nextLine());

                orders.getOrder(number).setShippingDate();
                orders.getOrder(number).setShippingAddress(address);
                orders.getOrder(number).setOrderTrackingNumber();
                orders.getOrder(number).setOrderStatus("SHIPPED");

                out.println("Your order № " + number + " has been sent " +
                        orders.getOrder(number).getSenderDate());
                out.println("Tracking number: " + orders.getOrder(number).getTrackingNumber());
            }
        }
    }
}


