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

public class MapaFundacionFragment extends Fragment {

    private static double LATITUD = 10.064757D;
    private static double LONGITUD = -69.282445999999993D;
    public static String TAG = "MapaContacto";
    private CameraPosition cameraPosition;
    private CameraUpdate cameraUpdate;
    private FragmentManager fm;
    private SupportMapFragment fragment;
    private GoogleMap gMap;
    private LatLng positionMap;

    public static MapaFundacionFragment newInstance() {
        MapaFundacionFragment fragment = new MapaFundacionFragment();
        fragment.setRetainInstance(true);
        return fragment;
    }

    public void onActivityCreated(Bundle paramBundle) {
        super.onActivityCreated(paramBundle);
        fm = getChildFragmentManager();
        fragment = ((SupportMapFragment) fm.findFragmentById(R.id.mapa_ubicacion_contacto));
        if (fragment == null) {
          fragment = SupportMapFragment.newInstance();
          fm.beginTransaction().replace(R.id.mapa_ubicacion_contacto, fragment).commit();
        }
    }

    public View onCreateView(LayoutInflater paramLayoutInflater, ViewGroup paramViewGroup, Bundle paramBundle) {
        return paramLayoutInflater.inflate(R.layout.fragment_inicial_fundacion_mapa, paramViewGroup, false);
    }

    public void onResume() {
        super.onResume();
        if (gMap == null) {
          gMap = fragment.getMap();
          positionMap = new LatLng(LATITUD, LONGITUD);
          cameraPosition = new CameraPosition.Builder().target(positionMap).zoom(16.0F).build();
          gMap.addMarker(new MarkerOptions().position(new LatLng(LATITUD, LONGITUD)).title(getString(R.string.mapa_fundacion)));
          gMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
          cameraUpdate = CameraUpdateFactory.newCameraPosition(cameraPosition);
          gMap.animateCamera(cameraUpdate);
        }
    }
}
