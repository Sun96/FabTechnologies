package com.xonetsapps.fabtechnologies.Http;

/**
 * Created by Arafat on 7/26/2016.
 */


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Amit Saha on 9/20/2015.
 */
public class InvoiceDatabase extends SQLiteOpenHelper {

    public static final String Database_NAME = "Member2.db";
    public static final String Table_NAME =  "Member_Table2";
    public static final String Col_1 =  "ID";
    public static final String Col_2 =  "NAME";
    SQLiteDatabase db;
    Context context;
    public InvoiceDatabase(Context context) {
        super(context, Database_NAME, null, 1);
        this.context=context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + Table_NAME + "(ID INTEGER PRIMARY KEY AUTOINCREMENT, NAME TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXIST"+Table_NAME);
        onCreate(db);
    }
    public void clearDatabase() {
        context.deleteDatabase(Database_NAME);

    }

    public boolean insertdata(String name){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(Col_2,name);
        long result= db.insert(Table_NAME, null, contentValues);
        if (result == -1)

            return false;

        else
            return true;
    }

    public Cursor getAllData(){

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res =db.rawQuery("select * from "+Table_NAME,null);
        return res;

    }

    public Boolean updateuserData(String id,String name){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(Col_1,id);
        contentValues.put(Col_2,name);
        db.update(Table_NAME , contentValues , "ID = ?",new String[] { id });
        return true;

    }
    public Integer deleteData(String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(Table_NAME, "ID = ?",new String[]{id} );


    }
}

