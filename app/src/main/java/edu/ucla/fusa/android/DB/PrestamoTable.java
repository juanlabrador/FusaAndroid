package edu.ucla.fusa.android.DB;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import edu.ucla.fusa.android.modelo.instrumentos.Instrumento;
import edu.ucla.fusa.android.modelo.instrumentos.Marca;
import edu.ucla.fusa.android.modelo.instrumentos.Modelo;
import edu.ucla.fusa.android.modelo.instrumentos.Prestamo;
import edu.ucla.fusa.android.modelo.instrumentos.TipoInstrumento;

/**
 * Created by juanlabrador on 22/01/15.
 */
public class PrestamoTable {

    private static final String TAG = "PrestamoTable";
    public static final String TABLE_NAME = "prestamo";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_FECHA_EMISION = "fecha_emision";
    public static final String COLUMN_FECHA_VENCIMIENTO = "fecha_vencimiento";
    public static final String COLUMN_ESTATUS = "estatus";
    public static final String COLUMN_TIPO_INSTRUMENTO = "tipo_instrumento";
    public static final String COLUMN_MODELO = "modelo";
    public static final String COLUMN_SERIAL = "serial";
    public static final String COLUMN_MARCA = "marca";
    public static final String COLUMN_ID_PRESTAMO = "prestamo_id";

    public static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " ("
            + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + COLUMN_FECHA_EMISION + " DATE, "
            + COLUMN_FECHA_VENCIMIENTO + " DATE, "
            + COLUMN_ESTATUS + " TEXT, "
            + COLUMN_TIPO_INSTRUMENTO + " TEXT, "
            + COLUMN_MODELO + " TEXT, "
            + COLUMN_SERIAL + " TEXT, "
            + COLUMN_MARCA + " TEXT, "
            + COLUMN_ID_PRESTAMO + " INTEGER);";

    private DataBaseHelper mHelper;
    private SQLiteDatabase mDatabase;
    private Cursor mCursor;
    private Prestamo mPrestamo;
    private SimpleDateFormat mDateFormat = new SimpleDateFormat("dd-MM-yyyy");
    private Marca mMarca;
    private Modelo mModelo;
    private TipoInstrumento mTipoInstrumento;
    private Instrumento mIntrumento;


    public PrestamoTable(Context context) {
        mHelper = DataBaseHelper.getInstance(context);
        mDatabase = mHelper.getWritableDatabase();
        mPrestamo = new Prestamo();
        mMarca = new Marca();
        mModelo = new Modelo();
        mTipoInstrumento = new TipoInstrumento();
        mIntrumento = new Instrumento();
    }

    private ContentValues generarValores (Date fechaEmision, Date fechaVencimiento, String estatus, 
                                          String tipoInstrumento, String modelo, String serial, String marca, int id) {

        ContentValues valores = new ContentValues();
        valores.put(COLUMN_ID_PRESTAMO, id);
        valores.put(COLUMN_FECHA_EMISION, mDateFormat.format(fechaEmision));
        valores.put(COLUMN_FECHA_VENCIMIENTO, mDateFormat.format(fechaVencimiento));
        valores.put(COLUMN_ESTATUS, estatus);
        valores.put(COLUMN_TIPO_INSTRUMENTO, tipoInstrumento);
        valores.put(COLUMN_MODELO, modelo);
        valores.put(COLUMN_SERIAL, serial);
        valores.put(COLUMN_MARCA, marca);

        return valores;
    }

    public void insertData(Date fechaEmision, Date fechaVencimiento, String estatus,
                           String tipoInstrumento, String modelo, String serial, String marca, int id) {

        mDatabase.insert(TABLE_NAME, null, generarValores(fechaEmision, fechaVencimiento, estatus,
                tipoInstrumento, modelo, serial, marca,id));
    }
    public Prestamo searchPrestamo() {
        try {
            String tiraSQL = "SELECT * FROM " + TABLE_NAME;
            mCursor = mDatabase.rawQuery(tiraSQL, null);
            if (mCursor.moveToFirst()) {
                mPrestamo.setId(mCursor.getInt(8));
                mPrestamo.setFechaEmision(mDateFormat.parse(mCursor.getString(1)));
                mPrestamo.setFechaVencimiento(mDateFormat.parse(mCursor.getString(2)));
                mPrestamo.setEstatus(mCursor.getString(3));
                mTipoInstrumento.setDescripcion(mCursor.getString(4));
                mModelo.setDescripcion(mCursor.getString(5));
                mIntrumento.setSerial(mCursor.getString(6));
                mIntrumento.setTipoInstrumento(mTipoInstrumento);
                mMarca.setDescripcion(mCursor.getString(7));
                mModelo.setMarca(mMarca);
                mIntrumento.setModelo(mModelo);
                mPrestamo.setInstrumento(mIntrumento);

            } else {
                mPrestamo = null;
            }
            return mPrestamo;
        }catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void updateData(int id, String estatus) {
        ContentValues valores = new ContentValues();
        valores.put(COLUMN_ESTATUS, estatus);
        String[] condicion = new String[]{String.valueOf(id)};
        mDatabase.update(TABLE_NAME, valores, COLUMN_ID_PRESTAMO + "=?", condicion);
    }

    public void deleteAll() {
        mDatabase.delete(TABLE_NAME, null, null);
    }

    public void destroyTable() {
        mDatabase.execSQL("DROP TABLE IF EXISTS "+ TABLE_NAME);
    }

    public void createTable() {
        mDatabase.execSQL(PrestamoTable.CREATE_TABLE);
    }
}
