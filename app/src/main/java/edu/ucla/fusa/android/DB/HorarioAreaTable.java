package edu.ucla.fusa.android.DB;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import edu.ucla.fusa.android.modelo.academico.AreaEstudio;
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
    public static final String COLUMN_AREA_ESTUDIO = "area_estudio";
    public static final String COLUMN_AGRUPACION = "agrupacion";
    public static final String COLUMN_ID_HORARIO_CLASE = "horario_clase_id";

    public static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " ("
            + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + COLUMN_CLASE + " INTEGER, "
            + COLUMN_AREA_ESTUDIO + " INTEGER, "
            + COLUMN_AGRUPACION + " INTEGER, "
            + COLUMN_ID_HORARIO_CLASE + " INTEGER);";

    private DataBaseHelper mHelper;
    private SQLiteDatabase mDatabase;
    private Cursor mCursor;
    private List<HorarioArea> mHorarioAreas;
    private HorarioTable mHorarioTable;
    private Horario mHorario;
    private AreaEstudio mAreaEstudio;
    private AreaEstudioTable mAreaEstudioTable;

    public HorarioAreaTable(Context context) {
        mHelper = DataBaseHelper.getInstance(context);
        mDatabase = mHelper.getWritableDatabase();
        mHorarioTable = new HorarioTable(context);
        mAreaEstudioTable = new AreaEstudioTable(context);
        mHorarioAreas = new ArrayList<>();
    }

    private ContentValues generarValores (int clase, int agrupacion, int id, int area) {

        ContentValues valores = new ContentValues();
        valores.put(COLUMN_ID_HORARIO_CLASE, id);
        valores.put(COLUMN_CLASE, clase);
        valores.put(COLUMN_AREA_ESTUDIO, area);
        valores.put(COLUMN_AGRUPACION, agrupacion);

        return valores;
    }

    public void insertData(int clase, int agrupacion, int id, int area) {
        mDatabase.insert(TABLE_NAME, null, generarValores(clase, agrupacion, id, area));
    }
    
    public List<HorarioArea> searchHorarioPorAgrupacion(int id) {
        Log.i(TAG, "¡Buscando el horario area por agrupacion!");
        String[] condicion = new String[]{String.valueOf(id)};
        String[] columnas = new String[]{COLUMN_ID_HORARIO_CLASE, COLUMN_AGRUPACION, COLUMN_AREA_ESTUDIO};
        mCursor = mDatabase.query(TABLE_NAME, columnas, COLUMN_AGRUPACION + "=?", condicion, null, null, null, null);
        while (mCursor.moveToNext()) {
            mHorario = mHorarioTable.searchHorario(mCursor.getInt(0));
            mAreaEstudio = mAreaEstudioTable.searchArea(mCursor.getInt(2));
            Log.i(TAG, "Dia: " + mHorario.getDia().getDescripcion());
            Log.i(TAG, "Hora Inicial: " + mHorario.getHoraInicio());
            Log.i(TAG, "Hora Final: " + mHorario.getHoraFin());
            mHorarioAreas.add(new HorarioArea(
                    mCursor.getInt(0),
                            mHorario,
                        mAreaEstudio));
            mHorario = null;
            Log.i(TAG, "¡Encontrado!");
        }
        for (int i = 0; i < mHorarioAreas.size(); i++) {
            Log.i(TAG, "Dia en el area: " + mHorarioAreas.get(i).getHorario().getDia().getDescripcion());
        }
        return mHorarioAreas;
    }

    public List<HorarioArea> searchHorarioPorClase(int id) {
        mHorarioAreas.clear();
        Log.i(TAG, "¡Buscando el horario area por clase!");
        String[] condicion = new String[]{String.valueOf(id)};
        String[] columnas = new String[]{COLUMN_ID_HORARIO_CLASE, COLUMN_CLASE};
        mCursor = mDatabase.query(TABLE_NAME, columnas, COLUMN_CLASE + "=?", condicion, null, null, null, null);
        while (mCursor.moveToNext()) {
            //mHorarioArea.setId(mCursor.getInt(0));
            mHorario = mHorarioTable.searchHorario(mCursor.getInt(0));
           // mHorarioArea.setHorario(mHorario);
            mAreaEstudio = mAreaEstudioTable.searchArea(mCursor.getInt(0));
           // mHorarioArea.setAreaEstudio(mAreaEstudio);
           // mHorarioAreas.add(mHorarioArea);
            Log.i(TAG, "¡Encontrado!");
        }
        return mHorarioAreas;
    }

    public void destroyTable() {
        mDatabase.execSQL("DROP TABLE IF EXISTS "+ TABLE_NAME);
    }

}
