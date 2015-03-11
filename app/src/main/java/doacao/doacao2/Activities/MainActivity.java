package doacao.doacao2.Activities;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.location.Location;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.List;

import doacao.doacao2.ListArrayAdapter;
import doacao.doacao2.Objects.Institution;
import doacao.doacao2.R;


public class MainActivity extends ActionBarActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private GoogleMap map;
    private Location mLastLocation;
    private GoogleApiClient mGoogleApiClient;
    private HashMap<String,Marker> markers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ParseUser current = ParseUser.getCurrentUser();
        if(current == null){
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();
        }
        else if(current.getString("type").equals("institution")){
            Intent intent = new Intent(this, InstitutionRegisterActivity.class);
            startActivity(intent);
            finish();
        }
        setContentView(R.layout.activity_main);

        markers = new HashMap<String,Marker>();

        MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.container);
        mapFragment.getMapAsync(this);

        buildGoogleApiClient();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        // Associate searchable configuration with the SearchView
        SearchManager searchManager =
                (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView =
                (SearchView) menu.findItem(R.id.action_search).getActionView();
        searchView.setSearchableInfo(
                searchManager.getSearchableInfo(getComponentName()));

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_add) {
            Intent intent = new Intent(this,AddDesireActivity.class);
            startActivity(intent);
            return true;
        }
        else if (id == R.id.action_logout) {
            ParseUser.logOut();
            Intent intent = new Intent(this,LoginActivity.class);
            startActivity(intent);
            finish();
        }
        else if (id == R.id.action_myDesires){
            Intent intent = new Intent(this,MyDesiresActivity.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onMapReady(GoogleMap map) {
        this.map = map;
        map.setMyLocationEnabled(true);
        map.getUiSettings().setMyLocationButtonEnabled(true);
        map.getUiSettings().setZoomControlsEnabled(true);
        map.getUiSettings().setAllGesturesEnabled(true);
        if(mLastLocation != null) {
            LatLng position = new LatLng(mLastLocation.getLatitude(),mLastLocation.getLongitude());
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(position, 13));
        }
        loadInstitutions();
    }

    @Override
    public void onNewIntent(Intent intent) {
        setIntent(intent);
        handleIntent(intent);
    }

    private void handleIntent(Intent intent) {
        markers.get(intent.getStringExtra("objectId")).showInfoWindow();
        map.animateCamera(CameraUpdateFactory.newLatLngZoom(markers.get(intent.getStringExtra("objectId")).getPosition(),13));
    }

    public void loadInstitutions(){
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Institution");
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> parseObjects, ParseException e) {
                if(e==null){
                    for(int i = 0; i < parseObjects.size(); i++){
                        ParseGeoPoint geopoint = ((Institution) parseObjects.get(i)).getLocation();
                        LatLng location = new LatLng(geopoint.getLatitude(),geopoint.getLongitude());
                        Marker marker = map.addMarker(new MarkerOptions()
                                .title(((Institution)parseObjects.get(i)).getName())
                                .snippet(((Institution)parseObjects.get(i)).getItems().get(0))//TODO: Fix this to get the whole list formatted correctly
                                .position(location));
                        markers.put(((Institution) parseObjects.get(i)).getObjectId(),marker);
                    }
                }
                else{
                    //TODO:something here? Guess not
                }
            }
        });
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
            LatLng position = new LatLng(mLastLocation.getLatitude(),mLastLocation.getLongitude());
            map.animateCamera(CameraUpdateFactory.newLatLngZoom(position, 13));
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }
}
