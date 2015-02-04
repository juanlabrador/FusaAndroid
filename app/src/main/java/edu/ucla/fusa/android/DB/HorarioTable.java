package edu.ucla.fusa.android.DB;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import edu.ucla.fusa.android.modelo.academico.Dia;
import edu.ucla.fusa.android.modelo.academico.Horario;

/**
 * Created by juanlabrador on 25/01/15.
 */
public class HorarioTable {

    private static final String TAG = "HorarioTable";
    public static final String TABLE_NAME = "horario";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_DIA = "dia";
    public static final String COLUMN_HORA_INICIAL = "hora_inicial";
    public static final String COLUMN_HORA_FINAL = "hora_final";
    public static final String COLUMN_ID_HORARIO = "horario_id";
    public static final String COLUMN_ID_HORARIO_AREA = "horario_area_id";

    public static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " ("
            + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + COLUMN_DIA + " INTEGER, "
            + COLUMN_HORA_INICIAL + " TEXT, "
            + COLUMN_HORA_FINAL + " TEXT, "
            + COLUMN_ID_HORARIO + " INTEGER, "
            + COLUMN_ID_HORARIO_AREA + " INTEGER);";

    private DataBaseHelper mHelper;
    private SQLiteDatabase mDatabase;
    private Cursor mCursor;
    private Horario mHorario;
    private Dia mDia;
    private DiaTable mDiaTable;

    public HorarioTable(Context context) {
        mHelper = DataBaseHelper.getInstance(context);
        mDatabase = mHelper.getWritableDatabase();
        mDiaTable = new DiaTable(context);
    }

    private ContentValues generarValores (int id, int dia, String hora_i, String hora_f, int id_horario_area) {

        ContentValues valores = new ContentValues();
        valores.put(COLUMN_ID_HORARIO, id);
        valores.put(COLUMN_DIA, dia);
        valores.put(COLUMN_HORA_INICIAL, hora_i);
        valores.put(COLUMN_HORA_FINAL, hora_f);
        valores.put(COLUMN_ID_HORARIO_AREA, id_horario_area);

        return valores;
    }

    public void insertData(int id, int dia, String hora_i, String hora_f, int id_horario_area) {
        mDatabase.insert(TABLE_NAME, null, generarValores(id, dia, hora_i, hora_f, id_horario_area));
    }
    
    public Horario searchHorario(int id) {
        Log.i(TAG, "¡Buscando el horario!");
        Log.i(TAG, "ID Horario Area: " + id);
        String[] condicion = new String[]{String.valueOf(id)};
        String[] columnas = new String[]{COLUMN_ID_HORARIO, COLUMN_DIA, COLUMN_HORA_INICIAL, COLUMN_HORA_FINAL, COLUMN_ID_HORARIO_AREA};
        mCursor = mDatabase.query(TABLE_NAME, columnas, COLUMN_ID_HORARIO_AREA + "=?", condicion, null, null, null, null);
        if (mCursor.moveToFirst()) {
            mHorario = new Horario();
            mDia = null;
            mHorario.setHorario_id(mCursor.getInt(0));
            mDia = mDiaTable.search(mCursor.getInt(1));
            Log.i(TAG, "Dia en Horario: " + mDia.getDescripcion());
            mHorario.setDia(mDia);
            mHorario.setHoraInicio(mCursor.getString(2));
            mHorario.setHoraFin(mCursor.getString(3));
            Log.i(TAG, "¡Encontrado!");
            return mHorario;
        }
        return null;
    }

    public void destroyTable() {
        mDatabase.execSQL("DROP TABLE IF EXISTS "+ TABLE_NAME);
    }

}
