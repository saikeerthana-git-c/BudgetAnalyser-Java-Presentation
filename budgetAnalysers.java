import java.io.IOException;
import java.nio.file.*;
import java.util.*;
import java.util.stream.Collectors;

public class budgetAnalysers {

    // =====================================================
    // CO4: CLASS AND OBJECT
    // =====================================================
    static class Expense {

        private String category;
        private String description;
        private double amount;

        // Constructor
        Expense(String category, String description, double amount) {
            this.category = category;
            this.description = description;
            this.amount = amount;
        }

        // Getter methods
        public String getCategory() {
            return category;
        }

        public String getDescription() {
            return description;
        }

        public double getAmount() {
            return amount;
        }
    }


    // =====================================================
    // CO3: METHOD - TOTAL
    // =====================================================
    static double calculateTotal(double[] amounts) {

        double total = 0;

        for (double amount : amounts) {
            total += amount;
        }

        return total;
    }


    // =====================================================
    // CO3: METHOD - AVERAGE
    // =====================================================
    static double calculateAverage(double[] amounts) {

        if (amounts.length == 0) {
            return 0;
        }

        return calculateTotal(amounts) / amounts.length;
    }


    // =====================================================
    // CO3: METHOD - HIGHEST EXPENSE
    // =====================================================
    static double findHighestExpense(double[] amounts) {

        if (amounts.length == 0) {
            return 0;
        }

        double highest = amounts[0];

        for (double amount : amounts) {

            if (amount > highest) {
                highest = amount;
            }
        }

        return highest;
    }


    // =====================================================
    // CO2: BUDGET STATUS
    // =====================================================
    static String getBudgetStatus(double budget, double total) {

        if (total < budget) {
            return "WITHIN BUDGET";
        }
        else if (total == budget) {
            return "BUDGET FULLY USED";
        }
        else {
            return "OVER BUDGET";
        }
    }


    // =====================================================
    // CO5: SAVE EXPENSES TO FILE
    // =====================================================
    static void saveExpenses(ArrayList<Expense> expenses) {

        try {

            ArrayList<String> lines = new ArrayList<>();

            for (Expense e : expenses) {

                String line =
                        e.getCategory()
                        + ","
                        + e.getDescription()
                        + ","
                        + e.getAmount();

                lines.add(line);
            }

            Files.write(
                    Path.of("expenses.txt"),
                    lines
            );

            System.out.println("\nExpenses saved successfully.");

        }
        catch (IOException e) {

            System.out.println("Error while saving expenses.");
        }
    }


    // =====================================================
    // CO5: READ EXPENSES FROM FILE
    // =====================================================
    static void readExpensesFromFile() {

        try {

            Path file = Path.of("expenses.txt");

            if (!Files.exists(file)) {

                System.out.println("No saved file found.");
                return;
            }

            List<String> lines =
                    Files.readAllLines(file);

            System.out.println("\n--- SAVED EXPENSE DATA ---");

            for (String line : lines) {

                String[] parts = line.split(",");

                if (parts.length == 3) {

                    System.out.println(
                            parts[0]
                            + " | "
                            + parts[1]
                            + " | ₹"
                            + parts[2]
                    );
                }
            }

        }
        catch (IOException e) {

            System.out.println("Error while reading file.");
        }
    }


    // =====================================================
    // CO6: CATEGORY-WISE ANALYSIS
    // =====================================================
    static void categoryAnalysis(
            ArrayList<Expense> expenses) {

        System.out.println("\n--- CATEGORY-WISE ANALYSIS ---");

        Map<String, Double> categoryTotals =
                expenses.stream()
                        .filter(e -> e.getAmount() > 0)
                        .collect(
                                Collectors.groupingBy(
                                        Expense::getCategory,
                                        Collectors.summingDouble(
                                                Expense::getAmount
                                        )
                                )
                        );

        categoryTotals.forEach(
                (category, total) ->
                        System.out.printf(
                                "%-15s : ₹%.2f%n",
                                category,
                                total
                        )
        );
    }


    // =====================================================
    // CO6: FIND HIGHEST SPENDING CATEGORY
    // =====================================================
    static String findHighestCategory(
            ArrayList<Expense> expenses) {

        Map<String, Double> categoryTotals =
                expenses.stream()
                        .collect(
                                Collectors.groupingBy(
                                        Expense::getCategory,
                                        Collectors.summingDouble(
                                                Expense::getAmount
                                        )
                                )
                        );

        if (categoryTotals.isEmpty()) {
            return "None";
        }

        return categoryTotals.entrySet()
                .stream()
                .max(
                        Map.Entry.comparingByValue()
                )
                .get()
                .getKey();
    }


    // =====================================================
    // CO6: HIGH EXPENSES
    // =====================================================
    static void showHighExpenses(
            ArrayList<Expense> expenses) {

        System.out.println(
                "\n--- HIGH EXPENSES (₹1000 OR MORE) ---"
        );

        expenses.stream()
                .filter(e -> e.getAmount() >= 1000)
                .forEach(e ->
                        System.out.println(
                                e.getCategory()
                                + " - "
                                + e.getDescription()
                                + " : ₹"
                                + e.getAmount()
                        )
                );
    }


