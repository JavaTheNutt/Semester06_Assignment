package ie.wit.application.service;

import android.support.annotation.NonNull;
import android.util.Log;

import com.annimon.stream.function.Consumer;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;

import org.jetbrains.annotations.Contract;

import java.util.Map;

import ie.wit.application.exceptions.UserNotFoundException;
import ie.wit.application.main.FinanceApp;
import ie.wit.application.model.User;
import ie.wit.application.model.ui.UserDisplayName;
import ie.wit.application.model.ui.UserDisplayNameObserver;
import ie.wit.application.service.data.UserDataService;

import static ie.wit.application.activities.BaseActivity.TAG;

/**
 * This will handle authorisation for the application
 */
public class AuthService
{
    private FirebaseAuth auth;
    private FirebaseAuth.AuthStateListener authListener;
    private UserDataService userDataService;
    private Consumer<String> userLoggedInCallback;
    private Consumer<String> userNotLoggedInCallback;
    private UserDisplayName userDisplayName;

    /**
     * instantiates a new auth service.
     */
    public AuthService()
    {
        this.auth = FirebaseAuth.getInstance();
        this.userDataService = new UserDataService();
        userDisplayName = new UserDisplayName();
    }

    /**
     * Start the auth service.
     */
    public void start()
    {
        Log.d(TAG, "start: starting auth service");
        userDataService.start();
        if (authListener == null) {
            authListener = setUpAuthListener();
        }
        auth.addAuthStateListener(authListener);

    }

    private void updateCurrentUser()
    {
        User currentUser = null;
        try {
            currentUser = userDataService.getUser(FinanceApp.getCurrentUserId());
        } catch (UserNotFoundException e) {
            Log.e(TAG, "updateCurrentUser: user not found", e);
        }
        FinanceApp.setCurrentUser(currentUser);
        if (currentUser != null) {
            userDisplayName.setName(currentUser.getFirstName(), currentUser.getSurname());
        }

    }

    public void registerUserNotLoggedInCallback(Consumer<String> userNotLoggedInCallback)
    {
        this.userNotLoggedInCallback = userNotLoggedInCallback;
    }

    public void registerUserLoggedInCallback(Consumer<String> userLoggedInCallback)
    {
        this.userLoggedInCallback = userLoggedInCallback;
    }

    public void registerUserObserver(UserDisplayNameObserver observer)
    {
        observer.observe(userDisplayName);
    }

    /**
     * Stop the auth service.
     */
    public void stop()
    {
        Log.d(TAG, "stop: auth service shutting down");
        userDataService.stop();
        if (authListener == null) {
            return;
        }
        auth.removeAuthStateListener(authListener);
    }

    public void signOut()
    {
        auth.signOut();
    }

    /**
     * Create account.
     *
     * @param userDetails the user details
     * @param callback    the callback
     */
    public void createAccount(Map<String, String> userDetails, Consumer<String> callback)
    {
        Log.d(TAG, "createAccount: creating an account for " + userDetails.get("email"));
        auth.createUserWithEmailAndPassword(userDetails.get("email"), userDetails.get("password")).addOnCompleteListener(setupSigninOnComplete(userDetails, callback));
    }

    /**
     * Login.
     *
     * @param email    the email
     * @param password the password
     * @param callback the callback that will trigger the new activity if successful
     */
    public void login(String email, String password, Consumer<String> callback)
    {
        Log.d(TAG, "login: user " + email + " attempting to log in");
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(setUpLogInComplete(email, callback));
    }

    /**
     * This will create an authstate listener
     *
     * @return an auth state listener to be attached to the firbase auth instance
     */
    @NonNull
    private FirebaseAuth.AuthStateListener setUpAuthListener()
    {
        return firebaseAuth -> {
            FirebaseUser user = firebaseAuth.getCurrentUser();
            if (user == null) {
                Log.d(TAG, "onAuthStateChanged: user signed out");
                FinanceApp.setCurrentUser(null);
                if (userNotLoggedInCallback != null) {
                    userNotLoggedInCallback.accept("logged out");
                }
                userDataService.registerUsernameCallback(null);
            } else {
                Log.d(TAG, "onAuthStateChanged: user signed in: " + user.getUid());
                FinanceApp.setCurrentUserId(user.getUid());
                userDataService.registerUsernameCallback((result) -> updateCurrentUser());
                if (userLoggedInCallback != null) {
                    userLoggedInCallback.accept("logged in");
                }
            }
        };
    }

    /**
     * This will create the on complete listener that will be used when a new account is created.
     *
     * @param userDetails a map containing the user details
     * @param callback    the callback to be triggered to pass the signin result back to the activity.
     * @return an on complete listener
     */
    @NonNull
    private OnCompleteListener<AuthResult> setupSigninOnComplete(Map<String, String> userDetails, Consumer<String> callback)
    {
        return task -> {
            Log.d(TAG, "setupSigninOnComplete: result: " + task.isSuccessful());
            if (!task.isSuccessful()) {
                Log.e(TAG, "setupSigninOnComplete: sign up failed", task.getException());
                FinanceApp.setCurrentUser(null);
                callback.accept(handleException(task.getException()));
            } else {
                Log.d(TAG, "setupSigninOnComplete: sign up succeeded");
                User user = userDataService.mapUser(userDetails);
                String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
                user.setUuid(uid);
                userDataService.addUser(user);
                FinanceApp.setCurrentUser(user);
                callback.accept("");
            }
        };
    }

    /**
     * This will create an on complete listener that will be used when a user attempt to log in
     *
     * @param email    the email address passed in
     * @param callback the callback that will be trigger changes in the activity
     * @return the on complete listener
     */
    @NonNull
    private OnCompleteListener<AuthResult> setUpLogInComplete(String email, Consumer<String> callback)
    {
        return task -> {
            Log.d(TAG, "setUpLogInComplete: " + task.isSuccessful());
            if (!task.isSuccessful()) {
                Log.e(TAG, "setUpLogInComplete: sign in failed", task.getException());
                FinanceApp.setCurrentUser(null);
                callback.accept(handleException(task.getException()));
            } else {
                Log.d(TAG, "setUpLogInComplete: login succeeded");
                callback.accept("");
            }
        };
    }

    @NonNull
    @Contract(pure = true)
    private String handleException(Exception e)
    {
        try {
            throw e;
        } catch (FirebaseAuthInvalidUserException e1) {
            return "User does not exist";
        } catch (FirebaseAuthUserCollisionException e2) {
            return "Email address is already in use";
        } catch (FirebaseAuthWeakPasswordException e3) {
            return "The password is not strong enough";
        } catch (FirebaseAuthInvalidCredentialsException e4) {
            return e4.getMessage();
        } catch (Exception e5) {
            return "An Unknown error has occurred";
        }
    }
}
