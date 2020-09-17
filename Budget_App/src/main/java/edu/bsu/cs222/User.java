package edu.bsu.cs222;

import java.util.ArrayList;

class User {

    String userName;
    String password;
    Budget spendingPlan;
    ArrayList<Budget> realSpending = new ArrayList<>();

    User(String userName, String password){
        this.userName= userName;
        this.password = password;
        this.spendingPlan = new Budget();
        initializeRealSpending();
    }

    private void initializeRealSpending() {
        for(int i = 0; i < 12; i++){
            Budget budget = new Budget();
            realSpending.add(budget);
        }
    }

    void setRealMonthlyIncome(int month, double amount) {
        Budget actual = realSpending.get(month - 1);
        actual.updateIncome(amount);
    }

    double getExpenseByMonth(int month, String category) {
        Budget actual = realSpending.get(month - 1);
        return actual.getExpenseAmount(category);
    }

    void editMonthlyRealSpending(int month, String category, double amount) {
        Budget actual = realSpending.get(month - 1);
        actual.updateExpenseValue(category, amount);
    }


    void setBudgetIncome(double amount) {
        spendingPlan.updateIncome(amount);
    }

    String printBudget() {
        return spendingPlan.displayBudget();
    }

    void updateBudget(String category, double amount) {
        spendingPlan.updateExpenseValue(category,amount);
    }

    String printRealSpending(int month) {
        Budget spending = realSpending.get(month-1);
        return spending.displayBudget();
    }


}
