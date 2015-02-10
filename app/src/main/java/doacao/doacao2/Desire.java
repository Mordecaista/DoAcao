package doacao.doacao2;

import com.parse.ParseClassName;
import com.parse.ParseObject;

@ParseClassName("Desire")
public class Desire extends ParseObject {

    public Desire(){

    }

    public Desire(String email, String phone, double latitude, double longitude, String item){
        put("email",email);
        put("phone",phone);
        put("latitude",latitude);
        put("longitude",longitude);
        put("item",item);
    }

    public String getEmail(){
        return getString("email");
    }
    public String getPhone(){
        return getString("phone");
    }
    public double getLatitude(){
        return getDouble("latitude");
    }
    public double getLongitude(){
        return getDouble("longitude");
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
    public void setLatitude(double latitude){
        put("latitude",latitude);
    }
    public void setLongitude(double longitude){
        put("longitude",longitude);
    }
    public void setItem(String item){
        put("item",item);
    }
}
