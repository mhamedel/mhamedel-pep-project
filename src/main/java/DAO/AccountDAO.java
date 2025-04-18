package DAO;

import Model.Account;
import Util.ConnectionUtil;
import java.sql.*;

public class AccountDAO {

    // Method to  insert a new account into the database
    public Account insertAccount(Account account) {
        try (Connection conn = ConnectionUtil.getConnection()) {
            // SQL statement to insert a new account (username and password)

            String sql = "INSERT INTO account (username, password) VALUES (?, ?)";
            PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, account.getUsername());
            ps.setString(2, account.getPassword());
            ps.executeUpdate();
            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                // Set the generated ID into the account object
                account.setAccount_id(rs.getInt(1));
                return account;
            }
        } catch (SQLException e) {
            e.printStackTrace(); 
        }
        return null; 
        // Return null if insertion fails
    }








    // Method to retrieve an account using credentialss
    public Account getAccountByUsernameAndPassword(String username, String password) {
        try (Connection conn = ConnectionUtil.getConnection()) {
            // SQL statement to select account matching given credentials
            String sql = "SELECT * FROM account WHERE username = ? AND password = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, username);
            ps.setString(2, password);
            
            ResultSet rs = ps.executeQuery();
            
            // If a match is found, create and return an account object
            if (rs.next()) {
                return new Account(
                    rs.getInt("account_id"),
                    rs.getString("username"),
                    rs.getString("password")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace(); 
        }
        return null; 
        // Return null if no matching account is found
    }






    // Method to check if a username already exists in the database
    public boolean usernameExists(String username) {
        try (Connection conn = ConnectionUtil.getConnection()) {
            // SQL statement to check for existence of a username
            String sql = "SELECT 1 FROM account WHERE username = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();
            
            // Return true if the username exists by checking if the result set has at least one row
            return rs.next();
        } catch (SQLException e) {
            e.printStackTrace(); 
        }
        return false; 
    }
}
