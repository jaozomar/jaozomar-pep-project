package DAO;

import Model.Account;
import Util.ConnectionUtil;

import java.sql.*;

public class AccountDAO {

    // insert an account into the Account table
    // parameters: Account acc
    // return: Account acc with the generated id if successful. Otherwise return null
    public Account insertAcount(Account acc) {
        Connection conn = ConnectionUtil.getConnection();
        try {
            String sql = "INSERT INTO Account (username, password) VALUES (?,?)";
            PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            // Set the username and password we will be adding into the table
            ps.setString(1, acc.getUsername());
            ps.setString(2, acc.getPassword());
            ps.executeUpdate();

            // The generated keys restult set. Should contain the new account_id that was automatically generated
            ResultSet res = ps.getGeneratedKeys();
            if(res.next()) {
                acc.setAccount_id(res.getInt("account_id")); //set the account_id
                return acc; //return acc with the generated account_id
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    // search the Account table for a record with the given username
    // parameters: String username, which represents the username to search for
    // return: Account object formed by the record matching the username if found. Otherwise return null
    public Account searchByUsername(String username) {
        Connection conn = ConnectionUtil.getConnection();
        try {
            String sql = "SELECT * FROM Account WHERE username=?";
            PreparedStatement ps = conn.prepareStatement(sql);

            //Set account username to search for
            ps.setString(1,username);

            //Look to see if there is a match in the result set
            ResultSet res = ps.executeQuery();
            while(res.next()) {
                //return the account found in the result set
                return new Account(
                    res.getInt("account_id"),
                    res.getString("username"),
                    res.getString("password")
                );
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    // search the Account table for a record with the given id
    // parameters: int accID, which represents the account id to search for
    // return: true if an account with account_id matching accID was found. Otherwise return false
    public boolean doesIDExist(int accID) {
        Connection conn = ConnectionUtil.getConnection();
        try {
            String sql = "SELECT * FROM Account WHERE account_id=?";
            PreparedStatement ps = conn.prepareStatement(sql);

            //Set account id to search for
            ps.setInt(1,accID);
            
            //Look to see if there is a match in the result set
            ResultSet res = ps.executeQuery();
            while(res.next()) {
                return true; //return true if a matching account was found
            }
        } catch(Exception e) {
            e.printStackTrace();
        }

        return false; //return false if no matching account was ever found
    }

}