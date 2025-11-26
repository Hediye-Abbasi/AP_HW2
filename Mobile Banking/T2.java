public class T2 {
    private static final java.util.ArrayList<String[]> USERS = new java.util.ArrayList<String[]>();
    private static int loggedIndex = -1;

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

        for (int i = 0; i < USERS.size(); i++) {
            String[] user = USERS.get(i);
            if (user[0].equals(username) && user[1].equals(password)) {
                loggedIndex = i;
                System.out.println("Login successful.");
                return;
            }
        }
        System.out.println("Error: invalid username or password.");
    }

    private static void handleShowBalance() {
        if (!isLoggedIn()) {
            System.out.println("Error: You should login first.");
            return;
        }
        System.out.println("Current balance: " + USERS.get(loggedIndex)[6]);
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

        double balance = parseDouble(USERS.get(loggedIndex)[6]).doubleValue();
        balance += amount;
        USERS.get(loggedIndex)[6] = String.valueOf(balance);
        System.out.println("Deposit successful. Current balance: " + USERS.get(loggedIndex)[6]);
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

        double balance = parseDouble(USERS.get(loggedIndex)[6]).doubleValue();
        if (balance < amount) {
            System.out.println("Error: insufficient balance.");
            return;
        }

        balance -= amount;
        USERS.get(loggedIndex)[6] = String.valueOf(balance);
        System.out.println("Withdrawal successful. Current balance: " + USERS.get(loggedIndex)[6]);
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

        int targetIndex = findUserByCard(targetCard);
        if (targetIndex == -1 || targetIndex == loggedIndex) {
            System.out.println("Error: invalid card number.");
            return;
        }

        double balance = parseDouble(USERS.get(loggedIndex)[6]).doubleValue();
        if (balance < amount) {
            System.out.println("Error: insufficient balance.");
            return;
        }

        double targetBalance = parseDouble(USERS.get(targetIndex)[6]).doubleValue();
        balance -= amount;
        targetBalance += amount;

        USERS.get(loggedIndex)[6] = String.valueOf(balance);
        USERS.get(targetIndex)[6] = String.valueOf(targetBalance);

        System.out.println("Transferred successfully.");
    }

    private static void handleLogout() {
        if (!isLoggedIn()) {
            System.out.println("Error: no user is logged in.");
            return;
        }
        loggedIndex = -1;
        System.out.println("Logout successful.");
    }

    private static boolean isLoggedIn() {
        return loggedIndex >= 0 && loggedIndex < USERS.size();
    }

    private static boolean isUsernameUnique(String username) {
        for (int i = 0; i < USERS.size(); i++) {
            if (USERS.get(i)[0].equals(username)) {
                return false;
            }
        }
        return true;
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

    private static int findUserByCard(String card) {
        for (int i = 0; i < USERS.size(); i++) {
            if (USERS.get(i)[5].equals(card)) {
                return i;
            }
        }
        return -1;
    }

    private static void addUser(String username, String password, String fullName,
                                String cardNumber, String phone, String email, double balance) {

        String[] user = new String[7];

        user[0] = username;
        user[1] = password;
        user[2] = fullName;
        user[3] = phone;
        user[4] = email;
        user[5] = cardNumber;
        user[6] = java.lang.Double.toString(balance);

        USERS.add(user);
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
}
