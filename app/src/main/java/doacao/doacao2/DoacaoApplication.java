package doacao.doacao2;

import android.app.Application;

import com.parse.Parse;
import com.parse.ParseObject;

import doacao.doacao2.Objects.Desire;
import doacao.doacao2.Objects.Institution;

/**
 * Created by Alexandre on 09/02/2015.
 */
public class DoacaoApplication extends Application{
    @Override

    public void onCreate() {
        super.onCreate();
        //Parse.enableLocalDatastore(this);
        ParseObject.registerSubclass(Desire.class);
        ParseObject.registerSubclass(Institution.class);
        Parse.initialize(this, "0CPdvwDWdEq0QWHcdM3QvzJUaq9icV5mlyJWiDhe", "LGqb2riXezBTZNonbfNksXsDjdS11NxNtivZow5L");

    }
}
