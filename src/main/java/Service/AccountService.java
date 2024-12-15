package Service;

import Model.Account;
import DAO.AccountDAO;


public class AccountService {
    private final AccountDAO accDAO;

    public AccountService() {
        accDAO = new AccountDAO();
    }

    //add an account to the Account table if another one with a matching username doesn't exist
    //parameters: Account object representing the account to be added
    //return: null if username is empty, password is too short
    //        or username already exists. Otherwise return the account that was added
    public Account addAccount(Account acc) {
        if(acc.getUsername() == null || acc.getUsername().length() < 1) //username can't be empty
            return null;
        else if(acc.getPassword().length() < 4) //password must be atleast 4 chars long
            return null;
        else if(accDAO.searchByUsername(acc.getUsername()) != null) //username already in use
            return null;
        else
            return accDAO.insertAcount(acc);
    }

    //attempt to log into an account with given info
    //parameters: an input Account object
    //return: null if unable to login. Otherwise, return the account that was logged in to
    public Account login(Account acc) {
        Account matchingAcc = accDAO.searchByUsername(acc.getUsername());
        
        if(matchingAcc == null)
            return null;
        else if(!(matchingAcc.getPassword()).equals(acc.getPassword()))
            return null;
        else
            return matchingAcc;
    }
}