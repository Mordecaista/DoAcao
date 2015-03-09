package doacao.doacao2.Activities;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.location.Location;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.Arrays;

import doacao.doacao2.MultiSpinner;
import doacao.doacao2.Objects.Desire;
import doacao.doacao2.R;


public class AddDesireActivity extends ActionBarActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, MultiSpinner.MultiSpinnerListener{

    private EditText mEmail;
    private EditText mPhone;
    private MultiSpinner mItems;
    private Button mSubmit;
    private Context mContext;
    private GoogleApiClient mGoogleApiClient;
    private GoogleMap map;
    private Location mLastLocation;
    private Marker loc;
    private ArrayList<String> items;
    private String[] possibleItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_desire);
        mContext = this;
        mEmail = (EditText)findViewById(R.id.edit_email);
        mPhone = (EditText)findViewById(R.id.edit_phone);
        mItems = (MultiSpinner)findViewById(R.id.items);
        mSubmit = (Button)findViewById(R.id.submit);
        mSubmit.setOnClickListener(mOnSubmitClickListener);

        mEmail.setText(ParseUser.getCurrentUser().getEmail());
        TelephonyManager tMgr = (TelephonyManager)mContext.getSystemService(Context.TELEPHONY_SERVICE);
        String mPhoneNumber = tMgr.getLine1Number();
        if(mPhoneNumber != null && !mPhoneNumber.equals("")){
            mPhone.setText(mPhoneNumber);
        }

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.AAD_map);
        mapFragment.getMapAsync(this);

        Resources res = getResources();
        possibleItems = res.getStringArray(R.array.items);
        mItems.setItems(Arrays.asList(possibleItems), getString(R.string.selectItems), this);

        buildGoogleApiClient();
    }

    private View.OnClickListener mOnSubmitClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v){
            String email = mEmail.getText().toString();
            String phone = mPhone.getText().toString();
            LatLng aux = loc.getPosition();
            Desire desire = new Desire(email,phone,aux.latitude,aux.longitude,items);
            desire.saveInBackground(new SaveCallback(){
                @Override
                public void done(ParseException e) {
                    if(e == null){
                        Toast.makeText(mContext,getString(R.string.submitted_with_success),Toast.LENGTH_SHORT).show();
                        finish();
                    }
                    else{
                        Toast.makeText(mContext,getString(R.string.error),Toast.LENGTH_SHORT).show();
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
        if (id == R.id.AAD_logout) {
            ParseUser.logOut();
            Intent intent = new Intent(this,LoginActivity.class);
            startActivity(intent);
            finish();
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
        map.setOnMarkerDragListener(new GoogleMap.OnMarkerDragListener() {
            @Override
            public void onMarkerDragStart(Marker marker) {
                //STUB
            }

            @Override
            public void onMarkerDrag(Marker marker) {
                //STUB
            }

            @Override
            public void onMarkerDragEnd(Marker marker) {
                //STUB
            }
        });
        if(mLastLocation != null) {
            LatLng position = new LatLng(mLastLocation.getLatitude(),mLastLocation.getLongitude());
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(position, 13));
            loc = map.addMarker(new MarkerOptions()
                    .position(position)
                    .draggable(true));
        }
        else{
            loc = map.addMarker(new MarkerOptions()
                    .position(new LatLng(0.0, 0.0))
                    .draggable(true));
        }
    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();
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

    @Override
    public void onItemsSelected(boolean[] selected) {
        ArrayList<String> aux = new ArrayList<String>();
        for(int i = 0; i < selected.length; i++){
            if(selected[i]) aux.add(possibleItems[i]);
        }
        this.items = aux;
    }
}
