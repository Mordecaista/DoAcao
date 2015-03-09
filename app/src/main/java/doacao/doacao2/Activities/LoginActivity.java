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

import com.facebook.FacebookRequestError;
import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.model.GraphUser;
import com.facebook.widget.LoginButton;
import com.parse.LogInCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseFacebookUtils;
import com.parse.ParseUser;

import java.util.Arrays;

import doacao.doacao2.Objects.User;
import doacao.doacao2.R;

public class LoginActivity extends ActionBarActivity {

    private EditText mEmail;
    private EditText mPassword;
    private EditText mPasswordConf;
    private Button mLogin;
    private Button faceButton;
    private Context mContext;
    private ActionBarActivity act;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        act = this;
        setContentView(R.layout.activity_login);
        if(ParseUser.getCurrentUser() != null){
            finishActivity("institution");
        }
        else{
            this.mEmail = (EditText)findViewById(R.id.AL_email);
            this.mPassword = (EditText)findViewById(R.id.AL_pass);
            this.mPasswordConf = (EditText)findViewById(R.id.AL_pass_conf);
            this.mLogin = (Button)findViewById(R.id.AL_login);
            mLogin.setOnClickListener(mOnLoginClickListener);
            faceButton = (Button)findViewById(R.id.AL_faceButton);
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
            finishActivity("institution");
        }
        catch (ParseException e){
            if(validate()) {
                ParseUser user = new ParseUser();
                user.setUsername(email);
                user.setPassword(pass);
                user.setEmail(email);
                user.put("type","institution");
                try {
                    user.signUp();
                    finishActivity("institution");
                } catch (ParseException f) {
                    Toast.makeText(mContext, getString(R.string.error_login), Toast.LENGTH_LONG).show();
                }
            }
        }
        }
    };

    private View.OnClickListener mFaceListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            ParseFacebookUtils.logIn(Arrays.asList("email","public_profile"),act, new LogInCallback(){
                @Override
                public void done(ParseUser user, ParseException err) {
                    if (user == null) {
                        Toast.makeText(mContext,getString(R.string.error_face_login),Toast.LENGTH_LONG).show();
                    }
                    else {
                        if (user.isNew()) {
                            makeRequest();
                        }
                        else {
                            finishActivity("user");
                        }
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        ParseFacebookUtils.finishAuthentication(requestCode, resultCode, data);
    }

    private void makeRequest() {
        Session session = ParseFacebookUtils.getSession();
        if (session != null && session.isOpened()) {
            Request request = Request.newMeRequest(
                    ParseFacebookUtils.getSession(), new Request.GraphUserCallback() {
                        @Override
                        public void onCompleted(GraphUser user, Response response) {
                            if (user != null) {
                                String name = user.getName();
                                String email = user.getProperty("email").toString();
                                ParseUser.getCurrentUser().setEmail(email);
                                ParseUser.getCurrentUser().setUsername(email);
                                ParseUser.getCurrentUser().put("name", name);
                                ParseUser.getCurrentUser().put("type", "facebook");
                                try {
                                    ParseUser.getCurrentUser().save();
                                    finishActivity("user");
                                }
                                catch (ParseException e){
                                    Toast.makeText(getApplicationContext(), R.string.logn_generic_error, Toast.LENGTH_LONG).show();
                                }

                            } else if (response.getError() != null) {
                                if ((response.getError().getCategory() == FacebookRequestError.Category.AUTHENTICATION_RETRY)
                                        || (response.getError().getCategory() == FacebookRequestError.Category.AUTHENTICATION_REOPEN_SESSION)) {
                                    Toast.makeText(getApplicationContext(), R.string.session_invalid_error, Toast.LENGTH_LONG).show();

                                } else {
                                    Toast.makeText(getApplicationContext(), R.string.logn_generic_error, Toast.LENGTH_LONG).show();
                                }
                            }
                        }
                    });
            request.executeAsync();
        }
    }

   private void finishActivity(String type){
       Intent intent = null;
       switch(type){
           case "user":
               intent = new Intent(mContext, MainActivity.class);
               startActivity(intent);
               finish();
               break;
           case "institution":
               intent = new Intent(mContext, InstitutionRegisterActivity.class);
               startActivity(intent);
               finish();
               break;
           default:
               break;
       }
    }

    private boolean validate() {
        Boolean isValid = true;
        if(mPasswordConf.getVisibility() != View.VISIBLE){
            mPasswordConf.setVisibility(View.VISIBLE);
            Toast.makeText(mContext,getString(R.string.new_institution_conf_pass),Toast.LENGTH_SHORT).show();
            isValid = false;
        }
        else if(!android.util.Patterns.EMAIL_ADDRESS.matcher(mEmail.getText()).matches()){
            Toast.makeText(mContext, getString(R.string.invalid_email), Toast.LENGTH_LONG).show();
            isValid = false;
        }
        return isValid;
    }
}
