package edu.ucla.fusa.android.DB;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import edu.ucla.fusa.android.modelo.academico.Catedra;
import edu.ucla.fusa.android.modelo.academico.ClaseParticular;
import edu.ucla.fusa.android.modelo.academico.Dia;
import edu.ucla.fusa.android.modelo.academico.Horario;
import edu.ucla.fusa.android.modelo.academico.HorarioArea;
import edu.ucla.fusa.android.modelo.academico.Instructor;

/**
 * Created by juanlabrador on 25/01/15.
 */
public class ClaseParticularTable {

    private static final String TAG = "ClaseParticularTable";
    public static final String TABLE_NAME = "clase_particular";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_CATEDRA = "catedra";
    public static final String COLUMN_INSTRUCTOR = "instructor";
    public static final String COLUMN_ID_CLASE = "clase_id";

    public static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " ("
            + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + COLUMN_CATEDRA + " TEXT, "
            + COLUMN_INSTRUCTOR + " INTEGER, "
            + COLUMN_ID_CLASE + " INTEGER);";

    private DataBaseHelper mHelper;
    private SQLiteDatabase mDatabase;
    private Cursor mCursor;
    private List<ClaseParticular> mClaseParticulares;
    private ClaseParticular mClaseParticular;
    private Catedra mCatedra;
    private Instructor mInstructor;
    private HorarioArea mHorarioArea;
    private List<HorarioArea> mHorarioAreas;
    private Horario mHorario;
    private Dia mDia;
    private InstructorTable mInstructorTable;
    private HorarioAreaTable mHorarioAreaTable;

    public ClaseParticularTable(Context context) {
        mHelper = DataBaseHelper.getInstance(context);
        mDatabase = mHelper.getWritableDatabase();
        mClaseParticulares = new ArrayList<>();
        mCatedra = new Catedra();
        mInstructor = new Instructor();
        mHorarioArea = new HorarioArea();
        mHorario = new Horario();
        mDia = new Dia();
        mInstructorTable = new InstructorTable(context);
        mHorarioAreaTable = new HorarioAreaTable(context);
        mClaseParticular = new ClaseParticular();
        mHorarioAreas = new ArrayList<>();
    }

    private ContentValues generarValores (String catedra, int instructor, int id) {

        ContentValues valores = new ContentValues();
        valores.put(COLUMN_ID_CLASE, id);
        valores.put(COLUMN_INSTRUCTOR, instructor);
        valores.put(COLUMN_CATEDRA, catedra);

        return valores;
    }

    public void insertData(String catedra, int instructor, int id) {
        mDatabase.insert(TABLE_NAME, null, generarValores(catedra, instructor, id));
    }
    public List<ClaseParticular> searchClases() {
        mClaseParticulares.clear();
        mHorarioAreas.clear();
        Log.i(TAG, "¡Buscando el clase!");
        String[] columnas = new String[]{COLUMN_ID_CLASE, COLUMN_INSTRUCTOR, COLUMN_CATEDRA};
        mCursor = mDatabase.query(TABLE_NAME, columnas, null, null, null, null, null, null);
        while (mCursor.moveToNext()) {
            mClaseParticular.setId(mCursor.getInt(0));
            mCatedra.setDescripcion(mCursor.getString(2));
            mClaseParticular.setCatedra(mCatedra);

            mHorarioAreas = mHorarioAreaTable.searchHorarioPorClase(mCursor.getInt(0));
            mClaseParticular.setHorarioArea(mHorarioAreas);
            mInstructor = mInstructorTable.searchInstructor(mCursor.getInt(1));
            mClaseParticular.setInstructor(mInstructor);

            mClaseParticulares.add(mClaseParticular);
            Log.i(TAG, "¡Encontrado!");
            return mClaseParticulares;
        }
        return null;
    }

    public void destroyTable() {
        mDatabase.execSQL("DROP TABLE IF EXISTS "+ TABLE_NAME);
    }

}
