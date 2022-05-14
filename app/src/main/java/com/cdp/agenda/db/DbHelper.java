package com.cdp.agenda.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DbHelper extends SQLiteOpenHelper {
    /*
    CREAR LA BD
     */
    private static final int DATABASE_VERSION = 4;
    private static final String DATABASE_NOMBRE = "agenda.db";
    public static final String TABLE_CONTACTOS = "t_contactos";

    public DbHelper(@Nullable Context context) {
        super(context, DATABASE_NOMBRE, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        sqLiteDatabase.execSQL("CREATE TABLE " + TABLE_CONTACTOS + "(" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "titulo TEXT NOT NULL," +
                "hora TEXT NOT NULL, " +
                "fecha TEXT NOT NULL," +
                "direccion TEXT NOT NULL," +
                "descripcion TEXT NOT NULL)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        /*
        se ejecuta cuando se cambia de version
         */
        sqLiteDatabase.execSQL("DROP TABLE " + TABLE_CONTACTOS);
        onCreate(sqLiteDatabase);

    }
}
