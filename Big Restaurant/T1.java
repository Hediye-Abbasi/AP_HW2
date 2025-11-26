class Person {
    private String name;
    private String phoneNumber;

    public Person(String name, String phoneNumber) {
        this.name = name;
        this.phoneNumber = phoneNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getInfo() {
        return "";
    }
}

class Customer extends Person {
    private static int customerCounter = 1;
    private String customerId;
    private int loyaltyPoints;

    public Customer(String name, String phoneNumber) {
        super(name, phoneNumber);
        this.customerId = String.format("C%03d", customerCounter++);
        this.loyaltyPoints = 0;
    }

    public void addLoyaltyPoints(double totalAmount) {
        if (totalAmount > 1_000_000) {
            loyaltyPoints += 2;
        } else if (totalAmount > 500_000) {
            loyaltyPoints += 1;
        }
    }

    public double getDiscount() {
        if (loyaltyPoints > 5) {
            return 0.10;
        } else if (loyaltyPoints > 3) {
            return 0.05;
        }
        return 0.0;
    }

    public String getCustomerId() {
        return customerId;
    }

    public int getLoyaltyPoints() {
        return loyaltyPoints;
    }

    public void setLoyaltyPoints(int loyaltyPoints) {
        this.loyaltyPoints = loyaltyPoints;
    }

    public String getInfo() {
        return "ID: " + customerId +
               ", Name: " + getName() +
               ", Phone: " + getPhoneNumber() +
               ", Loyalty Points: " + loyaltyPoints;
    }
}

class Employee extends Person {
    private static int employeeCounter = 1;
    private String employeeId;
    private String position;
    private double salary;
    private int hoursWorked;

    public Employee(String name, String phoneNumber, String position, double salary) {
        super(name, phoneNumber);
        this.employeeId = String.format("E%03d", employeeCounter++);
        this.position = position;
        this.salary = salary;
        this.hoursWorked = 0;
    }

    public void addHoursWorked(int hours) {
        if (hours > 0) {
            this.hoursWorked += hours;
        }
    }

    public double calculateSalary() {
        int baseHours = 160;
        if (hoursWorked <= baseHours) {
            return salary;
        }
        int overtimeHours = hoursWorked - baseHours;
        double overtimePay = (overtimeHours / 160.0) * (salary * 1.5);
        return salary + overtimePay;
    }

    public String getEmployeeId() {
        return employeeId;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public double getSalary() {
        return salary;
    }

    public void setSalary(double salary) {
        this.salary = salary;
    }

    public int getHoursWorked() {
        return hoursWorked;
    }

    public void setHoursWorked(int hoursWorked) {
        this.hoursWorked = hoursWorked;
    }

    public String getInfo() {
        return "ID: " + employeeId +
               ", Name: " + getName() +
               ", Phone:" + getPhoneNumber() +
               ", Position: " + position +
               ", HoursWorked: " + hoursWorked;
    }
}

class MenuItem {
    private static int itemCounter = 1;
    private String itemId;
    private String name;
    private double price;
    private String category;

    public MenuItem(String name, double price, String category) {
        this.itemId = String.valueOf(itemCounter++);
        this.name = name;
        this.price = price;
        this.category = category;
    }

    public String getItemId() {
        return itemId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDetails() {
        return "";
    }
}

class Food extends MenuItem {
    private String spiceLevel;
    private int preparationTime;

    public Food(String name, double price, String spiceLevel, int preparationTime) {
        super(name, price, "food");
        this.spiceLevel = spiceLevel;
        this.preparationTime = preparationTime;
    }

    public String getSpiceLevel() {
        return spiceLevel;
    }

    public void setSpiceLevel(String spiceLevel) {
        this.spiceLevel = spiceLevel;
    }

    public int getPreparationTime() {
        return preparationTime;
    }

    public void setPreparationTime(int preparationTime) {
        this.preparationTime = preparationTime;
    }

    public String getDetails() {
        return "ID: " + getItemId() +
               ", Name: " + getName() +
               ", Price: " + (long)getPrice() +
               ", Category: " + getCategory() +
               ", Spice: " + spiceLevel +
               ", Preparation Time: " + preparationTime + "min";
    }
}

class Beverage extends MenuItem {
    private String size;
    private String temperature;

    public Beverage(String name, double price, String size, String temperature) {
        super(name, price, "Beverage");
        this.size = size;
        this.temperature = temperature;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getTemperature() {
        return temperature;
    }

    public void setTemperature(String temperature) {
        this.temperature = temperature;
    }

    public String getDetails() {
        return "ID: " + getItemId() +
               ", Name: " + getName() +
               ", Price: " + (long)getPrice() +
               ", Category: " + getCategory() +
               ", Size: " + size +
               ", Temperature: " + temperature;
    }
}

class Order {
    private static int orderCounter = 1;
    private int orderId;
    private Customer customer;
    private MenuItem[] items;
    private double totalAmount;

    public Order(Customer customer, MenuItem[] items) {
        this.orderId = orderCounter++;
        this.customer = customer;
        this.items = items;
        calculateTotal();
    }

    public void calculateTotal() {
        double sum = 0;
        for (int i = 0; i < items.length; i++) {
            sum += items[i].getPrice();
        }
        customer.addLoyaltyPoints(sum);
        double discount = sum * customer.getDiscount();
        totalAmount = sum - discount;
    }

    public String getOrderSummary() {
        String s = "";
        for (int i = 0; i < items.length; i++) {
            s += items[i].getName();
            if (i < items.length - 1) {
                s += " - ";
            }
        }
        return "Order ID: " + orderId +
               ", Customer: " + customer.getName() +
               ", Total Amount: " + (long)totalAmount +
               "\nItems: " + s;
    }

    public int getOrderId() {
        return orderId;
    }

    public Customer getCustomer() {
        return customer;
    }

    public MenuItem[] getItems() {
        return items;
    }

    public double getTotalAmount() {
        return totalAmount;
    }
}

public class T1 {
    public static void main(String[] args) {
        Employee[] employees = new Employee[3];
        employees[0] = new Employee("Maryam", "09832736294", "chef", 80_000_000);
        employees[1] = new Employee("Reyhaneh", "09120000001", "accountant", 55_000_000);
        employees[2] = new Employee("Hossein", "09123334455", "waiter", 35_000_000);

        employees[0].addHoursWorked(178);
        employees[1].addHoursWorked(165);
        employees[2].addHoursWorked(172);

        Customer[] customers = new Customer[5];
        customers[0] = new Customer("Ali", "09034883429");
        customers[1] = new Customer("Amir", "09351231234");
        customers[2] = new Customer("Hediye", "09121234567");
        customers[3] = new Customer("Maral", "09137778899");
        customers[4] = new Customer("Zahra", "09125556677");

        MenuItem[] menu = new MenuItem[7];
        menu[0] = new Food("Pizza", 400000, "Medium", 25);
        menu[1] = new Food("Kebab", 520000, "Spicy", 30);
        menu[2] = new Food("Pasta", 350000, "Mild", 20);
        menu[3] = new Food("Burger", 320000, "Medium", 18);
        menu[4] = new Beverage("Tea", 60000, "large", "hot");
        menu[5] = new Beverage("Soda", 50000, "medium", "cold");
        menu[6] = new Beverage("Juice", 90000, "small", "cold");

        Order[] orders = new Order[15];
        int n = 0;

        orders[n++] = new Order(customers[0], new MenuItem[]{menu[0], menu[1], menu[6]});
        orders[n++] = new Order(customers[0], new MenuItem[]{menu[3], menu[4], menu[5]});
        orders[n++] = new Order(customers[0], new MenuItem[]{menu[2], menu[3]});

        orders[n++] = new Order(customers[1], new MenuItem[]{menu[1], menu[2], menu[6]});
        orders[n++] = new Order(customers[1], new MenuItem[]{menu[0], menu[1], menu[2]});
        orders[n++] = new Order(customers[1], new MenuItem[]{menu[3], menu[4], menu[5], menu[6]});

        orders[n++] = new Order(customers[2], new MenuItem[]{menu[0], menu[2], menu[6]});
        orders[n++] = new Order(customers[2], new MenuItem[]{menu[0], menu[1], menu[6]});
        orders[n++] = new Order(customers[2], new MenuItem[]{menu[0], menu[1], menu[2]});

        orders[n++] = new Order(customers[3], new MenuItem[]{menu[3], menu[0], menu[2]});
        orders[n++] = new Order(customers[3], new MenuItem[]{menu[3], menu[2]});
        orders[n++] = new Order(customers[3], new MenuItem[]{menu[4], menu[5], menu[6]});

        orders[n++] = new Order(customers[4], new MenuItem[]{menu[1], menu[0], menu[2], menu[6]});
        orders[n++] = new Order(customers[4], new MenuItem[]{menu[3], menu[1], menu[0]});
        orders[n++] = new Order(customers[4], new MenuItem[]{menu[0], menu[1], menu[2]});

        Customer mostLoyal = customers[0];
        for (int i = 1; i < customers.length; i++) {
            if (customers[i].getLoyaltyPoints() > mostLoyal.getLoyaltyPoints()) {
                mostLoyal = customers[i];
            }
        }

        System.out.println("=== Customers ===");
        for (int i = 0; i < customers.length; i++) {
            System.out.println(customers[i].getInfo());
        }

        System.out.println("\n=== Employees ===");
        for (int i = 0; i < employees.length; i++) {
            System.out.println(employees[i].getInfo() +
                    ", Calculated Salary: " + (long)employees[i].calculateSalary());
        }

        System.out.println("\n=== Menu ===");
        for (int i = 0; i < menu.length; i++) {
            System.out.println(menu[i].getDetails());
        }

        System.out.println("\n=== Orders ===");
        for (int i = 0; i < orders.length; i++) {
            System.out.println(orders[i].getOrderSummary());
            if (i < orders.length - 1) {
                System.out.println("-----");
            }
        }

        System.out.println("\nMost Loyal Customer: " +
                mostLoyal.getName() + " (" +
                mostLoyal.getCustomerId() + "), Loyalty Points: " +
                mostLoyal.getLoyaltyPoints());
    }
}
