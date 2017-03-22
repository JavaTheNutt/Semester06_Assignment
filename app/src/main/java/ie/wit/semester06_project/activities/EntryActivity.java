package ie.wit.semester06_project.activities;

import android.os.Bundle;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.Contract;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ie.wit.semester06_project.main.FinanceApp;
import ie.wit.semester06_project.model.User;
import ie.wit.semester06_project.service.EntryService;

/**
 * This will be the class that will any activities that relate to login/logout wil inherit from.
 */

public class EntryActivity extends BaseActivity
{
    protected DatabaseReference userDatabaseReference; //reference to the user segment of the database.
    protected List<User> allUsers; //map of users with the key being formed from the users email address
    protected String[] usernames; //list of all usernames in the system
    protected ValueEventListener valueEventListener;
    protected EntryService entryService;

    /**
     * {@inheritDoc}
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        userDatabaseReference = FirebaseDatabase.getInstance().getReference("users");
        entryService = FinanceApp.serviceFactory.getEntryService();
        allUsers = new ArrayList<>();
    }

    @Override
    protected void onStart()
    {
        super.onStart();
        valueEventListener = setUpValueEventListener();
        userDatabaseReference.addValueEventListener(valueEventListener);
    }

    @Override
    protected void onStop()
    {
        super.onStop();
        userDatabaseReference.removeEventListener(valueEventListener);
    }

    @Contract(" -> !null")
    private ValueEventListener setUpValueEventListener()
    {
        return new ValueEventListener()
        {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {
                // FIXME: 25/02/2017 iterate through keys and if the correct one exists, get that
                HashMap<String, Map> users = (HashMap<String, Map>) dataSnapshot.getValue();
                allUsers = entryService.mapUsers(users);
                usernames = entryService.getUsernames(allUsers);
            }

            @Override
            public void onCancelled(DatabaseError databaseError)
            {
                Log.e(TAG, "unable to retrieve user data", databaseError.toException());
            }

            /**
             * Convert Map to User POJO
             * @param user a map containing all of the data required to construct a user
             * @return User object
             */
            /*private User mapUser(Map.Entry<String, Map> user)
            {
                User tempUser = new User();
                String tempUsername = (String) user.getValue().get("emailAddress");
                tempUser.setFirstName((String) user.getValue().get("firstName"));
                tempUser.setSurname((String) user.getValue().get("surname"));
                tempUser.setEmailAddress(tempUsername);
                tempUser.setPassword((String) user.getValue().get("password"));
                Log.v(TAG, tempUsername);
                return tempUser;
            }*/
        };
    }

    protected User getUser(String email) throws RuntimeException
    {
        for (User user : allUsers) {
            if (user.getEmailAddress().equalsIgnoreCase(email)){
                return user;
            }
        }
        throw new RuntimeException("User Not Found");
    }
}
