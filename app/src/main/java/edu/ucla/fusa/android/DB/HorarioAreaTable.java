package edu.ucla.fusa.android.DB;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import edu.ucla.fusa.android.modelo.academico.Horario;
import edu.ucla.fusa.android.modelo.academico.HorarioArea;

/**
 * Created by juanlabrador on 26/01/15.
 */
public class HorarioAreaTable {

    private static final String TAG = "HorarioAreaTable";
    public static final String TABLE_NAME = "horario_area";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_CLASE = "clase";
    public static final String COLUMN_AGRUPACION = "agrupacion";
    public static final String COLUMN_ID_HORARIO_CLASE = "horario_clase_id";

    public static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " ("
            + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + COLUMN_CLASE + " INTEGER, "
            + COLUMN_AGRUPACION + " INTEGER, "
            + COLUMN_ID_HORARIO_CLASE + " INTEGER);";

    private DataBaseHelper mHelper;
    private SQLiteDatabase mDatabase;
    private Cursor mCursor;
    private List<HorarioArea> mHorarioAreas;
    private HorarioArea mHorarioArea;
    private HorarioTable mHorarioTable;
    private Horario mHorario;

    public HorarioAreaTable(Context context) {
        mHelper = DataBaseHelper.getInstance(context);
        mDatabase = mHelper.getWritableDatabase();
        mHorarioAreas = new ArrayList<>();
        mHorarioArea = new HorarioArea();
        mHorarioTable = new HorarioTable(context);
        mHorario = new Horario();
    }

    private ContentValues generarValores (int clase, int agrupacion, int id) {

        ContentValues valores = new ContentValues();
        valores.put(COLUMN_ID_HORARIO_CLASE, id);
        valores.put(COLUMN_CLASE, clase);
        valores.put(COLUMN_AGRUPACION, agrupacion);

        return valores;
    }

    public void insertData(int clase, int agrupacion, int id) {
        mDatabase.insert(TABLE_NAME, null, generarValores(clase, agrupacion, id));
    }
    
    public List<HorarioArea> searchHorarioPorAgrupacion(int id) {
        mHorarioAreas.clear();
        Log.i(TAG, "¡Buscando el horario area!");
        String[] condicion = new String[]{String.valueOf(id)};
        String[] columnas = new String[]{COLUMN_ID_HORARIO_CLASE, COLUMN_AGRUPACION};
        mCursor = mDatabase.query(TABLE_NAME, columnas, COLUMN_AGRUPACION + "=?", condicion, null, null, null, null);
        while (mCursor.moveToNext()) {
            mHorarioArea.setId(mCursor.getInt(0));
            mHorario = mHorarioTable.searchHorario(mCursor.getInt(0));
            mHorarioArea.setHorario(mHorario);
            mHorarioAreas.add(mHorarioArea);
            Log.i(TAG, "¡Encontrado!");
        }
        return mHorarioAreas;
    }

    public List<HorarioArea> searchHorarioPorClase(int id) {
        mHorarioAreas.clear();
        Log.i(TAG, "¡Buscando el horario area!");
        String[] condicion = new String[]{String.valueOf(id)};
        String[] columnas = new String[]{COLUMN_ID_HORARIO_CLASE, COLUMN_CLASE};
        mCursor = mDatabase.query(TABLE_NAME, columnas, COLUMN_CLASE + "=?", condicion, null, null, null, null);
        while (mCursor.moveToNext()) {
            mHorarioArea.setId(mCursor.getInt(0));
            mHorario = mHorarioTable.searchHorario(mCursor.getInt(0));
            mHorarioArea.setHorario(mHorario);
            mHorarioAreas.add(mHorarioArea);
            Log.i(TAG, "¡Encontrado!");
        }
        return mHorarioAreas;
    }

    public void destroyTable() {
        mDatabase.execSQL("DROP TABLE IF EXISTS "+ TABLE_NAME);
    }

}
