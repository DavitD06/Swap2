package com.example.myapplication;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {
    Button btnSingIn, btnLogIN;
    FirebaseAuth auth;
    FirebaseDatabase db;
    DatabaseReference users ;

    RelativeLayout root ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnSingIn = findViewById(R.id.btnSingin);
        btnLogIN = findViewById(R.id.btnRegister);
        auth = FirebaseAuth.getInstance();
        db = FirebaseDatabase.getInstance();
        users = db.getReference("Users");
        root = findViewById(R.id.root_element);

        btnLogIN.setOnClickListener(v -> showRegisterWindow());
        btnSingIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showLogInWindow();
            }
        });
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        String savedEmail = sharedPreferences.getString("email", "");
        String savedPassword = sharedPreferences.getString("password", "");

        // If saved email and password exist, automatically log in
        if (!savedEmail.isEmpty() && !savedPassword.isEmpty()) {
            auth.signInWithEmailAndPassword(savedEmail, savedPassword)
                    .addOnSuccessListener(authResult -> {
                        Intent myIntent = new Intent(MainActivity.this, MapActivity.class);
                        startActivity(myIntent);
                        finish();
                    })
                    .addOnFailureListener(e -> {
                        Snackbar.make(root, "Error. " + e.getMessage(), Snackbar.LENGTH_SHORT).show();
                    });
        } else {
            // If saved email and password do not exist, show the login window
            showLogInWindow();
        }

        btnLogIN.setOnClickListener(v -> showRegisterWindow());
        btnSingIn.setOnClickListener(v -> showLogInWindow());
    }


   private void showLogInWindow() {

       SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
       String savedEmail = sharedPreferences.getString("email", "");
       String savedPassword = sharedPreferences.getString("password", "");


       if (!savedEmail.isEmpty() && !savedPassword.isEmpty()) {
           Intent myIntent = new Intent(MainActivity.this, MapActivity.class);
           startActivity(myIntent);
           finish();
           return;
       }
       AlertDialog.Builder dialog = new AlertDialog.Builder(this);
       dialog.setTitle("Log in");
       dialog.setMessage("Enter data for log in");

       LayoutInflater infater = LayoutInflater.from(this);
       View loginwindows = infater.inflate(R.layout.sing_in_window, null);
       dialog.setView(loginwindows);

       final EditText email = loginwindows.findViewById(R.id.emailField);
       final EditText password = loginwindows.findViewById(R.id.passField);

       dialog.setNegativeButton("Back", (dialogInterface, which) -> dialogInterface.dismiss());
       dialog.setPositiveButton("Log In", (dialogInterface, which) -> {
           if (TextUtils.isEmpty(email.getText().toString())) {
               Snackbar.make(root, "Enter your email", Snackbar.LENGTH_SHORT).show();
               return;
           }
           if (password.getText().toString().length() < 8) {
               Snackbar.make(root, "Enter password more than 8 characters", Snackbar.LENGTH_SHORT).show();
               return;
           }

           auth.signInWithEmailAndPassword(email.getText().toString(), password.getText().toString())
                   .addOnSuccessListener(authResult -> {

                       SharedPreferences.Editor editor = sharedPreferences.edit();
                       editor.putString("email", email.getText().toString());
                       editor.putString("password", password.getText().toString());
                       editor.apply();

                       Intent myIntent = new Intent(MainActivity.this, MapActivity.class);
                       startActivity(myIntent);
                       finish();
                   })
                   .addOnFailureListener(e -> {
                       Snackbar.make(root, "Error. " + e.getMessage(), Snackbar.LENGTH_SHORT).show();
                   });
       });

       dialog.show();
   }


    private void showRegisterWindow() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("Register");
        dialog.setMessage("Enter all data for registration");

        LayoutInflater infater = LayoutInflater.from(this);
        View registerwindows = infater.inflate(R.layout.windows1,null);
        dialog.setView(registerwindows);

        final EditText email = registerwindows.findViewById(R.id.emailField);
        final EditText password= registerwindows.findViewById(R.id.passField);
        final EditText name = registerwindows.findViewById(R.id.nameField);
        final EditText phone = registerwindows.findViewById(R.id.phoneField);

        dialog.setNegativeButton("Back", (dialoginterfeas, which) -> dialoginterfeas.dismiss());
        dialog.setPositiveButton("Log In", (dialoginterfeas, which) -> {
            if(TextUtils.isEmpty(email.getText().toString())){
                Snackbar.make(root, "Еnter your email", Snackbar.LENGTH_SHORT).show();
                return;
            }
            if(TextUtils.isEmpty(name.getText().toString())){
                Snackbar.make(root, "Еnter your name", Snackbar.LENGTH_SHORT).show();
                return;
            }
            if(TextUtils.isEmpty(phone.getText().toString())){
                Snackbar.make(root, "Еnter your phone number", Snackbar.LENGTH_SHORT).show();
                return;
            }
            if(password.getText().toString().length()< 8 ){
                Snackbar.make(root,"enter password more than 8 characters", Snackbar.LENGTH_SHORT).show();
                return;
            }
            // регистрациа
            auth.createUserWithEmailAndPassword(email.getText().toString(), password.getText().toString())
                    .addOnSuccessListener(authResult -> {
                        User user = new User();
                        user.setEmail(email.getText().toString());
                        user.setPassword(password.getText().toString());
                        user.setName(name.getText().toString());
                        user.setPhone(phone.getText().toString());

                        users.child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                .setValue(user)
                                .addOnSuccessListener(unused -> Snackbar.make(root,"User added",Snackbar.LENGTH_SHORT).show());
                    });


        });

        dialog.show();


    }

}