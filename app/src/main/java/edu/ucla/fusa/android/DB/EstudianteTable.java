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
    private static String COLUMN_BECADO = "becado";
    private static String COLUMN_CONSERVATORIO = "inscritoConservatorio";
    private static String COLUMN_CORO = "inscritoCoro";
    private static String COLUMN_INSTRUMENTO = "instrumentoPropio";
    private static String COLUMN_NOMBRE_USUARIO = "username";

    public static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " ("
            + COLUMN_ID + " INTEGER PRIMARY KEY, "
            + COLUMN_NOMBRE + " TEXT, "
            + COLUMN_APELLIDO + " TEXT, "
            + COLUMN_CEDULA + " TEXT, "
            + COLUMN_CORREO + " TEXT, "
            + COLUMN_EDAD + " INTEGER, "
            + COLUMN_FECHA_NACIMIENTO + " DATE, "
            + COLUMN_SEXO + " TEXT, "
            + COLUMN_TELF_FIJO + " TEXT, "
            + COLUMN_TELF_MOVIL + " TEXT, "
            + COLUMN_FOTO + " BLOB, "
            + COLUMN_BECADO + " TEXT, "
            + COLUMN_CONSERVATORIO + " TEXT, "
            + COLUMN_CORO + " TEXT, "
            + COLUMN_INSTRUMENTO + " TEXT, "
            + COLUMN_NOMBRE_USUARIO + " TEXT);";

    private DataBaseHelper mHelper;
    private SQLiteDatabase mDataBase;
    private Cursor mCursor;
    private SimpleDateFormat mDateFormat = new SimpleDateFormat("dd-MM-yyyy");
    private Estudiante mEstudiante = new Estudiante();
    private UserTable mUserTable;
    private Usuario mUsuario;

    public EstudianteTable(Context context) {
        mHelper = DataBaseHelper.getInstance(context);
        mDataBase = mHelper.getWritableDatabase();
        mUserTable = new UserTable(context);
    }

    private ContentValues generarValores (int id, String nombre, String apellido, String cedula, String correo,
                                         int edad, Date fechaNac, String sexo,
                                          String telfFijo, String telfMovil, byte[] foto, String becado,
                                          String conservatorio, String coro, String instrumento, String username) {

        ContentValues valores = new ContentValues();
        valores.put(COLUMN_ID, id);
        valores.put(COLUMN_NOMBRE, nombre);
        valores.put(COLUMN_APELLIDO, apellido);
        valores.put(COLUMN_CEDULA, cedula);
        valores.put(COLUMN_CORREO, correo);
        valores.put(COLUMN_EDAD, edad);
        valores.put(COLUMN_FECHA_NACIMIENTO, mDateFormat.format(fechaNac));
        valores.put(COLUMN_SEXO, sexo);
        valores.put(COLUMN_TELF_FIJO, telfFijo);
        valores.put(COLUMN_TELF_MOVIL, telfMovil);
        valores.put(COLUMN_FOTO, foto);
        valores.put(COLUMN_BECADO, becado);
        valores.put(COLUMN_CONSERVATORIO, conservatorio);
        valores.put(COLUMN_CORO, coro);
        valores.put(COLUMN_INSTRUMENTO, instrumento);
        valores.put(COLUMN_NOMBRE_USUARIO, username);

        return valores;
    }

    public void insertData(int id, String nombre, String apellido, String cedula, String correo,
                           int edad, Date fechaNac, String sexo,
                           String telfFijo, String telfMovil, byte[] foto, String becado,
                           String conservatorio, String coro, String instrumento, String username) {
        //db = helper.getWritableDatabase();
        mDataBase.insert(TABLE_NAME, null, generarValores(id, nombre, apellido, cedula, correo,
                edad, fechaNac, sexo, telfFijo, telfMovil, foto, becado,
                conservatorio, coro, instrumento, username));
    }
    public Estudiante searchUser() {
        try {
            mUsuario = mUserTable.searchUser();
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
                mEstudiante.setFechanac(mDateFormat.parse(mCursor.getString(6)));
                mEstudiante.setSexo(mCursor.getString(7));
                mEstudiante.setTelefonoFijo(mCursor.getString(8));
                mEstudiante.setTelefonoMovil(mCursor.getString(9));
                mEstudiante.setImagen(mCursor.getBlob(10));
                mEstudiante.setBecado(mCursor.getString(11));
                mEstudiante.setInscritoConservatorio(mCursor.getString(12));
                mEstudiante.setInscritoCoro(mCursor.getString(13));
                mEstudiante.setInstrumentoPropio(mCursor.getString(14));
                mEstudiante.setUsuario(mUsuario);

            } else {
                mEstudiante = null;
            }
            return mEstudiante;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void destroyTable() {
        //db = helper.getWritableDatabase();
        mDataBase.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
    }
}
