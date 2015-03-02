package doacao.doacao2.Activities;

import android.content.Context;
import android.location.Location;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.parse.ParseException;
import com.parse.SaveCallback;

import doacao.doacao2.Objects.Desire;
import doacao.doacao2.R;


public class AddDesireActivity extends ActionBarActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private EditText mEmail;
    private EditText mPhone;
    private EditText mLatitude;
    private EditText mLongitude;
    private Spinner mItems;
    private Button mSubmit;
    private Context mContext;
    private GoogleApiClient mGoogleApiClient;
    private GoogleMap map;
    private Location mLastLocation;
    private Marker loc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_desire);
        mContext = this;
        mEmail = (EditText)findViewById(R.id.edit_email);
        mPhone = (EditText)findViewById(R.id.edit_phone);
        mLatitude = (EditText)findViewById(R.id.latitude);
        mLongitude = (EditText)findViewById(R.id.longitude);
        mItems = (Spinner)findViewById(R.id.items);
        mSubmit = (Button)findViewById(R.id.submit);
        mSubmit.setOnClickListener(mOnSubmitClickListener);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,R.array.items,android.R.layout.simple_spinner_item);
        mItems.setAdapter(adapter);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.AAD_map);
        mapFragment.getMapAsync(this);

        buildGoogleApiClient();
    }

    private View.OnClickListener mOnSubmitClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v){
            String email = mEmail.getText().toString();
            String phone = mPhone.getText().toString();
            //double latitude = Double.parseDouble(mLatitude.getText().toString());
            //double longitude = Double.parseDouble(mLongitude.getText().toString());
            LatLng aux = loc.getPosition();
            String item = mItems.getSelectedItem().toString();
            Desire desire = new Desire(email,phone,aux.latitude,aux.longitude,item);
            desire.saveInBackground(new SaveCallback(){
                @Override
                public void done(ParseException e) {
                    if(e == null){
                        Toast.makeText(mContext,getString(R.string.cadatrado_com_sucesso),Toast.LENGTH_SHORT).show();
                        finish();
                    }
                    else{
                        Toast.makeText(mContext,getString(R.string.erro),Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_add_desire, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        map.setMyLocationEnabled(true);
        map.getUiSettings().setMyLocationButtonEnabled(true);
        map.getUiSettings().setZoomControlsEnabled(true);
        map.getUiSettings().setAllGesturesEnabled(true);
        if(mLastLocation != null) {
            LatLng position = new LatLng(mLastLocation.getLatitude(),mLastLocation.getLongitude());
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(position, 13));
            loc = map.addMarker(new MarkerOptions()
                    .position(position)
                    .draggable(true));
        }
    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }

    @Override
    public void onConnected(Bundle bundle) {
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        if(map != null) {
            //LatLng position = new LatLng(mLastLocation.getLatitude(),mLastLocation.getLongitude());
            LatLng position = new LatLng(mLastLocation.getLatitude(),mLastLocation.getLongitude());
            map.animateCamera(CameraUpdateFactory.newLatLngZoom(position, 13));
            loc = map.addMarker(new MarkerOptions()
                    .position(position)
                    .draggable(true));

        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }
}
