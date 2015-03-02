package doacao.doacao2.Objects;

import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseClassName;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.Date;
import java.util.List;

@ParseClassName("Desire")
public class Desire extends ParseObject {

    public Desire(){

    }

    public Desire(String email, String phone, double latitude, double longitude, String item){
        put("email",email);
        put("phone",phone);
        put("item",item);

        ParseGeoPoint geoPoint = new ParseGeoPoint(latitude,longitude);
        put("location",geoPoint);

        put("userId", ParseUser.getCurrentUser().getObjectId());
    }

    public String getEmail(){
        return getString("email");
    }
    public String getPhone(){
        return getString("phone");
    }
    public ParseGeoPoint getLocation(){
        return getParseGeoPoint("location");
    }
    public String getItem(){
        return getString("item");
    }
    public void setEmail(String email){
        put("email",email);
    }
    public void setPhone(String phone){
        put("phone",phone);
    }
    public void setLocation(double latitude, double longitude){
        ParseGeoPoint geoPoint = new ParseGeoPoint(latitude,longitude);
        put("location",geoPoint);
    }
    public void setItem(String item){
        put("item",item);
    }
    public String getUsername(){
        String aux = "";
        ParseQuery<ParseUser> query = ParseUser.getQuery();
        query.whereEqualTo("objectId", get("userId").toString());
        try {
            List<ParseUser> results = query.find();
            if(results.size() > 0)
               aux = results.get(0).getUsername();
        }
        catch(ParseException e){
            e.printStackTrace();
        }
        finally{
            return aux;
        }
    }
}