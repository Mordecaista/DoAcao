package doacao.doacao2.Activities;

import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import doacao.doacao2.DoacaoApplication;
import doacao.doacao2.ListArrayAdapter;
import doacao.doacao2.Objects.Desire;
import doacao.doacao2.Objects.Institution;
import doacao.doacao2.R;


public class DesireSearchResultActivity extends ActionBarActivity {

    private ListView list;
    private Context mContext;
    private ProgressDialog progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_desire_search_result);
        mContext = this;
        list = (ListView) findViewById(R.id.ADS_results);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, final View view, int position, long id) {
                        Intent intent = new Intent(mContext,DesireDetailsActivity.class);
                        intent.putExtra("objectId",view.getTag().toString());
                        startActivity(intent);
                    }
                });
        handleIntent(getIntent());
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_desire_search_result, menu);
        SearchManager searchManager =
                (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView =
                (SearchView) menu.findItem(R.id.ADS_search).getActionView();
        searchView.setSearchableInfo(
                searchManager.getSearchableInfo(getComponentName()));

        return true;
    }

    @Override
    public void onNewIntent(Intent intent) {
        setIntent(intent);
        handleIntent(intent);
    }

    private void handleIntent(Intent intent) {

        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            searchDesires(intent.getStringExtra(SearchManager.QUERY));
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.ADS_logout) {
            ParseUser.logOut();
            Intent intent = new Intent(this,LoginActivity.class);
            intent.putExtra("source","institution");
            startActivity(intent);
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    public void searchDesires(String criteria){
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Desire");
        String clearedCriteria = clearCriteria(criteria);
        if(clearedCriteria != null){
            query.whereContainedIn("items", Arrays.asList(clearedCriteria));
        }
        if(DoacaoApplication.mLocation != null) {
            ParseGeoPoint userLocation = new ParseGeoPoint(DoacaoApplication.mLocation.getLatitude(), DoacaoApplication.mLocation.getLongitude());
            query.whereNear("location", userLocation);
        }
        query.setLimit(100);
        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> results, ParseException e) {
                ArrayList<ArrayList<String>> array = new ArrayList<ArrayList<String>>();
                if (e == null) {
                    for (int i = 0; i < results.size(); i++) {
                        ArrayList<String> temp = new ArrayList<String>();
                        temp.add(((Desire) results.get(i)).getUsername());
                        temp.add(((Desire) results.get(i)).getObjectId().toString());
                        array.add(temp);
                    }
                }
                else{
                    Toast.makeText(mContext, getString(R.string.search_error), Toast.LENGTH_LONG).show();
                }
                ListArrayAdapter adapter = new ListArrayAdapter(mContext, array);
                list.setAdapter(adapter);
                progress.dismiss();
            }
        });
        progress = ProgressDialog.show(this, getString(R.string.searching), "", true);
    }

    public String clearCriteria(String criteria){
        String cleared = "";
        if (criteria.length() == 0)
            return cleared;
        else if (criteria.length() == 1)
            cleared = criteria.toUpperCase();
        else
            cleared = criteria.substring(0,1).toUpperCase()+ criteria.substring(1).toLowerCase();
        if(cleared.charAt(cleared.length()-1)== ' ')
            cleared = cleared.substring(0,cleared.length()-1);
        return cleared;
    }
}
