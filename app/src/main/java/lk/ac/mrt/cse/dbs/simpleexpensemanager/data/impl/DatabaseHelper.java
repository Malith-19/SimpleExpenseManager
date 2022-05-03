package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Account;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseType;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Transaction;


public class DatabaseHelper extends SQLiteOpenHelper {
    public DatabaseHelper(Context context){
        super(context,"190145G.db",null,1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase){
        String accountTableStmt = "create table account ("+
                "accountNO text primary key, "+
                "bankName text, "+
                "holderName text, "+
                "balance real )"; //sql query for create account table

        String transactionTableStmt = "create table transactionLog ("+
                "date text, "+
                "accountNo text, "+
                "expenseType text check(expenseType in ('EXPENSE','INCOME')), "+
                "amount real, "+
                "foreign key(accountNo) references account(accountNo))"; //sql query for create transaction table

        sqLiteDatabase.execSQL(accountTableStmt); //creating account table
        sqLiteDatabase.execSQL(transactionTableStmt); //creating transactions table
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("drop table if exists account");
        sqLiteDatabase.execSQL("drop table if exists transactionLOg");
        onCreate(sqLiteDatabase);
    }

    //this method
    // will return all account from the database
    public List<Account> getAccounts(){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursorAccounts = db.rawQuery("select * from account",null);
        List<Account> accounts = new ArrayList<>();

        if(cursorAccounts.moveToFirst()){
            do{
                accounts.add(new Account(cursorAccounts.getString(0), cursorAccounts.getString(1),cursorAccounts.getString(2),cursorAccounts.getDouble(3)));
            }while(cursorAccounts.moveToNext());
        }

        cursorAccounts.close();
        //db.close();
        return accounts;
    }

    //this method will return all information about transactions
    public List<Transaction> getTransactions(){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursorTransactions = db.rawQuery("select * from transactionLog",null);
        List<Transaction> transactions = new ArrayList<>();

        if(cursorTransactions.moveToFirst()){
            try {
                transactions.add(new Transaction(new SimpleDateFormat("dd/MM/yyyy").parse(cursorTransactions.getString(1)), cursorTransactions.getString(2), ExpenseType.valueOf(cursorTransactions.getString(3)),cursorTransactions.getDouble(4)));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }while(cursorTransactions.moveToNext());

        cursorTransactions.close();
        return transactions;
    }

    //this method will add(insert data) account to account table
    public void addAccount(Account account){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put("accountNo",account.getAccountNo());
        contentValues.put("bankName",account.getBankName());
        contentValues.put("holderName",account.getAccountHolderName());
        contentValues.put("balance",account.getBalance());

        db.insert("account",null,contentValues);
        db.close();
    }

    //this method will add(insert data) transaction details to transaction table
    public void addTransaction(Transaction transaction){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put("date",new SimpleDateFormat("dd/MM/yyyy").format(transaction.getDate()));
        contentValues.put("accountNo",transaction.getAccountNo());
        contentValues.put("expenseType",transaction.getExpenseType().name());
        contentValues.put("amount",transaction.getAmount());

        db.insert("transactionLog",null,contentValues);
        db.close();
    }

    public void updateAccount(Account account){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put("accountNo", account.getAccountNo());
        contentValues.put("bankName", account.getBankName());
        contentValues.put("holderName", account.getAccountHolderName());
        contentValues.put("balance", account.getBalance());

        db.update("account",contentValues,"accountNo=?",new String[]{account.getAccountNo()});
        db.close();
    }


}
