
package assimilation.visdrotech.com.assimilation.retrofitModels;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class complaintStatus {

    @SerializedName("complaintStatus")
    @Expose
    private Boolean complaintStatus;

    public Boolean getcomplaintStatus() {
        return complaintStatus;
    }

    public void setcomplaintStatus(Boolean complaintStatus) {
        this.complaintStatus = complaintStatus;
    }

}