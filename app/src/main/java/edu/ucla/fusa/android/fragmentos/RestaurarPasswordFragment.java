package edu.ucla.fusa.android.fragmentos;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import com.ikimuhendis.ldrawer.DrawerArrowDrawable;
import com.juanlabrador.GroupLayout;

import edu.ucla.fusa.android.R;
import edu.ucla.fusa.android.validadores.ValidadorEmails;

public class RestaurarPasswordFragment extends Fragment implements View.OnClickListener, TextWatcher, Toolbar.OnMenuItemClickListener {

    private GroupLayout mCorreo;
    private View mView;
    private Toolbar mToolbar;
    private DrawerArrowDrawable mDrawerArrow;

    public static RestaurarPasswordFragment newInstance() {
        RestaurarPasswordFragment fragment = new RestaurarPasswordFragment();
        fragment.setRetainInstance(true);
        return fragment;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle arguments) {
        super.onCreateView(inflater, container, arguments);

        mView = inflater.inflate(R.layout.fragment_inicial_restaurar_password, container, false);
         
        mCorreo = (GroupLayout) mView.findViewById(R.id.correo_restaurar);
        mCorreo.addValidatorLayout(R.string.restaurar_correo);
        mCorreo.getValidatorLayoutAt(0).getEditText().addTextChangedListener(this);
        mCorreo.getValidatorLayoutAt(0).setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
        return mView;
    }

    public void onClick(View paramView) {
        
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
        mToolbar.setTitle(R.string.postularse_titulo_barra);
        mToolbar.setNavigationIcon(mDrawerArrow);
        mToolbar.inflateMenu(R.menu.action_enviar);
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
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
        
    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
        if (ValidadorEmails.validarEmail(mCorreo.getValidatorLayoutAt(0).getContent()) != true) {
            mCorreo.getValidatorLayoutAt(0).dataError();
        } else {
            mCorreo.getValidatorLayoutAt(0).dataCheck();
        }
    }

    @Override
    public void afterTextChanged(Editable editable) {

    }

    @Override
    public boolean onMenuItemClick(MenuItem menuItem) {
        return true;
    }
    
}