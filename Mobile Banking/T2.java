public class T2 {

    private static final java.util.HashMap<String, User> USERS_BY_USERNAME = new java.util.HashMap<String, User>();
    private static final java.util.HashMap<String, User> USERS_BY_CARD = new java.util.HashMap<String, User>();
    private static User loggedUser = null;
    private static long nextCard = 6037000000000000L;
    public static void main(String[] args) throws Exception {
        java.io.BufferedReader reader = new java.io.BufferedReader(new java.io.InputStreamReader(System.in));
        String line;
        while ((line = reader.readLine()) != null) {
            line = line.trim();
            if (line.length() == 0) {
                continue;
            }
            String[] tokens = split(line);
            if (tokens.length == 0) {
                continue;
            }

            String command = tokens[0].toLowerCase();
            if ("register".equals(command)) {
                handleRegister(tokens, line);
            } else if ("login".equals(command)) {
                handleLogin(tokens);
            } else if ("show".equals(command) && tokens.length > 1 && tokens[1].equalsIgnoreCase("balance")) {
                handleShowBalance();
            } else if ("deposit".equals(command)) {
                handleDeposit(tokens);
            } else if ("withdraw".equals(command)) {
                handleWithdraw(tokens);
            } else if ("transfer".equals(command)) {
                handleTransfer(tokens);
            } else if ("logout".equals(command)) {
                handleLogout();
            } else if ("exit".equals(command)) {
                System.out.println("Goodbye!");
                break;
            } else {
                System.out.println("Error: invalid command.");
            }
        }
    }

    private static void handleRegister(String[] tokens, String originalLine) {
        if (tokens.length < 6) {
            System.out.println("Error: invalid command.");
            return;
        }

        String username = tokens[1];
        String password = tokens[2];
        String phone = tokens[tokens.length - 2];
        String email = tokens[tokens.length - 1];
        String fullName = extractFullName(originalLine, username, password, phone);

        if (fullName.length() == 0) {
            System.out.println("Error: invalid command.");
            return;
        }
        if (!isUsernameUnique(username)) {
            System.out.println("Error: username already exists.");
            return;
        }
        if (!isValidPhone(phone)) {
            System.out.println("Error: invalid phone number.");
            return;
        }
        if (!isValidEmail(email)) {
            System.out.println("Error: invalid email.");
            return;
        }
        if (!isValidPassword(password)) {
            System.out.println("Error: invalid password.");
            return;
        }

        String cardNumber = generateCardNumber();
        addUser(username, password, fullName, cardNumber, phone, email, 0.0);

        System.out.println("Registered successfully.");
        System.out.println("Assigned card number: " + cardNumber);
    }

    private static void handleLogin(String[] tokens) {
        if (tokens.length != 3) {
            System.out.println("Error: invalid command.");
            return;
        }
        String username = tokens[1];
        String password = tokens[2];

        User u = USERS_BY_USERNAME.get(username);
        if (u != null && u.password.equals(password)) {
            loggedUser = u;
            System.out.println("Login successful.");
            return;
        }
        System.out.println("Error: invalid username or password.");
    }

    private static void handleShowBalance() {
        if (!isLoggedIn()) {
            System.out.println("Error: You should login first.");
            return;
        }
        System.out.println("Current balance: " + loggedUser.balance);
    }

    private static void handleDeposit(String[] tokens) {
        if (!isLoggedIn()) {
            System.out.println("Error: You should login first.");
            return;
        }
        if (tokens.length != 2) {
            System.out.println("Error: invalid amount.");
            return;
        }
        Double amount = parseDouble(tokens[1]);
        if (amount == null || amount <= 0) {
            System.out.println("Error: invalid amount.");
            return;
        }

        double balance = loggedUser.balance;
        balance += amount;
        loggedUser.balance = balance;
        System.out.println("Deposit successful. Current balance: " + loggedUser.balance);
    }

    private static void handleWithdraw(String[] tokens) {
        if (!isLoggedIn()) {
            System.out.println("Error: You should login first.");
            return;
        }
        if (tokens.length != 2) {
            System.out.println("Error: invalid amount.");
            return;
        }
        Double amount = parseDouble(tokens[1]);
        if (amount == null || amount <= 0) {
            System.out.println("Error: invalid amount.");
            return;
        }

        double balance = loggedUser.balance;
        if (balance < amount) {
            System.out.println("Error: insufficient balance.");
            return;
        }

        balance -= amount;
        loggedUser.balance = balance;
        System.out.println("Withdrawal successful. Current balance: " + loggedUser.balance);
    }

    private static void handleTransfer(String[] tokens) {
        if (!isLoggedIn()) {
            System.out.println("Error: You should login first.");
            return;
        }
        if (tokens.length != 3) {
            System.out.println("Error: invalid command.");
            return;
        }

        String targetCard = tokens[1];
        Double amount = parseDouble(tokens[2]);
        if (amount == null || amount <= 0) {
            System.out.println("Error: invalid amount.");
            return;
        }

        User target = USERS_BY_CARD.get(targetCard);
        if (target == null || target == loggedUser) {
            System.out.println("Error: invalid card number.");
            return;
        }

        double balance = loggedUser.balance;
        if (balance < amount) {
            System.out.println("Error: insufficient balance.");
            return;
        }

        double targetBalance = target.balance;

        balance -= amount;
        targetBalance += amount;

        loggedUser.balance = balance;
        target.balance = targetBalance;

        System.out.println("Transferred successfully.");
    }

    private static void handleLogout() {
        if (!isLoggedIn()) {
            System.out.println("Error: no user is logged in.");
            return;
        }
        loggedUser = null;
        System.out.println("Logout successful.");
    }

    private static boolean isLoggedIn() {
        return loggedUser != null;
    }

    private static boolean isUsernameUnique(String username) {
        return !USERS_BY_USERNAME.containsKey(username);
    }

    private static boolean isValidPhone(String phone) {
        if (phone.length() != 11) return false;
        if (!phone.startsWith("09")) return false;

        for (int i = 0; i < phone.length(); i++) {
            if (!java.lang.Character.isDigit(phone.charAt(i))) return false;
        }
        return true;
    }

    private static boolean isValidEmail(String email) {
        int at = email.indexOf('@');
        if (at <= 0) return false;
        if (email.charAt(0) == '.') return false;
        if (email.lastIndexOf('@') != at) return false;

        String domain = email.substring(at + 1);
        return "aut.com".equals(domain);
    }

    private static boolean isValidPassword(String password) {
        if (password.length() < 8) return false;
        boolean hasUpper = false, hasLower = false, hasDigit = false, hasSpecial = false;

        for (int i = 0; i < password.length(); i++) {
            char c = password.charAt(i);
            if (c >= 'A' && c <= 'Z') hasUpper = true;
            else if (c >= 'a' && c <= 'z') hasLower = true;
            else if (c >= '0' && c <= '9') hasDigit = true;
            else if (c == '@' || c == '!' || c == '&' || c == '$' || c == '?' || c == '\u061F')
                hasSpecial = true;
        }
        return hasUpper && hasLower && hasDigit && hasSpecial;
    }

    private static String generateCardNumber() {
        String card = java.lang.Long.toString(nextCard);
        // تا وقتی یکتا نشده، ادامه بده
        while (USERS_BY_CARD.containsKey(card)) {
            nextCard++;
            card = java.lang.Long.toString(nextCard);
        }
        nextCard++;
        return card;
    }

    private static Double parseDouble(String value) {
        try {
            return java.lang.Double.parseDouble(value);
        } catch (Exception e) {
            return null;
        }
    }

    private static void addUser(String username, String password, String fullName,
                                String cardNumber, String phone, String email, double balance) {

        User u = new User(username, password, fullName, phone, email, cardNumber, balance);
        USERS_BY_USERNAME.put(username, u);
        USERS_BY_CARD.put(cardNumber, u);
    }

    private static String extractFullName(String line, String username, String password, String phone) {
        int userIndex = line.indexOf(username);
        if (userIndex == -1) return "";

        int passwordIndex = line.indexOf(password, userIndex + username.length());
        if (passwordIndex == -1) return "";

        int nameStart = passwordIndex + password.length();
        int phoneIndex = line.lastIndexOf(phone);

        if (phoneIndex == -1 || phoneIndex <= nameStart) return "";

        while (nameStart < phoneIndex && line.charAt(nameStart) == ' ') nameStart++;
        int nameEnd = phoneIndex;

        while (nameEnd > nameStart && line.charAt(nameEnd - 1) == ' ')
            nameEnd--;

        if (nameEnd <= nameStart) return "";

        return line.substring(nameStart, nameEnd);
    }

    private static String[] split(String line) {
        int len = line.length();
        String[] temp = new String[len];
        int count = 0;
        StringBuilder current = new StringBuilder();
        boolean inSpace = true;

        for (int i = 0; i < len; i++) {
            char c = line.charAt(i);

            if (c == ' ' || c == '\t' || c == '\r' || c == '\n') {
                if (!inSpace) {
                    temp[count++] = current.toString();
                    current.setLength(0);
                }
                inSpace = true;
            } else {
                current.append(c);
                inSpace = false;
            }
        }
        if (!inSpace) temp[count++] = current.toString();

        String[] result = new String[count];
        for (int i = 0; i < count; i++) result[i] = temp[i];

        return result;
    }

    private static class User {
        String username;
        String password;
        String fullName;
        String phone;
        String email;
        String cardNumber;
        double balance;

        User(String username, String password, String fullName,
             String phone, String email, String cardNumber, double balance) {
            this.username = username;
            this.password = password;
            this.fullName = fullName;
            this.phone = phone;
            this.email = email;
            this.cardNumber = cardNumber;
            this.balance = balance;
        }
    }
}
