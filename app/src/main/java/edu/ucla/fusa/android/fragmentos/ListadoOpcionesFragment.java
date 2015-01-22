package edu.ucla.fusa.android.fragmentos;

import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import edu.ucla.fusa.android.DB.UserTable;
import edu.ucla.fusa.android.R;
import edu.ucla.fusa.android.adaptadores.ListConfiguracionAdapter;
import edu.ucla.fusa.android.adaptadores.ListOpcionesAdapter;
import edu.ucla.fusa.android.interfaces.Item;
import edu.ucla.fusa.android.modelo.herramientas.HeaderListConfiguracion;
import edu.ucla.fusa.android.modelo.herramientas.ItemListConfiguration;
import edu.ucla.fusa.android.modelo.herramientas.ItemListOpcionesMultimedia;
import edu.ucla.fusa.android.modelo.herramientas.JSONParser;
import edu.ucla.fusa.android.modelo.seguridad.Usuario;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;

public class ListadoOpcionesFragment extends Fragment  {

    private static String TAG = "ListadoOpcionesFragment";
    private static final int CROP_FROM_CAMERA = 2;
    private static final int PICK_FROM_CAMERA = 1;
    private static final int PICK_FROM_FILE = 3;
    private ListConfiguracionAdapter mListAdapter;
    private ArrayList<Item> mItemsOptions;
    private ListView mListOptions;
    private View mView;
    private ArrayList<ItemListOpcionesMultimedia> mItemsMultimedia;
    private Uri mFotoCaptureUri;
    private UserTable mUserTable;
    private JSONParser mJSONParser;
    private Usuario mUsuario;

    // Cambio de foto
    private byte[] mPhoto;
    private Bitmap bitmap;

    public static ListadoOpcionesFragment newInstance() {
        ListadoOpcionesFragment fragment = new ListadoOpcionesFragment();
        fragment.setRetainInstance(true);
        return fragment;
    }

