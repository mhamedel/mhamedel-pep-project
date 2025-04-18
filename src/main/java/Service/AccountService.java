package Service;

import DAO.AccountDAO;
import Model.Account;

public class AccountService {
    // DAO instance for accessing account-related database operations
    private AccountDAO accountDAO = new AccountDAO();

    /**
     * Registers a new account if:
     * - The username is not null or blank
     * - The password is at least 4 characters long
     * - The username does not already exist in the database
     * 
     * @param account The account to register
     * @return The inserted Account object with its generated ID, or null if validation fails
     */
    public Account register(Account account) {
        if (account.getUsername() == null || account.getUsername().isBlank() // invalid username
            || account.getPassword() == null || account.getPassword().length() < 4 // invalid password
            || accountDAO.usernameExists(account.getUsername())) { // username already exist
            return null;
        }
        return accountDAO.insertAccount(account); 
    }

    /**
     * Logs in a user by verifying their username and password.
     * 
     * @param account The account object containing login credentials
     * @return The matched Account from the database, or null if credentials are invalid
     */
    public Account login(Account account) {
        return accountDAO.getAccountByUsernameAndPassword(account.getUsername(), account.getPassword());
    }
}
