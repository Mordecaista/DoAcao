package doacao.doacao2.Activities;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

import doacao.doacao2.Objects.Desire;
import doacao.doacao2.R;

public class DesireDetailsActivity extends ActionBarActivity implements OnMapReadyCallback {

    private Desire desire;
    private GoogleMap map;
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_desire_details);
        intent = getIntent();
        TextView user = (TextView)findViewById(R.id.ADD_user);
        TextView items = (TextView)findViewById(R.id.ADD_items);
        TextView email = (TextView)findViewById(R.id.ADD_email);
        TextView phone = (TextView)findViewById(R.id.ADD_phone);

        desire = searchDesire(intent.getStringExtra("objectId"));
        user.setText(desire.getUsername());
        items.setText(formatItems(desire.getItems()));
        email.setText(desire.getEmail());
        phone.setText(desire.getPhone());

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.ADD_map);
        mapFragment.getMapAsync(this);
    }

    private String formatItems(ArrayList<String> items) {
        String text = "";
        for(String n : items){
            text += "-"+n+"\n";
        }
        return text;
    }

    public Desire searchDesire(String objectId) {
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Desire");
        query.whereEqualTo("objectId",objectId);
        try {
            List<ParseObject> results = query.find();
            if(results.size() > 0){
                return (Desire) results.get(0);
            }
            else{
                return null;
            }

        }
        catch (ParseException e){
            e.printStackTrace();
            return null;
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_desire_details, menu);
        String aux = getIntent().getStringExtra("source");
        if(aux != null && aux.equals("user")){
            menu.findItem(R.id.ADD_delete).setVisible(true);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.ADD_logout) {
            ParseUser.logOut();
            Intent intent = new Intent(this,LoginActivity.class);
            startActivity(intent);
            finish();
        }
        else if(id == R.id.ADD_delete){
            Intent intent = new Intent();
            intent.putExtra("objectId",desire.getObjectId().toString());
            setResult(RESULT_OK,intent);
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.map = googleMap;
        map.setMyLocationEnabled(true);
        map.getUiSettings().setMyLocationButtonEnabled(true);
        map.getUiSettings().setZoomControlsEnabled(true);
        map.getUiSettings().setAllGesturesEnabled(true);
        ParseGeoPoint geoPoint = desire.getLocation();
        LatLng position = new LatLng(geoPoint.getLatitude(), geoPoint.getLongitude());
        map.setMyLocationEnabled(true);
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(position, 13));
        map.addCircle(new CircleOptions()
                .center(position)
                .radius(300)
                .strokeColor(0x7F0000FF)
                .strokeWidth(5)
                .fillColor(0x7F00FFFF));
    }
}
