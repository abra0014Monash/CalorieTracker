package com.example.caloriestracker;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Validation {

    public static Boolean ValidateUsername(String username){
        Pattern pattern;
        Matcher matcher;

        final String username_PATTERN = "^[a-z0-9_-]{5,20}$"; //length 5 to 20 no

        pattern = Pattern.compile(username_PATTERN);
        matcher = pattern.matcher(username);

        return matcher.matches();
    }
    public static Boolean ValidatePassword(String password){
        Pattern pattern;
        Matcher matcher;

        final String PASSWORD_PATTERN = "^(?=.*\\d).{4,10}$";   // atleast one digit length 4 to 10

        pattern = Pattern.compile(PASSWORD_PATTERN);
        matcher = pattern.matcher(password);

        return matcher.matches();

    }
}
