package edu.bsu.cs222;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.chart.PieChart;

import java.util.HashMap;

public class Budget {

    private double  income = 0.0;
    HashMap<String, Double> expenseHashMap = new HashMap<>();

    public Budget() {
        expenseHashMap.put("tuition", 0.0);
        expenseHashMap.put("rent", 0.0);
        expenseHashMap.put("utilities", 0.0);
        expenseHashMap.put("groceries", 0.0);
        expenseHashMap.put("diningout", 0.0);
        expenseHashMap.put("savings", 0.0);
        expenseHashMap.put("subscription", 0.0);
        expenseHashMap.put("entertainment", 0.0);
        expenseHashMap.put("car", 0.0);
        expenseHashMap.put("other", 0.0);
    }

    double getIncome() {
        return income;
    }

    void updateIncome(double amount) {
        income = amount;
    }

    double calculateRemainingIncome() {
        return income - totalExpenses();
    }

    double getExpenseAmount(String category) {
        return expenseHashMap.get(category);
    }

    void updateExpenseValue(String category, double amount) {
        expenseHashMap.replace(category, amount);
    }

    double totalExpenses() {
        double total = 0.0;
        for(String expense : expenseHashMap.keySet()){
            total += expenseHashMap.get(expense);
        }
        return total;
    }

    boolean isExpenseEqualIncome() {
        return totalExpenses() == income;
    }

    String displayBudget() {
        StringBuilder output = new StringBuilder(String.format("Income: $%.2f\n", income));
        for(String expense : expenseHashMap.keySet()){
            output.append(String.format("Category: %-20s " + "Value: $%.2f\n", expense, expenseHashMap.get(expense)));
        }
        output.append(String.format("Total Expenses: $%.2f\n\n", totalExpenses()));
        return output.toString();
    }

    ObservableList<PieChart.Data> createPieChartData(){
        return FXCollections.observableArrayList(
                new PieChart.Data("Tuition", getExpenseAmount("tuition")),
                new PieChart.Data("Rent", getExpenseAmount("rent")),
                new PieChart.Data("Utilities", getExpenseAmount("utilities")),
                new PieChart.Data("Groceries", getExpenseAmount("groceries")),
                new PieChart.Data("Dining Out", getExpenseAmount("diningout")),
                new PieChart.Data("Savings", getExpenseAmount("savings")),
                new PieChart.Data("Subscriptions", getExpenseAmount("subscription")),
                new PieChart.Data("Entertainment", getExpenseAmount("entertainment")),
                new PieChart.Data("Car", getExpenseAmount("car")),
                new PieChart.Data("Other", getExpenseAmount("other")));
    }


}
