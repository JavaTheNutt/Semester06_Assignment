package ie.wit.application.service;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Map;
import java.util.function.Consumer;

import ie.wit.application.exceptions.UserNotFoundException;
import ie.wit.application.main.FinanceApp;
import ie.wit.application.model.User;
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

    /**
     * instantiates a new auth service.
     *
     * @param auth            the firebase auth instance
     * @param userDataService the user data service
     */
    public AuthService(FirebaseAuth auth, UserDataService userDataService)
    {
        this.auth = auth;
        this.userDataService = userDataService;
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

    /**
     * Create account.
     *
     * @param userDetails the user details
     * @param callback    the callback
     */
    public void createAccount(Map<String, String> userDetails, Consumer<Boolean> callback)
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
    public void login(String email, String password, Consumer<Boolean> callback)
    {
        Log.d(TAG, "login: user " + email + " attempting to log in");
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(setUpLogInComplete(email, callback));
    }

    /**
     * This will create an authstate listener
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
            } else {
                Log.d(TAG, "onAuthStateChanged: user signed in: " + user.getUid());
            }
        };
    }

    /**
     * This will create the on complete listener that will be used when a new account is created.
     * @param userDetails a map containing the user details
     * @param callback the callback to be triggered to pass the signin result back to the activity.
     * @return an on complete listener
     */
    @NonNull
    private OnCompleteListener<AuthResult> setupSigninOnComplete(Map<String, String> userDetails, Consumer<Boolean> callback)
    {
        return task -> {
            Log.d(TAG, "setupSigninOnComplete: result: " + task.isSuccessful());
            if (!task.isSuccessful()) {
                Log.e(TAG, "setupSigninOnComplete: sign up failed", task.getException());
                FinanceApp.setCurrentUser(null);
                callback.accept(false);
            }else{
                Log.d(TAG, "setupSigninOnComplete: sign up succeeded");
                User user = userDataService.mapUser(userDetails);
                String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
                user.setUuid(uid);
                userDataService.addUser(user);
                FinanceApp.setCurrentUser(user);
                callback.accept(true);
            }
        };
    }

    /**
     * This will create an on complete listener that will be used when a user attempt to log in
     * @param email the email address passed in
     * @param callback the callback that will be trigger changes in the activity
     * @return the on complete listener
     */
    @NonNull
    private OnCompleteListener<AuthResult> setUpLogInComplete(String email, Consumer<Boolean> callback)
    {
        return task -> {
            Log.d(TAG, "setUpLogInComplete: " + task.isSuccessful());
            if (!task.isSuccessful()) {
                Log.e(TAG, "setUpLogInComplete: sign in failed", task.getException());
                FinanceApp.setCurrentUser(null);
                callback.accept(false);
            }else{
                Log.d(TAG, "setUpLogInComplete: login succeeded");
                try {
                    FinanceApp.setCurrentUser(userDataService.getOne(email));
                    callback.accept(true);
                } catch (UserNotFoundException e) {
                    FinanceApp.setCurrentUser(null);
                    Log.e(TAG, "setUpLogInComplete: user not found", e);
                    callback.accept(false);
                }
            }
        };
    }
}
