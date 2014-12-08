package edu.ucla.fusa.android.adaptadores;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.androidsocialnetworks.lib.SocialNetworkManager;
import com.androidsocialnetworks.lib.impl.FacebookSocialNetwork;
import com.androidsocialnetworks.lib.impl.TwitterSocialNetwork;
import com.androidsocialnetworks.lib.listener.OnLoginCompleteListener;
import com.androidsocialnetworks.lib.listener.OnPostingCompleteListener;

import edu.ucla.fusa.android.R;
import edu.ucla.fusa.android.modelo.herramientas.AlertDialogFragment;
import edu.ucla.fusa.android.modelo.herramientas.ItemListNoticia;
import edu.ucla.fusa.android.modelo.herramientas.ItemListOpcionesMultimedia;
import edu.ucla.fusa.android.modelo.herramientas.ProgressDialogFragment;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class ListNoticiasAdapter extends BaseAdapter implements View.OnClickListener, SocialNetworkManager.OnInitializationCompleteListener, OnLoginCompleteListener {

    public static final String SOCIAL_NETWORK_TAG = "ConfiguracionAplicacionesFragment.SOCIAL_NETWORK_TAG";
    private static final String PROGRESS_DIALOG_TAG = "ConfiguracionAplicacionesFragment.PROGRESS_DIALOG_TAG";
    private static String TAG = "ListNoticiasAdapter";
    private FragmentActivity activity;
    private ListFragment fragment;
    private ArrayList<ItemListNoticia> arrayItems;
    private Fila fila;
    private ItemListNoticia item;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MMM/yyyy");
    private ArrayList<ItemListOpcionesMultimedia> items = new ArrayList();
    private SocialNetworkManager socialNetworkManager;
    protected boolean socialNetworkManagerInitialized = false;

    public ListNoticiasAdapter(FragmentActivity activity, ArrayList<ItemListNoticia> paramArrayList, ListFragment fragment) {
        this.activity = activity;
        this.arrayItems = paramArrayList;
        this.fragment = fragment;

        socialNetworkManager = (SocialNetworkManager) activity.getSupportFragmentManager().findFragmentByTag(SOCIAL_NETWORK_TAG);

        if (socialNetworkManager == null) {
            socialNetworkManager = SocialNetworkManager.Builder.from(activity)
                    .twitter("yVdvpAPRFsLfQa1Sgqlu2Kb26", "9XLmtFSSMuDSz4r2bZKuiAZDRBsmH2Uz6cdLIg4lSfeqheMHHV")
                    //.facebook()
                    .build();
            activity.getSupportFragmentManager().beginTransaction()
                    .add(socialNetworkManager, SOCIAL_NETWORK_TAG)
                    .commit();
            socialNetworkManager.setOnInitializationCompleteListener(this);

        } else {
            socialNetworkManagerInitialized = true;
        }
    }

    public int getCount() {
        return this.arrayItems.size();
    }

    public Object getItem(int paramInt) {
        return this.arrayItems.get(paramInt);
    }

    public long getItemId(int paramInt) {
        return paramInt;
    }

    @Override
    public void onSocialNetworkManagerInitialized() {

    }

    @Override
    public void onLoginSuccess(int i) {
        onSocialNetworkManagerInitialized();
        hideProgress();
        handleSuccess("Logueado correctamente", "Ahora puedes compartir noticias.");
    }

    @Override
    public void onError(int socialNetworkID, String requestID, String errorMessage, Object data) {
        hideProgress();
        //handleError(errorMessage);
        handleError("Cancelado...");
    }

    public static class Fila {
        ImageView boton;
        TextView fecha;
        ImageView imagen;
        TextView titulo;
    }

    public View getView(int paramInt, View paramView, ViewGroup paramViewGroup) {
        LayoutInflater layoutInflater = activity.getLayoutInflater();
        if (paramView == null) {
          fila = new Fila();
          paramView = layoutInflater.inflate(R.layout.custom_item_list_noticias, null);
          fila.titulo = ((TextView) paramView.findViewById(R.id.tv_titulo_noticia));
          fila.fecha = ((TextView) paramView.findViewById(R.id.tv_fecha_publicacion_noticia));
          fila.imagen = ((ImageView) paramView.findViewById(R.id.iv_foto_noticia));
          fila.boton = ((ImageView) paramView.findViewById(R.id.btn_compartir_noticia));

          paramView.setTag(fila);
          fila.boton.setTag(R.string.TAG_TITULO_NOTICIA, arrayItems.get(paramInt).getTitulo());
          fila.boton.setTag(R.string.TAG_IMAGEN_NOTICIA, arrayItems.get(paramInt).getImagen());
        } else {
            fila = ((Fila) paramView.getTag());
            fila.boton.setTag(R.string.TAG_TITULO_NOTICIA, arrayItems.get(paramInt).getTitulo());
            fila.boton.setTag(R.string.TAG_IMAGEN_NOTICIA, arrayItems.get(paramInt).getImagen());
            item = arrayItems.get(paramInt);
            fila.titulo.setText(item.getTitulo());
            fila.fecha.setText(dateFormat.format(item.getFecha()));
            if (item.getImagen() != null) {
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inSampleSize = 8;
                Bitmap bmp = BitmapFactory.decodeByteArray(item.getImagen(), 0, item.getImagen().length, options);
                fila.imagen.setImageBitmap(bmp);
            } else {
                fila.imagen.setVisibility(View.GONE);
            }
            fila.boton.setOnClickListener(this);
        }
        return paramView;
    }

    public void onClick(View paramView) {
        switch (paramView.getId()) {
            case R.id.btn_compartir_noticia:
                items.clear();
                items.add(new ItemListOpcionesMultimedia("Twitter", R.drawable.ic_twitter_disable));
                //items.add(new ItemListOpcionesMultimedia(getString(R.string.opcion_multimedia_galeria), R.drawable.ic_cambiar_foto));
                ListOpcionesAdapter adapter = new ListOpcionesAdapter(activity, items);
                new AlertDialog.Builder(activity)
                        .setTitle("Compartir noticia en...")
                        .setAdapter(adapter, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialogInterface, int item) {
                                if (item == 0) { /** Compartir en Twitter */
                                    if (!checkIsLoginned(TwitterSocialNetwork.ID)) {
                                        return;
                                    } else {
                                        String titulo = (String) fila.boton.getTag(R.string.TAG_TITULO_NOTICIA);
                                        byte[] imagen = (byte[]) fila.boton.getTag(R.string.TAG_IMAGEN_NOTICIA);
                                        File file = null;
                                        FileOutputStream fos = null;
                                        try {
                                            file = new File("imagen.png");
                                            fos = new FileOutputStream(file);
                                            fos.write(imagen);
                                            fos.flush();
                                            fos.close();
                                        } catch (Exception e) {
                                            Log.e(TAG, "Error al convertir byte[] en File");
                                            e.printStackTrace();
                                        }
                                        if (file != null) {
                                            showProgress("Compartiendo en Twitter...");
                                            socialNetworkManager.getTwitterSocialNetwork().requestPostPhoto(file, titulo);
                                                    new NoticiaOnPostingCompleteListener(null));
                                        } else {
                                            showProgress("Compartiendo en Twitter...");
                                            socialNetworkManager.getTwitterSocialNetwork().requestPostMessage(titulo,
                                                    new NoticiaOnPostingCompleteListener(null));
                                        }
                                    }
                                }
                            }
                        }).create().show();
                break;
        }
    }

    protected boolean checkIsLoginned(int socialNetworkID) {
        if (socialNetworkManager.getSocialNetwork(socialNetworkID).isConnected()) {
            return true;
        }
        AlertDialogFragment
                .newInstance("Requiere loguearse", "Esta acción requiere loguearse, por favor logueate en las configuraciones, opción de \"Cuentas enlazadas\".")
                .show(activity.getSupportFragmentManager(), null);
        return false;
    }

    protected void showProgress(String text) {
        ProgressDialogFragment progressDialogFragment = ProgressDialogFragment.newInstance(text);
        progressDialogFragment.setTargetFragment(fragment, 0);
        progressDialogFragment.show(activity.getSupportFragmentManager(), PROGRESS_DIALOG_TAG);
    }

    protected void hideProgress() {
        Fragment fragment = activity.getSupportFragmentManager().findFragmentByTag(PROGRESS_DIALOG_TAG);
        if (fragment != null) {
            activity.getSupportFragmentManager().beginTransaction().remove(fragment).commit();
        }
    }

    protected void handleError(String text) {
        AlertDialogFragment.newInstance("Error", text).show(activity.getSupportFragmentManager(), null);
    }
    protected void handleSuccess(String title, String message) {
        AlertDialogFragment.newInstance(title, message).show(activity.getSupportFragmentManager(), null);
    }

    private class NoticiaOnPostingCompleteListener implements OnPostingCompleteListener {
        private String mmMessage;
        private NoticiaOnPostingCompleteListener(String message) {
            mmMessage = message;
        }
        @Override
        public void onPostSuccessfully(int socialNetworkID) {
            hideProgress();
            handleSuccess("Exito", "Noticia posteada correctamente");
        }
        @Override
        public void onError(int socialNetworkID, String requestID, String errorMessage, Object data) {
            hideProgress();
            handleError(errorMessage);
        }
    }
}
