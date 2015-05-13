package doacao.doacao2;

import android.app.Application;
import android.location.Location;

import com.google.android.gms.maps.model.LatLng;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseUser;

import doacao.doacao2.Objects.Desire;
import doacao.doacao2.Objects.Institution;

/**
 * Created by Alexandre on 09/02/2015.
 */
public class DoacaoApplication extends Application{

    public static Location mLocation;
    public static Institution institution;

    @Override
    public void onCreate() {
        super.onCreate();
        //Parse.enableLocalDatastore(this);
        ParseObject.registerSubclass(Desire.class);
        ParseObject.registerSubclass(Institution.class);
        Parse.initialize(this, "0CPdvwDWdEq0QWHcdM3QvzJUaq9icV5mlyJWiDhe", "LGqb2riXezBTZNonbfNksXsDjdS11NxNtivZow5L");
    }
}
