package edu.ucla.fusa.android.DB;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;

import edu.ucla.fusa.android.modelo.instrumentos.TipoInstrumento;

/**
 * Created by juanlabrador on 22/01/15.
 */
public class TipoInstrumentoTable {

    private static final String TAG = "TipoInstrumentoTable";
    public static final String TABLE_NAME = "tipo_instrumento";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_DESCRIPCION = "descripcion";
    public static final String COLUMN_ID_TIPO_INSTRUMENTO = "tipo_instrumento_id";

    public static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " ("
            + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + COLUMN_DESCRIPCION + " TEXT, "
            + COLUMN_ID_TIPO_INSTRUMENTO + " INTEGER);";

    private DataBaseHelper mHelper;
    private SQLiteDatabase mDatabase;
    private Cursor mCursor;
    private ArrayList<TipoInstrumento> mTiposInstrumentos;

    public TipoInstrumentoTable(Context context) {
        mHelper = DataBaseHelper.getInstance(context);
        mDatabase = mHelper.getWritableDatabase();
        mTiposInstrumentos = new ArrayList<>();
    }

    private ContentValues generarValores (String descripcion, int id) {

        ContentValues valores = new ContentValues();
        valores.put(COLUMN_ID_TIPO_INSTRUMENTO, id);
        valores.put(COLUMN_DESCRIPCION, descripcion);

        return valores;
    }

    public void insertData(int id, String descripcion) {
        mDatabase.insert(TABLE_NAME, null, generarValores(descripcion, id));
    }

    public ArrayList<TipoInstrumento> searchTiposInstrumentos() {
        String tiraSQL = "SELECT * FROM " + TABLE_NAME;
        //db = helper.getReadableDatabase();
        mCursor = mDatabase.rawQuery(tiraSQL, null);
        while (mCursor.moveToNext()) {
            mTiposInstrumentos.add(new TipoInstrumento(
                    mCursor.getInt(2), // ID
                    mCursor.getString(1), // Nombre
                    "activo"
            ));
        }
        return mTiposInstrumentos;
    }

    public void destroyTable() {
        mDatabase.execSQL("DROP TABLE IF EXISTS "+ TABLE_NAME);
    }

}
