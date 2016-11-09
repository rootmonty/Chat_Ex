package badebaba.firechat;

import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.google.firebase.messaging.FirebaseMessaging;

/**
 * Created by badebaba on 11/9/2016.
 */

public class InstanceService extends FirebaseInstanceIdService {

    private static final String TAG = "InstanceIdService";
    private static String connected_topic = "engage";

    @Override
    public void onTokenRefresh() {

        String token = FirebaseInstanceId.getInstance().getToken();
        Log.i(TAG, token + "Token ");

        //Subscribing to topic
        FirebaseMessaging.getInstance().subscribeToTopic(connected_topic);

    }
}
