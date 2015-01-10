package edu.ucla.fusa.android.DB;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.sql.SQLClientInfoException;
import java.sql.SQLDataException;
import java.sql.SQLException;

import edu.ucla.fusa.android.modelo.academico.Estudiante;
import edu.ucla.fusa.android.modelo.academico.TipoUsuario;
import edu.ucla.fusa.android.modelo.academico.Usuario;

/**
 * Created by juanlabrador on 19/11/14.
 */
public class UserTable {

    public static final String TABLE_NAME = "usuario";
    private static final String COLUMN_ID_USER = "id_usuario";
    private static final String COLUMN_USERNAME = "username";
    private static final String COLUMN_PASSWORD = "password";
    private static final String COLUMN_PHOTO = "foto";
    private static final String COLUMN_TIPO_USER = "tipo_usuario";

    public static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " ("
                    + COLUMN_ID_USER + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + COLUMN_USERNAME + " TEXT, "
                    + COLUMN_PASSWORD + " TEXT, "
                    + COLUMN_PHOTO + " BLOB, "
                    + COLUMN_TIPO_USER + " INTEGER);";

    private DataBaseHelper helper;
    private SQLiteDatabase db;
    private Cursor cursor;
    private Usuario usuario = new Usuario();

    public UserTable(Context context) {
        helper = new DataBaseHelper(context);
        db = helper.getWritableDatabase();
    }

    private ContentValues generarValores (String username, String password, byte[] foto, int tipoUsuario) {

        ContentValues valores = new ContentValues();
        valores.put(COLUMN_USERNAME, username);
        valores.put(COLUMN_PASSWORD, password);
        valores.put(COLUMN_PHOTO, foto);
        valores.put(COLUMN_TIPO_USER, tipoUsuario);

        return valores;
    }

    public void insertData(String username, String password, byte[] foto, int tipoUsuario) {
        //db = helper.getWritableDatabase();
        db.insert(TABLE_NAME, null, generarValores(username, password, foto, tipoUsuario));
    }

    public Usuario searchUser() {
        String tiraSQL = "SELECT * FROM " + TABLE_NAME;
        //db = helper.getReadableDatabase();
        cursor = db.rawQuery(tiraSQL, null);
        if (cursor.moveToFirst()) {
            usuario.setId(cursor.getInt(0));
            usuario.setNombre(cursor.getString(1));
            usuario.setPassword(cursor.getString(2));
            usuario.setFoto(cursor.getBlob(3));
            usuario.setTipoUsuario(
                    new TipoUsuario(cursor.getInt(4), "", ""));
        } else {
            usuario = null;
        }

        return usuario;
    }

    public void destroyTable() {
        //db = helper.getWritableDatabase();
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
    }
}
