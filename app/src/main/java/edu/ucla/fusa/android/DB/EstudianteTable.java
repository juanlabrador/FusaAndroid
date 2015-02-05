package edu.ucla.fusa.android.DB;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import edu.ucla.fusa.android.modelo.academico.Estudiante;
import edu.ucla.fusa.android.modelo.seguridad.Usuario;

/**
 * Created by juanlabrador on 10/01/15.
 */
public class EstudianteTable {

    public static String TABLE_NAME = "estudiante";
    private static String COLUMN_ID = "id";
    private static String COLUMN_NOMBRE = "nombre";
    private static String COLUMN_APELLIDO = "apellido";
    private static String COLUMN_CEDULA = "cedula";
    private static String COLUMN_CORREO = "correo";

    public static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " ("
            + COLUMN_ID + " INTEGER PRIMARY KEY, "
            + COLUMN_NOMBRE + " TEXT, "
            + COLUMN_APELLIDO + " TEXT, "
            + COLUMN_CEDULA + " TEXT, "
            + COLUMN_CORREO + " TEXT);";

    private DataBaseHelper mHelper;
    private SQLiteDatabase mDataBase;
    private Cursor mCursor;
    private Estudiante mEstudiante = new Estudiante();

    public EstudianteTable(Context context) {
        mHelper = DataBaseHelper.getInstance(context);
        mDataBase = mHelper.getWritableDatabase();
    }

    private ContentValues generarValores (int id, String nombre, String apellido, String cedula, String correo) {

        ContentValues valores = new ContentValues();
        valores.put(COLUMN_ID, id);
        valores.put(COLUMN_NOMBRE, nombre);
        valores.put(COLUMN_APELLIDO, apellido);
        valores.put(COLUMN_CEDULA, cedula);
        valores.put(COLUMN_CORREO, correo);

        return valores;
    }

    public void insertData(int id, String nombre, String apellido, String cedula, String correo) {
        //db = helper.getWritableDatabase();
        mDataBase.insert(TABLE_NAME, null, generarValores(id, nombre, apellido, cedula, correo));
    }

    
    public Estudiante searchUser() {
        String tiraSQL = "SELECT * FROM " + TABLE_NAME;
        //db = helper.getReadableDatabase();
        mCursor = mDataBase.rawQuery(tiraSQL, null);
        if (mCursor.moveToFirst()) {
            mEstudiante.setId(mCursor.getInt(0));
            mEstudiante.setNombre(mCursor.getString(1));
            mEstudiante.setApellido(mCursor.getString(2));
            mEstudiante.setCedula(mCursor.getString(3));
            mEstudiante.setCorreo(mCursor.getString(4));

        } else {
            mEstudiante = null;
        }
        return mEstudiante;
    }

    public void destroyTable() {
        //db = helper.getWritableDatabase();
        mDataBase.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
    }
}
