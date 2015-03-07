package doacao.doacao2.Activities;

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

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

import doacao.doacao2.ListArrayAdapter;
import doacao.doacao2.Objects.Desire;
import doacao.doacao2.Objects.Institution;
import doacao.doacao2.R;

public class InstitutionSearchResultActivity extends ActionBarActivity {

    private ListView list;
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_institution_search_result);
        mContext = this;
        list = (ListView) findViewById(R.id.AIS_results);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, final View view, int position, long id) {
                Intent intent = new Intent(mContext,MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                intent.putExtra("objectId",view.getTag().toString());
                startActivity(intent);
                finish();
            }
        });
        handleIntent(getIntent());
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_institution_search_result, menu);
        SearchManager searchManager =
                (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView =
                (SearchView) menu.findItem(R.id.AIS_search).getActionView();
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
            ListArrayAdapter adapter = new ListArrayAdapter(this, searchInstitutions(intent.getStringExtra(SearchManager.QUERY)));
            list.setAdapter(adapter);
        }
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

    public ArrayList<ArrayList<String>> searchInstitutions(String criteria){
        ArrayList<ArrayList<String>> array = new ArrayList<ArrayList<String>>();
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Institution");
        if(criteria != null){
            query.whereEqualTo("name",criteria); //TODO:Change this criteria for a real one
        }
        try {
            List<ParseObject> results = query.find();
            for (int i = 0; i < results.size() && i < 100; i++) {
                ArrayList<String> temp = new ArrayList<String>();
                temp.add(((Institution) results.get(i)).getName());
                temp.add(((Institution) results.get(i)).getObjectId().toString());
                array.add(temp);
            }
        }
        catch (ParseException e){
            return array;
        }
        return array;
    }

}
