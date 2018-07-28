package assimilation.visdrotech.com.assimilation.retrofitModels;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class complaintsDatum {

    @SerializedName("eventCreatedBy")
    @Expose
    private String eventCreatedBy;
    @SerializedName("eventDate")
    @Expose
    private String eventDate;
    @SerializedName("eventHelpers")
    @Expose
    private List<String> eventHelpers = null;
    @SerializedName("eventVenue")
    @Expose
    private String eventVenue;
    @SerializedName("eventTitle")
    @Expose
    private String eventTitle;
    @SerializedName("complaintBy")
    @Expose
    private String complaintBy;
    @SerializedName("complaintDateTime")
    @Expose
    private String complaintDateTime;
    @SerializedName("complaintMessage")
    @Expose
    private String complaintMessage;
    @SerializedName("complaintResolutionStatus")
    @Expose
    private Boolean complaintResolutionStatus;
    @SerializedName("complaintId")
    @Expose
    private String complaintId;

    public String getEventCreatedBy() {
        return eventCreatedBy;
    }

    public void setEventCreatedBy(String eventCreatedBy) {
        this.eventCreatedBy = eventCreatedBy;
    }

    public String getEventDate() {
        return eventDate;
    }

    public void setEventDate(String eventDate) {
        this.eventDate = eventDate;
    }

    public List<String> getEventHelpers() {
        return eventHelpers;
    }

    public void setEventHelpers(List<String> eventHelpers) {
        this.eventHelpers = eventHelpers;
    }

    public String getEventVenue() {
        return eventVenue;
    }

    public void setEventVenue(String eventVenue) {
        this.eventVenue = eventVenue;
    }

    public String getEventTitle() {
        return eventTitle;
    }

    public void setEventTitle(String eventTitle) {
        this.eventTitle = eventTitle;
    }

    public String getComplaintBy() {
        return complaintBy;
    }

    public void setComplaintBy(String complaintBy) {
        this.complaintBy = complaintBy;
    }

    public String getComplaintDateTime() {
        return complaintDateTime;
    }

    public void setComplaintDateTime(String complaintDateTime) {
        this.complaintDateTime = complaintDateTime;
    }

    public String getComplaintMessage() {
        return complaintMessage;
    }

    public void setComplaintMessage(String complaintMessage) {
        this.complaintMessage = complaintMessage;
    }

    public Boolean getComplaintResolutionStatus() {
        return complaintResolutionStatus;
    }

    public void setComplaintResolutionStatus(Boolean complaintResolutionStatus) {
        this.complaintResolutionStatus = complaintResolutionStatus;
    }

    public String getComplaintId() {
        return complaintId;
    }

    public void setComplaintId(String complaintId) {
        this.complaintId = complaintId;
    }

}

