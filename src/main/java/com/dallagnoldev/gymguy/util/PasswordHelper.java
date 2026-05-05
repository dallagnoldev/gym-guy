package com.dallagnoldev.gymguy.util;

import java.util.regex.Pattern;

public class PasswordHelper {

        public static PasswordResponseValidation validatePassword(String password) {

            if (password == null || password.isBlank()) {
                return new PasswordResponseValidation(false, "Password cannot be blank");
            }

            if (password.length() < 8) {
                return new PasswordResponseValidation(false, "Password must be at least 8 characters long");
            }

            if (!Pattern.compile("[A-Z]").matcher(password).find()) {
                return new PasswordResponseValidation(false, "Password must contain at least one capital letter");
            }

            if (!Pattern.compile("[!@#$%^&*(),.?\":{}|<>]").matcher(password).find()) {
                return new PasswordResponseValidation(false, "Password must contain at least  one special character");
            }

            if (!Pattern.compile("[0-9]").matcher(password).find()) {
                return new PasswordResponseValidation(false, "Password must contain at least one number");
            }

            return new PasswordResponseValidation(true, "Password is valid!");
    }
}
