package com.example.tahakothawala.myapplication;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

/**
 * Created by tahakothawala on 19/09/2017 AD.
 */

public class DatabaseHandler extends SQLiteOpenHelper{

    String DB_NAME = "MyDatabase";
    String TABLE_NAME = "Transactions";
    int DB_VERSION = 1;



    public DatabaseHandler(Context context) {
        super(context,"MyDatabase", null, 1);
    }

    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_NAME + "(id INTEGER PRIMARY KEY AUTOINCREMENT,category TEXT," +
                "description TEXT,amount INTEGER,date TEXT);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME + ");");
        onCreate(sqLiteDatabase);
    }

    public void addEntry(TransactionDetails transactionDetails) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        String query = "insert into "+TABLE_NAME+" values(null,\""+transactionDetails.CATEGORY+"\",\""+transactionDetails.DESCRIPTION+"\","+transactionDetails.AMOUNT+",\""+transactionDetails.DATE+"\");";
        sqLiteDatabase.execSQL(query);
        sqLiteDatabase.close();
    }

    public void deleteEntry(int ID) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        sqLiteDatabase.execSQL("delete from " + TABLE_NAME + " where id=" + ID + " ;");
        sqLiteDatabase.close();
    }

    public void deleteAllEntries() {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        sqLiteDatabase.execSQL("delete from " + TABLE_NAME + ";");
        sqLiteDatabase.close();
    }

    public ArrayList<TransactionDetails> getAllEntries() {
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        ArrayList<TransactionDetails> arrayList = new ArrayList<>();

        Cursor cursor = sqLiteDatabase.rawQuery("select * from " + TABLE_NAME+" order by id  desc", null);
        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {

                int a = cursor.getInt(cursor.getColumnIndex("id"));
                String b = cursor.getString(cursor.getColumnIndex("category"));
                String c = cursor.getString(cursor.getColumnIndex("description"));
                int d = cursor.getInt(cursor.getColumnIndex("amount"));
                String e = cursor.getString(cursor.getColumnIndex("date"));

                TransactionDetails transactionDetails = new TransactionDetails();
                transactionDetails.ID = a;
                transactionDetails.CATEGORY = b;
                transactionDetails.DESCRIPTION = c;
                transactionDetails.AMOUNT = d;
                transactionDetails.DATE = e;

                arrayList.add(transactionDetails);
                cursor.moveToNext();
            }
        }
        return arrayList;
    }

    public ArrayList<TransactionDetails> getThreeEntries() {
        SQLiteDatabase database = getReadableDatabase();
        ArrayList<TransactionDetails> arrayList = new ArrayList<>();

        Cursor cursor = database.rawQuery("select * from " + TABLE_NAME+" order by id  desc limit 3", null);
        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {

                int a = cursor.getInt(cursor.getColumnIndex("id"));
                String b = cursor.getString(cursor.getColumnIndex("category"));
                String c = cursor.getString(cursor.getColumnIndex("description"));
                int d = cursor.getInt(cursor.getColumnIndex("amount"));
                String e = cursor.getString(cursor.getColumnIndex("date"));

                TransactionDetails transactionDetails = new TransactionDetails();
                transactionDetails.ID = a;
                transactionDetails.CATEGORY = b;
                transactionDetails.DESCRIPTION = c;
                transactionDetails.AMOUNT = d;
                transactionDetails.DATE = e;

                arrayList.add(transactionDetails);
                cursor.moveToNext();
            }
        }
        return arrayList;

    }

    public Integer noOfEntries() {
            String countQuery = "SELECT  * FROM " + TABLE_NAME;
            SQLiteDatabase db = this.getReadableDatabase();
            Cursor cursor = db.rawQuery(countQuery, null);
            Integer cnt = cursor.getCount();
            cursor.close();
            return cnt;
    }

    public int getAmountOfSingleTransaction(int ID)
    {
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();
        String countQuery = "SELECT AMOUNT FROM " +TABLE_NAME+" WHERE id="+ID+" ;";
        Cursor cursor = sqLiteDatabase.rawQuery(countQuery, null);
        if (cursor != null) {
            cursor.moveToFirst();
        }
        int a = cursor.getInt(cursor.getColumnIndex("amount"));
        return a;
    }
}
