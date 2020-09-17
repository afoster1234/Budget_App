package edu.bsu.cs222;

import javafx.collections.ObservableList;
import javafx.scene.chart.PieChart;
import org.junit.Assert;
import org.junit.Test;

public class BudgetTests {

    @Test
    public void testBudget() {
        Budget budget = new Budget();
        Assert.assertNotNull(budget);
    }

    @Test
    public void testIncome() {
        Budget budget = new Budget();
        double userIncome = budget.getIncome();
        Assert.assertEquals(0, userIncome, 0);
    }

    @Test
    public void testUpdateIncome(){
        Budget budget = new Budget();
        budget.updateIncome(100.00);
        Assert.assertEquals(100.00, budget.getIncome(), 0);
    }

    @Test
    public void makeExpenseHashMap(){
        Budget budget = new Budget();
        Assert.assertNotNull(budget.expenseHashMap);
    }

    @Test
    public void testGetExpenseAmount(){
        Budget budget = new Budget();
        double rent = budget.getExpenseAmount("rent");
        Assert.assertEquals(rent, 0.0, 0);
    }

    @Test
    public void testUpdateExpenseAmount(){
        Budget budget = new Budget();
        budget.updateExpenseValue("tuition", 10000.0);
        double tuition = budget.getExpenseAmount("tuition");
        Assert.assertEquals(10000, tuition, 0);
    }

    @Test
    public void testTotalExpenses(){
        Budget budget = new Budget();
        budget.updateExpenseValue("rent", 300.00);
        double totalExpenses = budget.totalExpenses();
        Assert.assertEquals(totalExpenses, 300.00, 0);
    }

    @Test
    public void testExpensesEqualIncome(){
        Budget budget = new Budget();
        budget.updateIncome(400.00);
        budget.updateExpenseValue("rent", 400.00);
        boolean expenseEqualIncome = budget.isExpenseEqualIncome();
        Assert.assertTrue(expenseEqualIncome);
    }

    @Test
    public void testRemainingIncome(){
        Budget budget = new Budget();
        budget.updateIncome(400.00);
        budget.updateExpenseValue("rent", 300.00);
        double remainingIncome = budget.calculateRemainingIncome();
        Assert.assertEquals(remainingIncome, 100.00, 0);
    }

    @Test
    public void testMakePieChartData(){
        Budget budget = new Budget();
        budget.updateExpenseValue("tuition", 500.00);
        budget.updateExpenseValue("rent", 300.00);
        ObservableList<PieChart.Data> pieChartData = budget.createPieChartData();
        Assert.assertEquals(pieChartData.get(0).getPieValue(), 500.00, 0);
    }
}
