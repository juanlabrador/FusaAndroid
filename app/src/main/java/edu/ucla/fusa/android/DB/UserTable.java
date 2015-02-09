package edu.ucla.fusa.android.DB;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import edu.ucla.fusa.android.modelo.seguridad.TipoUsuario;
import edu.ucla.fusa.android.modelo.seguridad.Usuario;

/**
 * Created by juanlabrador on 19/11/14.
 */
public class UserTable {

    public static final String TABLE_NAME = "usuario";
    private static final String COLUMN_ID_USER = "id_usuario";
    private static final String COLUMN_USERNAME = "username";
    private static final String COLUMN_PASSWORD = "password";
    private static final String COLUMN_NOMBRE = "nombre";
    private static final String COLUMN_APELLIDO = "apellido";
    private static final String COLUMN_CORREO = "correo";
    private static final String COLUMN_PHOTO = "foto";
    private static final String COLUMN_TIPO_USER = "tipo_usuario";

    public static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " ("
                    + COLUMN_ID_USER + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + COLUMN_USERNAME + " TEXT, "
                    + COLUMN_PASSWORD + " TEXT, "
                    + COLUMN_NOMBRE + " TEXT, "
                    + COLUMN_APELLIDO + " TEXT, "
                    + COLUMN_CORREO + " TEXT, "
                    + COLUMN_PHOTO + " BLOB, "
                    + COLUMN_TIPO_USER + " INTEGER);";

    private DataBaseHelper mHelper;
    private SQLiteDatabase mDataBase;
    private Cursor mCursor;
    private Usuario mUsuario = new Usuario();

    public UserTable(Context context) {
        mHelper = DataBaseHelper.getInstance(context);
        mDataBase = mHelper.getWritableDatabase();
    }

    private ContentValues generarValores (String username, String password, String nombre, String apellido, String correo, byte[] foto, int tipoUsuario) {

        ContentValues valores = new ContentValues();
        valores.put(COLUMN_USERNAME, username);
        valores.put(COLUMN_PASSWORD, password);
        valores.put(COLUMN_NOMBRE, nombre);
        valores.put(COLUMN_APELLIDO, apellido);
        valores.put(COLUMN_CORREO, correo);
        valores.put(COLUMN_PHOTO, foto);
        valores.put(COLUMN_TIPO_USER, tipoUsuario);

        return valores;
    }

    public void insertData(String username, String password, String nombre, String apellido, String correo, byte[] foto, int tipoUsuario) {
        //db = helper.getWritableDatabase();
        mDataBase.insert(TABLE_NAME, null, generarValores(username, password, nombre, apellido, correo, foto, tipoUsuario));
    }

    public void updateFoto(String username, byte[] foto) {
        ContentValues valores = new ContentValues();
        valores.put(COLUMN_PHOTO, foto);
        String[] condicion = new String[]{String.valueOf(username)};
        mDataBase.update(TABLE_NAME, valores, COLUMN_USERNAME + "=?", condicion);
    }

    public void updatePassword(String username, String pass) {
        ContentValues valores = new ContentValues();
        valores.put(COLUMN_PASSWORD, pass);
        String[] condicion = new String[]{String.valueOf(username)};
        mDataBase.update(TABLE_NAME, valores, COLUMN_USERNAME + "=?", condicion);
    }

    public Usuario searchUser() {
        String tiraSQL = "SELECT * FROM " + TABLE_NAME;
        //db = helper.getReadableDatabase();
        mCursor = mDataBase.rawQuery(tiraSQL, null);
        if (mCursor.moveToFirst()) {
            mUsuario.setUsername(mCursor.getString(1));
            mUsuario.setPassword(mCursor.getString(2));
            mUsuario.setNombre(mCursor.getString(3));
            mUsuario.setApellido(mCursor.getString(4));
            mUsuario.setCorreo(mCursor.getString(5));
            mUsuario.setFoto(mCursor.getBlob(6));
            mUsuario.setTipoUsuario(
                    new TipoUsuario(mCursor.getInt(7), "Estudiante", "activo"));
        } else {
            mUsuario = null;
        }
        return mUsuario;
    }

    public void destroyTable() {
        //db = helper.getWritableDatabase();
        mDataBase.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
    }
}
