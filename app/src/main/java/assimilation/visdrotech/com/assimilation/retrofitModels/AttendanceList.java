package assimilation.visdrotech.com.assimilation.retrofitModels;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AttendanceList {

    @SerializedName("roll")
    @Expose
    private String roll;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("attendanceStatus")
    @Expose
    private Boolean attendanceStatus;

    public String getRoll() {
        return roll;
    }

    public void setRoll(String roll) {
        this.roll = roll;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getAttendanceStatus() {
        return attendanceStatus;
    }

    public void setAttendanceStatus(Boolean attendanceStatus) {
        this.attendanceStatus = attendanceStatus;
    }

    public String toString(){
        return (name + " | " + roll + " | " + attendanceStatus.toString());
    }

}