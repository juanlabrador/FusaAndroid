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
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import edu.ucla.fusa.android.R;

/**
 * Created by juanlabrador on 26/10/14.
 */
public class ViewPagerEventoUbicacionFragment extends Fragment {

    private GoogleMap gMap;
    private View view;
    private static double LATITUD = 10.066191;
    private static double LONGITUD = -69.316363;
    private LatLng positionMap; //Variable para indicar coordenadas.
    private CameraPosition cameraPosition; //Variable para definir la ubicación en el mapa.
    private CameraUpdate cameraUpdate; //Variable para movimiento de la camara.

    public static ViewPagerEventoUbicacionFragment newInstance() {
        ViewPagerEventoUbicacionFragment fragment = new ViewPagerEventoUbicacionFragment();
        fragment.setRetainInstance(true);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_viewpager_evento_ubicacion, container, false);
        inicializarMapa();
        return view;
    }

    private void inicializarMapa() {
        if (gMap == null) {
            gMap = ((SupportMapFragment) getFragmentManager()
                    .findFragmentById(R.id.map_ubicacion_evento)).getMap();

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
                .title("Teatro Juarez"));
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

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        /**try {
            fragment = (SupportMapFragment) getActivity().getSupportFragmentManager()
                    .findFragmentById(R.id.map_ubicacion_evento);
            if (fragment != null) {
                getFragmentManager().beginTransaction().remove(fragment).commit();
            }
        } catch (IllegalStateException e) {

        }*/
    }
}
