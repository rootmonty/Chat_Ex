package badebaba.firechat;

import android.app.Activity;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.firebase.client.Query;

public class ChatListAdapter extends FirebaseListAdapter<Chat> {

    // The mUsername for our model
    private String mUsername;

    public ChatListAdapter(Query ref, Activity activity, int layout, String mUsername) {
        super(ref, Chat.class, layout, activity);
        this.mUsername = mUsername;
    }

    @Override
    protected void populateView(View view, Chat chat) {
        // Map a Chat object to an entry in our listview
        String author = chat.getAuthor();
        LinearLayout ll = (LinearLayout) view.findViewById(R.id.view);
        LinearLayout theview = (LinearLayout) view.findViewById(R.id.theview);
        TextView authorText = (TextView) view.findViewById(R.id.author);
        TextView time = (TextView) view.findViewById(R.id.time);
        authorText.setText(author + ": ");
        // If the message was sent by this user, color it differently
        if (author != null && author.equals(mUsername)) {
            time.setText(chat.getTimestamp());
            time.setGravity(Gravity.RIGHT);
            ll.setGravity(Gravity.RIGHT);
            theview.setGravity(Gravity.CENTER);
            //theview.setBackgroundColor(Color.RED);
            authorText.setTextColor(Color.RED);

        } else {
            time.setText(chat.getTimestamp());
            time.setGravity(Gravity.RIGHT);
            ll.setGravity(Gravity.LEFT);
            theview.setGravity(Gravity.CENTER);
            //theview.setBackgroundColor(Color.BLUE);
            authorText.setTextColor(Color.BLUE);
        }
        ((TextView) view.findViewById(R.id.message)).setText(chat.getMessage());
    }
}
