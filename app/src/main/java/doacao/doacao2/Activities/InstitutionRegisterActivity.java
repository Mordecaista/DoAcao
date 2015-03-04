package doacao.doacao2.Activities;

import android.app.SearchManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.res.Resources;
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
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import doacao.doacao2.MultiSpinner;
import doacao.doacao2.Objects.Institution;
import doacao.doacao2.R;


public class InstitutionRegisterActivity extends ActionBarActivity implements MultiSpinner.MultiSpinnerListener{

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
    private MultiSpinner mItems;
    private Button mSubmit;
    private Context mContext;
    private ArrayList<String> items;
    private String[] possibleItems;
    private Institution institution;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_institution_register);
        mContext = this;
        getInstitution();
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

        mItems = (MultiSpinner)findViewById(R.id.AIG_items);
        Resources res = getResources();
        possibleItems = res.getStringArray(R.array.items);

        mSubmit = (Button)findViewById(R.id.AIG_submit);
        mSubmit.setOnClickListener(mOnSubmitClickListener);

        mEmail.setText(ParseUser.getCurrentUser().getEmail());

        if(institution != null) {
            mName.setText(institution.getName());
            mCNPJ.setText(""+institution.getCNPJ());
            mCountry.setText(institution.getCountry());
            mState.setText(institution.getState());
            mCity.setText(institution.getCity());
            mStreet.setText(institution.getStreet());
            mNumber.setText(""+institution.getNumber());
            mApartment.setText(institution.getApartment());
            mPhone.setText(""+institution.getPhone());

            boolean[] selected = new boolean[possibleItems.length];
            ArrayList<String> selectList = institution.getItems();
            for(int i = 0; i < possibleItems.length; i++){
                if(selectList.indexOf(possibleItems[i]) >= 0) selected[i] = true;
                else selected[i] = false;
            }
            mItems.setItems(Arrays.asList(possibleItems), getString(R.string.selectItems), this,selected);
            this.items = selectList;
        }
        else{
            mItems.setItems(Arrays.asList(possibleItems), getString(R.string.selectItems), this);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_institution_register, menu);
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.AIG_search).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(new ComponentName(getApplicationContext(), DesireSearchResultActivity.class)));
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.AIG_logout) {
            ParseUser.logOut();
            finish(); //TODO:Return to institution login page
        }

        return super.onOptionsItemSelected(item);
    }

    private View.OnClickListener mOnSubmitClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v){
            String name = mName.getText().toString();
            int cnpj = Integer.parseInt(mCNPJ.getText().toString());
            String email = mEmail.getText().toString();
            String password = mPassword.getText().toString();
            String passConf = mPasswordConf.getText().toString();
            String country = mCountry.getText().toString();
            String state = mState.getText().toString();
            String city = mCity.getText().toString();
            String street = mStreet.getText().toString();
            int number = Integer.parseInt(mNumber.getText().toString());
            String apartment = mApartment.getText().toString();
            int phone = Integer.parseInt(mPhone.getText().toString());

            ArrayList<Double> geopoint = getLocationFromAddress(number+" "+street+", "+city+", "+state+", "+country);
            if(valid(name, cnpj, email, password, passConf, country, state, city, street, number, apartment, geopoint.get(0), geopoint.get(1), items, phone)) {
                if(password != null){
                    ParseUser.getCurrentUser().setPassword(password);
                    ParseUser.getCurrentUser().setEmail(email);
                    ParseUser.getCurrentUser().setUsername(email);
                    ParseUser.getCurrentUser().put("name",name);
                    try {
                        ParseUser.getCurrentUser().save();
                    }
                    catch (ParseException e){
                        Toast.makeText(mContext, getString(R.string.cant_save_user), Toast.LENGTH_LONG).show();
                    }
                }
                if(institution == null) {
                    institution = new Institution(name, cnpj, country, state, city, street, number, apartment, geopoint.get(0), geopoint.get(1), items, phone);
                }
                else{
                    institution.setName(name);
                    institution.setCNPJ(cnpj);
                    institution.setCountry(country);
                    institution.setState(state);
                    institution.setCity(city);
                    institution.setStreet(street);
                    institution.setNumber(number);
                    institution.setApartment(apartment);
                    institution.setLocation(geopoint.get(0), geopoint.get(1));
                    institution.setItems(items);
                    institution.setPhone(phone);
                }
                institution.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        if (e == null) {
                            Toast.makeText(mContext, getString(R.string.submitted_with_success), Toast.LENGTH_SHORT).show();
                        } else {
                            e.printStackTrace();
                            Toast.makeText(mContext, getString(R.string.error), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        }
    };

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

    public void getInstitution(){
        Institution inst = null;
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Institution");
        query.whereEqualTo("userId",ParseUser.getCurrentUser().getObjectId()); //TODO:Change this criteria for a real one
        try {
            List<ParseObject> results = query.find();
            if(results.size()>0)
                inst = (Institution)results.get(0);
        }
        catch (ParseException e){
            institution = inst;
        }
        institution = inst;
    }

    @Override
    public void onItemsSelected(boolean[] selected) {
        ArrayList<String> aux = new ArrayList<String>();
        for(int i = 0; i < selected.length; i++){
            if(selected[i]) aux.add(possibleItems[i]);
        }
        this.items = aux;
    }

    private boolean valid(String name, int cnpj, String email, String password, String passConf, String country, String state, String city, String street, int number, String apartment, Double aDouble, Double aDouble1, ArrayList<String> items, int phone) {
        //TODO:Add more validations if needed (fields null pending)
        boolean isValid = true;
        if(!password.equals(passConf)){
            Toast.makeText(mContext, getString(R.string.password_doesnt_match), Toast.LENGTH_LONG).show();
            isValid = false;
        }
        else if(items.size() == 0){
            Toast.makeText(mContext, getString(R.string.no_items), Toast.LENGTH_LONG).show();
            isValid = false;
        }
        else if(!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            Toast.makeText(mContext, getString(R.string.invalid_email), Toast.LENGTH_LONG).show();
            isValid = false;
        }
        return isValid;
    }
}
