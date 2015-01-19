package edu.ucla.fusa.android.DB;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import edu.ucla.fusa.android.modelo.evento.Lugar;

/**
 * Created by juanlabrador on 17/01/15.
 */
public class LugarTable {

    private static final String TAG = "LugarTable";
    public static final String TABLE_NAME = "lugar";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_DESCRIPCION = "descripcion";
    public static final String COLUMN_DIRECCION = "direccion";

    public static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " ("
            + COLUMN_ID + " INTEGER PRIMARY KEY, "
            + COLUMN_DESCRIPCION + " TEXT, "
            + COLUMN_DIRECCION + " TEXT);";

    private DataBaseHelper mHelper;
    private SQLiteDatabase mDatabase;
    private Cursor mCursor;
    private Lugar mLugar;

    public LugarTable(Context context) {
        mHelper = new DataBaseHelper(context);
        mDatabase = mHelper.getWritableDatabase();
        mLugar = new Lugar();
    }

    private ContentValues generarValores (int id, String descripcion, String direccion) {

        ContentValues valores = new ContentValues();
        valores.put(COLUMN_ID, id);
        valores.put(COLUMN_DESCRIPCION, descripcion);
        valores.put(COLUMN_DIRECCION, direccion);

        return valores;
    }

    public void insertData(int id, String descripcion, String direccion) {
        mDatabase.insert(TABLE_NAME, null, generarValores(id, descripcion, direccion));
    }

    public Lugar searchLugar(String id) {
        Log.i(TAG, "¡Buscando el lugar!");
        String[] condicion = new String[] {id};
        String[] columnas = new String[] {COLUMN_ID, COLUMN_DESCRIPCION, COLUMN_DIRECCION};
        mCursor = mDatabase.query(TABLE_NAME, columnas, COLUMN_ID + " =?", condicion, null, null, null, null);
        if (mCursor.moveToFirst()) {
            mLugar.setId(mCursor.getInt(0));
            mLugar.setDescripcion(mCursor.getString(1));
            mLugar.setDireccion(mCursor.getString(2));
        }
        return mLugar;
    }

    public void destroyTable() {
        mDatabase.execSQL("DROP TABLE IF EXISTS "+ TABLE_NAME);
    }

}
