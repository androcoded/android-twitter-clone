package com.example.twitterclone;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckedTextView;
import android.widget.ListView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.LogOutCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.List;

public class TwitterUser extends AppCompatActivity implements AdapterView.OnItemClickListener {

    private Toolbar mToolbar;
    private ArrayList<String> users;
    private ListView mListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_twitter_user);
        mToolbar = findViewById(R.id.toolBar);
        mToolbar.setTitle("Twitter User");
        setSupportActionBar(mToolbar);
        users = new ArrayList<>();

        gettingAllUserFromParse();
    }


    private void gettingAllUserFromParse(){
        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this,android.R.layout.simple_list_item_checked,users);
        mListView  = findViewById(R.id.listView);
        mListView.setChoiceMode(AbsListView.CHOICE_MODE_MULTIPLE);
        mListView.setOnItemClickListener(this);
        final ParseQuery<ParseUser> allUser = ParseUser.getQuery();
        allUser.whereNotEqualTo("username",ParseUser.getCurrentUser().getUsername());
        allUser.findInBackground(new FindCallback<ParseUser>() {
            @Override
            public void done(List<ParseUser> objects, ParseException e) {

                if (objects.size()>0 && e == null){
                    for (ParseUser user: objects){
                        users.add(user.getUsername());
                    }
                }else {
                    Toast.makeText(TwitterUser.this, e.getMessage()+"", Toast.LENGTH_SHORT).show();
                }
                mListView.setAdapter(arrayAdapter);
                for (String mUser : users){
                    if (ParseUser.getCurrentUser().getList("fanOf")!=null) {

                        if (ParseUser.getCurrentUser().getList("fanOf").contains(mUser)) {
                            mListView.setItemChecked(users.indexOf(mUser), true);
                        }

                    }

                }
            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.my_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.mntLogout:
                ParseUser.getCurrentUser().logOutInBackground(new LogOutCallback() {
                    @Override
                    public void done(ParseException e) {
                        if (e == null){
                            Toast.makeText(TwitterUser.this, "Logged out successfully!", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(TwitterUser.this,LoginActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    }
                });
                break;
            case R.id.mntSend:
                Intent intent = new Intent(TwitterUser.this,SendTwit.class);
                startActivity(intent);
                break;
        }
        return true;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        CheckedTextView checkedTextView = (CheckedTextView) view;
        if (checkedTextView.isChecked()){
            Toast.makeText(this, users.get(position)+" is followed", Toast.LENGTH_SHORT).show();
            ParseUser.getCurrentUser().add("fanOf",users.get(position));
        }else{
            Toast.makeText(this, users.get(position)+" is unfollowed", Toast.LENGTH_SHORT).show();
            ParseUser.getCurrentUser().getList("fanOf").remove(users.get(position));
            List currentUserFanOf = ParseUser.getCurrentUser().getList("fanOf");
            ParseUser.getCurrentUser().remove("fanOf");
            ParseUser.getCurrentUser().put("fanOf",currentUserFanOf);
        }
        ParseUser.getCurrentUser().saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null){
                    Toast.makeText(TwitterUser.this, "saved", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

}
