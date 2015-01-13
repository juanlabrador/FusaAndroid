package edu.ucla.fusa.android.fragmentos;

import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import de.hdodenhof.circleimageview.CircleImageView;
import edu.ucla.fusa.android.R;
import edu.ucla.fusa.android.adaptadores.ListOpcionesAdapter;
import edu.ucla.fusa.android.modelo.herramientas.ItemListOpcionesMultimedia;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;

public class DrawerPerfilFragment extends Fragment implements View.OnClickListener {

    private static final int CROP_FROM_CAMERA = 2;
    private static final int PICK_FROM_CAMERA = 1;
    private static final int PICK_FROM_FILE = 3;
    private Button cambiarPassword;
    private CircleImageView foto;
    private ArrayList<ItemListOpcionesMultimedia> items = new ArrayList();
    private Uri mFotoCaptureUri;
    private View view;

    public static DrawerPerfilFragment newInstance() {
        DrawerPerfilFragment fragment = new DrawerPerfilFragment();
        fragment.setRetainInstance(true);
        return fragment;
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

    private byte[] convertImageToByte(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 90, stream);
        return  stream.toByteArray();
    }

    private void showDialog() {
        items.clear();
        items.add(new ItemListOpcionesMultimedia(getString(R.string.opcion_multimedia_camara), R.drawable.ic_camara));
        items.add(new ItemListOpcionesMultimedia(getString(R.string.opcion_multimedia_galeria), R.drawable.ic_cambiar_foto));
        ListOpcionesAdapter adapter = new ListOpcionesAdapter(getActivity(), items);
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
                        Bitmap bitmap = b.getParcelable("data");
                        //foto.setImageBitmap(bitmap);
                        ((CircleImageView) getActivity().findViewById(R.id.iv_foto_perfil_drawer)).setImageBitmap(bitmap);
                    }
                    file = new File(mFotoCaptureUri.getPath());
                    if (file.exists()) {
                        file.delete();
                    }
                    break;
            }
        }
    }

    public void onCreate(Bundle paramBundle) {
        super.onCreate(paramBundle);
        getActivity().getActionBar().setIcon(R.drawable.ic_usuario_blanco);
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
}
