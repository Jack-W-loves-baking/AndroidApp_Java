package com.example.dmsassignment4;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {

    public static final String TAG = "TAG";
    private FirebaseAuth mAuth;
    private EditText name, email, password, code;
    private ProgressBar progressbar;
    private Button registration;
    private FirebaseFirestore db;
    private String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        //back to login activities
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //initialize firebaseauth instance
        mAuth = FirebaseAuth.getInstance();

        //initialize variables from user input
        name = findViewById(R.id.editName);
        email = findViewById(R.id.editEmail);
        password = findViewById(R.id.editPassword);
        code = findViewById(R.id.editCode);
        progressbar = findViewById(R.id.progressBarRegister);
        registration = findViewById(R.id.registrateNewUserButton);
        db = FirebaseFirestore.getInstance();

        registration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String myEmail = email.getText().toString().trim();
                String myPassword = password.getText().toString().trim();
                String myCode = code.getText().toString().trim();
                String myName = name.getText().toString().trim();

                //validate user inputs
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


                if (TextUtils.isEmpty(myCode)) {
                    code.setError("Please enter a recommended code from members");
                    return;
                }

                //Our design was to verify the code provided by other members, this was to make sure that
                //strangers cannot get location details inside of the group.
                //Currently we set up the default code as "1234" in string format.
                if (!myCode.equalsIgnoreCase("1234")) {
                    code.setError("Invalid code");
                    return;
                }

                //During the connection with google server, the progress bar will appear.
                progressbar.setVisibility(View.VISIBLE);

                //firebase inbuilt functions, create users
                mAuth.createUserWithEmailAndPassword(myEmail, myPassword)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {

                                if (task.isSuccessful()) {
                                    // Sign in success
                                    Toast.makeText(RegisterActivity.this, "Created user.",
                                            Toast.LENGTH_SHORT).show();

                                    //get firebase auth unique userID
                                    userID = mAuth.getCurrentUser().getUid();

                                    // store user data in firebase cloud
                                    DocumentReference documentReference = db.collection("users").document(userID);

                                    // hashmap with reference key and object.
                                    Map<String, Object> user = new HashMap<>();

                                    //add into database
                                    user.put("name", myName);
                                    user.put("email", myEmail);
                                    user.put("code", myCode);
                                    documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Log.d(TAG, "onSuccess: User Profile has been created " + userID);
                                        }
                                    });

                                    //redirect to login page
                                    Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                                    startActivity(intent);
                                    progressbar.setVisibility(View.INVISIBLE);
                                } else {
                                    // If sign in fails, display a message to the user.
                                    Toast.makeText(RegisterActivity.this, "Error! " + task.getException().getMessage(),
                                            Toast.LENGTH_SHORT).show();
                                    //The user will have to input again if some errors occurred.Progressbar set back to invisible.
                                    progressbar.setVisibility(View.INVISIBLE);
                                }
                            }
                        });
            }

        });

    }
}