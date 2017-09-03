package com.entra.barbers;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.entra.barbers.utility.FirebaseUtil;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import es.dmoral.toasty.Toasty;

public class LoginActivity extends AppCompatActivity {
    EditText loginEdit;
    TextInputLayout loginEditLayout;
    Button loginSubmit;

    FirebaseUtil firebase;
    FirebaseAuth mAuth;

    FirebaseAuth.AuthStateListener mAuthListener;

    private static final String TAG = "LoginActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        firebase = new FirebaseUtil();
        mAuth = firebase.getAuth();

        loginEdit = (EditText) findViewById(R.id.login_phone);
        loginEditLayout = (TextInputLayout) findViewById(R.id.login_phone_layout);
        loginSubmit = (Button) findViewById(R.id.login_submit);

        loginEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                validatePhone();
            }
        });

        loginSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitForm();
            }
        });
        
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if(user != null){
                    Log.d(TAG, "onAuthStateChanged: user logged in" + user.getUid());
                    Intent in = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(in);
                    finish();
                } else{
                    Log.d(TAG, "onAuthStateChanged: user signed out");
                }
            }
        };


    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        mAuth.removeAuthStateListener(mAuthListener);
    }

    private void submitForm(){
        if(loginEdit.getText().toString().trim().isEmpty() || !isValidPhone()){
            loginEditLayout.setError("Invalid Phone");
            Log.d(TAG, "submitForm: " + loginEdit.getText().toString().trim() + " " + isValidPhone());
        } else{
            loginUser(loginEdit.getText().toString().trim());
            //login
        }
    }

    private void loginUser(String email){
        mAuth.createUserWithEmailAndPassword("some" + email + "abc@gmail.com", "123456789")
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "onComplete: " + task.isSuccessful() + " " + task.getException());

                        if(!task.isSuccessful()){
                            Toasty.error(LoginActivity.this, "User could not be created ", Toast.LENGTH_SHORT).show();
                        } else{
                            Intent in = new Intent(LoginActivity.this, MainActivity.class);
                            startActivity(in);
                            finish();
                        }
                    }
                });
    }

    private boolean isValidPhone(){
        String phone = loginEdit.getText().toString().trim();
        if(phone.substring(0, 4).equals("+91-") && phone.length()==14) return true;
        else return false;
    }

    private boolean validatePhone(){
        if(loginEdit.getText().toString().trim().isEmpty()){
            loginEditLayout.setError("Invalid Phone");
            return false;
        } else{
            loginEditLayout.setErrorEnabled(false);
        }
        return true;
    }
}
