package ie.wit.semester06_project.activities;

import android.os.Bundle;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ie.wit.semester06_project.model.User;

/**
 * Created by joewe on 25/02/2017.
 */

public class EntryActivity extends BaseActivity
{
    protected DatabaseReference userDatabaseReference;
    protected Map<String, User> allUsers;
    protected String[] usernames;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        userDatabaseReference = FirebaseDatabase.getInstance().getReference("users");
        userDatabaseReference.addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {
                HashMap<String, Map> users = (HashMap<String, Map>) dataSnapshot.getValue();
                usernames = new String[users.size()];
                allUsers = new HashMap<String, User>(usernames.length);
                int i = 0;
                for(Map.Entry<String, Map> user : users.entrySet()){
                    String tempUsername = (String) user.getValue().get("emailAddress");
                    //User tempUser = (User) user.getValue();
                    User tempUser = new User();
                    tempUser.setFirstName((String)user.getValue().get("firstName"));
                    tempUser.setSurname((String) user.getValue().get("surname"));
                    tempUser.setEmailAddress(tempUsername);
                    tempUser.setPassword((String) user.getValue().get("password"));
                    allUsers.put(tempUser.getEmailAddress(), tempUser);
                    Log.v(TAG, tempUsername);
                    usernames[i] = tempUsername;
                    i++;
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError)
            {
                Log.e(TAG, databaseError.toString());
            }
        });
    }
}
