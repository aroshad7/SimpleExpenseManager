package lk.ac.mrt.cse.dbs.simpleexpensemanager.control;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl.PersistentAccountDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl.PersistentTransactionDAO;


/**
 * Created by aroshad7 on 11/20/16.
 */
public class PersistentExpenseManager extends ExpenseManager {
    private Context ctx;
    public PersistentExpenseManager(Context ctx){
        this.ctx = ctx;
        setup();
    }
    @Override
    public void setup(){

        SQLiteDatabase myDataB = ctx.openOrCreateDatabase("140135F", ctx.MODE_PRIVATE, null);

        myDataB.execSQL("CREATE TABLE IF NOT EXISTS Account(" +
                "Account_no VARCHAR PRIMARY KEY," +
                "Bank VARCHAR," +
                "Holder VARCHAR," +
                "Initial_amt REAL" +
                " );");

        myDataB.execSQL("CREATE TABLE IF NOT EXISTS TransactionLog(" +
                "Transaction_id INTEGER PRIMARY KEY," +
                "Account_no VARCHAR," +
                "Type INT," +
                "Amt REAL," +
                "Log_date DATE," +
                "FOREIGN KEY (Account_no) REFERENCES Account(Account_no)" +
                ");");



        PersistentAccountDAO accountDAO = new PersistentAccountDAO(myDataB);

        setAccountsDAO(accountDAO);

        setTransactionsDAO(new PersistentTransactionDAO(myDataB));
    }
}
