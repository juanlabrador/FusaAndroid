package edu.ucla.fusa.android.DB;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import edu.ucla.fusa.android.modelo.academico.Agrupacion;
import edu.ucla.fusa.android.modelo.academico.Dia;
import edu.ucla.fusa.android.modelo.academico.Horario;
import edu.ucla.fusa.android.modelo.academico.HorarioArea;
import edu.ucla.fusa.android.modelo.academico.Instructor;
import edu.ucla.fusa.android.modelo.academico.TipoAgrupacion;

/**
 * Created by juanlabrador on 25/01/15.
 */
public class AgrupacionTable {

    private static final String TAG = "AgrupacionTable";
    public static final String TABLE_NAME = "agrupacion";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_NOMBRE = "nombre";
    public static final String COLUMN_TIPO_AGRUPACION = "tipo";
    public static final String COLUMN_INSTRUCTOR = "instructor";
    public static final String COLUMN_ID_AGRUPACION = "agrupacion_id";

    public static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " ("
            + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + COLUMN_NOMBRE + " TEXT, "
            + COLUMN_TIPO_AGRUPACION + " TEXT, "
            + COLUMN_INSTRUCTOR + " INTEGER, "
            + COLUMN_ID_AGRUPACION + " INTEGER);";

    private DataBaseHelper mHelper;
    private SQLiteDatabase mDatabase;
    private Cursor mCursor;
    private Agrupacion mAgrupacion;
    private TipoAgrupacion mTipoAgrupacion;
    private Instructor mInstructor;
    private HorarioArea mHorarioArea;
    private List<HorarioArea> mHorarioAreas;
    private Horario mHorario;
    private Dia mDia;
    private InstructorTable mInstructorTable;
    private HorarioTable mHorarioTable;
    private List<Horario> mHorarios;
    private HorarioAreaTable mHorarioAreaTable;

    public AgrupacionTable(Context context) {
        mHelper = DataBaseHelper.getInstance(context);
        mDatabase = mHelper.getWritableDatabase();
        mAgrupacion = new Agrupacion();
        mTipoAgrupacion = new TipoAgrupacion();
        mInstructor = new Instructor();
        mHorarioArea = new HorarioArea();
        mHorario = new Horario();
        mDia = new Dia();
        mInstructorTable = new InstructorTable(context);
        mHorarioTable = new HorarioTable(context);
        mHorarioAreas = new ArrayList<>();
        mHorarioAreaTable = new HorarioAreaTable(context);
    }

    private ContentValues generarValores (String nombre, String tipo, int instructor, int id) {

        ContentValues valores = new ContentValues();
        valores.put(COLUMN_ID_AGRUPACION, id);
        valores.put(COLUMN_NOMBRE, nombre);
        valores.put(COLUMN_INSTRUCTOR, instructor);
        valores.put(COLUMN_TIPO_AGRUPACION, tipo);

        return valores;
    }

    public void insertData(String nombre, String tipo, int instructor, int id) {
        mDatabase.insert(TABLE_NAME, null, generarValores(nombre, tipo, instructor, id));
    }
    public Agrupacion searchAgrupacion() {
        mHorarioAreas.clear();
        Log.i(TAG, "¡Buscando el clase!");
        String[] columnas = new String[]{COLUMN_ID_AGRUPACION, COLUMN_NOMBRE,  COLUMN_INSTRUCTOR, COLUMN_TIPO_AGRUPACION};
        mCursor = mDatabase.query(TABLE_NAME, columnas, null, null, null, null, null, null);
        if (mCursor.moveToFirst()) {
            mAgrupacion.setId(mCursor.getInt(0));
            mAgrupacion.setDescripcion(mCursor.getString(1));
            mTipoAgrupacion.setDescripcion(mCursor.getString(3));
            mAgrupacion.setTipoAgrupacion(mTipoAgrupacion);
            
            mHorarioAreas = mHorarioAreaTable.searchHorarioPorAgrupacion(mCursor.getInt(0));

            mAgrupacion.setHorarioArea(mHorarioAreas);
            mInstructor = mInstructorTable.searchInstructor(mCursor.getInt(2));
            mAgrupacion.setInstructor(mInstructor);

            Log.i(TAG, "¡Encontrado!");
            return mAgrupacion;
        }
        return null;
    }

    public void destroyTable() {
        mDatabase.execSQL("DROP TABLE IF EXISTS "+ TABLE_NAME);
    }

}
