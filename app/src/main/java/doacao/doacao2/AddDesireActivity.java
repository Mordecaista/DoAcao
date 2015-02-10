package doacao.doacao2;

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
import com.parse.ParseObject;
import com.parse.SaveCallback;


public class AddDesireActivity extends ActionBarActivity {

    private EditText mEmail;
    private EditText mPhone;
    private EditText mLatitude;
    private EditText mLongitude;
    private Spinner mItens;
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
        mItens = (Spinner)findViewById(R.id.itens);
        mSubmit = (Button)findViewById(R.id.submit);
        mSubmit.setOnClickListener(mOnSubmitClickListener);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,R.array.itens,android.R.layout.simple_spinner_item);
        mItens.setAdapter(adapter);

    }

    private View.OnClickListener mOnSubmitClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v){
            String email = mEmail.getText().toString();
            String phone = mPhone.getText().toString();
            double latitude = Double.parseDouble(mLatitude.getText().toString());
            double longitude = Double.parseDouble(mLongitude.getText().toString());
            String item = mItens.getSelectedItem().toString();
            Desire desire = new Desire(email,phone,latitude,longitude,item);
            desire.saveInBackground(new SaveCallback(){
                @Override
                public void done(ParseException e) {
                    if(e == null){
                        Toast.makeText(mContext,getString(R.string.cadatrado_com_sucesso),Toast.LENGTH_SHORT).show();
                    }
                    else{
                        Toast.makeText(mContext,getString(R.string.erro),Toast.LENGTH_SHORT).show();
                    }
                }
            });
//            ParseObject parse = new ParseObject("Desire");
//            parse.put("email", email);
//            parse.put("phone",phone);
//            parse.put("latitude",latitude);
//            parse.put("longitude",longitude);
//            parse.put("item",item);
//            parse.saveInBackground(new SaveCallback(){
//                @Override
//                public void done(ParseException e) {
//                    if(e == null){
//                        Toast.makeText(mContext,getString(R.string.cadatrado_com_sucesso),Toast.LENGTH_SHORT).show();
//                    }
//                    else{
//                        Toast.makeText(mContext,getString(R.string.erro),Toast.LENGTH_SHORT).show();
//                    }
//                }
//            });
        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_add_desire, menu);
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
