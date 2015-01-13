package edu.ucla.fusa.android.fragmentos;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.SystemClock;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import de.hdodenhof.circleimageview.CircleImageView;
import edu.ucla.fusa.android.DB.DataBaseHelper;
import edu.ucla.fusa.android.DB.UserTable;
import edu.ucla.fusa.android.R;
import edu.ucla.fusa.android.VistasInicialesActivity;
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

public class ConfiguracionListadoFragment extends Fragment implements AdapterView.OnItemClickListener {

    private static final int CROP_FROM_CAMERA = 2;
    private static final int PICK_FROM_CAMERA = 1;
    private static final int PICK_FROM_FILE = 3;
    private ConfiguracionAcercaNosotrosFragment acercaNosotros;
    private ConfiguracionAcercaVersionFragment acercaVersion;
    private ListConfiguracionAdapter adapter;
    private CerrarSesionAsyncTaks asyncTaks;
    private FragmentManager fragmentManager;
    private ArrayList<Item> items = new ArrayList();
    private ListView list;
    private SharedPreferences preferencias;
    private View view;
    private ArrayList<ItemListOpcionesMultimedia> options = new ArrayList();
    private Uri mFotoCaptureUri;
    private UserTable bd;
    private JSONParser jsonParser = new JSONParser();

    // Cambio de foto
    private byte[] bytes;
    private Bitmap bitmap;

    public static ConfiguracionListadoFragment newInstance() {
        ConfiguracionListadoFragment fragment = new ConfiguracionListadoFragment();
        fragment.setRetainInstance(true);
        return fragment;
    }

    public void onCreate(Bundle paramBundle) {
        super.onCreate(paramBundle);
        getActivity().getActionBar().setTitle(R.string.configuracion);
        getActivity().getActionBar().setIcon(R.drawable.ic_configuracion_blanco);
        bd = new UserTable(getActivity());
    }

    @Override
    public void onResume() {
        super.onResume();
        getActivity().getActionBar().setTitle(R.string.configuracion);
        getActivity().getActionBar().setIcon(R.drawable.ic_configuracion_blanco);
    }

    public View onCreateView(LayoutInflater paramLayoutInflater, ViewGroup paramViewGroup, Bundle paramBundle) {
        super.onCreateView(paramLayoutInflater, paramViewGroup, paramBundle);
        view = paramLayoutInflater.inflate(R.layout.fragment_drawer_configuraciones, paramViewGroup, false);
        list = ((ListView) view.findViewById(R.id.list_view_configuraciones));
        items.clear();
        items.add(new HeaderListConfiguracion(
                getResources().getString(R.string.configuracion_cuenta), R.drawable.ic_usuario));
        items.add(new ItemListConfiguration(
                getResources().getString(R.string.configuracion_cuenta_cambiar_foto),
                R.drawable.ic_change_photo));
        items.add(new ItemListConfiguration(
                getResources().getString(R.string.configuracion_cuenta_cambiar_password),
                R.drawable.ic_cambiar_password));
        items.add(new ItemListConfiguration(
                getResources().getString(R.string.configuracion_cuenta_cerrar_sesion),
                R.drawable.ic_cerrar_sesion));
        items.add(new ItemListConfiguration(
                getResources().getString(R.string.configuracion_cuenta_postularse),
                R.drawable.ic_estudiantes));
        items.add(new ItemListConfiguration(
                getResources().getString(R.string.configuracion_cuenta_participar),
                R.drawable.ic_profesores));
        items.add(new ItemListConfiguration(
                getResources().getString(R.string.configuracion_cuenta_contribuir),
                R.drawable.ic_instrumentos));
        items.add(new ItemListConfiguration(
                getResources().getString(R.string.configuracion_cuenta_contratar),
                R.drawable.ic_eventos));
        items.add(new HeaderListConfiguracion(
                getResources().getString(R.string.configuracion_aplicaciones), R.drawable.ic_aplicaciones));
        items.add(new ItemListConfiguration(
                getResources().getString(R.string.configuracion_aplicaciones_cuentas_enlazadas),
                R.drawable.ic_cuentas_enlazadas));
        items.add(new HeaderListConfiguracion(
                getResources().getString(R.string.configuracion_informacion), 0));
        items.add(new ItemListConfiguration(
                getResources().getString(R.string.configuracion_informacion_version),
                R.drawable.ic_informacion_version));
        items.add(new ItemListConfiguration(
                getResources().getString(R.string.configuracion_informacion_nosotros),
                R.drawable.ic_acerca_de));
        items.add(new ItemListConfiguration(
                getResources().getString(R.string.configuracion_informacion_web),
                R.drawable.ic_informacion_web));
        items.add(new HeaderListConfiguracion(
                getResources().getString(R.string.configuracion_ayuda), R.drawable.ic_ayuda));
        items.add(new ItemListConfiguration(
                getResources().getString(R.string.configuracion_informar_problema),
                R.drawable.ic_informacion_problema));
        items.add(new ItemListConfiguration(
                getResources().getString(R.string.configuracion_sugerencia),
                R.drawable.ic_sugerencia));
        adapter = new ListConfiguracionAdapter(getActivity(), this.items);
        list.setAdapter(this.adapter);
        list.setOnItemClickListener(this);
        return view;
    }

