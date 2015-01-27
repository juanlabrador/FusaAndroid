package edu.ucla.fusa.android.DB;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import edu.ucla.fusa.android.modelo.academico.AreaEstudio;
import edu.ucla.fusa.android.modelo.evento.Lugar;

/**
 * Created by juanlabrador on 25/01/15.
 */
public class AreaEstudioTable {

    private static final String TAG = "AreaEstudioTable";
    public static final String TABLE_NAME = "area_estudio";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_AREA = "area";
    public static final String COLUMN_ID_LUGAR = "area_id";

    public static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " ("
            + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + COLUMN_AREA + " TEXT, "
            + COLUMN_ID_LUGAR + " INTEGER);";

    private DataBaseHelper mHelper;
    private SQLiteDatabase mDatabase;
    private Cursor mCursor;
    private AreaEstudio mAreaEstudio;

    public AreaEstudioTable(Context context) {
        mHelper = DataBaseHelper.getInstance(context);
        mDatabase = mHelper.getWritableDatabase();
        mAreaEstudio = new AreaEstudio();
    }

    private ContentValues generarValores (String area, int id) {

        ContentValues valores = new ContentValues();
        valores.put(COLUMN_ID_LUGAR, id);
        valores.put(COLUMN_AREA, area);

        return valores;
    }

    public void insertData(String area, int id) {
        mDatabase.insert(TABLE_NAME, null, generarValores(area, id));
    }
    public AreaEstudio searchArea(int id) {
        Log.i(TAG, "¡Buscando el Area!");
        String[] condicion = new String[]{String.valueOf(id)};
        String[] columnas = new String[]{COLUMN_ID_LUGAR, COLUMN_AREA};
        mCursor = mDatabase.query(TABLE_NAME, columnas, COLUMN_ID_LUGAR + "=?", condicion, null, null, null, null);
        if (mCursor.moveToFirst()) {
            mAreaEstudio.setId(mCursor.getInt(0));
            mAreaEstudio.setDescripcion(mCursor.getString(1));
            Log.i(TAG, "¡Encontrado!");
            return mAreaEstudio;
        }
        return null;
    }

    public void destroyTable() {
        mDatabase.execSQL("DROP TABLE IF EXISTS "+ TABLE_NAME);
    }

}
