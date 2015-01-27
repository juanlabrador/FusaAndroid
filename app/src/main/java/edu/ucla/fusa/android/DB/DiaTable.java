package edu.ucla.fusa.android.DB;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import edu.ucla.fusa.android.modelo.academico.Dia;

/**
 * Created by juanlabrador on 26/01/15.
 */
public class DiaTable {

    private static final String TAG = "DiaTable";
    public static final String TABLE_NAME = "dia";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_DESCRIPCION = "descripcion";
    public static final String COLUMN_ID_DIA = "dia_id";

    public static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " ("
            + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + COLUMN_DESCRIPCION + " TEXT, "
            + COLUMN_ID_DIA + " INTEGER);";

    private DataBaseHelper mHelper;
    private SQLiteDatabase mDatabase;
    private Cursor mCursor;
    private Dia mDia;

    public DiaTable(Context context) {
        mHelper = DataBaseHelper.getInstance(context);
        mDatabase = mHelper.getWritableDatabase();
        mDia = new Dia();
    }

    private ContentValues generarValores (String descripcion, int id) {

        ContentValues valores = new ContentValues();
        valores.put(COLUMN_ID_DIA, id);
        valores.put(COLUMN_DESCRIPCION, descripcion);

        return valores;
    }

    public void insertData(int id, String descripcion) {
        mDatabase.insert(TABLE_NAME, null, generarValores(descripcion, id));
    }
    public Dia search(int id) {
        Log.i(TAG, "¡Buscando el dia!");
        String[] condicion = new String[]{String.valueOf(id)};
        String[] columnas = new String[]{COLUMN_ID_DIA, COLUMN_DESCRIPCION};
        mCursor = mDatabase.query(TABLE_NAME, columnas, COLUMN_ID_DIA + "=?", condicion, null, null, null, null);
        if (mCursor.moveToFirst()) {
            mDia.setDia_id(mCursor.getInt(0));
            mDia.setDescripcion(mCursor.getString(1));
            Log.i(TAG, "¡Encontrado!");
            return mDia;
        }
        return null;
    }

    public void destroyTable() {
        mDatabase.execSQL("DROP TABLE IF EXISTS "+ TABLE_NAME);
    }

}
