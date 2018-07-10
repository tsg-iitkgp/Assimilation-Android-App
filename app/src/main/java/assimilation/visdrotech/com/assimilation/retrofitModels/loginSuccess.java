package assimilation.visdrotech.com.assimilation.retrofitModels;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class loginSuccess {

    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("audience")
    @Expose
    private String audience;
    @SerializedName("token")
    @Expose
    private String token;
    @SerializedName("helpers")
    @Expose
    private String helpers;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAudience() {
        return audience;
    }

    public void setAudience(String audience) {
        this.audience = audience;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getHelpers() {
        return helpers;
    }

    public void setHelpers(String helpers) {
        this.helpers = helpers;
    }

}