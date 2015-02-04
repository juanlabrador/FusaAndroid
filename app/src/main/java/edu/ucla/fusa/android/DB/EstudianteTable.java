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
    private static String COLUMN_EDAD = "edad";
    private static String COLUMN_FECHA_NACIMIENTO = "fecha_nac";
    private static String COLUMN_SEXO = "sexo";
    private static String COLUMN_TELF_FIJO = "telefono_fijo";
    private static String COLUMN_TELF_MOVIL = "telefono_movil";
    private static String COLUMN_FOTO = "foto";

    public static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " ("
            + COLUMN_ID + " INTEGER PRIMARY KEY, "
            + COLUMN_NOMBRE + " TEXT, "
            + COLUMN_APELLIDO + " TEXT, "
            + COLUMN_CEDULA + " TEXT, "
            + COLUMN_CORREO + " TEXT, "
            + COLUMN_EDAD + " INTEGER, "
            + COLUMN_FECHA_NACIMIENTO + " TEXT, "
            + COLUMN_SEXO + " TEXT, "
            + COLUMN_TELF_FIJO + " TEXT, "
            + COLUMN_TELF_MOVIL + " TEXT, "
            + COLUMN_FOTO + " BLOB);";

    private DataBaseHelper mHelper;
    private SQLiteDatabase mDataBase;
    private Cursor mCursor;
    private SimpleDateFormat mDateFormat = new SimpleDateFormat("dd-MM-yyyy");
    private Estudiante mEstudiante = new Estudiante();

    public EstudianteTable(Context context) {
        mHelper = DataBaseHelper.getInstance(context);
        mDataBase = mHelper.getWritableDatabase();
    }

    private ContentValues generarValores (int id, String nombre, String apellido, String cedula, String correo,
                                         int edad, String fechaNac, String sexo,
                                          String telfFijo, String telfMovil, byte[] foto) {

        ContentValues valores = new ContentValues();
        valores.put(COLUMN_ID, id);
        valores.put(COLUMN_NOMBRE, nombre);
        valores.put(COLUMN_APELLIDO, apellido);
        valores.put(COLUMN_CEDULA, cedula);
        valores.put(COLUMN_CORREO, correo);
        valores.put(COLUMN_EDAD, edad);
        valores.put(COLUMN_FECHA_NACIMIENTO, fechaNac);
        valores.put(COLUMN_SEXO, sexo);
        valores.put(COLUMN_TELF_FIJO, telfFijo);
        valores.put(COLUMN_TELF_MOVIL, telfMovil);
        valores.put(COLUMN_FOTO, foto);

        return valores;
    }

    public void insertData(int id, String nombre, String apellido, String cedula, String correo,
                           int edad, String fechaNac, String sexo,
                           String telfFijo, String telfMovil, byte[] foto) {
        //db = helper.getWritableDatabase();
        mDataBase.insert(TABLE_NAME, null, generarValores(id, nombre, apellido, cedula, correo,
                edad, fechaNac, sexo, telfFijo, telfMovil, foto));
    }

    public void updateFoto(int id, byte[] foto) {
        ContentValues valores = new ContentValues();
        valores.put(COLUMN_FOTO, foto);
        String[] condicion = new String[]{String.valueOf(id)};
        mDataBase.update(TABLE_NAME, valores, COLUMN_ID + "=?", condicion);
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
            mEstudiante.setEdad(mCursor.getInt(5));
            mEstudiante.setFechanac(mCursor.getString(6));
            mEstudiante.setSexo(mCursor.getString(7));
            mEstudiante.setTelefonoFijo(mCursor.getString(8));
            mEstudiante.setTelefonoMovil(mCursor.getString(9));
            mEstudiante.setImagen(mCursor.getBlob(10));

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
