package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl;

import java.util.ArrayList;
import java.util.List;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.AccountDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.exception.InvalidAccountException;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Account;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseType;

public class PersistentAccountDAO implements AccountDAO {

    private DatabaseHelper dbHelper;

    public PersistentAccountDAO(DatabaseHelper dbHelper){
        this.dbHelper = dbHelper;
    }

    @Override
    public List<String> getAccountNumbersList() {
        List<String> accountNumbers = new ArrayList<>();
        for(Account account: dbHelper.getAccounts()){
            accountNumbers.add(account.getAccountNo());
        }
        return accountNumbers;
    }

    @Override
    public List<Account> getAccountsList() {
        return dbHelper.getAccounts();
    }

    @Override
    public Account getAccount(String accountNo) throws InvalidAccountException {
        for(Account account:dbHelper.getAccounts()){
            if(account.getAccountNo().equals(accountNo)){
                return account;            }
        }
        String errorMsg = "This account you entered is not valid!";
        throw new InvalidAccountException(errorMsg);
    }

    @Override
    public void addAccount(Account account) {
        dbHelper.addAccount(account);
    }

    @Override
    public void removeAccount(String accountNo) throws InvalidAccountException {
        for(Account account: dbHelper.getAccounts()){
            if(account.getAccountNo().equals(accountNo)){
                dbHelper.removeAccount(accountNo);
                return;
            }
        }
        String errorMsg = "This account you entered is not valid!";
        throw new InvalidAccountException(errorMsg);
    }

    @Override
    public void updateBalance(String accountNo, ExpenseType expenseType, double amount) throws InvalidAccountException {
        for(Account account: dbHelper.getAccounts()){
            if(account.getAccountNo().equals(accountNo)){
                switch (expenseType){
                    case EXPENSE:
                        account.setBalance(account.getBalance() - amount);
                        break;
                    case INCOME:
                        account.setBalance(account.getBalance()+amount);
                        break;
                }
                dbHelper.updateAccount(account);
                return;
            }
        }

        String errorMsg = "This account you entered is not valid!";
        throw new InvalidAccountException(errorMsg);

    }
}
