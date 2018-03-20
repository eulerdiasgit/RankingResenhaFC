package br.com.euprogramador.rankingresenhafc.ui.activities;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import br.com.euprogramador.rankingresenhafc.R;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    @BindView(R.id.login_et_email)
    TextInputEditText etEmail;

    @BindView(R.id.login_et_senha)
    TextInputEditText etSenha;

    @BindView(R.id.login_pb_loading)
    ProgressBar pbLoadingIndicator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        ButterKnife.bind(this);
        mAuth = FirebaseAuth.getInstance();

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();

                if (user != null) {
                    Toast.makeText(LoginActivity.this, user.getEmail(), Toast.LENGTH_SHORT).show();
                } else {

                }
            }
        };
    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @OnClick(R.id.login_btn_entrar)
    public void loginUser() {
        showProgressBar();
        String email = etEmail.getText().toString().trim();
        String senha = etSenha.getText().toString().trim();

        mAuth.signInWithEmailAndPassword(email, senha).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                hideProgressBar();

                if (!task.isSuccessful()) {
                    Toast.makeText(LoginActivity.this, "Email e/ou senha inválidos.", Toast.LENGTH_SHORT).show();
                    return;
                }

            }
        });

        Log.v("Login", email + " - " + senha);
    }

    private void showProgressBar() {
        pbLoadingIndicator.setVisibility(View.VISIBLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        getWindow().setBackgroundDrawable(getResources().getDrawable(R.drawable.background_opacity));
    }

    private void hideProgressBar() {
        pbLoadingIndicator.setVisibility(View.GONE);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        ;
    }


    @Override
    protected void onStop() {
        super.onStop();
        mAuth.removeAuthStateListener(mAuthListener);
    }
}
