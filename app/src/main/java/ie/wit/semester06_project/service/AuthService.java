package ie.wit.semester06_project.service;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Map;
import java.util.function.Consumer;

import ie.wit.semester06_project.main.FinanceApp;
import ie.wit.semester06_project.service.data.UserDataService;

import static ie.wit.semester06_project.activities.BaseActivity.TAG;

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
     * @param auth the auth
     */
    public AuthService(FirebaseAuth auth, UserDataService userDataService)
    {
        this.auth = auth;
        this.userDataService = userDataService;
    }

    /**
     * Start.
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
     * Stop.
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
     */
    public void login(String email, String password, Consumer<Boolean> callback)
    {
        Log.d(TAG, "login: user " + email + " attempting to log in");
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(setUpLogInComplete(email, callback));
    }

    @NonNull
    private FirebaseAuth.AuthStateListener setUpAuthListener()
    {
        return firebaseAuth -> {
            FirebaseUser user = firebaseAuth.getCurrentUser();
            if (user == null) {
                Log.d(TAG, "onAuthStateChanged: user signed out");
            } else {
                Log.d(TAG, "onAuthStateChanged: user signed in: " + user.getUid());
                /*try {
                    FinanceApp.setCurrentUser(userDataService.getOne(user.getEmail()));
                } catch (Exception e) {
                    Log.e(TAG, "setUpAuthListener: unable to find user with specified email address", e);
                    e.printStackTrace();
                }*/
            }
        };
    }

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
                FinanceApp.setCurrentUser(userDataService.addUser(userDetails));
                callback.accept(true);
            }
        };
    }

    @NonNull
    private OnCompleteListener<AuthResult> setUpLogInComplete(String email, Consumer<Boolean> callback)
    {
        return task -> {
            Log.d(TAG, "setUpLogInComplete: " + task.isSuccessful());
            if (!task.isSuccessful()) {
                Log.e(TAG, "setUpLogInComplete: sign up failed", task.getException());
                FinanceApp.setCurrentUser(null);
                callback.accept(false);
            }else{
                Log.d(TAG, "setUpLogInComplete: login succeeded");
                callback.accept(true);
                try {
                    FinanceApp.setCurrentUser(userDataService.getOne(email));
                } catch (Exception e) {
                    e.printStackTrace();
                    FinanceApp.setCurrentUser(null);
                    Log.e(TAG, "setUpLogInComplete: user not found", e);
                }
            }
        };
    }
}
