package doacao.doacao2.Activities;

import android.app.SearchManager;
import android.content.ComponentName;
import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.SaveCallback;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import doacao.doacao2.Objects.Institution;
import doacao.doacao2.R;


public class InstitutionRegisterActivity extends ActionBarActivity {

    private EditText mName;
    private EditText mCNPJ;
    private EditText mEmail;
    private EditText mPassword;
    private EditText mPasswordConf;
    private EditText mCountry;
    private EditText mState;
    private EditText mCity;
    private EditText mStreet;
    private EditText mNumber;
    private EditText mApartment;
    private EditText mPhone;
    private Spinner mItems;
    private Button mSubmit;
    private Context mContext;

    private View.OnClickListener mOnSubmitClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v){
            String name = mName.getText().toString();
            int cnpj = Integer.parseInt(mCNPJ.getText().toString());
            String email = mEmail.getText().toString();
            String password = mPassword.getText().toString();
            String country = mCountry.getText().toString();
            String state = mState.getText().toString();
            String city = mCity.getText().toString();
            String street = mStreet.getText().toString();
            int number = Integer.parseInt(mNumber.getText().toString());
            String apartment = mApartment.getText().toString();
            int phone = Integer.parseInt(mPhone.getText().toString());

            ArrayList<String> items = new ArrayList<String>();
            items.add(mItems.getSelectedItem().toString());
            ArrayList<Double> geopoint = getLocationFromAddress(number+" "+street+", "+city+", "+state+", "+country);

            Institution institution = new Institution(name, cnpj, email, password, country, state, street, number, apartment,geopoint.get(0), geopoint.get(1), items, phone);
            institution.saveInBackground(new SaveCallback(){
                @Override
                public void done(ParseException e) {
                    if(e == null){
                        Toast.makeText(mContext, getString(R.string.cadatrado_com_sucesso), Toast.LENGTH_SHORT).show();
                    }
                    else{
                        e.printStackTrace();
                        Toast.makeText(mContext,getString(R.string.erro),Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_institution_register);
        mContext = this;
        mName = (EditText)findViewById(R.id.AIG_edit_name);
        mCNPJ = (EditText)findViewById(R.id.AIG_edit_cnpj);
        mEmail = (EditText)findViewById(R.id.AIG_edit_email);
        mPassword = (EditText)findViewById(R.id.AIG_edit_password);
        mPasswordConf = (EditText)findViewById(R.id.AIG_edit_conf_pass);
        mCountry = (EditText)findViewById(R.id.AIG_edit_country);
        mState = (EditText)findViewById(R.id.AIG_edit_state);
        mCity = (EditText)findViewById(R.id.AIG_edit_city);
        mStreet = (EditText)findViewById(R.id.AIG_edit_street);
        mNumber = (EditText)findViewById(R.id.AIG_edit_number);
        mApartment = (EditText)findViewById(R.id.AIG_edit_apartment);
        mPhone = (EditText)findViewById(R.id.AIG_edit_phone);

        mItems = (Spinner)findViewById(R.id.AIG_items);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,R.array.items,android.R.layout.simple_spinner_item);
        mItems.setAdapter(adapter);

        mSubmit = (Button)findViewById(R.id.AIG_submit);
        mSubmit.setOnClickListener(mOnSubmitClickListener);

        mName.setText("test");
        mCNPJ.setText("123456789");
        mEmail.setText("test@test.com");
        mPassword.setText("t");
        mPasswordConf.setText("e");
        mCountry.setText("Brasil");
        mState.setText("Rio Grande do Sul");
        mCity.setText("Porto Alegre");
        mStreet.setText("São mateus");
        mNumber.setText("486");
        mApartment.setText("901");
        mPhone.setText("123");
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_institution_register, menu);
        // Associate searchable configuration with the SearchView
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.AIG_search).getActionView();
        //searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setSearchableInfo(searchManager.getSearchableInfo(new ComponentName(getApplicationContext(), DesireSearchResultActivity.class)));
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

    public ArrayList<Double> getLocationFromAddress(String strAddress) {

        Geocoder coder = new Geocoder(this);
        List<Address> address;
        ArrayList<Double> p1 = new ArrayList<Double>();

        try {
            address = coder.getFromLocationName(strAddress, 1);
            if (address == null) {
                return null;
            }
            Address location = address.get(0);
            p1.add(location.getLatitude());
            p1.add(location.getLongitude());
        } catch (IOException e) {
            e.printStackTrace();
            p1.add(0.0);
            p1.add(0.0);
        } finally {
            return p1;
        }
    }
}
