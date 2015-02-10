package doacao.doacao2;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.MapView;


public class MapPane extends MapFragment {


    private static final String LATITUDE = "latitude";
    private static final String LONGITUDE = "longitude";

    private double mLatitude;
    private double mLongitude;
    private GoogleMap mMap;
    private MapView mapView;


    public static MapPane newInstance(double latitude, double longitude) {
        MapPane fragment = new MapPane();
        Bundle args = new Bundle();
        args.putDouble(LATITUDE, latitude);
        args.putDouble(LONGITUDE, longitude);
        fragment.setArguments(args);
        return fragment;
    }

    public MapPane() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mLatitude = getArguments().getDouble(LATITUDE);
            mLongitude = getArguments().getDouble(LONGITUDE);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_map, container, false);
        mapView = (MapView) view.findViewById(R.id.mapview);
        return view;
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

}
