package com.example.ferterlizer;


import static android.content.ContentValues.TAG;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Userlogin extends AppCompatActivity {

    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_userlogin);
        TextView btn=findViewById(R.id.textViewSignUp);
        EditText email=findViewById(R.id.inputEmail);
        EditText password=findViewById(R.id.inputPassword);
        Button login =findViewById(R.id.btnlogin);

        Dialog dialog = new MaterialAlertDialogBuilder(this)
                .setTitle("Login in progress...")
                .setMessage("Please wait")
                .create();
        mAuth = FirebaseAuth.getInstance();
        btn.setOnClickListener(v -> {

            // move to register activity
            Intent intent = new Intent(this, RegisterAcivity.class);
            startActivity(intent);

        });

        login.setOnClickListener(v -> {
            {
                dialog.show();

                //validate inputs
                String email1 = email.getText().toString();
                String password1 = password.getText().toString();
                if (email1.isEmpty()||password1.isEmpty()){
                    dialog.dismiss();
                    Toast.makeText(Userlogin.this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                }
                else if (password1.length()<6){
                    dialog.dismiss();
                    Toast.makeText(Userlogin.this, "Password must be at least 6 characters", Toast.LENGTH_SHORT).show();
                }
                else {
                    mAuth.signInWithEmailAndPassword(email1, password1)
                            .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        // Sign in success, update UI with the signed-in user's information
                                        Log.d(TAG, "signInWithEmail:success");
                                        FirebaseUser user = mAuth.getCurrentUser();
                                        dialog.dismiss();
                                        startActivity(new Intent(Userlogin.this, MainActivity.class));
                                        //updateUI(user);
                                    } else {
                                        // If sign in fails, display a message to the user.
                                        Log.w(TAG, "signInWithEmail:failure", task.getException());
                                        Toast.makeText(Userlogin.this, "Authentication failed.",
                                                Toast.LENGTH_SHORT).show();
                                        dialog.dismiss();
                                        //updateUI(null);
                                    }
                                }
                            });
                }
            }
        });



        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}