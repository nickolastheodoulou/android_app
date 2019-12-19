package com.example.myapplicationnick;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.facebook.CallbackManager;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import android.util.Log;
import com.facebook.AccessToken;
import com.google.firebase.auth.FacebookAuthProvider;

import com.google.firebase.auth.AuthCredential;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Arrays;


public class SignupActivity extends AppCompatActivity {
    EditText emailId, password;
    Button btnSignUp;
    TextView tvSignIn;
    private FirebaseAuth mAuth;

    // section on FaceBook login
    private CallbackManager mCallbackManager;
    private static final String TAG = "FACELOG";

    TextView tvFacebook;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // FacebookSdk.sdkInitialize(this.getApplicationContext());
        setContentView(R.layout.activity_signup);


        mAuth = FirebaseAuth.getInstance();
        emailId = findViewById(R.id.txtemail);
        password = findViewById(R.id.txtpassword);
        btnSignUp = findViewById(R.id.button);
        tvSignIn = findViewById(R.id.go_to_sign_in);

        // Initialize Facebook Login button
        tvFacebook = findViewById(R.id.facebook_login);
        mCallbackManager = CallbackManager.Factory.create();



        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailId.getText().toString();
                String pwd = password.getText().toString();
                if(email.isEmpty()){
                    emailId.setError("Please enter email id");
                    emailId.requestFocus();
                }
                else  if(pwd.isEmpty()){
                    password.setError("Please enter your password");
                    password.requestFocus();
                }
                else  if(email.isEmpty() && pwd.isEmpty()){
                    Toast.makeText(SignupActivity.this,"Fields Are Empty!",Toast.LENGTH_SHORT).show();
                }
                else  if(!(email.isEmpty() && pwd.isEmpty())){
                    mAuth.createUserWithEmailAndPassword(email, pwd).addOnCompleteListener(SignupActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(!task.isSuccessful()){
                                Toast.makeText(SignupActivity.this,"SignUp Unsuccessful, Please Try Again",Toast.LENGTH_SHORT).show();
                            }
                            else {
                                startActivity(new Intent(SignupActivity.this,HomeActivity.class));
                            }
                        }
                    });
                }
                else{
                    Toast.makeText(SignupActivity.this,"Error Occurred!",Toast.LENGTH_SHORT).show();

                }
            }
        });

        tvSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(SignupActivity.this,LoginActivity.class);
                startActivity(i);
            }
        });





        tvFacebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LoginManager.getInstance().logInWithReadPermissions(SignupActivity.this, Arrays.asList("email", "public_profile"));
                LoginManager.getInstance().registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        handleFacebookAccessToken(loginResult.getAccessToken());
                    }

                    @Override
                    public void onCancel() {
                        Log.d(TAG, "facebook:onCancel");
                        // ...
                    }

                    @Override
                    public void onError(FacebookException error) {
                        Log.d(TAG, "facebook:onError", error);
                        // ...
                    }
                });
            }
        });





    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();

        if(currentUser !=null)
        {
            updateUI();
        }

    }

    private void updateUI()
    {
        Toast.makeText(SignupActivity.this, "You're logged in", Toast.LENGTH_LONG).show();
        Intent accountIntent = new Intent(SignupActivity.this, LoginActivity.class);
        startActivity(accountIntent);
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Pass the activity result back to the Facebook SDK
        mCallbackManager.onActivityResult(requestCode, resultCode, data);
    }


    private void handleFacebookAccessToken(AccessToken token) {
        Log.d(TAG, "handleFacebookAccessToken:" + token);

        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI();
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            Toast.makeText(SignupActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();

                        }

                        // ...
                    }
                });
    }

}

// https://www.youtube.com/watch?v=mapLbSKNc6I
// https://firebase.google.com/docs/auth/android/facebook-login
// 28 min un