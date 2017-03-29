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
import java.util.List;
import java.util.Map;

import ie.wit.semester06_project.model.User;

import static ie.wit.semester06_project.activities.BaseActivity.TAG;

/**
 * This class abstracts the details of dealing with the Firebase Database.
 * An ArrayList&lt;User&gt; is maintained in sync with firebase to provide data to the rest of the app.
 */
public class UserDataService
{
    private DatabaseReference databaseReference;
    private ValueEventListener valueEventListener;
    private List<User> users;

    /**
     * Instantiates a new User data service.
     *
     * @param databaseReference the database reference
     */
    public UserDataService(DatabaseReference databaseReference)
    {
        users = new ArrayList<>();
        this.databaseReference = databaseReference;
    }


    /**
     * Start the user data service.
     */
    public void start()
    {
        if (valueEventListener == null) {
            valueEventListener = setUpValueEventListener();
        }
        databaseReference.child("users").addValueEventListener(valueEventListener);
    }


    /**
     * Stop the user data service.
     */
    public void stop()
    {
        databaseReference.child("users").removeEventListener(valueEventListener);
    }


    /**
     * Gets all.
     *
     * @return the list of users
     */
    public List<User> getAll()
    {
        if (users == null) {
            users = new ArrayList<>();
        }
        return users;
    }

    /**
     * Gets one.
     *
     * @param email the email
     * @return a single user based on that email address
     * @throws Exception if the user is not found
     */
// TODO: 25/03/2017 create custom exception
    public User getOne(String email) throws Exception
    {
        for (User user : users) {
            if (user.getEmail().equalsIgnoreCase(email)) {
                return user;
            }
        }
        throw new Exception("user not found");
    }

    public void addUser(User user){
        databaseReference.child("users").child(user.getUuid()).setValue(user);
    }

    /**
     * Gets the usernames of all users
     *
     * @return the usernames
     */
    public List<String> getUsernames()
    {
        return Stream.of(users).map(User::getEmail).collect(Collectors.toList());
    }

    /**
     * This will set up the value event listener to the database
     *
     * @return the value event listener
     */
    @Contract(" -> !null")
    private ValueEventListener setUpValueEventListener()
    {
        return new ValueEventListener()
        {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {
                for (DataSnapshot userSnaphot : dataSnapshot.getChildren()) {
                    User user = userSnaphot.getValue(User.class);
                    users.add(user);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError)
            {
                Log.e(TAG, "unable to retrieve user data", databaseError.toException());
            }
        };
    }

    /**
     * Convert a map of the users details into a user object
     *
     * @param userDetails a map containing all of the details required to create a user
     * @return a user
     */
    public User mapUser(Map<String, String> userDetails)
    {
        return Stream.of(userDetails).map(User::new).collect(Collectors.toList()).get(0);
    }

}
