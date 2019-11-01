package br.com.insertkoin.toaq;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final int MY_REQUEST_COODE = 6996;
    private static final String TAG = "EmailPassword";
    Button openCamera;
    Button buttonSignOut;
    TextView loginCheck;
    private FirebaseAuth mAuth;
    List<AuthUI.IdpConfig> providers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        providers = Arrays.asList(
                //new AuthUI.IdpConfig.PhoneBuilder().build(),
                new AuthUI.IdpConfig.EmailBuilder().build()
        );

        loginCheck = findViewById(R.id.loginCheck);
        mAuth = FirebaseAuth.getInstance();

        buttonSignOut = findViewById(R.id.buttonSignOut);
        buttonSignOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                AuthUI.getInstance().signOut(MainActivity.this)
//                        .addOnCompleteListener(new OnCompleteListener<Void>() {
//                            @Override
//                            public void onComplete(@NonNull Task<Void> task) {
//                                buttonSignOut.setEnabled(false);
//                                showSignInOptions();
//                            }
//                        }).addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception e) {
//                        Toast.makeText(MainActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
//                    }
//                });
                mAuth.signOut();
                updateUI(null);
            }
        });

        openCamera = findViewById(R.id.buttonOpenCamera);
        openCamera.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(MainActivity.this, QRCamera.class);
            MainActivity.this.startActivity(intent);
        }
    });

    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);
    }

    private void updateUI(FirebaseUser user) {
        if (user != null) {
            if (!user.isEmailVerified()) {
            loginCheck.setText("Email não verificado!");
            loginCheck.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    sendEmailVerification();
                }
            });
            } else {
                loginCheck.setText("Email verificado!");
            }
        } else {
            Intent intent = new Intent(this, Login.class);
            startActivity(intent);
            finish();
            //showSignInOptions();
        }
    }

    private void showSignInOptions() {
        startActivityForResult(
                AuthUI.getInstance().createSignInIntentBuilder()
                .setAvailableProviders(providers).build(),MY_REQUEST_COODE
        );
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == MY_REQUEST_COODE) {
            IdpResponse response = IdpResponse.fromResultIntent(data);
            if (resultCode == RESULT_OK) {
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                Toast.makeText(this,""+user.getEmail(), Toast.LENGTH_SHORT).show();
                buttonSignOut.setEnabled(true);
            } else {
                Toast.makeText(this, ""+response.getError().getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void sendEmailVerification() {

        final FirebaseUser user = mAuth.getCurrentUser();
        user.sendEmailVerification()
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        if (task.isSuccessful()) {
                            Toast.makeText(MainActivity.this,
                                    "Verification email sent to " + user.getEmail(),
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            Log.e(TAG, "sendEmailVerification", task.getException());
                            Toast.makeText(MainActivity.this,
                                    "Failed to send verification email.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}