    /*public void onCreate(Bundle paramBundle) {
        super.onCreate(paramBundle);
        getActivity().getActionBar().setTitle(R.string.configuracion);
        getActivity().getActionBar().setIcon(R.drawable.ic_configuracion_blanco);
        mJSONParser = new JSONParser();
        mUserTable = new UserTable(getActivity());
        mItemsMultimedia = new ArrayList<ItemListOpcionesMultimedia>();
        mItemsOptions = new ArrayList<Item>();
    }

    @Override
    public void onResume() {
        super.onResume();
        getActivity().getActionBar().setTitle(R.string.configuracion);
        getActivity().getActionBar().setIcon(R.drawable.ic_configuracion_blanco);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle bundle) {
        super.onCreateView(inflater, container, bundle);
        mView = inflater.inflate(R.layout.fragment_drawer_configuraciones, container, false);
        mListOptions = (ListView) mView.findViewById(R.id.list_view_configuraciones);
        mItemsOptions.clear();
        mItemsOptions.add(new HeaderListConfiguracion(
                getResources().getString(R.string.configuracion_cuenta), R.drawable.ic_usuario));
        mItemsOptions.add(new ItemListConfiguration(
                getResources().getString(R.string.configuracion_cuenta_cambiar_foto),
                R.drawable.ic_change_photo));
        mItemsOptions.add(new ItemListConfiguration(
                getResources().getString(R.string.configuracion_cuenta_cambiar_password),
                R.drawable.ic_cambiar_password));
        mItemsOptions.add(new ItemListConfiguration(
                getResources().getString(R.string.configuracion_cuenta_postularse),
                R.drawable.ic_estudiantes));
        mItemsOptions.add(new ItemListConfiguration(
                getResources().getString(R.string.configuracion_cuenta_participar),
                R.drawable.ic_profesores));
        mItemsOptions.add(new ItemListConfiguration(
                getResources().getString(R.string.configuracion_cuenta_contribuir),
                R.drawable.ic_instrumentos));
        mItemsOptions.add(new ItemListConfiguration(
                getResources().getString(R.string.configuracion_cuenta_contratar),
                R.drawable.ic_eventos));
        mListAdapter = new ListConfiguracionAdapter(getActivity(), mItemsOptions);
        mListOptions.setAdapter(mListAdapter);
        mListOptions.setOnItemClickListener(this);
        return mView;
    }

    public void onItemClick(AdapterView<?> parent, View view, int position, long l) {
        switch (position) {
            case 1:
                showDialog();
                break;
            case 2:
                getFragmentManager().beginTransaction()
                        .replace(R.id.frame_container, CambiarPasswordFragment.newInstance())
                        .addToBackStack(null).commit();
                break;
            case 3:
                getFragmentManager().beginTransaction()
                        .replace(R.id.frame_container, EstudianteAspiranteFragment.newInstance())
                        .addToBackStack(null)
                        .commit();
                break;
            case 4:
                getFragmentManager().beginTransaction()
                        .replace(R.id.frame_container, InstructorAspiranteFragment.newInstance())
                        .addToBackStack(null)
                        .commit();
                break;
            case 5:
                getFragmentManager().beginTransaction()
                        .replace(R.id.frame_container, DonarInstrumentoFragment.newInstance())
                        .addToBackStack(null)
                        .commit();
                break;
            case 6:
                getFragmentManager().beginTransaction()
                        .replace(R.id.frame_container, SolicitanteExternoFragment.newInstance())
                        .addToBackStack(null)
                        .commit();
                break;
        }
    }

    // Cambiar foto de perfil

    private void showDialog() {
        mItemsMultimedia.clear();
        mItemsMultimedia.add(new ItemListOpcionesMultimedia(getString(R.string.opcion_multimedia_camara), R.drawable.ic_camara));
        mItemsMultimedia.add(new ItemListOpcionesMultimedia(getString(R.string.opcion_multimedia_galeria), R.drawable.ic_cambiar_foto));
        ListOpcionesAdapter adapter = new ListOpcionesAdapter(getActivity(), mItemsMultimedia);
        new AlertDialog.Builder(getActivity()).setAdapter(adapter, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int item) {
                if (item == 0) { /** Desde la cámara */
                 /*   Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
                    mFotoCaptureUri = Uri.fromFile(new File(Environment.getExternalStorageDirectory(), "tmp_avatar_" + String.valueOf(System.currentTimeMillis()) + ".jpg"));
                    intent.putExtra("output", mFotoCaptureUri);
                    try {
                        intent.putExtra("return-data", true);
                        startActivityForResult(intent, PICK_FROM_CAMERA);
                    } catch (ActivityNotFoundException e) {
                        e.printStackTrace();
                    }
                } else { /** Desde galeria de imagenes */
                  /*  Intent intent = new Intent();
                    intent.setType("image/*");
                    intent.setAction("android.intent.action.GET_CONTENT");
                    startActivityForResult(Intent.createChooser(intent, "Completa la acción usando..."), PICK_FROM_FILE);
                }
            }
        }).create().show();
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        File file;
        mUsuario = mUserTable.searchUser();
        if (getActivity().RESULT_OK == resultCode) {
            switch (requestCode) {
                case PICK_FROM_CAMERA:
                    doCrop();
                    break;
                case PICK_FROM_FILE:
                    mFotoCaptureUri = data.getData();
                    doCrop();
                    break;
                case CROP_FROM_CAMERA:
                    Bundle b = data.getExtras();
                    if (b != null) {
                        bitmap = b.getParcelable("data");
                        mPhoto = convertImageToByte(bitmap);
                        new UploadFoto().execute(mUsuario.getUsername(), mPhoto.toString());
                        new UploadFotoEstudiante().execute(mUsuario.getUsername(), mPhoto.toString());
                    }
                    file = new File(mFotoCaptureUri.getPath());
                    if (file.exists()) {
                        file.delete();
                    }
                    break;
            }
        }
    }

    private byte[] convertImageToByte(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        return Base64.encodeBytesToBytes(stream.toByteArray());
    }

    /*private void doCrop() {
        startActivityForResult(new Intent("com.android.camera.action.CROP")
                .setDataAndType(mFotoCaptureUri, "image/*")
                .putExtra("crop", "true")
                .putExtra("outputX", 200)
                .putExtra("outputY", 200)
                .putExtra("aspectX", 1)
                .putExtra("aspectY", 1)
                .putExtra("scale", true)
                .putExtra("return-data", true), CROP_FROM_CAMERA);
    }

    private class UploadFoto extends AsyncTask<String, Void, Integer> {

        @Override
        protected Integer doInBackground(String... params) {
            /** Cargamos los parametros que enviaremos por URL */
          /*  ArrayList<NameValuePair> parametros = new ArrayList<NameValuePair>();
            parametros.add(new BasicNameValuePair("username", params[0]));
            parametros.add(new BasicNameValuePair("foto", params[1]));

            return mJSONParser.serviceChangeFoto(parametros);
        }

        @Override
        protected void onPostExecute(Integer result) {
            super.onPostExecute(result);
            switch (result) {
                case 0:
                    Log.i(TAG, "¡No se cargo la foto!");
                    break;
                case 1:
                    Log.i(TAG, "¡Foto actualizada!");
                    break;
            }
        }
    }

    private class UploadFotoEstudiante extends AsyncTask<String, Void, Integer> {

        @Override
        protected Integer doInBackground(String... params) {
            /** Cargamos los parametros que enviaremos por URL */
          /*  ArrayList<NameValuePair> parametros = new ArrayList<NameValuePair>();
            parametros.add(new BasicNameValuePair("username", params[0]));
            parametros.add(new BasicNameValuePair("imagen", params[1]));

            return mJSONParser.serviceChangeFotoEstudiante(parametros);
        }

        @Override
        protected void onPostExecute(Integer result) {
            super.onPostExecute(result);
            switch (result) {
                case 0:
                    Log.i(TAG, "¡No se cargo la foto!");
                    Toast.makeText(getActivity(), R.string.mensaje_error_cambiar_foto, Toast.LENGTH_SHORT).show();
                    break;
                case 1:
                    Log.i(TAG, "¡Foto actualizada!");
                    ((CircleImageView) getActivity().findViewById(R.id.iv_foto_perfil_drawer)).setImageBitmap(bitmap);
                    break;
            }
        }
    }*/
}
