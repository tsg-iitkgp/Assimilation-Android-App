package assimilation.visdrotech.com.assimilation.retrofitModels;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Complaints {

    @SerializedName("status")
    @Expose
    private Boolean status;
    @SerializedName("data")
    @Expose
    private List<complaintsDatum> data = null;

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public List<complaintsDatum> getData() {
        return data;
    }

    public void setData(List<complaintsDatum> data) {
        this.data = data;
    }

}