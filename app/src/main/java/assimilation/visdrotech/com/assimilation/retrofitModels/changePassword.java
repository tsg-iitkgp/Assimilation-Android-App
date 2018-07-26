
package assimilation.visdrotech.com.assimilation.retrofitModels;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class changePassword {

    @SerializedName("passwordChangeStatus")
    @Expose
    private Boolean passwordChangeStatus;

    public Boolean getPasswordChangeStatus() {
        return passwordChangeStatus;
    }

    public void setPasswordChangeStatus(Boolean passwordChangeStatus) {
        this.passwordChangeStatus = passwordChangeStatus;
    }

}