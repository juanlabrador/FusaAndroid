package edu.ucla.fusa.android.fragmentos;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import edu.ucla.fusa.android.R;

public class EventoMapaFragment extends Fragment {

    private static double LATITUD = 10.066191D;
    private static double LONGITUD = -69.316362999999996D;
    private CameraPosition cameraPosition;
    private CameraUpdate cameraUpdate;
    private FragmentManager fm;
    private SupportMapFragment fragment;
    private GoogleMap gMap;
    private LatLng positionMap;

    public static EventoMapaFragment newInstance() {
        EventoMapaFragment fragment = new EventoMapaFragment();
        fragment.setRetainInstance(true);
        return fragment;
    }

    public void onActivityCreated(Bundle paramBundle) {
        super.onActivityCreated(paramBundle);
        fm = getChildFragmentManager();
        fragment = ((SupportMapFragment) fm.findFragmentById(R.id.map_ubicacion_evento));
        if (fragment == null) {
            fragment = SupportMapFragment.newInstance();
            fm.beginTransaction().replace(R.id.map_ubicacion_evento, fragment).commit();
        }
    }

    public void onCreate(Bundle paramBundle) {
        super.onCreate(paramBundle);
        getActivity().getActionBar().setIcon(android.R.color.transparent);
        getActivity().getActionBar().setTitle(R.string.contenido_evento_ubicacion_tab_titulo);
    }

    public View onCreateView(LayoutInflater paramLayoutInflater, ViewGroup paramViewGroup, Bundle paramBundle) {
        return paramLayoutInflater.inflate(R.layout.fragment_evento_mapa, paramViewGroup, false);
    }

    public void onResume() {
        super.onResume();
        if (gMap == null) {
            gMap = fragment.getMap();
            positionMap = new LatLng(LATITUD, LONGITUD);
            cameraPosition = new CameraPosition.Builder().target(positionMap).zoom(16.0F).build();
            gMap.addMarker(new MarkerOptions().position(new LatLng(LATITUD, LONGITUD)).title("Teatro Juarez"));
            gMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
            cameraUpdate = CameraUpdateFactory.newCameraPosition(cameraPosition);
            gMap.animateCamera(cameraUpdate);
        }
    }
}
