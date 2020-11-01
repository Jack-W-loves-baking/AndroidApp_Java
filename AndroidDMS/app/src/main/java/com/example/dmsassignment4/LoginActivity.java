package com.example.dmsassignment4;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.UnderlineSpan;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/**
 * This class is to process login.
 */
public class LoginActivity extends AppCompatActivity {

    private Button registerButton;
    private Button loginBtn;
    private FirebaseAuth mAuth;
    private ProgressBar progressBar;
    private EditText email, password, username;
    public static String USERNAME;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //Initialize variables.
        progressBar = findViewById(R.id.progressBar);
        email = findViewById(R.id.editUsername);
        password = findViewById(R.id.editPassword);
        username = findViewById(R.id.editUsername);
        mAuth = FirebaseAuth.getInstance();
        //back to main activities
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }


    public void toRegistrate(View view) {

        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
    }

    public void toLogin(View view) {

        String myEmail = email.getText().toString().trim();
        String myPassword = password.getText().toString().trim();

        if (TextUtils.isEmpty(myPassword)) {
            password.setError("Please enter your password");
            return;
        }

        if (TextUtils.isEmpty(myEmail)) {
            email.setError("Please enter an email");
            return;
        }

        if (myPassword.length() < 6) {
            password.setError("Password needs at least 6 digits");
            return;
        }

        //During the connection with google server, the progress bar will appear.
        progressBar.setVisibility(View.VISIBLE);

        mAuth.signInWithEmailAndPassword(myEmail, myPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    // Sign in success
                    Toast.makeText(LoginActivity.this, "Login successfully.",
                            Toast.LENGTH_SHORT).show();
                    //redirect to login page
                    Intent intent = new Intent(getApplicationContext(), MapsActivity.class);
                    startActivity(intent);

                    //get user import username.
                    USERNAME = username.getText().toString().trim();
                    progressBar.setVisibility(View.INVISIBLE);
                } else {
                    // If sign in fails, display a message to the user.
                    Toast.makeText(LoginActivity.this, "Error! " + task.getException().getMessage(),
                            Toast.LENGTH_SHORT).show();
                    //The user will have to input again if some errors occurred.Progressbar set back to invisible.
                    progressBar.setVisibility(View.INVISIBLE);

                }

            }
        });

    }
}