package edu.ucla.fusa.android.fragmentos;

import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;


import com.androidsocialnetworks.lib.SocialNetwork;
import com.androidsocialnetworks.lib.SocialNetworkManager;
import com.androidsocialnetworks.lib.listener.OnLoginCompleteListener;

import edu.ucla.fusa.android.R;
import edu.ucla.fusa.android.adaptadores.ListAplicationsAdapter;
import edu.ucla.fusa.android.modelo.herramientas.AlertDialogFragment;
import edu.ucla.fusa.android.modelo.herramientas.ItemListAplications;
import edu.ucla.fusa.android.modelo.herramientas.ProgressDialogFragment;

import java.util.ArrayList;

public class ConfiguracionAplicacionesFragment extends Fragment implements AdapterView.OnItemClickListener, OnLoginCompleteListener, SocialNetworkManager.OnInitializationCompleteListener{

    private static String TAG = "ConfiguracionAplicacionesFragment";
    public static final String SOCIAL_NETWORK_TAG = "ConfiguracionAplicacionesFragment.SOCIAL_NETWORK_TAG";
    private static final String PROGRESS_DIALOG_TAG = "ConfiguracionAplicacionesFragment.PROGRESS_DIALOG_TAG";
    private ListAplicationsAdapter adapter;
    private TypedArray aplicationIcons;
    private ImageView checkApp;
    private ImageView iconoApp;
    private ArrayList<ItemListAplications> items;
    private ListView list;
    private TextView tituloApp;
    private String[] titulos;
    private SocialNetworkManager socialNetworkManager;
    protected boolean socialNetworkManagerInitialized = false;

    public static ConfiguracionAplicacionesFragment newInstance() {
        ConfiguracionAplicacionesFragment fragment = new ConfiguracionAplicacionesFragment();
        fragment.setRetainInstance(true);
        return fragment;
    }


    public void onCreate(Bundle paramBundle) {
        super.onCreate(paramBundle);
        getActivity().getActionBar().setIcon(R.drawable.ic_cuentas_enlazadas_white);
        getActivity().getActionBar().setTitle(R.string.configuracion_aplicaciones_cuentas_enlazadas);

        socialNetworkManager = (SocialNetworkManager) getFragmentManager().findFragmentByTag(SOCIAL_NETWORK_TAG);

        if (socialNetworkManager == null) {
            socialNetworkManager = SocialNetworkManager.Builder.from(getActivity())
                    .twitter("yVdvpAPRFsLfQa1Sgqlu2Kb26", "9XLmtFSSMuDSz4r2bZKuiAZDRBsmH2Uz6cdLIg4lSfeqheMHHV")
                    //.facebook()
                    .build();
            getFragmentManager().beginTransaction()
                    .add(socialNetworkManager, SOCIAL_NETWORK_TAG)
                    .commit();
            socialNetworkManager.setOnInitializationCompleteListener(this);

        } else {
            socialNetworkManagerInitialized = true;
        }
    }

