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
     * Start.
     */
    public void start()
    {
        if (valueEventListener == null) {
            valueEventListener = setUpValueEventListener();
        }
        databaseReference.addValueEventListener(valueEventListener);
    }


    /**
     * Stop.
     */
    public void stop()
    {
        databaseReference.removeEventListener(valueEventListener);
    }


    /**
     * Gets all.
     *
     * @return the all
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
     * @return the one
     * @throws Exception the exception
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


    /**
     * Add item.
     *
     * @param user the user
     */
    public void addItem(User user)
    {
        databaseReference.child(user.getKey()).setValue(user);
    }

    /**
     * Add user user.
     *
     * @param userData the user data
     * @return the user
     */
    public User addUser(Map<String, String> userData)
    {
        User user = mapUser(userData);
        databaseReference.child(user.getKey()).setValue(user);
        return user;
    }

    /**
     * User exists boolean.
     *
     * @param email the email
     * @return the boolean
     */
    public boolean userExists(String email)
    {
        return getUsernames().contains(email);
    }

    /**
     * Gets usernames.
     *
     * @return the usernames
     */
    public List<String> getUsernames()
    {
        return Stream.of(users).map(User::getEmail).collect(Collectors.toList());
    }


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

    private User mapUser(Map<String, String> userList)
    {
        return Stream.of(userList).map(User::new).collect(Collectors.toList()).get(0);
    }

}
