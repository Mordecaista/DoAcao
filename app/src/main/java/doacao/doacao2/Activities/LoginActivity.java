package doacao.doacao2.Activities;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.facebook.widget.LoginButton;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseFacebookUtils;
import com.parse.ParseUser;

import java.util.Arrays;

import doacao.doacao2.R;

public class LoginActivity extends ActionBarActivity {

    private EditText mEmail;
    private EditText mPassword;
    private Button mLogin;
    private LoginButton faceButton;
    private Context mContext;
    private ActionBarActivity act;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        act = this;
        setContentView(R.layout.activity_login);
        if(ParseUser.getCurrentUser() != null){
            this.finish();
            Intent intent = new Intent(mContext, InstitutionRegisterActivity.class);
            startActivity(intent);
        }
        else{
            this.mEmail = (EditText)findViewById(R.id.AL_email);
            this.mPassword = (EditText)findViewById(R.id.AL_pass);
            this.mLogin = (Button)findViewById(R.id.AL_login);
            mLogin.setOnClickListener(mFaceListener);
            faceButton = (LoginButton)findViewById(R.id.AL_faceButton);
            faceButton.setReadPermissions(Arrays.asList("public_profile","email"));
            faceButton.setOnClickListener(mFaceListener);

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

    private View.OnClickListener mFaceListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            ParseFacebookUtils.logIn(Arrays.asList("public_profile","email"),act, new LogInCallback(){
                @Override
                public void done(ParseUser user, ParseException err) {
                    if (user == null) {
                        Toast.makeText(mContext,getString(R.string.error_face_login),Toast.LENGTH_LONG).show();
                    }
                    else {
                        if (user.isNew()) {
                            Log.d("MyApp", "User signed up and logged in through Facebook!");
                        } else {
                            Log.d("MyApp", "User logged in through Facebook!");
                        }
                        Intent intent = new Intent(mContext,MainActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }
            });
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
