package edu.ucla.fusa.android.DB;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import edu.ucla.fusa.android.modelo.academico.LapsoAcademico;

/**
 * Created by juanlabrador on 26/01/15.
 */
public class LapsoAcademicoTable {

    private static final String TAG = "DiaTable";
    public static final String TABLE_NAME = "lapso_academico";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_LAPSO = "lapso";
    public static final String COLUMN_ID_LAPSO = "lapso_id";

    public static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " ("
            + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + COLUMN_LAPSO + " INTEGER, "
            + COLUMN_ID_LAPSO + " INTEGER);";

    private DataBaseHelper mHelper;
    private SQLiteDatabase mDatabase;
    private Cursor mCursor;
    private LapsoAcademico mLapsoAcademico;

    public LapsoAcademicoTable(Context context) {
        mHelper = DataBaseHelper.getInstance(context);
        mDatabase = mHelper.getWritableDatabase();
        mLapsoAcademico = new LapsoAcademico();
    }

    private ContentValues generarValores (int lapso, int id) {

        ContentValues valores = new ContentValues();
        valores.put(COLUMN_ID_LAPSO, id);
        valores.put(COLUMN_LAPSO, lapso);

        return valores;
    }

    public void insertData(int id, int lapso) {
        mDatabase.insert(TABLE_NAME, null, generarValores(lapso, id));
    }
    public LapsoAcademico search(int id) {
        Log.i(TAG, "¡Buscando el dia!");
        String[] condicion = new String[]{String.valueOf(id)};
        String[] columnas = new String[]{COLUMN_ID_LAPSO, COLUMN_LAPSO};
        mCursor = mDatabase.query(TABLE_NAME, columnas, COLUMN_ID_LAPSO + "=?", condicion, null, null, null, null);
        if (mCursor.moveToFirst()) {
            mLapsoAcademico.setId(mCursor.getInt(0));
            mLapsoAcademico.setLapso(mCursor.getInt(1));
            Log.i(TAG, "¡Encontrado!");
            return mLapsoAcademico;
        }
        return null;
    }

    public void destroyTable() {
        mDatabase.execSQL("DROP TABLE IF EXISTS "+ TABLE_NAME);
    }

}
