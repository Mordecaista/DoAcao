package doacao.doacao2.Objects;

import com.parse.ParseUser;

/**
 * Created by Alexandre on 05/03/2015.
 */
public class User extends ParseUser {

    public User(){
    }

    public User(String email, String password, String name){
        setUsername(email);
        setEmail(email);
        setPassword(password);
        put("name",name);
    }

    public String getName(){
        return getString("name");
    }
    public void setName(String name){
        put("name",name);
    }

}
