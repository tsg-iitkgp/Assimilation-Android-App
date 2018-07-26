
package assimilation.visdrotech.com.assimilation.retrofitModels;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class deleteEvent {

    @SerializedName("deleteStatus")
    @Expose
    private Boolean deleteStatus;

    public Boolean getdeleteStatus() {
        return deleteStatus;
    }

    public void setdeleteStatus(Boolean deleteStatus) {
        this.deleteStatus = deleteStatus;
    }

}