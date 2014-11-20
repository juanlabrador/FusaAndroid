package edu.ucla.fusa.android.DB;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.sql.SQLClientInfoException;
import java.sql.SQLDataException;
import java.sql.SQLException;

import edu.ucla.fusa.android.modelo.academico.Estudiante;
import edu.ucla.fusa.android.modelo.academico.Usuario;

/**
 * Created by juanlabrador on 19/11/14.
 */
public class UserTable {

    public static final String TABLE_NAME = "usuario";
    private static final String COLUMN_ID_USER = "id_usuario";
    private static final String COLUMN_EMAIL = "correo";
    private static final String COLUMN_PASSWORD = "clave";
    private static final String COLUMN_PHOTO = "foto";
    private static final String COLUMN_CEDULA = "cedula";
    private static final String COLUMN_FIRST_NAME = "nombre";
    private static final String COLUMN_LAST_NAME = "apellido";
    private static final String COLUMN_ID_STUDENT = "id_estudiante";

    public static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " ("
                    + COLUMN_ID_USER + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + COLUMN_EMAIL + " TEXT,"
                    + COLUMN_PASSWORD + " TEXT,"
                    + COLUMN_PHOTO + " BLOB,"
                    + COLUMN_ID_STUDENT + " INTEGER,"
                    + COLUMN_CEDULA + " TEXT, "
                    + COLUMN_FIRST_NAME + " TEXT,"
                    + COLUMN_LAST_NAME + " TEXT);";

    private DataBaseHelper helper;
    private SQLiteDatabase db;
    private Cursor cursor;
    private Estudiante estudiante = new Estudiante();

    public UserTable(Context context) {
        helper = new DataBaseHelper(context);
        db = helper.getWritableDatabase();
    }

    private ContentValues generarValores (int idUsuario, String correo, String clave, byte[] foto,
                                          int idEstudiante, String cedula, String nombre, String apellido) {

        ContentValues valores = new ContentValues();
        valores.put(COLUMN_ID_USER, idUsuario);
        valores.put(COLUMN_EMAIL, correo);
        valores.put(COLUMN_PASSWORD, clave);
        valores.put(COLUMN_PHOTO, foto);
        valores.put(COLUMN_ID_STUDENT, idEstudiante);
        valores.put(COLUMN_CEDULA, cedula);
        valores.put(COLUMN_FIRST_NAME, nombre);
        valores.put(COLUMN_LAST_NAME, apellido);

        return valores;
    }

    public void insertData(int idUsuario, String correo, String clave, byte[] foto,
                        int idEstudiante, String cedula, String nombre, String apellido) {
        //db = helper.getWritableDatabase();
        db.insert(TABLE_NAME, null, generarValores(idUsuario, correo, clave, foto, idEstudiante, cedula, nombre, apellido));
    }

    public Estudiante searchUser() {
        String tiraSQL = "SELECT * FROM " + TABLE_NAME;
        //db = helper.getReadableDatabase();
        cursor = db.rawQuery(tiraSQL, null);
        if (cursor.moveToFirst()) {
            estudiante.setId(cursor.getInt(4));
            estudiante.setCedula(cursor.getString(5));
            estudiante.setNombre(cursor.getString(6));
            estudiante.setApellido(cursor.getString(7));
            estudiante.setUsuario(new Usuario(cursor.getInt(0),
                    cursor.getString(1),
                    cursor.getString(2),
                    cursor.getBlob(3)));
        } else {
            estudiante = null;
        }

        return estudiante;
    }

    public void destroyTable() {
        //db = helper.getWritableDatabase();
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
    }
}
