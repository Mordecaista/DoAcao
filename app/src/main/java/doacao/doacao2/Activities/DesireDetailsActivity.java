package doacao.doacao2.Activities;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
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

import java.util.List;

import doacao.doacao2.Objects.Desire;
import doacao.doacao2.R;

public class DesireDetailsActivity extends ActionBarActivity implements OnMapReadyCallback {

    private Desire desire;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_desire_details);
        Intent intent = getIntent();
        TextView user = (TextView)findViewById(R.id.ADD_user);
        TextView items = (TextView)findViewById(R.id.ADD_items);
        TextView email = (TextView)findViewById(R.id.ADD_email);
        TextView phone = (TextView)findViewById(R.id.ADD_phone);

        desire = searchDesire(intent.getStringExtra("objectId"));
        user.setText(desire.getUsername());
        items.setText(formatItems(desire.getItem()));
        email.setText(desire.getEmail());
        phone.setText(desire.getPhone());

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.ADD_map);
        mapFragment.getMapAsync(this);
    }

    private String formatItems(String item) {
        return "-"+item;
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
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_desire_details, menu);
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
        ParseGeoPoint geoPoint = desire.getLocation();
        LatLng position = new LatLng(geoPoint.getLatitude(), geoPoint.getLongitude());

        googleMap.setMyLocationEnabled(true);
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(position, 13));
        googleMap.addCircle(new CircleOptions()
                .center(position)
                .radius(300)
                .strokeColor(0x7F0000FF)
                .strokeWidth(5)
                .fillColor(0x7F00FFFF));
    }
}
