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
import android.widget.TextView;

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;

import doacao.doacao2.ListArrayAdapter;
import doacao.doacao2.Objects.Desire;
import doacao.doacao2.R;


public class DesireSearchResultActivity extends ActionBarActivity {

    private ListView list;
    private Context mContext;

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
                        intent.putExtra("objectId",((TextView)list.findViewById(R.id.list_item_id)).getText());
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

    public ArrayList<ArrayList<String>> searchInstitutions(String criteria){
        ArrayList<ArrayList<String>> array = new ArrayList<ArrayList<String>>();
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Desire");
        if(criteria != null){
            query.whereEqualTo("phone",criteria);
        }
        try {
            List<ParseObject> results = query.find();
            for (int i = 0; i < results.size() && i < 100; i++) {
                ArrayList<String> temp = new ArrayList<String>();
                //temp.add(((Desire) results.get(i)).getUsername() + ((Desire) results.get(i)).getCreatedAt().toString());
                temp.add(((Desire) results.get(i)).getCreatedAt().toString());
                temp.add(((Desire) results.get(i)).getObjectId().toString());
                array.add(temp);
            }
        }
        catch (ParseException e){
            return array;
        }
        return array;
    }

    private void handleIntent(Intent intent) {

        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            ListArrayAdapter adapter = new ListArrayAdapter(this, searchInstitutions(intent.getStringExtra(SearchManager.QUERY)));
            list.setAdapter(adapter);
        }
    }
}
