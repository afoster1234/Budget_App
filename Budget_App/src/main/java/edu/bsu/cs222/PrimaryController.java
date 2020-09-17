package edu.bsu.cs222;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class PrimaryController {
    //Login.fxml
    @FXML public Label lblStatus;
    @FXML public TextField loginUsername;
    @FXML public TextField loginPassword;
    @FXML public Label lblNumTries;

    //NewUser.fxml
    @FXML public TextField newUsername;
    @FXML public TextField newPassword;
    @FXML public Label checkUserName;

    //createSP.fxml
    @FXML public Label lblUserName;
    @FXML public TextArea budgetTxtArea;
    @FXML public TextField expenseAmountTxtField;
    @FXML public TextField incomeTxtField;
    @FXML public ComboBox<String> spendingPlanComboBox = new ComboBox<>();
    public String income,tuition,rent,utilities,groceries,diningout,savings,car,entertainment,subscription,other;
    @FXML public Label amountLeftToAllocate;
    @FXML public Label unbalancedExpenses;

    //App.fxml
    @FXML public ComboBox<String> monthComboBox = new ComboBox<>();
    @FXML public ComboBox<String> categoryComboBox = new ComboBox<>();
    @FXML public TextField realExpenseAmountTxtField;
    @FXML public TextArea spendingPlanTxtArea;
    @FXML public TextArea realSpendingTxtArea;
    public String updateBudget,January,February,March,April,May,June,July,August,September,October,November,December;
    @FXML public Button exitButton;
    @FXML public PieChart budgetPieChart;
    @FXML public PieChart realSpendingPieChart;
    @FXML public Label expenseAmountErrorLbl;
    @FXML public Button activeSideButton;
    @FXML public Label leftLabel;
    @FXML public Label rightLabel;



    private static UserBank userBank = new UserBank();
    private static FileHandler fileHandler = new FileHandler();
    private static int numTries = 5;
    private static String globalUserName;
    private static Stage primaryStage = new Stage();
    private static boolean activeSide = true;

    public void login() throws Exception {
        if(numTries > 1) {
            if (checkCredentials()) {
                globalUserName = loginUsername.getText();
                userBank.getUsersFromJsonFiles();
                lblStatus.setText("Login Successful");
                Parent root = FXMLLoader.load(getClass().getResource("App.fxml"));
                primaryStage.getIcons().add(new Image("edu/bsu/cs222/taskbar_icon.png"));
                primaryStage.setScene(new Scene(root));
                primaryStage.show();
            } else if (!checkCredentials()) {
                numTries -= 1;
                lblStatus.setText("Username/Password is incorrect.");
                lblNumTries.setText("You have " + numTries + " attempts left.");
            }
        }else {
            System.exit(0);
        }
    }

    private Boolean checkCredentials() {
        try {
            String realUsername = fileHandler.getUsernameFromJsonFile(loginUsername.getText());
            String realPassword = fileHandler.getPasswordFromJsonFile(loginUsername.getText());
            String userName = loginUsername.getText();
            String password = loginPassword.getText();
            return userName.equals(realUsername) && password.equals(realPassword);
        } catch (NullPointerException e ){
            lblStatus.setText("User Does Not Exist: Please Try Again");
        }
        return false;
    }

    public void newUserWindow() throws Exception{
        userBank.getUsersFromJsonFiles();
        Parent root = FXMLLoader.load(getClass().getResource("NewUser.fxml"));
        primaryStage.getIcons().add(new Image("edu/bsu/cs222/taskbar_icon.png"));
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }

    public void createNewUser() throws IOException {
        User user = new User(newUsername.getText(), newPassword.getText());
        if (userBank.getUser(newUsername.getText()) == null) {
            userBank.addUser(newUsername.getText(), user);
            globalUserName = newUsername.getText();
            Parent root = FXMLLoader.load(getClass().getResource("createSP.fxml"));
            primaryStage.getIcons().add(new Image("edu/bsu/cs222/taskbar_icon.png"));
            primaryStage.setScene(new Scene(root));
            primaryStage.show();
        } else {
            checkUserName.setText("Username is already in use. Please try another one.");
        }
    }

    public void updateBudgetCategory() {
        User user = userBank.getUser(globalUserName);
        double income = Double.parseDouble(incomeTxtField.getText());
        double amount = Double.parseDouble(expenseAmountTxtField.getText());
        String category = spendingPlanComboBox.getValue().toLowerCase().replace(" ","");
        user.updateBudget(category, amount);
        user.setBudgetIncome(income);
        budgetTxtArea.setText(user.printBudget());
        amountLeftToAllocate.setText(Double.toString(user.spendingPlan.calculateRemainingIncome()));
    }

    public void finalizeSpendingPlan() throws Exception{
        User user = userBank.getUser(globalUserName);
        if (user.spendingPlan.isExpenseEqualIncome()) {
            Parent root = FXMLLoader.load(getClass().getResource("App.fxml"));
            primaryStage.getIcons().add(new Image("edu/bsu/cs222/taskbar_icon.png"));
            primaryStage.setScene(new Scene(root));
            primaryStage.show();
        }
        else {
            unbalancedExpenses.setText("Total Expenses Does Not Equal Income | Try Again");
        }
    }

    public void updateMoneyValues() {
        try {
            User user = userBank.getUser(globalUserName);
            List<String> months = Arrays.asList("January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December");
            int month = months.indexOf(monthComboBox.getValue()) + 1;
            String category = categoryComboBox.getValue().toLowerCase().replace(" ","");
            String longMonth = monthComboBox.getValue();
            expenseAmountErrorLbl.setText(" ");
            if (longMonth.equals("Update Budget")) {
                if (category.equals("income")) {
                    user.setBudgetIncome(Double.parseDouble(realExpenseAmountTxtField.getText()));
                } else {
                    double expenseAmount = user.spendingPlan.getExpenseAmount(category);
                    user.updateBudget(category, Double.parseDouble(realExpenseAmountTxtField.getText()));
                    if (user.spendingPlan.getIncome() < user.spendingPlan.totalExpenses()) {
                        expenseAmountErrorLbl.setText("Total Expenses Are Over Income | Try Again");
                        user.updateBudget(category,expenseAmount);
                    }
                }
            } else {
                if (category.equals("income")) {
                    user.setRealMonthlyIncome(month, Double.parseDouble(realExpenseAmountTxtField.getText()));
                } else {
                    user.editMonthlyRealSpending(month, category, Double.parseDouble(realExpenseAmountTxtField.getText()));
                }
            }
            leftLabel.setText("Budget");
            spendingPlanTxtArea.setText(user.printBudget());
            budgetPieChart.setData(user.spendingPlan.createPieChartData());
            budgetPieChart.setLabelsVisible(false);
            if (!longMonth.equals("Update Budget")) {
                rightLabel.setText(longMonth);
                realSpendingTxtArea.setText(user.printRealSpending(month));
                realSpendingPieChart.setData(user.realSpending.get(month - 1).createPieChartData());
                realSpendingPieChart.setLabelsVisible(false);
            }
        }catch (NullPointerException | NumberFormatException e){
            expenseAmountErrorLbl.setText("Please make a selection to continue");
        }
    }

    public void showJanRealSpending(){
        User user = userBank.getUser(globalUserName);
        if(activeSide) {
            rightLabel.setText("January");
            realSpendingTxtArea.setText(user.printRealSpending(1));
            realSpendingPieChart.setData(user.realSpending.get(0).createPieChartData());
            realSpendingPieChart.setLabelsVisible(false);
        }else{
            leftLabel.setText("January");
            spendingPlanTxtArea.setText(user.printRealSpending(1));
            budgetPieChart.setData(user.realSpending.get(0).createPieChartData());
            budgetPieChart.setLabelsVisible(false);
        }
    }
    public void showFebRealSpending(){
        User user = userBank.getUser(globalUserName);
        if(activeSide) {
            rightLabel.setText("February");
            realSpendingTxtArea.setText(user.printRealSpending(2));
            realSpendingPieChart.setData(user.realSpending.get(1).createPieChartData());
            realSpendingPieChart.setLabelsVisible(false);
        }else{
            leftLabel.setText("February");
            spendingPlanTxtArea.setText(user.printRealSpending(2));
            budgetPieChart.setData(user.realSpending.get(1).createPieChartData());
            budgetPieChart.setLabelsVisible(false);
        }
    }
    public void showMarRealSpending(){
        User user = userBank.getUser(globalUserName);
        if(activeSide) {
            rightLabel.setText("March");
            realSpendingTxtArea.setText(user.printRealSpending(3));
            realSpendingPieChart.setData(user.realSpending.get(2).createPieChartData());
            realSpendingPieChart.setLabelsVisible(false);
        }else{
            leftLabel.setText("March");
            spendingPlanTxtArea.setText(user.printRealSpending(3));
            budgetPieChart.setData(user.realSpending.get(2).createPieChartData());
            budgetPieChart.setLabelsVisible(false);
        }
    }
    public void showAprRealSpending(){
        User user = userBank.getUser(globalUserName);
        if(activeSide) {
            rightLabel.setText("April");
            realSpendingTxtArea.setText(user.printRealSpending(4));
            realSpendingPieChart.setData(user.realSpending.get(3).createPieChartData());
            realSpendingPieChart.setLabelsVisible(false);
        }else{
            leftLabel.setText("April");
            spendingPlanTxtArea.setText(user.printRealSpending(4));
            budgetPieChart.setData(user.realSpending.get(3).createPieChartData());
            budgetPieChart.setLabelsVisible(false);
        }
    }
    public void showMayRealSpending(){
        User user = userBank.getUser(globalUserName);
        if(activeSide) {
            rightLabel.setText("May");
            realSpendingTxtArea.setText(user.printRealSpending(5));
            realSpendingPieChart.setData(user.realSpending.get(4).createPieChartData());
            realSpendingPieChart.setLabelsVisible(false);
        }else{
            leftLabel.setText("May");
            spendingPlanTxtArea.setText(user.printRealSpending(5));
            budgetPieChart.setData(user.realSpending.get(4).createPieChartData());
            budgetPieChart.setLabelsVisible(false);
        }
    }
    public void showJunRealSpending(){
        User user = userBank.getUser(globalUserName);
        if(activeSide) {
            rightLabel.setText("June");
            realSpendingTxtArea.setText(user.printRealSpending(6));
            realSpendingPieChart.setData(user.realSpending.get(5).createPieChartData());
            realSpendingPieChart.setLabelsVisible(false);
        }else{
            leftLabel.setText("June");
            spendingPlanTxtArea.setText(user.printRealSpending(6));
            budgetPieChart.setData(user.realSpending.get(5).createPieChartData());
            budgetPieChart.setLabelsVisible(false);
        }
    }
    public void showJulRealSpending(){
        User user = userBank.getUser(globalUserName);
        if(activeSide) {
            rightLabel.setText("July");
            realSpendingTxtArea.setText(user.printRealSpending(7));
            realSpendingPieChart.setData(user.realSpending.get(6).createPieChartData());
            realSpendingPieChart.setLabelsVisible(false);
        }else{
            leftLabel.setText("July");
            spendingPlanTxtArea.setText(user.printRealSpending(7));
            budgetPieChart.setData(user.realSpending.get(6).createPieChartData());
            budgetPieChart.setLabelsVisible(false);
        }
    }
    public void showAugRealSpending(){
        User user = userBank.getUser(globalUserName);
        if(activeSide) {
            rightLabel.setText("August");
            realSpendingTxtArea.setText(user.printRealSpending(8));
            realSpendingPieChart.setData(user.realSpending.get(7).createPieChartData());
            realSpendingPieChart.setLabelsVisible(false);
        }else{
            leftLabel.setText("August");
            spendingPlanTxtArea.setText(user.printRealSpending(8));
            budgetPieChart.setData(user.realSpending.get(7).createPieChartData());
            budgetPieChart.setLabelsVisible(false);
        }
    }
    public void showSepRealSpending(){
        User user = userBank.getUser(globalUserName);
        if(activeSide) {
            rightLabel.setText("September");
            realSpendingTxtArea.setText(user.printRealSpending(9));
            realSpendingPieChart.setData(user.realSpending.get(8).createPieChartData());
            realSpendingPieChart.setLabelsVisible(false);
        }else{
            leftLabel.setText("September");
            spendingPlanTxtArea.setText(user.printRealSpending(9));
            budgetPieChart.setData(user.realSpending.get(8).createPieChartData());
            budgetPieChart.setLabelsVisible(false);
        }
    }
    public void showOctRealSpending(){
        User user = userBank.getUser(globalUserName);
        if(activeSide) {
            rightLabel.setText("October");
            realSpendingTxtArea.setText(user.printRealSpending(10));
            realSpendingPieChart.setData(user.realSpending.get(9).createPieChartData());
            realSpendingPieChart.setLabelsVisible(false);
        }else{
            leftLabel.setText("October");
            spendingPlanTxtArea.setText(user.printRealSpending(10));
            budgetPieChart.setData(user.realSpending.get(9).createPieChartData());
            budgetPieChart.setLabelsVisible(false);
        }
    }
    public void showNovRealSpending(){
        User user = userBank.getUser(globalUserName);
        if(activeSide) {
            rightLabel.setText("November");
            realSpendingTxtArea.setText(user.printRealSpending(11));
            realSpendingPieChart.setData(user.realSpending.get(10).createPieChartData());
            realSpendingPieChart.setLabelsVisible(false);
        }else{
            leftLabel.setText("November");
            spendingPlanTxtArea.setText(user.printRealSpending(11));
            budgetPieChart.setData(user.realSpending.get(10).createPieChartData());
            budgetPieChart.setLabelsVisible(false);
        }
    }
    public void showDecRealSpending(){
        User user = userBank.getUser(globalUserName);
        if(activeSide) {
            rightLabel.setText("December");
            realSpendingTxtArea.setText(user.printRealSpending(12));
            realSpendingPieChart.setData(user.realSpending.get(11).createPieChartData());
            realSpendingPieChart.setLabelsVisible(false);
        }else{
            leftLabel.setText("December");
            spendingPlanTxtArea.setText(user.printRealSpending(12));
            budgetPieChart.setData(user.realSpending.get(11).createPieChartData());
            budgetPieChart.setLabelsVisible(false);
        }
    }

    public void showBudget(){
        User user = userBank.getUser(globalUserName);
        if(activeSide) {
            rightLabel.setText("Budget");
            realSpendingTxtArea.setText(user.printBudget());
            realSpendingPieChart.setData(user.spendingPlan.createPieChartData());
            realSpendingPieChart.setLabelsVisible(false);
        }else{
            leftLabel.setText("Budget");
            spendingPlanTxtArea.setText(user.printBudget());
            budgetPieChart.setData(user.spendingPlan.createPieChartData());
            budgetPieChart.setLabelsVisible(false);
        }
    }

    public void changeActiveSide() {
        activeSide = !activeSide;
        if(activeSide){
            activeSideButton.setText(">");
        }else{
            activeSideButton.setText("<");
        }
    }



    public void exitApp(){
        userBank.saveUsersToJsonFiles();
        System.exit(0);
    }
}

