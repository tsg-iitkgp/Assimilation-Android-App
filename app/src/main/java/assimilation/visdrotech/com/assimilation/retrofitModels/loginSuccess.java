package assimilation.visdrotech.com.assimilation.retrofitModels;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class loginSuccess {

    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("groups")
    @Expose
    private String groups;
    @SerializedName("token")
    @Expose
    private String token;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGroups() {
        return groups;
    }

    public void setGroups(String groups) {
        this.groups = groups;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

}

