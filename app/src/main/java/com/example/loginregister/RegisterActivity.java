package com.example.loginregister;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends AppCompatActivity {

    EditText editTextFirstName, editTextLastName,editTextMobile,editTextAddress, editTextEmail,editTextPassword, editTextConfirmPassword;
    Button cirRegisterButton;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    ProgressDialog progressDialog;

    FirebaseAuth mAuth;
    FirebaseUser mUser;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        changeStatusBarColor();

        editTextFirstName=findViewById(R.id.editTextFirstName);
        editTextLastName=findViewById(R.id.editTextLastName);
        editTextMobile=findViewById(R.id.editTextMobile);
        editTextAddress=findViewById(R.id.editTextAddress);
        editTextEmail=findViewById(R.id.editTextEmail);
        editTextPassword=findViewById (R.id.editTextPassword);
        editTextConfirmPassword=findViewById(R.id.editTextConfirmPassword);
        cirRegisterButton=findViewById(R.id.cirRegisterButton);
        progressDialog = new ProgressDialog(this);
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();

        cirRegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                PerformAuth();
            }
        });
    }

    public void PerformAuth()
    {
        String firstname = editTextFirstName.getText().toString();
        String lastname = editTextLastName.getText().toString();
        String mobile = editTextMobile.getText().toString();
        String address = editTextAddress.getText().toString();
        String email = editTextEmail.getText().toString();
        String pass = editTextPassword.getText().toString();
        String confirmpass = editTextConfirmPassword.getText().toString();


        if (firstname.isEmpty())
        {
            editTextFirstName.setError("First Name is required");
            editTextFirstName.requestFocus();
        }
        else if (lastname.isEmpty())
        {
            editTextLastName.setError("Last Name is required");
            editTextLastName.requestFocus();
        }
        else if (mobile.isEmpty())
        {
            editTextMobile.setError("Mobile number is required");
            editTextMobile.requestFocus();
        }
        else if (address.isEmpty())
        {
            editTextAddress.setError("Address is required");
            editTextAddress.requestFocus();
        }
        else if (!email.matches(emailPattern))
        {
            editTextEmail.setError("Please provide valid email!");
            editTextEmail.requestFocus();
        }
        else if (pass.isEmpty())
        {
            editTextPassword.setError("Password is required");
            editTextPassword.requestFocus();
        }
        else if (pass.length() < 6)
        {
            editTextPassword.setError("Password must be at least 6 characters");
        }
        else if (!pass.equals(confirmpass))
        {
            editTextConfirmPassword.setError("Password Doesn't Match");
            editTextConfirmPassword.requestFocus();
        }
        else
        {
            progressDialog.setMessage("Please Wait While Registration...");
            progressDialog.setTitle("Registration");
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();


            mAuth.createUserWithEmailAndPassword(email,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful())
                    {
                        User user = new User (firstname,lastname,mobile, address, email);
                        FirebaseDatabase.getInstance().getReference("Users")
                                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful())
                                {
                                    progressDialog.dismiss();
                                    sendUserToLoginActivity();
                                    Toast.makeText(RegisterActivity.this, "Registration Successful", Toast.LENGTH_SHORT).show();

                                }
                                else
                                {
                                    Toast.makeText(RegisterActivity.this, "Failed to register, Try again!", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                    else
                    {
                        Toast.makeText(RegisterActivity.this, "Failed to register, Email is taken", Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    }
                }
            });
        }

    }

    public void sendUserToLoginActivity() {
        Intent intent = new Intent(RegisterActivity.this,LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    public void changeStatusBarColor()
    {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getResources().getColor(R.color.register_bk_color));
        }
    }
    public void onLoginClick(View view)
    {
        startActivity(new Intent(this, LoginActivity.class));
        overridePendingTransition(R.anim.slide_in_left, android.R.anim.slide_out_right);
    }
}