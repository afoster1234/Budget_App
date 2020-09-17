package edu.bsu.cs222;

import com.google.gson.*;
import org.junit.Assert;
import org.junit.Test;

public class JSONTests {
    @Test
    public void testGetUserPasswordFromJson(){
        FileHandler fileHandler = new FileHandler();
        JsonObject joeObject = fileHandler.getUserFromJsonFile("Joe");
        Assert.assertEquals(joeObject.get("password").getAsString(), "1234");
    }

    @Test
    public void testMakeBudgetObjectFromJson(){
        FileHandler fileHandler = new FileHandler();
        Budget joesBudget = fileHandler.makeSpendingPlanFromJsonToJava("Joe");
        Assert.assertEquals(joesBudget.getExpenseAmount("groceries"), 50.0, 0.0);
    }

    @Test
    public void testMakeUserObjectFromJson(){
        FileHandler fileHandler = new FileHandler();
        User joe = fileHandler.makeUserFromJsonToJava("Joe");
        joe.printRealSpending(1);
        Assert.assertEquals(joe.getExpenseByMonth(6, "subscription"), 50.0, 0.0);
    }

    @Test
    public void testMakeJsonBudgetFromJava(){
        FileHandler fileHandler = new FileHandler();
        Budget budget = new Budget();
        budget.updateIncome(100.0);
        JsonObject spendingPlan = fileHandler.makeSpendingPlanFromJavaToJson(budget);
        Assert.assertEquals(spendingPlan.get("income").getAsDouble(), 100.0, 0.0);
    }

    @Test
    public void testMakeJsonRealSpendingFromJava() {
        FileHandler fileHandler = new FileHandler();
        User user = new User("Bob", "5678");
        user.setRealMonthlyIncome(1, 100.00);
        JsonArray realSpendingArray = fileHandler.makeRealSpendingFromJavaToJson(user);
        Assert.assertEquals(realSpendingArray.get(0).getAsJsonObject().get("January").getAsJsonObject().get("income").getAsDouble(), 100.0, 0);
    }

    @Test
    public void testMakeJsonUserFromJava(){
        FileHandler fileHandler = new FileHandler();
        User user = new User("Bob", "5678");
        fileHandler.makeUserFromJavaToJson(user);
    }
}
