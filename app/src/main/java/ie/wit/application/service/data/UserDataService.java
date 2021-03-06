package ie.wit.application.service.data;

import android.util.Log;

import com.annimon.stream.Collectors;
import com.annimon.stream.Stream;
import com.annimon.stream.function.Consumer;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.Contract;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import ie.wit.application.exceptions.UserNotFoundException;
import ie.wit.application.model.User;

import static ie.wit.application.activities.BaseActivity.TAG;

/**
 * This class abstracts the details of dealing with the Firebase Database.
 * An ArrayList&lt;User&gt; is maintained in sync with firebase to provide data to the rest of the app.
 */
public class UserDataService
{
    private static final String CHILD_NAME = "users";
    private DatabaseReference databaseReference;
    private ValueEventListener valueEventListener;
    private List<User> users;
    private Consumer<String> dataLoaded;

    /**
     * Instantiates a new User data service.
     */
    public UserDataService()
    {
        users = new ArrayList<>();
        this.databaseReference = FirebaseDatabase.getInstance().getReference();
    }


    /**
     * Start the user data service.
     */
    public void start()
    {
        if (valueEventListener == null) {
            valueEventListener = setUpValueEventListener();
        }
        databaseReference.child(CHILD_NAME).addValueEventListener(valueEventListener);
    }


    /**
     * Stop the user data service.
     */
    public void stop()
    {
        databaseReference.child(CHILD_NAME).removeEventListener(valueEventListener);
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
     * @throws UserNotFoundException the user not found exception
     */
    public User getOne(String email) throws UserNotFoundException
    {
        for (User user : users) {
            if (user.getEmail().equalsIgnoreCase(email)) {
                return user;
            }
        }
        throw new UserNotFoundException("user not found");
    }

    /**
     * Gets user.
     *
     * @param id the id
     * @return the user
     * @throws UserNotFoundException the user not found exception
     */
    public User getUser(String id) throws UserNotFoundException
    {
        for (User user : users) {
            if (user.getUuid() != null && user.getUuid().equals(id)) {
                return user;
            }
        }
        throw new UserNotFoundException("user not found or user has not received uuid yet");
    }

    /**
     * Add user.
     *
     * @param user the user
     */
    public void addUser(User user)
    {
        databaseReference.child(CHILD_NAME).child(user.getUuid()).setValue(user);
    }

    /**
     * Register username callback.
     *
     * @param callback the callback
     */
    public void registerUsernameCallback(Consumer<String> callback)
    {
        this.dataLoaded = callback;
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
                if (dataLoaded != null) {
                    dataLoaded.accept("data loaded");
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
