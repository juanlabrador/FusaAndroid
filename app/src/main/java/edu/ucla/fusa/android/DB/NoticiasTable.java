package edu.ucla.fusa.android.DB;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import edu.ucla.fusa.android.modelo.herramientas.ItemListNoticia;

/**
 * Created by juanlabrador on 05/12/14.
 */
public class NoticiasTable {

    public static final String TABLE_NAME = "noticias";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_TITULO = "titulo";
    public static final String COLUMN_DESCRIPCION = "descripcion";
    public static final String COLUMN_FECHA_PUBLICACION = "fecha";
    public static final String COLUMN_IMAGEN = "imagen";
    public static final String COLUMN_TIENE_IMAGEN = "tiene_imagen";
    public static final String COLUMN_ID_NOTICIA = "id_noticia";

    public static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " ("
            + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + COLUMN_TITULO + " TEXT, "
            + COLUMN_DESCRIPCION + " TEXT, "
            + COLUMN_FECHA_PUBLICACION + " DATE, "
            + COLUMN_IMAGEN + " BLOB, "
            + COLUMN_TIENE_IMAGEN + " INTEGER, "
            + COLUMN_ID_NOTICIA + " INTEGER);";

    private DataBaseHelper mHelper;
    private SQLiteDatabase mDataBase;
    private Cursor mCursor;
    private SimpleDateFormat mDateFormat = new SimpleDateFormat("dd-MM-yyyy");
    private ArrayList<ItemListNoticia> mNoticias = new ArrayList<>();

    public NoticiasTable(Context context) {
        mHelper = DataBaseHelper.getInstance(context);
        mDataBase = mHelper.getWritableDatabase();

    }

    private ContentValues generarValores (String titulo, String descripcion, Date fecha,
                                          byte[] imagen, long idNoticia, int haveFoto) {

        ContentValues valores = new ContentValues();
        valores.put(COLUMN_TITULO, titulo);
        valores.put(COLUMN_DESCRIPCION, descripcion);
        valores.put(COLUMN_FECHA_PUBLICACION, mDateFormat.format(fecha));
        valores.put(COLUMN_IMAGEN, imagen);
        valores.put(COLUMN_ID_NOTICIA, idNoticia);
        valores.put(COLUMN_TIENE_IMAGEN, haveFoto);

        return valores;
    }

    public void insertData(String titulo, String descripcion, Date fecha,
                         byte[] imagen, long idNoticia, int haveFoto) {
        mDataBase.insert(TABLE_NAME, null, generarValores(titulo, descripcion, fecha, imagen, idNoticia, haveFoto));
    }

    public ArrayList<ItemListNoticia> searchNews() {
        String[] columnas = new String[]{COLUMN_ID, COLUMN_TITULO, COLUMN_DESCRIPCION,
                COLUMN_FECHA_PUBLICACION, COLUMN_IMAGEN, COLUMN_TIENE_IMAGEN, COLUMN_ID_NOTICIA};
        mCursor = mDataBase.query(TABLE_NAME, columnas, null, null, null, null, COLUMN_ID, "50");
        mNoticias.clear();
        while (mCursor.moveToNext()) {
            try {
                mNoticias.add(new ItemListNoticia(
                        mCursor.getInt(6), //ID
                        mCursor.getString(1), //Titulo
                        mDateFormat.parse(mCursor.getString(3)), //Fecha
                        mCursor.getBlob(4), //Imagen
                        mCursor.getString(2), //Descripción
                        mCursor.getInt(5)
                ));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return mNoticias;
    }

    public ArrayList<ItemListNoticia> searchOldNews(String id) {
        String[] condicion = new String[]{id};
        String[] columnas = new String[]{COLUMN_ID, COLUMN_TITULO, COLUMN_DESCRIPCION,
                COLUMN_FECHA_PUBLICACION, COLUMN_IMAGEN, COLUMN_TIENE_IMAGEN, COLUMN_ID_NOTICIA};
        mCursor = mDataBase.query(TABLE_NAME, columnas, COLUMN_ID_NOTICIA + "<?", condicion, null, null, COLUMN_ID + " DESC", "20");
        while (mCursor.moveToNext()) {
            try {
                mNoticias.add(new ItemListNoticia(
                        mCursor.getInt(6), //ID
                        mCursor.getString(1), //Titulo
                        mDateFormat.parse(mCursor.getString(3)), //Fecha
                        mCursor.getBlob(4), //Imagen
                        mCursor.getString(2), //Descripción
                        mCursor.getInt(5)
                ));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        return mNoticias;
    }

    public void destroyTable() {
        mDataBase.execSQL("DROP TABLE IF EXISTS "+ TABLE_NAME);
    }

    public Integer searchLastNews(){
        String[] columnas = new String[]{"MAX(" + COLUMN_ID + ")"};
        mCursor = mDataBase.query(TABLE_NAME, columnas, null, null, null, null, null);
        mCursor.moveToFirst();
        return mCursor.getInt(0);
    }
}
