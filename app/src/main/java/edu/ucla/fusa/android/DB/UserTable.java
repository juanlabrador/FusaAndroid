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
    private static final String COLUMN_PHOTO = "foto";
    private static final String COLUMN_TIPO_USER = "tipo_usuario";

    public static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " ("
                    + COLUMN_ID_USER + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + COLUMN_USERNAME + " TEXT, "
                    + COLUMN_PASSWORD + " TEXT, "
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
        mDataBase.insert(TABLE_NAME, null, generarValores(username, password, foto, tipoUsuario));
    }

    public void updateFoto(int id, byte[] foto) {
        ContentValues valores = new ContentValues();
        valores.put(COLUMN_PHOTO, foto);
        String[] condicion = new String[]{String.valueOf(id)};
        mDataBase.update(TABLE_NAME, valores, COLUMN_ID_USER + "=?", condicion);
    }

    public void updatePassword(int id, String pass) {
        ContentValues valores = new ContentValues();
        valores.put(COLUMN_PASSWORD, pass);
        String[] condicion = new String[]{String.valueOf(id)};
        mDataBase.update(TABLE_NAME, valores, COLUMN_ID_USER + "=?", condicion);
    }

    public Usuario searchUser() {
        String tiraSQL = "SELECT * FROM " + TABLE_NAME;
        //db = helper.getReadableDatabase();
        mCursor = mDataBase.rawQuery(tiraSQL, null);
        if (mCursor.moveToFirst()) {
            mUsuario.setId(mCursor.getInt(0));
            mUsuario.setUsername(mCursor.getString(1));
            mUsuario.setPassword(mCursor.getString(2));
            mUsuario.setFoto(mCursor.getBlob(3));
            mUsuario.setTipoUsuario(
                    new TipoUsuario(mCursor.getInt(4), "", ""));
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
