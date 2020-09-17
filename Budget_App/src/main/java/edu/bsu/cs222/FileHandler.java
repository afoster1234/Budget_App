package edu.bsu.cs222;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.*;
import java.util.ArrayList;
import java.util.Map;

class FileHandler {

    User makeUserFromJsonToJava(String userName) {
        String password = getPasswordFromJsonFile(userName);
        User user = new User(userName, password);
        user.spendingPlan = makeSpendingPlanFromJsonToJava(userName);
        JsonObject userObject = getUserFromJsonFile(userName);
        ArrayList<Budget> realSpending = new ArrayList<>();
        for(int i = 0; i<12; i++){
            realSpending.add(makeRealSpendingFromJsonToJava(userObject.get("realSpending").getAsJsonArray().get(i).getAsJsonObject()));
        }
        user.realSpending = realSpending;
        return user;
    }

    void makeUserFromJavaToJson(User user) {
        try{
            JsonObject userDetails = new JsonObject();
            JsonObject root = new JsonObject();
            userDetails.addProperty("userName", user.userName);
            userDetails.addProperty("password", user.password);
            userDetails.add("spendingPlan", makeSpendingPlanFromJavaToJson(user.spendingPlan));
            userDetails.add("realSpending", makeRealSpendingFromJavaToJson(user));
            root.add(user.userName, userDetails);
            BufferedWriter writer = new BufferedWriter(new FileWriter("src/main/Resources/" + user.userName +".json"));
            writer.append(root.toString());
            writer.flush();
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    private Budget makeRealSpendingFromJsonToJava(JsonObject jsonSpendingMonth) {
        Budget realSpending = new Budget();
        JsonObject jsonSpending = null;
        for (Map.Entry<String, JsonElement> entry : jsonSpendingMonth.entrySet()) {
            jsonSpending = entry.getValue().getAsJsonObject();
        }
        assert jsonSpending != null;
        realSpending.updateIncome(jsonSpending.get("income").getAsDouble());
        realSpending.updateExpenseValue("tuition", jsonSpending.get("tuition").getAsDouble());
        realSpending.updateExpenseValue("rent", jsonSpending.get("rent").getAsDouble());
        realSpending.updateExpenseValue("utilities", jsonSpending.get("utilities").getAsDouble());
        realSpending.updateExpenseValue("groceries", jsonSpending.get("groceries").getAsDouble());
        realSpending.updateExpenseValue("diningout", jsonSpending.get("diningOut").getAsDouble());
        realSpending.updateExpenseValue("savings", jsonSpending.get("savings").getAsDouble());
        realSpending.updateExpenseValue("subscription", jsonSpending.get("subscriptions").getAsDouble());
        realSpending.updateExpenseValue("entertainment", jsonSpending.get("entertainment").getAsDouble());
        realSpending.updateExpenseValue("car", jsonSpending.get("car").getAsDouble());
        realSpending.updateExpenseValue("other", jsonSpending.get("other").getAsDouble());
        return realSpending;
    }
    Budget makeSpendingPlanFromJsonToJava(String userName) {
        Budget spendingPlan = new Budget();
        JsonObject userObject = getUserFromJsonFile(userName);
        JsonObject jsonSpendingPlan = userObject.get("spendingPlan").getAsJsonObject();
        spendingPlan.updateIncome(jsonSpendingPlan.get("income").getAsDouble());
        spendingPlan.updateExpenseValue("tuition", jsonSpendingPlan.get("tuition").getAsDouble());
        spendingPlan.updateExpenseValue("rent", jsonSpendingPlan.get("rent").getAsDouble());
        spendingPlan.updateExpenseValue("utilities", jsonSpendingPlan.get("utilities").getAsDouble());
        spendingPlan.updateExpenseValue("groceries", jsonSpendingPlan.get("groceries").getAsDouble());
        spendingPlan.updateExpenseValue("diningout", jsonSpendingPlan.get("diningOut").getAsDouble());
        spendingPlan.updateExpenseValue("savings", jsonSpendingPlan.get("savings").getAsDouble());
        spendingPlan.updateExpenseValue("subscription", jsonSpendingPlan.get("subscriptions").getAsDouble());
        spendingPlan.updateExpenseValue("entertainment", jsonSpendingPlan.get("entertainment").getAsDouble());
        spendingPlan.updateExpenseValue("car", jsonSpendingPlan.get("car").getAsDouble());
        spendingPlan.updateExpenseValue("other", jsonSpendingPlan.get("other").getAsDouble());
        return spendingPlan;
    }
    JsonObject makeSpendingPlanFromJavaToJson(Budget budget) {
        JsonObject spendingPlan = new JsonObject();
        spendingPlan.addProperty("income", budget.getIncome());
        spendingPlan.addProperty("tuition", budget.getExpenseAmount("tuition"));
        spendingPlan.addProperty("rent", budget.getExpenseAmount("rent"));
        spendingPlan.addProperty("utilities", budget.getExpenseAmount("utilities"));
        spendingPlan.addProperty("groceries", budget.getExpenseAmount("groceries"));
        spendingPlan.addProperty("diningOut", budget.getExpenseAmount("diningout"));
        spendingPlan.addProperty("savings", budget.getExpenseAmount("savings"));
        spendingPlan.addProperty("subscriptions", budget.getExpenseAmount("subscription"));
        spendingPlan.addProperty("entertainment", budget.getExpenseAmount("entertainment"));
        spendingPlan.addProperty("car", budget.getExpenseAmount("car"));
        spendingPlan.addProperty("other", budget.getExpenseAmount("other"));
        return spendingPlan;
    }
    JsonArray makeRealSpendingFromJavaToJson(User user) {
        JsonArray realSpending = new JsonArray();
        String[] months = new String[]{"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
        for(int i =0 ; i < 12; i++){
            JsonObject monthlySpending = new JsonObject();
            monthlySpending.add(months[i], makeSpendingPlanFromJavaToJson(user.realSpending.get(i)));
            realSpending.add(monthlySpending);
        }
        return realSpending;
    }

    JsonObject getUserFromJsonFile(String userName) {
        JsonParser jsonParser = new JsonParser();
        InputStream input = getClass().getClassLoader().getResourceAsStream( userName + ".json");
        assert input != null;
        Reader reader = new InputStreamReader(input);
        JsonObject rootObject = jsonParser.parse(reader).getAsJsonObject();
        return rootObject.get(userName).getAsJsonObject();
    }

    String getPasswordFromJsonFile(String userName ) {
        JsonParser jsonParser = new JsonParser();
        InputStream input = getClass().getClassLoader().getResourceAsStream(userName + ".json");
        assert input != null;
        Reader reader = new InputStreamReader(input);
        JsonObject rootObject = jsonParser.parse(reader).getAsJsonObject();
        JsonObject userObject = rootObject.get(userName).getAsJsonObject();
        return  userObject.get("password").getAsString();
    }
    String getUsernameFromJsonFile(String userName){
        JsonParser jsonParser = new JsonParser();
        InputStream input = getClass().getClassLoader().getResourceAsStream( userName + ".json");
        assert input != null;
        Reader reader = new InputStreamReader(input);
        JsonObject rootObject = jsonParser.parse(reader).getAsJsonObject();
        JsonObject userObject = rootObject.get(userName).getAsJsonObject();
        return  userObject.get("userName").getAsString();
    }

}
