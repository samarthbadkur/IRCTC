package org.example.app.Util;

import org.mindrot.jbcrypt.BCrypt;


// Utility class for user-related services such as password hashing and verification
// We use utility class to group related static methods that can be used across the application without needing to create an instance of the class.
public class UserServiceUtil {
    // Here we are using BCrypt library to hash passwords or salting passwords.
    public static String hashPassword(String plainPassword) {
        // Implement password hashing and comparison logic here

        return BCrypt.hashpw(plainPassword, BCrypt.gensalt());
    }

    // Method to check if the plain password matches the hashed password when user is logging in
    // It checks the plain password entered by user during login with the hashed password stored in database.
    public static boolean checkPassword(String plainPassword, String hashedPassword) {
        return BCrypt.checkpw(plainPassword, hashedPassword);
    }
}