    public View onCreateView(LayoutInflater paramLayoutInflater, ViewGroup paramViewGroup, Bundle paramBundle) {
        super.onCreateView(paramLayoutInflater, paramViewGroup, paramBundle);
        return paramLayoutInflater.inflate(R.layout.fragment_configuraciones_aplicaciones, paramViewGroup, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        list = (ListView) view.findViewById(R.id.list_view_aplicaciones);
        aplicationIcons = getResources().obtainTypedArray(R.array.icons_app);
        titulos = getResources().getStringArray(R.array.redes_app);
        items = new ArrayList();
        items.clear();
        items.add(new ItemListAplications(
                titulos[0],
                aplicationIcons.getResourceId(0, -1), false));

        items.add(new ItemListAplications(
                titulos[1],
                aplicationIcons.getResourceId(1, -1), false));

        items.add(new ItemListAplications(
                titulos[2],
                aplicationIcons.getResourceId(2, -1), false));
        adapter = new ListAplicationsAdapter(getActivity(), items);
        list.setAdapter(adapter);
        list.setOnItemClickListener(this);

        if (socialNetworkManagerInitialized) {
            onSocialNetworkManagerInitialized();
        }
    }

    public void onItemClick(AdapterView<?> paramAdapterView, View paramView, int paramInt, long paramLong) {
        iconoApp = ((ImageView) paramView.findViewById(R.id.ivIconoApp));
        tituloApp = ((TextView) paramView.findViewById(R.id.tvTituloApp));
        checkApp = ((ImageView) paramView.findViewById(R.id.ivIconoActivacionApp));
        switch (paramInt) {
            case 0:
                socialNetworkManager.getFacebookSocialNetwork().requestLogin();
                if (checkApp.getVisibility() == View.INVISIBLE) {
                    checkApp.setVisibility(View.VISIBLE);
                    iconoApp.setImageResource(R.drawable.ic_facebook_enable);
                    tituloApp.setTextColor(getResources().getColor(android.R.color.holo_blue_light));
                    break;
                }
                checkApp.setVisibility(View.INVISIBLE);
                iconoApp.setImageResource(R.drawable.ic_facebook_disable);
                tituloApp.setTextColor(getResources().getColor(R.color.gris_oscuro));
                break;
            case 1:
                if (checkApp.getVisibility() == View.INVISIBLE) {
                    checkApp.setVisibility(View.VISIBLE);
                    iconoApp.setImageResource(R.drawable.ic_google_enable);
                    tituloApp.setTextColor(getResources().getColor(android.R.color.holo_blue_light));
                    break;
                }
                checkApp.setVisibility(View.INVISIBLE);
                iconoApp.setImageResource(R.drawable.ic_google_disable);
                tituloApp.setTextColor(getResources().getColor(R.color.gris_oscuro));
                break;
            case 2:
                //Si esta conectado, puedo cerrar sesión
                if (socialNetworkManager.getTwitterSocialNetwork().isConnected()) {
                    socialNetworkManager.getTwitterSocialNetwork().logout();
                    if (!socialNetworkManager.getTwitterSocialNetwork().isConnected()) {
                        checkApp.setVisibility(View.INVISIBLE);
                        iconoApp.setImageResource(R.drawable.ic_twitter_disable);
                        tituloApp.setTextColor(getResources().getColor(R.color.gris_oscuro));
                    }
                } else { //Si no esta conectado, me logueo
                    showProgress("Autenticando... Twitter");
                    socialNetworkManager.getTwitterSocialNetwork().requestLogin();
                    //checkApp.setVisibility(View.VISIBLE);
                    //iconoApp.setImageResource(R.drawable.ic_twitter_enable);
                    //tituloApp.setTextColor(getResources().getColor(android.R.color.holo_blue_light));
                }
                break;
        }
    }

    @Override
    public void onLoginSuccess(int i) {
        // let's reset buttons, we need to disable buttons
        onSocialNetworkManagerInitialized();
        hideProgress();
        handleSuccess("onLoginSuccess", "Now you can try other API Demos.");

        items.clear();
        items.add(new ItemListAplications(
                titulos[0],
                aplicationIcons.getResourceId(0, -1), false));

        items.add(new ItemListAplications(
                titulos[1],
                aplicationIcons.getResourceId(1, -1), false));
        if (socialNetworkManager.getTwitterSocialNetwork().isConnected()) {
            items.add(new ItemListAplications(
                    titulos[2],
                    R.drawable.ic_twitter_enable, true));
        } else {
            items.add(new ItemListAplications(
                    titulos[2],
                    aplicationIcons.getResourceId(2, -1), false));
        }
        adapter = new ListAplicationsAdapter(getActivity(), items);
        list.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onError(int socialNetworkID, String requestID, String errorMessage, Object data) {
        hideProgress();
        handleError(errorMessage);

        items.clear();
        items.add(new ItemListAplications(
                titulos[0],
                aplicationIcons.getResourceId(0, -1), false));

        items.add(new ItemListAplications(
                titulos[1],
                aplicationIcons.getResourceId(1, -1), false));
        if (socialNetworkManager.getTwitterSocialNetwork().isConnected()) {
            items.add(new ItemListAplications(
                    titulos[2],
                    R.drawable.ic_twitter_enable, true));
        } else {
            items.add(new ItemListAplications(
                    titulos[2],
                    aplicationIcons.getResourceId(2, -1), false));
        }
        adapter = new ListAplicationsAdapter(getActivity(), items);
        list.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onSocialNetworkManagerInitialized() {

        for (SocialNetwork socialNetwork : socialNetworkManager.getInitializedSocialNetworks()) {
            socialNetwork.setOnLoginCompleteListener(this);
        }

        items.clear();
        items.add(new ItemListAplications(
                titulos[0],
                aplicationIcons.getResourceId(0, -1), false));

        items.add(new ItemListAplications(
                titulos[1],
                aplicationIcons.getResourceId(1, -1), false));
        if (socialNetworkManager.getTwitterSocialNetwork().isConnected()) {
            items.add(new ItemListAplications(
                    titulos[2],
                    R.drawable.ic_twitter_enable, true));
        } else {
            items.add(new ItemListAplications(
                    titulos[2],
                    aplicationIcons.getResourceId(2, -1), false));
        }
        adapter = new ListAplicationsAdapter(getActivity(), items);
        adapter.notifyDataSetChanged();
    }

    protected void hideProgress() {
        Fragment fragment = getFragmentManager().findFragmentByTag(PROGRESS_DIALOG_TAG);
        if (fragment != null) {
            getFragmentManager().beginTransaction().remove(fragment).commit();
        }
    }

    protected void handleError(String text) {
        AlertDialogFragment.newInstance("Error", text).show(getFragmentManager(), null);
    }
    protected void handleSuccess(String title, String message) {
        AlertDialogFragment.newInstance(title, message).show(getFragmentManager(), null);
    }

    protected void showProgress(String text) {
        ProgressDialogFragment progressDialogFragment = ProgressDialogFragment.newInstance(text);
        progressDialogFragment.setTargetFragment(this, 0);
        progressDialogFragment.show(getFragmentManager(), PROGRESS_DIALOG_TAG);
    }

    public void onRequestCancel() {
        Log.d(TAG, "ConfiguracionAplicacionesFragment.onRequestCancel");
        for (SocialNetwork socialNetwork : socialNetworkManager.getInitializedSocialNetworks()) {
            socialNetwork.cancelAll();
        }
    }

    protected boolean checkIsLoginned(int socialNetworkID) {
        if (socialNetworkManager.getSocialNetwork(socialNetworkID).isConnected()) {
            return true;
        }
        AlertDialogFragment
                .newInstance("Request Login", "This action request login, please go to login demo first.")
                .show(getFragmentManager(), null);
        return false;
    }
}
