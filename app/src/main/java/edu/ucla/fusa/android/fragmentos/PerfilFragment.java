package edu.ucla.fusa.android.fragmentos;

import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import de.hdodenhof.circleimageview.CircleImageView;
import edu.ucla.fusa.android.R;
import edu.ucla.fusa.android.adaptadores.ListOpcionesAdapter;
import edu.ucla.fusa.android.modelo.ItemListOpcionesMultimedia;
import java.io.File;
import java.util.ArrayList;

public class PerfilFragment extends Fragment implements View.OnClickListener {

    private static final int CROP_FROM_CAMERA = 2;
    private static final int PICK_FROM_CAMERA = 1;
    private static final int PICK_FROM_FILE = 3;
    private Button cambiarPassword;
    private CircleImageView foto;
    private ArrayList<ItemListOpcionesMultimedia> items = new ArrayList();
    private Uri mFotoCaptureUri;
    private View view;

    private void doCrop() {
        startActivityForResult(new Intent("com.android.camera.action.CROP")
                .setDataAndType(mFotoCaptureUri, "image/*")
                .putExtra("crop", "true")
                .putExtra("outputX", 200)
                .putExtra("outputY", 200)
                .putExtra("aspectX", 1)
                .putExtra("aspectY", 1)
                .putExtra("scale", true)
                .putExtra("return-data", true), 2);
    }

    public static PerfilFragment newInstance() {
        PerfilFragment fragment = new PerfilFragment();
        fragment.setRetainInstance(true);
        return fragment;
    }

    private void showDialog() {
        items.clear();
        items.add(new ItemListOpcionesMultimedia(getString(R.string.opcion_multimedia_camara), R.drawable.ic_camara));
        items.add(new ItemListOpcionesMultimedia(getString(R.string.opcion_multimedia_galeria), R.drawable.ic_cambiar_foto));
        ListOpcionesAdapter localListOpcionesAdapter = new ListOpcionesAdapter(getActivity(), this.items);
        new AlertDialog.Builder(getActivity()).setAdapter(localListOpcionesAdapter, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface paramAnonymousDialogInterface, int paramAnonymousInt) {
                if (paramAnonymousInt == 0) {
                    Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
                    //intent.PerfilFragment.access$002(PerfilFragment.this, Uri.fromFile(new File(Environment.getExternalStorageDirectory(), "tmp_avatar_" + String.valueOf(System.currentTimeMillis()) + ".jpg")));
                    intent.putExtra("output", mFotoCaptureUri);
                    try {
                        intent.putExtra("return-data", true);
                        startActivityForResult(intent, 1);
                    } catch (ActivityNotFoundException e) {
                        e.printStackTrace();
                    }
                } else {
                    Intent intent = new Intent();
                    intent.setType("image/*");
                    intent.setAction("android.intent.action.GET_CONTENT");
                    PerfilFragment.this.startActivityForResult(Intent.createChooser(intent, "Completa la acción usando..."), 3);
                }
            }
        }).create().show();
    }

    public void onActivityResult(int paramInt1, int paramInt2, Intent paramIntent) {
        switch (paramInt1) {
        case PICK_FROM_CAMERA:
            doCrop();
            break;
        case PICK_FROM_FILE:
            mFotoCaptureUri = paramIntent.getData();
            doCrop();
            break;
        case CROP_FROM_CAMERA:
            Bundle b = paramIntent.getExtras();
            if (b != null) {
                Bitmap localBitmap = b.getParcelable("data");
                foto.setImageBitmap(localBitmap);
                ((CircleImageView) getActivity().findViewById(R.id.iv_foto_perfil_drawer)).setImageBitmap(localBitmap);
            }
          //localFile = new File(mFotoCaptureUri.getPath());
        }
    }

    public void onClick(View paramView) {
        switch (paramView.getId()) {
        case R.id.btn_cambiar_password_perfil:
            getFragmentManager().beginTransaction().replace(android.R.id.content, ConfiguracionCambiarPasswordFragment.newInstance()).addToBackStack(null).commit();
            return;
        case R.id.iv_foto_perfil:
            showDialog();
            break;
        }
    }

    public void onCreate(Bundle paramBundle) {
        super.onCreate(paramBundle);
        getActivity().getActionBar().setIcon(R.drawable.ic_perfil);
        getActivity().getActionBar().setTitle(R.string.contenido_perfil_action_bar_titulo);
    }

    public View onCreateView(LayoutInflater paramLayoutInflater, ViewGroup paramViewGroup, Bundle paramBundle) {
        super.onCreateView(paramLayoutInflater, paramViewGroup, paramBundle);
        view = paramLayoutInflater.inflate(R.layout.fragment_drawer_perfil, paramViewGroup, false);
        foto = ((CircleImageView) view.findViewById(R.id.iv_foto_perfil));
        foto.setOnClickListener(this);
        cambiarPassword = ((Button) view.findViewById(R.id.btn_cambiar_password_perfil));
        cambiarPassword.setOnClickListener(this);
        return view;
    }
}
