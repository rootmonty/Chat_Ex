package badebaba.firechat;

import android.app.ListActivity;
import android.content.SharedPreferences;
import android.database.DataSetObserver;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

import badebaba.firechat.POST.ChatMessage;
import badebaba.firechat.POST.Data;
import badebaba.firechat.POST.Retrofit_Interface;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/***
 * The present iteration contains the basic model of chat
 * This includes sending messages in a single line.
 * Notifications working
 * Every registered phone is getting the message
 *
 *
 * Work pending on the UI
 * Make the chat messages on either side
 * Try to display the messages in a bubble
 *
 */
public class MainActivity extends ListActivity {

    //Locating the URL
    private static final String FIREBASE_URL = "https://firechat-c0726.firebaseio.com/";

    String mUsername;
    Firebase mFirebaseRef;
    ValueEventListener mConnectedListener;
    ChatListAdapter mChatListAdapter;
    Retrofit retrofit;
    Retrofit_Interface retro;
    ChatMessage chatMessage;
    List<String> collection_token = new ArrayList<>();
    Firebase mref_token;
    String token1;
    public static String MY_TOKEN = "fGuB9r65Zhc:APA91bGiWZsHrrrP-xbAxgPZtbvdesRhJS41CJeQIXLQGUGfG4n2kLiz1d3EVyGoLN2GR4q9VcfDFqCs6PA-hon5-oMx_2uPy9zWB80m3BXug9Xcz1gt31VoXCkk7BQqvbHthKDpQiFF";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Firebase.setAndroidContext(this);

      /*  if(FirebaseInstanceId.getInstance().getId() == null)
            Log.i("Token is :", "null");
        else{
            getmytoken = FirebaseInstanceId.getInstance().getId();
        }
      */
        // collection_token.add(getmytoken);
        // Make sure we have a mUsername
        setupUsername();
        mref_token = new Firebase("https://firechat-c0726.firebaseio.com/");
       // mref_token.child("tokens").push().setValue(MY_TOKEN);

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();

        retrofit = new Retrofit.Builder()
                .baseUrl("https://fcm.googleapis.com/fcm/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();

        retro = retrofit.create(Retrofit_Interface.class);


        setTitle("Chatting as " + mUsername);

        mFirebaseRef = new Firebase(FIREBASE_URL).child("chat");

        // Setup our input methods. Enter key on the keyboard or pushing the send button
        EditText inputText = (EditText) findViewById(R.id.messageInput);
        inputText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                if (actionId == EditorInfo.IME_NULL && keyEvent.getAction() == KeyEvent.ACTION_DOWN) {
                    sendMessage();
                }
                return true;
            }
        });

        findViewById(R.id.sendButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendMessage();
            }
        });


    }

    @Override
    public void onStart() {
        super.onStart();
        // Setup our view and list adapter. Ensure it scrolls to the bottom as data changes
        final ListView listView = getListView();
        // Tell our list adapter that we only want 50 messages at a time
        mChatListAdapter = new ChatListAdapter(mFirebaseRef.limit(50), this, R.layout.chat_message, mUsername);
        listView.setAdapter(mChatListAdapter);
        mChatListAdapter.registerDataSetObserver(new DataSetObserver() {
            @Override
            public void onChanged() {
                super.onChanged();
                listView.setSelection(mChatListAdapter.getCount() - 1);
            }
        });

        // Finally, a little indication of connection status
        mConnectedListener = mFirebaseRef.getRoot().child(".info/connected").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                boolean connected = (Boolean) dataSnapshot.getValue();
                if (connected) {
                    Toast.makeText(MainActivity.this, "Connected to Firebase", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(MainActivity.this, "Disconnected from Firebase", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                // No-op
            }
        });
    }

    @Override
    public void onStop() {
        super.onStop();
        mFirebaseRef.getRoot().child(".info/connected").removeEventListener(mConnectedListener);
        mChatListAdapter.cleanup();
    }

    private void setupUsername() {
        SharedPreferences prefs = getApplication().getSharedPreferences("ChatPrefs", 0);
        mUsername = prefs.getString("username", null);
        if (mUsername == null) {
            Random r = new Random();
            // Assign a random user name if we don't have one saved.
            mUsername = "User:" + r.nextInt(100000);
            prefs.edit().putString("username", mUsername).commit();
        }
    }

    private void sendMessage() {
        EditText inputText = (EditText) findViewById(R.id.messageInput);
        String input = inputText.getText().toString();
        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm");
        String currentDateTime = dateFormat.format(new Date());
        token1 = FirebaseInstanceId.getInstance().getToken();
        String username = mUsername;
        if (!input.equals("")) {
            // Create our 'model', a Chat object
            Chat chat = new Chat(input,mUsername,currentDateTime);
            // Create a new, auto-generated child of that chat location, and save our chat data there
            mFirebaseRef.push().setValue(chat);
            inputText.setText("");
            chatMessage = new ChatMessage();
            Data data = new Data();

            getCollection_token(mref_token);
           // experiment.add(FirebaseInstanceId.getInstance().getId());
            data.setText(input);
            data.setTitle(username);
            chatMessage.setData(data);
            //Testing phase : using experiment array list to test the format
            chatMessage.setRegistration_ids(collection_token);


            Call<Chat> post_chat = retro.notification(chatMessage);
            Log.i("TOKEN SENDING",token1);
            post_chat.enqueue(new Callback<Chat>() {
                @Override
                public void onResponse(Call<Chat> call, Response<Chat> response) {
                    Log.i("json response",call.toString());
                    // Log.i("Message sent :", post_string);
                    Log.i("message:",chatMessage.getData().getText());
                    if(!response.isSuccessful())
                     Log.i("retrofit ",response.errorBody()+" "+response.message());
                }

                @Override
                public void onFailure(Call<Chat> call, Throwable t) {

                    Log.i("Retrofit POST :",t.toString());
                }
            });
        }
    }

    public void getCollection_token(Firebase mref){
        mref.child("tokens").push().setValue(token1);
       // collection_token.add(token);
        mref.getRoot().child("tokens").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                String note = dataSnapshot.getValue(String.class);
             //   Log.i("Existing Token",token);
              //  Log.i("Token secured",token+"");
               if(note == MY_TOKEN){
                    Log.i("Token matched","Not added:"+note);
                }
               else
                 {
                    Log.i("Token added",note);
                    collection_token.add(note);
                //    Log.i("checking from array",collection_token.);
                  //  Log.i("Token in arraylist",token);
               }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
        collection_token.add(token1);
    }
}
