package com.example.ferterlizer;

import static android.content.ContentValues.TAG;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
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
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class RegisterAcivity extends AppCompatActivity {

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register_acivity);

        Button btn=findViewById(R.id.btnRegister);
        TextView txtAccount=findViewById(R.id.alreadyHaveAccount);
        txtAccount.setOnClickListener(v -> {
            startActivity(new Intent(RegisterAcivity.this, LoginActivity.class));
        });

        EditText email=findViewById(R.id.inputEmail);
        EditText password=findViewById(R.id.inputPassword);
        EditText confirmPassword=findViewById(R.id.inputConfirmPassword);
        EditText username=findViewById(R.id.inputUsername);

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        String uid = currentUser.getUid();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        Dialog dialog = new MaterialAlertDialogBuilder(this)
                .setTitle("Login in progress...")
                .setMessage("Please wait")
                .create();

     btn.setOnClickListener(v -> {
         dialog.show();

         //validate inputs
         String email1 = email.getText().toString();
         String password1 = password.getText().toString();
         String username1 = username.getText().toString();
         String confirmPassword1 = confirmPassword.getText().toString();
         if (email1.isEmpty()||password1.isEmpty()){
                dialog.dismiss();
             Toast.makeText(RegisterAcivity.this, "Please fill all fields", Toast.LENGTH_SHORT).show();
         }
         else if (password1.length()<6){
             dialog.dismiss();
             Toast.makeText(RegisterAcivity.this, "Password must be at least 6 characters", Toast.LENGTH_SHORT).show();

         }
            else if (!password1.equals(confirmPassword1)){
                dialog.dismiss();
                Toast.makeText(RegisterAcivity.this, "Passwords do not match", Toast.LENGTH_SHORT).show();
            }
         else {
             mAuth.createUserWithEmailAndPassword(email1, password1)
                     .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                         @Override
                         public void onComplete(@NonNull Task<AuthResult> task) {
                             if (task.isSuccessful()) {
                                 // Sign in success, update UI with the signed-in user's information
                                 Log.d(TAG, "createUserWithEmail:success");


                                 //save user details to firestore
                                 Map<String, Object> user = new HashMap<>();
                                    user.put("username", username1);
                                    user.put("email", email1);

                                    db.collection("users").document(uid)
                                            .set(user)
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                    Log.d(TAG, "DocumentSnapshot added with ID: " + uid);
                                                    startActivity(new Intent(RegisterAcivity.this, LoginActivity.class));
                                                }
                                            })
                                            .addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    Log.w(TAG, "Error adding document", e);
                                                }
                                            });

                                 dialog.dismiss();
                             } else {

                                 dialog.dismiss();

                                 // If sign in fails, display a message to the user.
                                 Log.w(TAG, "createUserWithEmail:failure", task.getException());
                                 Toast.makeText(RegisterAcivity.this, "Authentication failed.",
                                         Toast.LENGTH_SHORT).show();

                                 //updateUI(null);
                             }
                         }
                     });
         }
    });
}
}