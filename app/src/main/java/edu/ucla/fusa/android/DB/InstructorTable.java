package edu.ucla.fusa.android.DB;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import edu.ucla.fusa.android.modelo.academico.Instructor;

/**
 * Created by juanlabrador on 25/01/15.
 */
public class InstructorTable {

    private static final String TAG = "InstructorTable";
    public static final String TABLE_NAME = "instructor";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_NOMBRE = "nombre";
    public static final String COLUMN_APELLIDO = "apellido";
    public static final String COLUMN_CORREO = "correo";
    public static final String COLUMN_TELEFONO = "telefono";
    public static final String COLUMN_TELEFONO_FIJO = "fijo";
    public static final String COLUMN_FOTO = "foto";
    public static final String COLUMN_ID_INSTRUCTOR = "instructor_id";

    public static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " ("
            + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + COLUMN_NOMBRE + " TEXT, "
            + COLUMN_APELLIDO + " TEXT, "
            + COLUMN_CORREO + " TEXT, "
            + COLUMN_TELEFONO + " TEXT, "
            + COLUMN_TELEFONO_FIJO + " TEXT, "
            + COLUMN_FOTO + " BLOB, "
            + COLUMN_ID_INSTRUCTOR + " INTEGER);";

    private DataBaseHelper mHelper;
    private SQLiteDatabase mDatabase;
    private Cursor mCursor;
    private Instructor mInstructor;

    public InstructorTable(Context context) {
        mHelper = DataBaseHelper.getInstance(context);
        mDatabase = mHelper.getWritableDatabase();
        mInstructor = new Instructor();
    }

    private ContentValues generarValores (int id, String nombre, String apellido, String correo, 
                                          String telefono, String fijo, byte[] foto) {

        ContentValues valores = new ContentValues();
        valores.put(COLUMN_ID_INSTRUCTOR, id);
        valores.put(COLUMN_NOMBRE, nombre);
        valores.put(COLUMN_APELLIDO, apellido);
        valores.put(COLUMN_CORREO, correo);
        valores.put(COLUMN_TELEFONO, telefono);
        valores.put(COLUMN_TELEFONO_FIJO, fijo);
        valores.put(COLUMN_FOTO, foto);

        return valores;
    }

    public void insertData(int id, String nombre, String apellido, String correo,
                           String telefono, String fijo, byte[] foto) {
        mDatabase.insert(TABLE_NAME, null, generarValores(id, nombre, apellido, correo, telefono, fijo, foto));
    }
    public Instructor searchInstructor(int id) {
        Log.i(TAG, "¡Buscando el instructor!");
        String[] condicion = new String[]{String.valueOf(id)};
        String[] columnas = new String[]{COLUMN_ID_INSTRUCTOR, COLUMN_NOMBRE, COLUMN_APELLIDO, COLUMN_CORREO, COLUMN_TELEFONO, COLUMN_TELEFONO_FIJO, COLUMN_FOTO};
        mCursor = mDatabase.query(TABLE_NAME, columnas, COLUMN_ID_INSTRUCTOR + "=?", condicion, null, null, null, null);
        if (mCursor.moveToFirst()) {
            mInstructor.setId(mCursor.getInt(0));
            mInstructor.setNombre(mCursor.getString(1));
            mInstructor.setApellido(mCursor.getString(2));
            mInstructor.setCorreo(mCursor.getString(3));
            mInstructor.setTelefonoMovil(mCursor.getString(4));
            mInstructor.setTelefonoFijo(mCursor.getString(5));
            mInstructor.setImagen(mCursor.getBlob(6));
            Log.i(TAG, "¡Encontrado!");
            return mInstructor;
        }
        return null;
    }

    public void destroyTable() {
        mDatabase.execSQL("DROP TABLE IF EXISTS "+ TABLE_NAME);
    }

}
