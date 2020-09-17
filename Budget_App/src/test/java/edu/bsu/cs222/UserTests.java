package edu.bsu.cs222;

import org.junit.Assert;
import org.junit.Test;

public class UserTests {

    @Test
    public void testUser(){
        User user = new User("Jim_Bob", "1234");
        Assert.assertNotNull(user);
    }

    @Test
    public void testUserContainsRealSpendingList(){
        User user = new User("Jim_Bob", "1234");
        Assert.assertEquals(user.realSpending.size(), 12);
    }

    @Test
    public void testUserGetExpenseByMonth(){
        User user = new User("Jim_Bob", "1234");
        double expense = user.getExpenseByMonth(1, "rent");
        Assert.assertEquals(expense, 0.0, 0);
    }

    @Test
    public void testUserEditMonthRealSpending(){
        User user = new User("Jim_Bob", "1234");
        user.editMonthlyRealSpending(1, "tuition", 10000.00);
        Assert.assertEquals(user.getExpenseByMonth(1, "tuition"), 10000.00, 0);
    }


    @Test
    public void testUserSetRealMonthlyIncome(){
        User user = new User("Jim_Bob", "1234");
        user.setRealMonthlyIncome(1, 500);
        Assert.assertEquals(user.realSpending.get(0).getIncome(), 500.00, 0);
    }

    @Test
    public void testUserHasPassword(){
        User user = new User("Jim", "1234");
        Assert.assertEquals("1234", user.password);
    }

    @Test
    public void testUserBankToJsonFiles(){
        UserBank userBank = new UserBank();
        User user = new User("Yingpei", "0987");
        userBank.addUser("Yingpei", user);
        userBank.saveUsersToJsonFiles();
    }

    @Test
    public void testJsonFilesToUserBank(){
        UserBank userBank = new UserBank();
        userBank.getUsersFromJsonFiles();
        Assert.assertNotNull(userBank.getUser("Gabe"));
    }

}
