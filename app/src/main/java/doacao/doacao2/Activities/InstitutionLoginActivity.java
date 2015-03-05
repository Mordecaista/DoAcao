package doacao.doacao2.Activities;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import doacao.doacao2.Objects.Desire;
import doacao.doacao2.R;

public class InstitutionLoginActivity extends ActionBarActivity {

    private EditText mEmail;
    private EditText mPassword;
    private Button mLogin;
    private Button mUser;
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        setContentView(R.layout.activity_institution_login);
        if(ParseUser.getCurrentUser() != null){
            this.finish();
            Intent intent = new Intent(mContext, InstitutionRegisterActivity.class);
            startActivity(intent);
        }
        else{
            this.mEmail = (EditText)findViewById(R.id.AIL_email);
            this.mPassword = (EditText)findViewById(R.id.AIL_pass);
            this.mLogin = (Button)findViewById(R.id.AIL_login);
            this.mUser = (Button)findViewById(R.id.AIL_user);
            mLogin.setOnClickListener(mOnLoginClickListener);
            mUser.setOnClickListener(mOnUserClickListener);
        }
    }

    private View.OnClickListener mOnLoginClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v){
        String email = mEmail.getText().toString();
        String pass = mPassword.getText().toString();
        try {
            ParseUser.logIn(email,pass);
            finish();
            Intent intent = new Intent(mContext, InstitutionRegisterActivity.class);
            startActivity(intent);
        }
        catch (ParseException e){
            ParseUser user = new ParseUser();
            user.setUsername(email);
            user.setPassword(pass);
            user.setEmail(email);
            try {
                user.signUp();
                finish();
                Intent intent = new Intent(mContext, InstitutionRegisterActivity.class);
                startActivity(intent);
            }
            catch (ParseException f){
                Toast.makeText(mContext,getString(R.string.error_login),Toast.LENGTH_LONG).show();
            }
        }
        }
    };

    private View.OnClickListener mOnUserClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v){
            finish();
            Intent intent = new Intent(mContext, InstitutionRegisterActivity.class); //TODO:Add correct user login activity
            startActivity(intent);
        }
    };


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //getMenuInflater().inflate(R.menu.menu_institution_login, menu);
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
