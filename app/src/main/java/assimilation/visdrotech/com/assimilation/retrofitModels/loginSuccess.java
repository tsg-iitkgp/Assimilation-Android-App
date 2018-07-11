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
    @SerializedName("isAttendanceTaker")
    @Expose
    private Boolean isAttendanceTaker;
    @SerializedName("isSuperAdmin")
    @Expose
    private Boolean isSuperAdmin;
    @SerializedName("isStudent")
    @Expose
    private Boolean isStudent;
    @SerializedName("isGymakhanaGsec")
    @Expose
    private Boolean isGymakhanaGsec;

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

    public Boolean getIsAttendanceTaker() {
        return isAttendanceTaker;
    }

    public void setIsAttendanceTaker(Boolean isAttendanceTaker) {
        this.isAttendanceTaker = isAttendanceTaker;
    }

    public Boolean getIsSuperAdmin() {
        return isSuperAdmin;
    }

    public void setIsSuperAdmin(Boolean isSuperAdmin) {
        this.isSuperAdmin = isSuperAdmin;
    }

    public Boolean getIsStudent() {
        return isStudent;
    }

    public void setIsStudent(Boolean isStudent) {
        this.isStudent = isStudent;
    }

    public Boolean getIsGymakhanaGsec() {
        return isGymakhanaGsec;
    }

    public void setIsGymakhanaGsec(Boolean isGymakhanaGsec) {
        this.isGymakhanaGsec = isGymakhanaGsec;
    }

}
