package edu.ucla.fusa.android.DB;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import edu.ucla.fusa.android.modelo.academico.Estudiante;
import edu.ucla.fusa.android.modelo.academico.Usuario;

/**
 * Created by juanlabrador on 10/01/15.
 */
public class EstudianteTable {

    private static String TABLE_NAME = "estudiante";
    private static String COLUMN_ID = "id";
    private static String COLUMN_NOMBRE = "nombre";
    private static String COLUMN_APELLIDO = "apellido";
    private static String COLUMN_CEDULA = "cedula";
    private static String COLUMN_CORREO = "correo";
    private static String COLUMN_DIRECCION = "direccion";
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

    public static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " ("
            + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + COLUMN_NOMBRE + " TEXT, "
            + COLUMN_APELLIDO + " TEXT, "
            + COLUMN_CEDULA + " TEXT, "
            + COLUMN_CORREO + " TEXT, "
            + COLUMN_DIRECCION + " TEXT, "
            + COLUMN_EDAD + " INTEGER, "
            + COLUMN_FECHA_NACIMIENTO + " DATE, "
            + COLUMN_SEXO + " TEXT, "
            + COLUMN_TELF_FIJO + " TEXT, "
            + COLUMN_TELF_MOVIL + " TEXT, "
            + COLUMN_FOTO + " BLOB, "
            + COLUMN_BECADO + " TEXT, "
            + COLUMN_CONSERVATORIO + " TEXT, "
            + COLUMN_CORO + " TEXT, "
            + COLUMN_INSTRUMENTO + " TEXT);";

    private DataBaseHelper helper;
    private SQLiteDatabase db;
    private Cursor cursor;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MMMM/yyyy");
    private Estudiante estudiante = new Estudiante();

    public EstudianteTable(Context context) {
        helper = new DataBaseHelper(context);
        db = helper.getWritableDatabase();
    }

    private ContentValues generarValores (String nombre, String apellido, String cedula, String correo,
                                          String direccion, int edad, Date fechaNac, String sexo,
                                          String telfFijo, String telfMovil, byte[] foto, String becado,
                                          String conservatorio, String coro, String instrumento) {

        ContentValues valores = new ContentValues();
        valores.put(COLUMN_NOMBRE, nombre);
        valores.put(COLUMN_APELLIDO, apellido);
        valores.put(COLUMN_CEDULA, cedula);
        valores.put(COLUMN_CORREO, correo);
        valores.put(COLUMN_DIRECCION, direccion);
        valores.put(COLUMN_EDAD, edad);
        valores.put(COLUMN_FECHA_NACIMIENTO, dateFormat.format(fechaNac));
        valores.put(COLUMN_SEXO, sexo);
        valores.put(COLUMN_TELF_FIJO, telfFijo);
        valores.put(COLUMN_TELF_MOVIL, telfMovil);
        valores.put(COLUMN_FOTO, foto);
        valores.put(COLUMN_BECADO, becado);
        valores.put(COLUMN_CONSERVATORIO, conservatorio);
        valores.put(COLUMN_CORO, coro);
        valores.put(COLUMN_INSTRUMENTO, instrumento);

        return valores;
    }

    public void insertData(String nombre, String apellido, String cedula, String correo,
                           String direccion, int edad, Date fechaNac, String sexo,
                           String telfFijo, String telfMovil, byte[] foto, String becado,
                           String conservatorio, String coro, String instrumento) {
        //db = helper.getWritableDatabase();
        db.insert(TABLE_NAME, null, generarValores(nombre, apellido, cedula, correo,
                direccion, edad, fechaNac, sexo, telfFijo, telfMovil, foto, becado,
                conservatorio, coro, instrumento));
    }

    public Estudiante searchUser() throws ParseException {
        String tiraSQL = "SELECT * FROM " + TABLE_NAME;
        //db = helper.getReadableDatabase();
        cursor = db.rawQuery(tiraSQL, null);
        if (cursor.moveToFirst()) {
            estudiante.setId(cursor.getInt(0));
            estudiante.setNombre(cursor.getString(1));
            estudiante.setApellido(cursor.getString(2));
            estudiante.setCedula(cursor.getString(3));
            estudiante.setDireccion(cursor.getString(4));
            estudiante.setEdad(cursor.getInt(5));
            estudiante.setFechanac(dateFormat.parse(cursor.getString(6)));
            estudiante.setSexo(cursor.getString(7));
            estudiante.setTelefonoFijo(cursor.getString(8));
            estudiante.setTelefonoMovil(cursor.getString(9));
            estudiante.setImagen(cursor.getBlob(10));
            estudiante.setBecado(cursor.getString(11));
            estudiante.setInscritoConservatorio(cursor.getString(12));
            estudiante.setInscritoCoro(cursor.getString(13));
            estudiante.setInstrumentoPropio(cursor.getString(14));

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
