package ie.wit.semester06_project.service;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import static android.content.ContentValues.TAG;

/**
 * This will handle authorisation for the application
 */
public class AuthService
{
    private FirebaseAuth auth;
    private FirebaseAuth.AuthStateListener authListener;

    /**
     * Instantiates a new Auth service.
     *
     * @param auth the auth
     */
    public AuthService(FirebaseAuth auth)
    {
        this.auth = auth;
    }

    /**
     * Start.
     */
    public void start()
    {
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
        if (authListener == null) {
            return;
        }
        auth.removeAuthStateListener(authListener);
    }

    /**
     * Create account.
     *
     * @param email    the email
     * @param password the password
     */
    public void createAccount(String email, String password)
    {
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(setupSigninOnComplete());
    }

    /**
     * Login.
     *
     * @param email    the email
     * @param password the password
     */
    public void login(String email, String password)
    {
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(setUpLogInComplete());
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
            }
        };
    }

    @NonNull
    private OnCompleteListener<AuthResult> setupSigninOnComplete()
    {
        return task -> {
            Log.d(TAG, "setupSigninOnComplete: result: " + task.isSuccessful());
            if (!task.isSuccessful()) {
                Log.e(TAG, "setupSigninOnComplete: sign up failed", task.getException());
            }
        };
    }

    @NonNull
    private OnCompleteListener<AuthResult> setUpLogInComplete()
    {
        return task -> {
            Log.d(TAG, "setUpLogInComplete: " + task.isSuccessful());
            if (!task.isSuccessful()) {
                Log.e(TAG, "setUpLogInComplete: sign up failed", task.getException());
            }
        };
    }
}
