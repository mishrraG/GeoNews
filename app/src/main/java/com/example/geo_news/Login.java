package com.example.geo_news;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class Login extends AppCompatActivity {

    public Spinner spinner;
    EditText phno, otp;
    TextView otptxt;
    Button get, check, gsign;
    ProgressBar progressBar;
    String varCode, verid;
    FirebaseAuth firebaseAuth, mAuth;
    GoogleSignInClient mGoogleSignInClient;
    private String num;
    private static final int RC_SIGN_IN = 123;//for identifying the activity result

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_login);
        FirebaseApp.initializeApp(this);
        phno = findViewById(R.id.user_number);//input field for phone number
        otp = findViewById(R.id.otp);//input field for OTP code.
        otptxt = findViewById(R.id.otpshw);//text view
        get = findViewById(R.id.otpget);//button of GET OTP!
        progressBar = findViewById(R.id.progress);
        check = findViewById(R.id.verify);//button for verifying otp code
        gsign = findViewById(R.id.google_sign);//google signIn button
        otp.setVisibility(View.GONE);
        check.setVisibility(View.GONE);
        otptxt.setVisibility(View.GONE);
        progressBar.setVisibility(View.INVISIBLE);


        //button to start the otp validation
        get.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                num = phno.getText().toString().trim();//10 digit phone number
                if ((num.isEmpty())) {
                    Toast.makeText(Login.this, "Cannot be blank", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(Login.this, "OTP sent!", Toast.LENGTH_SHORT).show();
                    num = "+91" + num;   //county code + phone number

                    check.setVisibility(View.VISIBLE);
                    otp.setVisibility(View.VISIBLE);
                    otptxt.setVisibility(View.VISIBLE);
                    progressBar.setVisibility(View.VISIBLE);
                    otpFun(num);//putting logic code in function
                }
            }
        });

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .requestIdToken("64854599121-sj5cgulnebp3mdi4ahpb9l7de7fml747.apps.googleusercontent.com")
                .build();
        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        mAuth = FirebaseAuth.getInstance();
        //button to start google signIn
        gsign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signEmail();
            }
        });

        //if user logged in through number this happens
        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            Intent itn = new Intent(Login.this, IntroActivity.class);
            startActivity(itn);
        }

        //if user logged in through google account this happens
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        if (account != null) {
            Intent itn = new Intent(Login.this, IntroActivity.class);
            startActivity(itn);
        }
    }

    //<GOOGLESIGN IN complex don't touch it >
    //for activity chnaging see in function handleSignInResult(Task<GoogleSignInAccount> completedTask)
    public void signEmail() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);

        }
    }

    public void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            String idToken = account.getIdToken();
            // Signed in successfully, show authenticated UI.
            firebaseAuthWithGoogle(idToken);
            //updateUI(account);
            Intent intent = new Intent(Login.this, IntroActivity.class);
            startActivity(intent);
        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            //Log.w(TAG, "signInResult:failed code=" + e.getStatusCode());
            Toast.makeText(this, e.toString(), Toast.LENGTH_LONG).show();
            //updateUI(null);
        }
    }

    public void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                        } else {
                            // If sign in fails, display a message to the user.
                            //Log.w(TAG, "signInWithCredential:failure", task.getException());
                        }

                    }
                });
    }
    //</GOOGLE SIGN IN>

    //function for creating and initiating otp login
    private void otpFun(String num) {

        firebaseAuth = FirebaseAuth.getInstance();
        sendCode(num);//sending number to Firebase for OTP

        //for manually validating OTP
        check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String cod;
                cod = otp.getText().toString().trim();
                if ((cod.isEmpty()) || (cod.length() != 6)) {
                    Toast.makeText(Login.this, "Invalid", Toast.LENGTH_LONG).show();
                } else {
                    verify(cod);
                }
            }
        });
    }

    //<Firebase implementation for Phone number validation through otp.>
    PhoneAuthProvider.OnVerificationStateChangedCallbacks
            mCallBack = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        @Override
        public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
            String c = phoneAuthCredential.getSmsCode();
            if (c != null) {
                progressBar.setVisibility(View.INVISIBLE);
                verify(c);
            }
        }

        @Override
        public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);
            verid = s;
        }

        @Override
        public void onVerificationFailed(@NonNull FirebaseException e) {
            Toast.makeText(Login.this, e.getMessage().toString(), Toast.LENGTH_LONG).show();
            //Snackbar.make();
            progressBar.setVisibility(View.INVISIBLE);
            otp.setVisibility(View.INVISIBLE);
            check.setVisibility(View.INVISIBLE);
            otptxt.setVisibility(View.INVISIBLE);
            get.setVisibility(View.VISIBLE);
        }
    };
    //</Firebase implementation for Phone number validation through otp.>

    //fun for verifying phone number with country code
    public void verify(String cod) {

        PhoneAuthCredential phoneAuthCredential = PhoneAuthProvider.getCredential(verid, cod);
        signIn(phoneAuthCredential);
    }

    //function for changing activity after successful authentication through OTP
    private void signIn(PhoneAuthCredential phoneAuthCredential) {
        firebaseAuth.signInWithCredential(phoneAuthCredential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Intent intent = new Intent(Login.this, IntroActivity.class);
                            startActivity(intent);
                        } else {
                            Toast.makeText(Login.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    //fun for getting otp
    private void sendCode(String num) {

        PhoneAuthProvider.getInstance().verifyPhoneNumber(num, 60, TimeUnit.SECONDS, TaskExecutors.MAIN_THREAD, mCallBack);
    }


}