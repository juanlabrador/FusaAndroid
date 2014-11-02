package edu.ucla.fusa.android.fragmentos;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import edu.ucla.fusa.android.R;

/**
 * Created by juanlabrador on 26/10/14.
 */
public class InicialViewPagerContactoInformacionUbicacionFragment extends Fragment {

    private GoogleMap gMap;
    private View view;
    private static double LATITUD = 10.064757;
    private static double LONGITUD = -69.282446;
    private LatLng positionMap; //Variable para indicar coordenadas.
    private CameraPosition cameraPosition; //Variable para definir la ubicación en el mapa.
    private CameraUpdate cameraUpdate; //Variable para movimiento de la camara.

    public static InicialViewPagerContactoInformacionUbicacionFragment newInstance() {
        InicialViewPagerContactoInformacionUbicacionFragment activity = new InicialViewPagerContactoInformacionUbicacionFragment();
        activity.setRetainInstance(true);
        return activity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (container == null) {
            return null;
        }

        view = inflater.inflate(R.layout.fragment_viewpager_informacion_contacto_mapa, container, false);
        inicializarMapa();
        return view;
    }

    private void inicializarMapa() {
        if (gMap == null) {
            gMap = ((SupportMapFragment) getActivity().getSupportFragmentManager()
                    .findFragmentById(R.id.mapa_ubicacion_contacto)).getMap();

            if (gMap == null) {
                Toast.makeText(getActivity(), R.string.mensaje_error_crear_mapa, Toast.LENGTH_SHORT).show();
            } else {
                setUpMap();
            }
        }
    }

    private void setUpMap() {
        /** Le asignamos una dirección en coordenadas */
        positionMap = new LatLng(LATITUD, LONGITUD);
        /** Asignamos en donde se va a posicionar la camara, y a que distancia. */
        cameraPosition = new CameraPosition.Builder()
                .target(positionMap)
                .zoom(16)
                .build();
        /** Colocamos un marcador para conocer la posición asignada, a su vez un nombre. */
        gMap.addMarker(new MarkerOptions().position(new LatLng(LATITUD, LONGITUD))
                .title(getString(R.string.fundacion)));
        /** Asignamos un tipo de mapa para visualización */
        gMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        /** Asignamos los datos para que actualice la camara. */
        cameraUpdate = CameraUpdateFactory.newCameraPosition(cameraPosition);
        /** Agrega los cambios en el mapa. */
        gMap.animateCamera(cameraUpdate);
    }

    @Override
    public void onResume() {
        super.onResume();
        inicializarMapa();
    }

}
