package doacao.doacao2.Activities;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;

import doacao.doacao2.Objects.Desire;
import doacao.doacao2.R;

public class DesireDetailsActivity extends ActionBarActivity {

    private TextView user;
    private TextView items;
    private TextView email;
    private TextView phone;
    private Desire desire;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_desire_details);
        Intent intent = getIntent();
        user = (TextView)findViewById(R.id.ADD_user);
        items = (TextView)findViewById(R.id.ADD_items);
        email = (TextView)findViewById(R.id.ADD_email);
        phone = (TextView)findViewById(R.id.ADD_phone);

        desire = searchDesire(intent.getStringExtra("objectId"));
        user.setText(desire.getUsername());
        items.setText(formatItems(desire.getItem()));
        email.setText(desire.getEmail());
        phone.setText(desire.getPhone());
    }

    private String formatItems(String item) {
        return "-"+item;
    }

    public Desire searchDesire(String objectId) {
        ArrayList<ArrayList<String>> array = new ArrayList<ArrayList<String>>();
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Desire");
        query.whereEqualTo("objectId",objectId);
        try {
            return (Desire) query.getFirst();
        }
        catch (ParseException e){
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
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
