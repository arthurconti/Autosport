package com.conti.autosport.sql;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.conti.autosport.R;

public class MySQLiteHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 12;
    private Context context;

    public MySQLiteHelper(Context context) {
        super(context, context.getResources().getString(
                R.string.table_cidades), null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        createTables(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        dropTables(db);

        // create fresh table
        this.onCreate(db);
    }

    private void createTables(SQLiteDatabase db) {
        db.execSQL(this.context.getResources().getString(
                R.string.create_table_cidades));
    }

    private void dropTables(SQLiteDatabase db) {
        db.execSQL(this.context.getResources().getString(
                R.string.drop_table_cidades));
    }
}