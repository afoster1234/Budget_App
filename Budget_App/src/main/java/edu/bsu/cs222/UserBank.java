package edu.bsu.cs222;

import java.io.File;
import java.util.HashMap;

class UserBank {
    private HashMap<String, User> userBank = new HashMap<>();

    void addUser(String userName, User user) {
        userBank.put(userName, user);
    }

    User getUser(String userName) {
        return userBank.getOrDefault(userName, null);
    }


    void getUsersFromJsonFiles() {
        FileHandler fileHandler = new FileHandler();
        File directory = new File("src/main/Resources/");
        File[] directoryListing = directory.listFiles();
        if (directoryListing != null) {
            for (File file : directoryListing) {
                if(file.getName().length() > 5) {
                    String userName = file.getName().replace(".json", "");
                    addUser(userName, fileHandler.makeUserFromJsonToJava(userName));
                }
            }
        }
    }

    void saveUsersToJsonFiles() {
        FileHandler fileHandler = new FileHandler();
        for(String userName : userBank.keySet()){
            fileHandler.makeUserFromJavaToJson(getUser(userName));
        }
    }


}
