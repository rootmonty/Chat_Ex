package badebaba.firechat.POST;

import badebaba.firechat.Chat;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

/**
 * Created by badebaba on 11/12/2016.
 */

public interface Retrofit_Interface  {

    @Headers({
            "Authorization:key=AIzaSyA1tgUfIv03n0kuvqGE3g2HpZp3TVsj7tY",
            "Content-Type:application/json"
    })
    @POST("send")
     Call<Chat> notification(@Body ChatMessage message );

}
