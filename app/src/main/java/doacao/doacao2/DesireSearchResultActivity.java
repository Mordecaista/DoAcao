package doacao.doacao2;

import android.app.Activity;
import android.app.ListActivity;
import android.app.SearchManager;
import android.content.Context;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;


public class DesireSearchResultActivity extends ActionBarActivity {

    private ListView list;
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_desire_search_result);
        mContext = this;
        list = (ListView) findViewById(R.id.ADS_results);
        ListArrayAdapter adapter = new ListArrayAdapter(this, searchInstitutions("blank"));
        list.setAdapter(adapter);
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

    public ArrayList<String> searchInstitutions(String criteria){
        ArrayList<String> array = new ArrayList<String>();
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Desire");
        try {
            List<ParseObject> results = query.find();
            for (int i = 0; i < results.size() && i < 100; i++) {
                array.add(((Desire) results.get(i)).getUsername() + ((Desire) results.get(i)).getCreatedAt().toString());
                //array.add(((Desire) results.get(i)).getCreatedAt().toString());
            }
        }
        catch (ParseException e){
            return array;
        }
        return array;
    }
}
