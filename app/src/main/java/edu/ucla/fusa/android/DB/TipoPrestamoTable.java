package edu.ucla.fusa.android.DB;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;

import edu.ucla.fusa.android.modelo.instrumentos.TipoPrestamo;

/**
 * Created by juanlabrador on 21/01/15.
 */
public class TipoPrestamoTable {

    private static final String TAG = "TipoPrestamoTable";
    public static final String TABLE_NAME = "tipo_prestamo";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_DESCRIPCION = "descripcion";
    public static final String COLUMN_ID_TIPO_PRESTAMO = "tipo_prestamo_id";

    public static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " ("
            + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + COLUMN_DESCRIPCION + " TEXT, "
            + COLUMN_ID_TIPO_PRESTAMO + " INTEGER);";

    private DataBaseHelper mHelper;
    private SQLiteDatabase mDatabase;
    private Cursor mCursor;
    private ArrayList<TipoPrestamo> mTiposPrestamos;

    public TipoPrestamoTable(Context context) {
        mHelper = new DataBaseHelper(context);
        mDatabase = mHelper.getWritableDatabase();
        mTiposPrestamos = new ArrayList<>();
    }

    private ContentValues generarValores (String descripcion, int id) {

        ContentValues valores = new ContentValues();
        valores.put(COLUMN_ID_TIPO_PRESTAMO, id);
        valores.put(COLUMN_DESCRIPCION, descripcion);

        return valores;
    }

    public void insertData(int id, String descripcion) {
        mDatabase.insert(TABLE_NAME, null, generarValores(descripcion, id));
    }

    public ArrayList<TipoPrestamo> searchTiposPrestamos() {
        String tiraSQL = "SELECT * FROM " + TABLE_NAME;
        //db = helper.getReadableDatabase();
        mCursor = mDatabase.rawQuery(tiraSQL, null);
        while (mCursor.moveToNext()) {
            Log.i(TAG, "¡Buscando Tipos de prestamos!");
            mTiposPrestamos.add(new TipoPrestamo(
                    mCursor.getInt(2), // ID
                    mCursor.getString(1), // Nombre
                    "activo"
            ));
        }
        return mTiposPrestamos;
    }

    public void destroyTable() {
        mDatabase.execSQL("DROP TABLE IF EXISTS "+ TABLE_NAME);
    }

}
