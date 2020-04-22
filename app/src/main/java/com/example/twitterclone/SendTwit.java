package com.example.twitterclone;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SendTwit extends AppCompatActivity {

    private Toolbar mToolbar;
    private EditText edtTweet;
    private Button btnSendTweet,btnViewTweets;
    private ListView mListView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_twit);
        mToolbar = findViewById(R.id.toolbarSendTweet);
        mToolbar.setTitle("Send Tweet");
        setSupportActionBar(mToolbar);
        edtTweet = findViewById(R.id.edtTwitt);
        mListView = findViewById(R.id.listView);

        btnSendTweet = findViewById(R.id.btnSendTwitt);
        btnSendTweet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendTweetToParse();
            }
        });

        btnViewTweets=findViewById(R.id.btnViewTweet);
        btnViewTweets.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gettingAllTweets();
            }
        });


    }

    private void sendTweetToParse(){
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
        progressDialog.show();
        ParseObject myTweet = new ParseObject("MyTweet");
        myTweet.put("myTweet",edtTweet.getText().toString());
        myTweet.put("userN", ParseUser.getCurrentUser().getUsername());
        myTweet.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null){
                    Toast.makeText(SendTwit.this, ParseUser.getCurrentUser().getUsername()+
                            "is successfully tweeted", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(SendTwit.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
                progressDialog.dismiss();
            }
        });



    }

    private void gettingAllTweets(){
        final ArrayList<HashMap<String,String>> userTweet = new ArrayList<>();
        final SimpleAdapter simpleAdapter = new SimpleAdapter(SendTwit.this,userTweet,android.R.layout.simple_list_item_2
        , new String[]{"tweetUserName","tweet"},new int[]{android.R.id.text1,android.R.id.text2});
        ParseUser currentUser = ParseUser.getCurrentUser();
        List allFanFollowed = currentUser.getList("fanOf");
        ParseQuery<ParseObject> allTweets = ParseQuery.getQuery("MyTweet");
        allTweets.whereContainedIn("userN",allFanFollowed);
        allTweets.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if (objects.size()>0 && e == null){
                    for (ParseObject user: objects){
                        HashMap<String,String> stringHashMap = new HashMap<>();
                        stringHashMap.put("tweetUserName",user.getString("userN"));
                        stringHashMap.put("tweet",user.getString("myTweet"));
                        userTweet.add(stringHashMap);
                    }
                }
                mListView.setAdapter(simpleAdapter);
            }
        });

    }

}
