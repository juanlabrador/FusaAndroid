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
    public static final String COLUMN_ID_LUGAR = "lugar_id";

    public static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " ("
            + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + COLUMN_DESCRIPCION + " TEXT, "
            + COLUMN_DIRECCION + " TEXT, "
            + COLUMN_ID_LUGAR + " INTEGER);";

    private DataBaseHelper mHelper;
    private SQLiteDatabase mDatabase;
    private Cursor mCursor;
    private Lugar mLugar;

    public LugarTable(Context context) {
        mHelper = new DataBaseHelper(context);
        mDatabase = mHelper.getWritableDatabase();
        mLugar = new Lugar();
    }

    private ContentValues generarValores (String descripcion, String direccion, int id) {

        ContentValues valores = new ContentValues();
        valores.put(COLUMN_ID_LUGAR, id);
        valores.put(COLUMN_DESCRIPCION, descripcion);
        valores.put(COLUMN_DIRECCION, direccion);

        return valores;
    }

    public void insertData(int id, String descripcion, String direccion) {
        mDatabase.insert(TABLE_NAME, null, generarValores(descripcion, direccion, id));
    }

    public Lugar searchLugar(String id) {
        Log.i(TAG, "¡Buscando el lugar!");
        String[] condicion = new String[] {id};
        String[] columnas = new String[] {COLUMN_ID, COLUMN_DESCRIPCION, COLUMN_DIRECCION, COLUMN_ID_LUGAR};
        mCursor = mDatabase.query(TABLE_NAME, columnas, COLUMN_ID_LUGAR + "=?", condicion, null, null, null, null);
        if (mCursor.moveToFirst()) {
            mLugar.setId(mCursor.getInt(3));
            mLugar.setDescripcion(mCursor.getString(1));
            mLugar.setDireccion(mCursor.getString(2));
        }
        return mLugar;
    }

    public void destroyTable() {
        mDatabase.execSQL("DROP TABLE IF EXISTS "+ TABLE_NAME);
    }

}
