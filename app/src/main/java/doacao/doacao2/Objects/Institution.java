package doacao.doacao2.Objects;

import android.util.Log;
import android.widget.Toast;

import com.parse.ParseClassName;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseUser;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

@ParseClassName("Institution")
public class Institution extends ParseObject{

    public Institution(){

    }

    public Institution(String name, int cnpj, String country, String state, String city, String street, int number, String apartment,
                       double latitude, double longitude, ArrayList<String> items, int phone, String email){
        put("name",name);
        put("cnpj",cnpj);
        put("country",country);
        put("state",state);
        put("city",city);
        put("street",street);
        put("number",number);
        put("apartment",apartment);
        put("phone",phone);
        put("email",email);

        JSONArray myArray = new JSONArray();
        for(String n : items) myArray.put(n);
        put("items",myArray);

        ParseGeoPoint geoPoint = new ParseGeoPoint(latitude,longitude);
        put("location",geoPoint);
        ParseUser user = ParseUser.getCurrentUser();
        put("userId",user.getObjectId());

//        ParseUser user = new ParseUser();
//        user.setUsername(name);
//        user.setPassword(password);
//        user.setEmail(email);
//        try {
//            user.signUp();
//            ParseUser current = ParseUser.getCurrentUser();
//            put("userId",user.getObjectId());
//        }
//        catch (ParseException e){
//            Log.d("DOACAO2",e.toString());
//            e.printStackTrace();
//        }
    }

    public String getName(){
        return getString("name");
    }
    public int getCNPJ(){
        return getInt("cnpj");
    }
    public String getCountry(){
        return getString("country");
    }
    public String getState(){
        return getString("state");
    }
    public String getCity(){
        return getString("city");
    }
    public String getStreet(){
        return getString("street");
    }
    public String getEmail(){
        return getString("email");
    }
    public int getNumber(){
        return getInt("number");
    }
    public String getApartment(){
        return getString("apartment");
    }
    public ParseGeoPoint getLocation(){
        return getParseGeoPoint("location");
    }
    public ArrayList<String> getItems(){
            ArrayList<String> items = new ArrayList<String>();
            JSONArray array = getJSONArray("items");
            try{
                for(int i = 0; i < array.length(); i++) items.add(array.get(i).toString());
            }
            catch (JSONException e){
                e.printStackTrace();
            }
            finally{
                return items;
            }
    }
    public int getPhone(){
        return getInt("phone");
    }
    public void setName(String name){
        put("name",name);
    }
    public void setCNPJ(int cnpj){
        put("cnpj",cnpj);
    }
    public void setCountry(String country){
        put("country",country);
    }
    public void setState(String state){
        put("state",state);
    }
    public void setCity(String city){
        put("city",city);
    }
    public void setStreet(String street){
        put("street",street);
    }
    public void setEmail(String email){
        put("email",email);
    }
    public void setNumber(int number){
        put("number",number);
    }
    public void setApartment(String apartment){
        put("apartment",apartment);
    }
    public void setLocation(double latitude, double longitude){
        ParseGeoPoint geoPoint = new ParseGeoPoint(latitude,longitude);
        put("location",geoPoint);
    }
    public void setItems(ArrayList<String> items){
        JSONArray myArray = new JSONArray();
        for(String n : items) myArray.put(n);
        put("items",myArray);
    }
    public void setPhone(int phone){
        put("number",phone);
    }
}