    public void onItemClick(AdapterView<?> paramAdapterView, View paramView, int paramInt, long paramLong) {
        switch (paramInt) {
            case 14:
            case 10:
            case 0:
            default:
            case 1:
                showDialog();
                break;
            case 2:
                getFragmentManager().beginTransaction()
                        .replace(R.id.frame_container, ConfiguracionCambiarPasswordFragment.newInstance())
                        .addToBackStack(null).commit();
                break;
            case 3:
                preferencias = getActivity().getSharedPreferences("index", 0);
                new CerrarSesionAsyncTaks().execute();
                break;
            case 4:
                getFragmentManager().beginTransaction()
                        .replace(R.id.frame_container, InicialPostulacionesFragment.newInstance())
                        .addToBackStack(null)
                        .commit();
                break;
            case 5:
                getFragmentManager().beginTransaction()
                        .replace(R.id.frame_container, InicialParticipaFragment.newInstance())
                        .addToBackStack(null)
                        .commit();
                break;
            case 6:
                getFragmentManager().beginTransaction()
                        .replace(R.id.frame_container, InicialDonacionesFragment.newInstance())
                        .addToBackStack(null)
                        .commit();
                break;
            case 7:
                getFragmentManager().beginTransaction()
                        .replace(R.id.frame_container, InicialContratacionesFragment.newInstance())
                        .addToBackStack(null)
                        .commit();
                break;
            case 9:
                getFragmentManager().beginTransaction()
                        .replace(R.id.frame_container, ConfiguracionAplicacionesFragment.newInstance())
                        .addToBackStack(null)
                        .commit();
                break;
            case 11:
                fragmentManager = getFragmentManager();
                acercaVersion = new ConfiguracionAcercaVersionFragment();
                acercaVersion.show(fragmentManager, "AcercaVersión");
                break;
            case 12:
                fragmentManager = getFragmentManager();
                acercaNosotros = new ConfiguracionAcercaNosotrosFragment();
                acercaNosotros.show(fragmentManager, "AcercaNosotros");
                break;
            case 13:
                getActivity().startActivity(new Intent("android.intent.action.VIEW")
                        .setData(Uri.parse(getActivity()
                                .getResources()
                                .getString(R.string.pagina_web))));
                break;
            case 15:
                getFragmentManager().beginTransaction()
                        .replace(R.id.frame_container, ConfiguracionInformarProblemaFragment.newInstance())
                        .addToBackStack(null)
                        .commit();
                break;
            case 16:
                getFragmentManager().beginTransaction()
                        .replace(R.id.frame_container, ConfiguracionSugerenciaFragment.newInstance())
                        .addToBackStack(null)
                        .commit();
                break;
        }
    }

    private class CerrarSesionAsyncTaks extends AsyncTask<Void, Void, Void> {

        private ProgressDialog dialog;
        private SharedPreferences.Editor editor;

        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(getActivity());
            dialog.setMessage(getResources().getString(R.string.cerrar_sesion));
            dialog.setIndeterminate(false);
            dialog.setCancelable(false);
            dialog.show();
        }

        protected Void doInBackground(Void[] paramArrayOfVoid) {
            editor = preferencias.edit();
            editor.clear();
            editor.commit();
            SystemClock.sleep(2000L);
            getActivity().deleteDatabase(DataBaseHelper.NAME);
            return null;
        }

        protected void onPostExecute(Void paramVoid) {
            super.onPostExecute(paramVoid);
            dialog.dismiss();
            startActivity(new Intent(getActivity(), VistasInicialesActivity.class));
            getActivity().finish();
        }
    }

    // Cambiar foto de perfil

    private void showDialog() {
        options.clear();
        options.add(new ItemListOpcionesMultimedia(getString(R.string.opcion_multimedia_camara), R.drawable.ic_camara));
        options.add(new ItemListOpcionesMultimedia(getString(R.string.opcion_multimedia_galeria), R.drawable.ic_cambiar_foto));
        ListOpcionesAdapter adapter = new ListOpcionesAdapter(getActivity(), options);
        new AlertDialog.Builder(getActivity()).setAdapter(adapter, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int item) {
                if (item == 0) { /** Desde la cámara */
                    Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
                    mFotoCaptureUri = Uri.fromFile(new File(Environment.getExternalStorageDirectory(), "tmp_avatar_" + String.valueOf(System.currentTimeMillis()) + ".jpg"));
                    intent.putExtra("output", mFotoCaptureUri);
                    try {
                        intent.putExtra("return-data", true);
                        startActivityForResult(intent, PICK_FROM_CAMERA);
                    } catch (ActivityNotFoundException e) {
                        e.printStackTrace();
                    }
                } else { /** Desde galeria de imagenes */
                    Intent intent = new Intent();
                    intent.setType("image/*");
                    intent.setAction("android.intent.action.GET_CONTENT");
                    startActivityForResult(Intent.createChooser(intent, "Completa la acción usando..."), PICK_FROM_FILE);
                }
            }
        }).create().show();
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        File file;
        Usuario usuario = bd.searchUser();
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
                        bytes = convertImageToByte(bitmap);
                        //((CircleImageView) findViewById(R.id.iv_foto_perfil_drawer)).setImageBitmap(bitmap);
                        new UploadFoto().execute(usuario.getUsername(), bytes.toString());
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
        bitmap.compress(Bitmap.CompressFormat.JPEG, 90, stream);
        return  stream.toByteArray();
    }

    private void doCrop() {
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
            ArrayList<NameValuePair> parametros = new ArrayList<NameValuePair>();
            parametros.add(new BasicNameValuePair("username", params[0]));
            parametros.add(new BasicNameValuePair("foto", params[1]));

            return jsonParser.serviceChangeFoto(parametros);
        }

        @Override
        protected void onPostExecute(Integer result) {
            super.onPostExecute(result);
            if (result == 0) {
                Toast.makeText(getActivity(), R.string.mensaje_error_cambiar_foto, Toast.LENGTH_SHORT).show();
            } else {
                ((CircleImageView) getActivity().findViewById(R.id.iv_foto_perfil_drawer)).setImageBitmap(bitmap);
            }
        }
    }
}
