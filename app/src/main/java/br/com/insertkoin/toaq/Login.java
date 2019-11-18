package br.com.insertkoin.toaq;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Login extends Base implements ResetPassword.ResetPasswordInterface {

    private static final String TAG = "EmailPassword";
    private FirebaseAuth mAuth;
    EditText inputEmail;
    EditText inputSenha;
    Button buttonEntrar;
    TextView forgotPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();
        inputEmail = findViewById(R.id.inputEmail);
        inputSenha = findViewById(R.id.inputSenha);
        buttonEntrar = findViewById(R.id.buttonEntrar);
        forgotPassword = findViewById(R.id.forgotPassword);

        buttonEntrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn(inputEmail.getText().toString(), inputSenha.getText().toString());
            }
        });

        forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ResetPassword resetPassword = new ResetPassword();
                resetPassword.show(getSupportFragmentManager(), "resetPassword");
            }
        });

    }

    private void signIn(String email, String password) {
        Log.d(TAG, "signIn:" + email);
        if (!validateForm()) {
            return;
        }

        showProgressDialog();

        // [Inicio Login]
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "Login Efetuado com Sucesso!");
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "Login Falhou!", task.getException());
                            Toast.makeText(Login.this, "Autenticação Falhou!",
                                    Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }

                        // [START_EXCLUDE]
                        if (!task.isSuccessful()) {
                            Toast.makeText(Login.this, "Autenticação Falhou!",
                                    Toast.LENGTH_SHORT).show();
                        }
                        hideProgressDialog();
                        // [END_EXCLUDE]
                    }
                });
        // [END sign_in_with_email]
    }

    private boolean validateForm() {
        boolean valid = true;

        String email = inputEmail.getText().toString();
        if (TextUtils.isEmpty(email)) {
            inputEmail.setError("Required.");
            valid = false;
        } else {
            inputEmail.setError(null);
        }

        String password = inputSenha.getText().toString();
        if (TextUtils.isEmpty(password)) {
            inputSenha.setError("Required.");
            valid = false;
        } else {
            inputSenha.setError(null);
        }

        return valid;
    }

    private void updateUI(FirebaseUser user) {
        hideProgressDialog();
        if (user != null) {

            Toast.makeText(this,"Autenticado como: "+user.getEmail(), Toast.LENGTH_LONG).show();
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();

        } else {

        }
    }


    @Override
    public void onButtonClicked(String text) {
        mAuth.sendPasswordResetEmail(text).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                showProgressDialog();
                if(task.isSuccessful()){
                    hideProgressDialog();
                    Toast.makeText(Login.this,"Email de recuperação enviado com Sucesso!",Toast.LENGTH_SHORT).show();
                } else {
                    hideProgressDialog();
                    Toast.makeText(Login.this,"Falha ao enviar email de recuperação",Toast.LENGTH_SHORT).show();
                }

            }
        });
    }
}
