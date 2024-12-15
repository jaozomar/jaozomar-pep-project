package Service;

import Model.Account;
import DAO.AccountDAO;

import java.util.List;

public class AccountService {
    private AccountDAO accDAO;

    public AccountService() {
        accDAO = new AccountDAO();
    }

    public Account addAccount(Account acc) {
        if(accDAO.searchByUsername(acc.getUsername()) != null)
            return null;
        return accDAO.insertAcount(acc);
    }
}