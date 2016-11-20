package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import java.util.ArrayList;
import java.util.List;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.AccountDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.exception.InvalidAccountException;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Account;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseType;

/**
 * Created by aroshad7 on 11/20/16.
 */
public class PersistentAccountDAO implements AccountDAO {
    private SQLiteDatabase database;

    public PersistentAccountDAO(SQLiteDatabase db){
        this.database = db;
    }
    @Override
    public List<String> getAccountNumbersList() {
        Cursor result = database.rawQuery("SELECT Account_no FROM Account",null);
        List<String> accountsList = new ArrayList<String>();

        if(result.moveToFirst()) {
            do {
                accountsList.add(result.getString(result.getColumnIndex("Account_no")));
            } while (result.moveToNext());
        }

        return accountsList;
    }

    @Override
    public List<Account> getAccountsList() {
        Cursor result = database.rawQuery("SELECT * FROM Account",null);
        List<Account> accountList = new ArrayList<Account>();

        if(result.moveToFirst()) {
            do {
                Account account = new Account(result.getString(result.getColumnIndex("Account_no")),
                        result.getString(result.getColumnIndex("Bank")),
                        result.getString(result.getColumnIndex("Holder")),
                        result.getDouble(result.getColumnIndex("Initial_amt")));
                accountList.add(account);
            } while (result.moveToNext());
        }

        return accountList;
    }

    @Override
    public Account getAccount(String accountNo) throws InvalidAccountException {
        Cursor resultSet = database.rawQuery("SELECT * FROM Account WHERE Account_no = " + accountNo,null);
        Account account = null;

        if(resultSet.moveToFirst()) {
            do {
                account = new Account(resultSet.getString(resultSet.getColumnIndex("Account_no")),
                        resultSet.getString(resultSet.getColumnIndex("Bank")),
                        resultSet.getString(resultSet.getColumnIndex("Holder")),
                        resultSet.getDouble(resultSet.getColumnIndex("Initial_amt")));
            } while (resultSet.moveToNext());
        }

        return account;
    }

    @Override
    public void addAccount(Account account) {

        String sql = "INSERT INTO Account (Account_no,Bank,Holder,Initial_amt) VALUES (?,?,?,?)";
        SQLiteStatement sqlState = database.compileStatement(sql);



        sqlState.bindString(1, account.getAccountNo());
        sqlState.bindString(2, account.getBankName());
        sqlState.bindString(3, account.getAccountHolderName());
        sqlState.bindDouble(4, account.getBalance());

        sqlState.executeInsert();


    }

    @Override
    public void removeAccount(String accountNo) throws InvalidAccountException {
        String sqlStr = "DELETE FROM Account WHERE Account_no = ?";
        SQLiteStatement SqlStatement = database.compileStatement(sqlStr);

        SqlStatement.bindString(1,accountNo);

        SqlStatement.executeUpdateDelete();
    }

    @Override
    public void updateBalance(String accountNo, ExpenseType expenseType, double amount) throws InvalidAccountException {
        String sqlStrng = "UPDATE Account SET Initial_amt = Initial_amt + ?";
        SQLiteStatement SqlStatem = database.compileStatement(sqlStrng);
        if(expenseType == ExpenseType.EXPENSE){
            SqlStatem.bindDouble(1,-amount);
        }else{
            SqlStatem.bindDouble(1,amount);
        }

        SqlStatem.executeUpdateDelete();
    }
}
