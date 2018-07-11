package assimilation.visdrotech.com.assimilation.retrofitModels;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UpcomingEvent {

    @SerializedName("eventData")
    @Expose
    private List<EventDatum> eventData = null;

    public List<EventDatum> getEventData() {
        return eventData;
    }

    public void setEventData(List<EventDatum> eventData) {
        this.eventData = eventData;
    }

}