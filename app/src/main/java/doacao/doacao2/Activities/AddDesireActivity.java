package doacao.doacao2.Activities;

import android.content.Context;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
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

import doacao.doacao2.Objects.Desire;
import doacao.doacao2.R;


public class AddDesireActivity extends ActionBarActivity {

    private EditText mEmail;
    private EditText mPhone;
    private EditText mLatitude;
    private EditText mLongitude;
    private Spinner mItems;
    private Button mSubmit;
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_desire);
        mContext = this;
        mEmail = (EditText)findViewById(R.id.edit_email);
        mPhone = (EditText)findViewById(R.id.edit_phone);
        mLatitude = (EditText)findViewById(R.id.latitude);
        mLongitude = (EditText)findViewById(R.id.longitude);
        mItems = (Spinner)findViewById(R.id.items);
        mSubmit = (Button)findViewById(R.id.submit);
        mSubmit.setOnClickListener(mOnSubmitClickListener);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,R.array.items,android.R.layout.simple_spinner_item);
        mItems.setAdapter(adapter);

    }

    private View.OnClickListener mOnSubmitClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v){
            String email = mEmail.getText().toString();
            String phone = mPhone.getText().toString();
            double latitude = Double.parseDouble(mLatitude.getText().toString());
            double longitude = Double.parseDouble(mLongitude.getText().toString());
            String item = mItems.getSelectedItem().toString();
            Desire desire = new Desire(email,phone,latitude,longitude,item);
            desire.saveInBackground(new SaveCallback(){
                @Override
                public void done(ParseException e) {
                    if(e == null){
                        Toast.makeText(mContext,getString(R.string.cadatrado_com_sucesso),Toast.LENGTH_SHORT).show();
                        finish();
                    }
                    else{
                        Toast.makeText(mContext,getString(R.string.erro),Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_add_desire, menu);
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
}
