package com.conti.autosport.sql;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.conti.autosport.R;
import com.conti.autosport.model.City;

import java.util.LinkedList;
import java.util.List;

public class Database {

    private final String FIELD_ID;
    private final String FIELD_NAME;
    private final String TABLE;
    private String[] ALL_COLUMNS;
    private final String DELETEALL_TABLE;

    private SQLiteDatabase database;
    private MySQLiteHelper dbHelper;

    public Database(Context ctx) {

        TABLE = ctx.getResources().getString(R.string.table_cidades);
        FIELD_ID = ctx.getResources().getString(R.string.field_cidades_id);

        FIELD_NAME = ctx.getResources().getString(R.string.field_cidades_name);

        ALL_COLUMNS = new String[]{FIELD_ID, FIELD_NAME};

        DELETEALL_TABLE = ctx.getResources().getString(
                R.string.deleteall_table_cidades);

        dbHelper = new MySQLiteHelper(ctx);
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public long addCidade(City cidade) {
        long numberRow;

        // Log.d(LOG_TAG, "adding client: " + client.toJSON().toString());

        ContentValues values = new ContentValues();

        values.put(FIELD_NAME, getEmptyStringFromNULL(cidade.getNome()));
        values.put(FIELD_ID, cidade.getID());

        numberRow = database.insert(TABLE, null, values);
        return numberRow;
    }

    public List<City> getAllCidades() {
        LinkedList<City> ret = new LinkedList<City>();
        Log.d("Autosport", "getallCidades");

        Cursor cursor = database.query(TABLE, ALL_COLUMNS, null, null, null,
                null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {

            City cidade = cursorToCidade(cursor);
            ret.add(cidade);
            cursor.moveToNext();
        }

        // make sure to close the cursor
        cursor.close();

        // database.execSQL(DELETEALL_TABLE);

        return ret;
    }

    public void deleteAll() {
        database.execSQL(DELETEALL_TABLE);
    }

    private City cursorToCidade(Cursor cursor) {

        City ret = new City();
        ret.setNome(cursor.getString(cursor.getColumnIndex(FIELD_NAME)));
        ret.setID(cursor.getInt(cursor.getColumnIndex(FIELD_ID)));

        // Log.d(LOG_TAG, "read: " + ret.toJSON().toString());
        return ret;
    }

    private String getEmptyStringFromNULL(String s) {
        return s == null ? "" : s;
    }
}
