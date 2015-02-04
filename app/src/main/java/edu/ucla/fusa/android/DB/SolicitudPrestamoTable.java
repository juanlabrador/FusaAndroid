package edu.ucla.fusa.android.DB;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import edu.ucla.fusa.android.modelo.instrumentos.SolicitudPrestamo;
import edu.ucla.fusa.android.modelo.instrumentos.TipoInstrumento;
import edu.ucla.fusa.android.modelo.instrumentos.TipoPrestamo;

/**
 * Created by juanlabrador on 22/01/15.
 */
public class SolicitudPrestamoTable {

    private static final String TAG = "SolicitudPrestamoTable";
    public static final String TABLE_NAME = "solicitud_prestamo";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_PERIODO = "periodo";
    public static final String COLUMN_ESTATUS = "estatus";
    public static final String COLUMN_TIPO_INSTRUMENTO = "tipo_instrumento";
    public static final String COLUMN_FECHA_EMISION = "fecha_emision";
    public static final String COLUMN_FECHA_VENCIMIENTO = "fecha_vencimiento";
    public static final String COLUMN_ID_SOLICITUD_PRESTAMO = "solicitud_prestamo_id";

    public static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " ("
            + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + COLUMN_PERIODO + " TEXT, "
            + COLUMN_ESTATUS + " TEXT, "
            + COLUMN_TIPO_INSTRUMENTO + " TEXT, "
            + COLUMN_FECHA_EMISION + " TEXT, "
            + COLUMN_FECHA_VENCIMIENTO + " TEXT, "
            + COLUMN_ID_SOLICITUD_PRESTAMO + " INTEGER);";

    private DataBaseHelper mHelper;
    private SQLiteDatabase mDatabase;
    private Cursor mCursor;
    private SolicitudPrestamo mSolicitudPrestamo;
    private TipoPrestamo mTipoPrestamo;
    private TipoInstrumento mTipoInstrumento;


    public SolicitudPrestamoTable(Context context) {
        mHelper = DataBaseHelper.getInstance(context);
        mDatabase = mHelper.getWritableDatabase();
        mSolicitudPrestamo = new SolicitudPrestamo();
        mTipoPrestamo = new TipoPrestamo();
        mTipoInstrumento = new TipoInstrumento();
    }

    private ContentValues generarValores (String periodo, String estatus, String tipo, String fechaEmision, String fechaVencimiento, int id) {

        ContentValues valores = new ContentValues();
        valores.put(COLUMN_ID_SOLICITUD_PRESTAMO, id);
        valores.put(COLUMN_PERIODO, periodo);
        valores.put(COLUMN_ESTATUS, estatus);
        valores.put(COLUMN_TIPO_INSTRUMENTO, tipo);
        valores.put(COLUMN_FECHA_EMISION, fechaEmision);
        valores.put(COLUMN_FECHA_VENCIMIENTO, fechaVencimiento);

        return valores;
    }

    public void insertData(int id, String periodo, String estatus, String tipo, String fechaEmision, String fechaVencimiento) {
        mDatabase.insert(TABLE_NAME, null, generarValores(periodo, estatus, tipo, fechaEmision, fechaVencimiento, id));
    }

    public void updateData(int id, String estatus) {
        ContentValues valores = new ContentValues();
        valores.put(COLUMN_ESTATUS, estatus);
        String[] condicion = new String[]{String.valueOf(id)};
        mDatabase.update(TABLE_NAME, valores, COLUMN_ID_SOLICITUD_PRESTAMO + "=?", condicion);
    }
    
    public SolicitudPrestamo searchSolicitudPrestamo() {
        Log.i(TAG, "¡Buscando la solicitud!");
        String tiraSQL = "SELECT * FROM " + TABLE_NAME;
        //db = helper.getReadableDatabase();
        mCursor = mDatabase.rawQuery(tiraSQL, null);
        if (mCursor.moveToFirst()) {
            mSolicitudPrestamo.setId(mCursor.getInt(6));
            mTipoPrestamo.setDescripcion(mCursor.getString(1));
            mSolicitudPrestamo.setTipoPrestamo(mTipoPrestamo);
            mSolicitudPrestamo.setEstatus(mCursor.getString(2));
            mTipoInstrumento.setDescripcion(mCursor.getString(3));
            mSolicitudPrestamo.setTipoInstrumento(mTipoInstrumento);
            mSolicitudPrestamo.setFechaEmision(mCursor.getString(4));
            mSolicitudPrestamo.setFechaVencimiento(mCursor.getString(5));
            return mSolicitudPrestamo;
        }
        return null;
    }

    public void deleteAll() {
        mDatabase.delete(TABLE_NAME, null, null);
    }

    public void destroyTable() {
        mDatabase.execSQL("DROP TABLE IF EXISTS "+ TABLE_NAME);
    }

    public void createTable() {
        mDatabase.execSQL(SolicitudPrestamoTable.CREATE_TABLE);
    }
}
