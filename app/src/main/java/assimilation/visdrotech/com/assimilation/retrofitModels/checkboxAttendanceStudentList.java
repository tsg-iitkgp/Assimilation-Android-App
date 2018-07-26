package assimilation.visdrotech.com.assimilation.retrofitModels;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class checkboxAttendanceStudentList {

    @SerializedName("attendanceList")
    @Expose
    private List<AttendanceList> attendanceList = null;

    public List<AttendanceList> getAttendanceList() {
        return attendanceList;
    }

    public void setAttendanceList(List<AttendanceList> attendanceList) {
        this.attendanceList = attendanceList;
    }

    public String toString() {
        String toReturn = "";
        for (AttendanceList a : attendanceList) {
            toReturn += a.toString() + "\n";
        }
        return toReturn;
    }
}