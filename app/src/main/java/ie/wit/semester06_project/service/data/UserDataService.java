package ie.wit.semester06_project.service.data;

import android.util.Log;

import com.annimon.stream.Collectors;
import com.annimon.stream.Stream;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.Contract;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ie.wit.semester06_project.main.FinanceApp;
import ie.wit.semester06_project.model.User;
import ie.wit.semester06_project.service.EntryService;

import static ie.wit.semester06_project.activities.BaseActivity.TAG;

/**
 * Created by joewe on 24/03/2017.
 */

public class UserDataService
{
    private DatabaseReference databaseReference;
    private ValueEventListener valueEventListener;
    private List<User> users;
    private EntryService entryService;

    public UserDataService(DatabaseReference databaseReference)
    {
        users = new ArrayList<>();
        this.databaseReference = databaseReference;
        this.entryService = FinanceApp.serviceFactory.getEntryService();
    }

    public List<User> getUsers()
    {
        if (users == null) {
            users = new ArrayList<>();
        }
        return users;
    }

    public void start(){
        if(valueEventListener == null){
            valueEventListener = setUpValueEventListener();
        }
        databaseReference.addValueEventListener(valueEventListener);
    }
    public void stop(){
        databaseReference.removeEventListener(valueEventListener);
    }

    //https://github.com/aNNiMON/Android-Java-8-Stream-Example
    public User getUser(String email){
        return Stream.of(users).filter((user) -> user.getEmailAddress().equalsIgnoreCase(email)).collect(Collectors.toList()).get(0);
    }

    public String[] getUsernames(){
        return (String[]) Stream.of(users).map(User::getEmailAddress).collect(Collectors.toList()).toArray();
    }
    @Contract(" -> !null")
    private ValueEventListener setUpValueEventListener(){
        return new ValueEventListener()
        {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {
                // FIXME: 25/02/2017 iterate through keys and if the correct one exists, get that
                HashMap<String, Map> tempUsers = (HashMap<String, Map>) dataSnapshot.getValue();
                users = entryService.mapUsers(tempUsers);
            }

            @Override
            public void onCancelled(DatabaseError databaseError)
            {
                Log.e(TAG, "unable to retrieve user data", databaseError.toException());
            }

        };
    }
}