    // =====================================================
    // MAIN METHOD
    // =====================================================
    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);


        // =================================================
        // CO1: INPUT
        // =================================================

        System.out.println(
                "=============================================="
        );

        System.out.println(
                "       MONTHLY EXPENSE & BUDGET ANALYZER"
        );

        System.out.println(
                "=============================================="
        );

        System.out.print(
                "Enter your monthly budget: ₹"
        );

        double budget = sc.nextDouble();

        System.out.print(
                "Enter number of expenses: "
        );

        int n = sc.nextInt();

        sc.nextLine();


        // =================================================
        // CO3: ARRAY
        // =================================================

        double[] amounts = new double[n];


        // =================================================
        // CO6: ARRAYLIST
        // =================================================

        ArrayList<Expense> expenses =
                new ArrayList<>();


        // =================================================
        // CO2: LOOP
        // =================================================

        for (int i = 0; i < n; i++) {

            System.out.println(
                    "\nExpense " + (i + 1)
            );


            System.out.print(
                    "Enter category: "
            );

            String category =
                    sc.nextLine();


            System.out.print(
                    "Enter description: "
            );

            String description =
                    sc.nextLine();


            // =============================================
            // CO4: EXCEPTION HANDLING
            // =============================================

            double amount;

            while (true) {

                try {

                    System.out.print(
                            "Enter amount: ₹"
                    );

                    amount =
                            Double.parseDouble(
                                    sc.nextLine()
                            );

                    if (amount <= 0) {

                        throw new
                                IllegalArgumentException(
                                        "Amount must be positive."
                                );
                    }

                    break;

                }
                catch (Exception e) {

                    System.out.println(
                            "Invalid amount. Please try again."
                    );
                }
            }


            // Store amount in array
            amounts[i] = amount;


            // CO4: Create object
            Expense expense =
                    new Expense(
                            category,
                            description,
                            amount
                    );


            // Add object to ArrayList
            expenses.add(expense);
        }


        // =================================================
        // CO3: CALCULATIONS
        // =================================================

        double total =
                calculateTotal(amounts);

        double average =
                calculateAverage(amounts);

        double highest =
                findHighestExpense(amounts);


        // =================================================
        // ACTUAL BUDGET ANALYSIS
        // =================================================

        double remaining =
                budget - total;

        double budgetUsedPercentage =
                (total / budget) * 100;


        String status =
                getBudgetStatus(
                        budget,
                        total
                );


        String highestCategory =
                findHighestCategory(
                        expenses
                );


        // =================================================
        // FINAL REPORT
        // =================================================

        System.out.println(
                "\n=============================================="
        );

        System.out.println(
                "              BUDGET ANALYSIS"
        );

        System.out.println(
                "=============================================="
        );


        System.out.printf(
                "Monthly Budget       : ₹%.2f%n",
                budget
        );


        System.out.printf(
                "Total Spent          : ₹%.2f%n",
                total
        );


        System.out.printf(
                "Average Expense      : ₹%.2f%n",
                average
        );


        System.out.printf(
                "Highest Expense      : ₹%.2f%n",
                highest
        );


        // =============================================
        // REMAINING / EXCEEDED
        // =============================================

        if (remaining >= 0) {

            System.out.printf(
                    "Remaining Budget     : ₹%.2f%n",
                    remaining
            );

        }
        else {

            System.out.printf(
                    "Amount Exceeded      : ₹%.2f%n",
                    Math.abs(remaining)
            );
        }


        // =============================================
        // BUDGET PERCENTAGE
        // =============================================

        System.out.printf(
                "Budget Used          : %.2f%%%n",
                budgetUsedPercentage
        );


        System.out.println(
                "Budget Status        : " + status
        );


        System.out.println(
                "Highest Category     : "
                        + highestCategory
        );


        // =================================================
        // CO2: BUDGET WARNING
        // =================================================

        System.out.println(
                "\n--- BUDGET MESSAGE ---"
        );


        if (budgetUsedPercentage < 50) {

            System.out.println(
                    "Good! You are using less than 50% "
                            + "of your budget."
            );

        }
        else if (budgetUsedPercentage < 80) {

            System.out.println(
                    "Careful! You have used more than 50% "
                            + "of your budget."
            );

        }
        else if (budgetUsedPercentage <= 100) {

            System.out.println(
                    "Warning! You are close to your "
                            + "monthly budget."
            );

        }
        else {

            System.out.println(
                    "Alert! You have exceeded your "
                            + "monthly budget."
            );
        }


        // =================================================
        // CO6: CATEGORY ANALYSIS
        // =================================================

        categoryAnalysis(expenses);


        // =================================================
        // CO6: HIGH EXPENSES
        // =================================================

        showHighExpenses(expenses);


        // =================================================
        // CO5: SAVE DATA
        // =================================================

        saveExpenses(expenses);


        // =================================================
        // CO5: READ DATA
        // =================================================

        readExpensesFromFile();


        // =================================================
        // FINAL MESSAGE
        // =================================================

        System.out.println(
                "\n=============================================="
        );

        System.out.println(
                "             ANALYSIS COMPLETED"
        );

        System.out.println(
                "=============================================="
        );

        sc.close();
    }
}
