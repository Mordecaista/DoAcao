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

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@ParseClassName("Desire")
public class Desire extends ParseObject {

    public Desire(){

    }

    public Desire(String email, String phone, double latitude, double longitude, ArrayList<String> items){
        put("email",email);
        put("phone",phone);
        addAllUnique("items", items);

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
    public ArrayList<String> getItems(){
        ArrayList<String> result = new ArrayList<String>();
        JSONArray list = getJSONArray("items");
        for(int i = 0; i <list.length();i++){
            try{result.add(list.get(i).toString());}
            catch (JSONException e){}
        }
        return result;
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
    public void setItems(ArrayList<String> items){
        addAllUnique("items", items);
    }
    public String getUsername(){
        String aux = "";
        ParseQuery<ParseUser> query = ParseUser.getQuery();
        query.whereEqualTo("objectId", get("userId").toString());
        try {
            List<ParseUser> results = query.find();
            if(results.size() > 0)
               aux = results.get(0).getString("name");
        }
        catch(ParseException e){
            e.printStackTrace();
        }
        finally{
            return aux;
        }
    }
}
