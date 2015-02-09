package edu.ucla.fusa.android.fragmentos;

import android.app.NotificationManager;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.NotificationCompat;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import com.ikimuhendis.ldrawer.DrawerArrowDrawable;
import com.juanlabrador.grouplayout.GroupContainer;
import com.nispok.snackbar.Snackbar;
import com.nispok.snackbar.SnackbarManager;
import com.nispok.snackbar.enums.SnackbarType;

import edu.ucla.fusa.android.R;
import edu.ucla.fusa.android.modelo.herramientas.JSONParser;

public class RestaurarPasswordFragment extends Fragment implements Toolbar.OnMenuItemClickListener {

    private GroupContainer mUser;
    private Toolbar mToolbar;
    private DrawerArrowDrawable mDrawerArrow;
    private RestaurarPassword mServiceRestore;
    private JSONParser mJSONParser;
    private NotificationManager mManager;

    public static RestaurarPasswordFragment newInstance() {
        RestaurarPasswordFragment fragment = new RestaurarPasswordFragment();
        fragment.setRetainInstance(true);
        return fragment;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle arguments) {
        super.onCreateView(inflater, container, arguments);
        return inflater.inflate(R.layout.fragment_inicial_restaurar_password, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mDrawerArrow = new DrawerArrowDrawable(getActivity()) {
            @Override
            public boolean isLayoutRtl() {
                return false;
            }
        };

        mDrawerArrow.setProgress(1f);

        mToolbar = (Toolbar) view.findViewById(R.id.toolbar);
        mToolbar.setTitle(R.string.restaurar_titulo_barra);
        mToolbar.setNavigationIcon(mDrawerArrow);
        mToolbar.inflateMenu(R.menu.action_enviar);
        mToolbar.setOnMenuItemClickListener(this);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //mFragment.restoreData();
                getFragmentManager().popBackStack();
                /*getFragmentManager()
                        .beginTransaction()
                        .replace(android.R.id.content, LoginFragment.newInstance())
                        .commit();*/
            }
        });
        mUser = (GroupContainer) view.findViewById(R.id.correo_restaurar);
        mUser.addEditTextLayout(R.string.restaurar_user);
        mJSONParser = new JSONParser();
    }  
    
    @Override
    public boolean onMenuItemClick(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.action_enviar:
                if (!mUser.getEditTextLayoutAt(0).getContent().equals("")) {
                    mServiceRestore = new RestaurarPassword();
                    mServiceRestore.execute(mUser.getEditTextLayoutAt(0).getContent());
                }
        }
        return true;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (mServiceRestore != null) {
            if (!mServiceRestore.isCancelled()) {
                mServiceRestore.cancel(true);
            }
        }
    }

    private class RestaurarPassword extends AsyncTask<String, Void, Integer> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if (mToolbar.getMenu().findItem(R.id.action_enviar).getActionView() == null) {
                mToolbar.getMenu().findItem(R.id.action_enviar).setActionView(R.layout.custom_progress_bar);
            }
            sendNotificacion();
        }

        @Override
        protected Integer doInBackground(String... strings) {
            return mJSONParser.serviceRestaurarPassword(strings[0]);
        }

        @Override
        protected void onPostExecute(Integer result) {
            super.onPostExecute(result);
            mManager.cancel(1);
            switch (result) {
                case 100:
                    SnackbarManager.show(
                            Snackbar.with(getActivity())
                                    .type(SnackbarType.MULTI_LINE)
                                    .text(R.string.restaurar_mensaje));
                    getFragmentManager().popBackStack();
                    break;
                case 0:
                    mToolbar.getMenu().findItem(R.id.action_enviar).setActionView(null);

                    SnackbarManager.show(
                            Snackbar.with(getActivity())
                                    .type(SnackbarType.MULTI_LINE)
                                    .text(R.string.mensaje_error_servidor));
                    break;
                case -1:
                    mToolbar.getMenu().findItem(R.id.action_enviar).setActionView(null);
                    SnackbarManager.show(
                            Snackbar.with(getActivity())
                                    .type(SnackbarType.MULTI_LINE)
                                    .text(R.string.restaurar_error_user));
                    break;
            }
        }
    }

    private void sendNotificacion() {
        try {
            NotificationCompat.Builder mNotificacion = new NotificationCompat.Builder(getActivity())
                    .setSmallIcon(R.drawable.ic_notification)
                    .setContentTitle(getString(R.string.restaurar_enviar))
                    .setTicker(getString(R.string.restaurar_enviar))
                    .setPriority(NotificationCompat.PRIORITY_HIGH);

            mNotificacion.setAutoCancel(true);
            mManager = (NotificationManager) getActivity().getSystemService(Context.NOTIFICATION_SERVICE);
            mManager.notify(1, mNotificacion.build());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}