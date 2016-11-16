package badebaba.firechat.POST;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by badebaba on 11/14/2016.
 */

public class ChatMessage implements Serializable {

    @SerializedName("data")
    @Expose
    private Data data;
    @SerializedName("registration_ids")
    @Expose
    private List<String> registration_ids = new ArrayList<>();

    /**
     *
     * @return
     * The data
     */
    public Data getData() {
        return data;
    }

    /**
     *
     * @param data
     * The data
     */
    public void setData(Data data) {
        this.data = data;
    }

    /**
     *
     * @return
     * The to
     */
    public List<String> getRegistration_Ids() {
        return registration_ids;
    }

    /**
     *
     * @param registration_ids
     * The to
     */
    public void setRegistration_ids(List<String> registration_ids) {
        this.registration_ids = registration_ids;
    }
}
