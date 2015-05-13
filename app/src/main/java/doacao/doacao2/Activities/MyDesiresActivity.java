package doacao.doacao2.Activities;

import android.app.AlertDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
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

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

import doacao.doacao2.ListArrayAdapter;
import doacao.doacao2.Objects.Desire;
import doacao.doacao2.R;

public class MyDesiresActivity extends ActionBarActivity {

    private ListView list;
    private Context mContext;
    private View tempView;
    private ListArrayAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_desires);
        mContext = this;
        list = (ListView) findViewById(R.id.AMD_results);
        list.setLongClickable(true);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, final View view, int position, long id) {
                tempView = view;
                Intent intent = new Intent(mContext,DesireDetailsActivity.class);
                intent.putExtra("objectId",view.getTag().toString());
                intent.putExtra("source","user");
                startActivityForResult(intent,0);
            }
        });
        list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                tempView = view;
                AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                builder.setMessage(R.string.apagar_desejo);
                builder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int position) {
                        if (deleteDesire(tempView.getTag().toString())) {
                            list.removeViewInLayout(tempView);
                            Toast.makeText(mContext, getString(R.string.desire_delete_sucessfull), Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(mContext, getString(R.string.desire_delete_unsucessfull), Toast.LENGTH_LONG).show();
                        }
                        dialog.cancel();
                    }
                });
                builder.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int position) {
                        dialog.cancel();
                    }
                });
                AlertDialog alert = builder.create();
                alert.show();
                return true;
            }
        });
        handleIntent(getIntent());
    }

    private boolean deleteDesire(String objectId) {
        boolean ret = true;
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Desire");
        query.whereEqualTo("objectId", objectId);
        try {
            List<ParseObject> results = query.find();
            results.get(0).deleteInBackground();
        }
        catch (ParseException e){
            ret = false;
        }
        return ret;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_my_desires, menu);
        return true;
    }

    @Override
    public void onNewIntent(Intent intent) {
        setIntent(intent);
        handleIntent(intent);
    }

    private void handleIntent(Intent intent) {
        adapter = new ListArrayAdapter(this, searchDesires());
        list.setAdapter(adapter);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        return super.onOptionsItemSelected(item);
    }

    public ArrayList<ArrayList<String>> searchDesires(){
        ArrayList<ArrayList<String>> array = new ArrayList<ArrayList<String>>();
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Desire");
        query.whereEqualTo("userId", ParseUser.getCurrentUser().getObjectId());
        try {
            List<ParseObject> results = query.find();
            for (int i = 0; i < results.size() && i < 100; i++) {
                ArrayList<String> temp = new ArrayList<String>();
                ArrayList<String> items = ((Desire) results.get(i)).getItems();
                String aux = "";
                for(String n : items){
                    aux += n+", ";
                }
                aux = aux.substring(0,aux.length()-2);
                temp.add(aux);
                temp.add(((Desire) results.get(i)).getObjectId().toString());
                array.add(temp);
            }
        }
        catch (ParseException e){
            return array;
        }
        return array;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 0) {
            if (resultCode == RESULT_OK) {
                if(tempView != null && deleteDesire(tempView.getTag().toString())){
                    list.removeViewInLayout(tempView);
                    Toast.makeText(mContext,getString(R.string.desire_delete_sucessfull),Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(mContext,getString(R.string.desire_delete_unsucessfull),Toast.LENGTH_LONG).show();
                }
            }
        }
    }
}
