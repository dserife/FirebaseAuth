package com.example.firebaseauth;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import android.widget.Toolbar;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final int MY_REQUEST_CODE = 1212; //numara herhangi bir numara olabilir.
    List<AuthUI.IdpConfig> providers;

    Button btnSignOut;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //çıkış yap buton tanımlaması
        btnSignOut=findViewById(R.id.btnSignOut);

        btnSignOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AuthUI.getInstance()
                        .signOut(MainActivity.this)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                btnSignOut.setEnabled(false);
                                showSignInOptions();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                        Toast.makeText(MainActivity.this,""+e.getMessage(),Toast.LENGTH_LONG).show();
                    }
                });
            }
        });

        //giriş fonksiyonlarının tanımlanması, firebaseui çağrılma
        providers = Arrays.asList(
                new AuthUI.IdpConfig.EmailBuilder().build(), //email ile giriş 
                new AuthUI.IdpConfig.GoogleBuilder().build() //google hesabı ile giriş
        );
        
        showSignInOptions();
    }

    private void showSignInOptions() {

        startActivityForResult(
                AuthUI.getInstance().createSignInIntentBuilder()
                .setAvailableProviders(providers)
                .setTheme(R.style.MyTheme)
                .build(),MY_REQUEST_CODE
        );
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == MY_REQUEST_CODE){
            IdpResponse response = IdpResponse.fromResultIntent(data);

            if (resultCode==RESULT_OK){
                //kullanıcı oluşturma
                FirebaseUser firebaseUser= FirebaseAuth.getInstance().getCurrentUser();

                //giriş yapılan email adresini ekrana toast mesajı olarak gösterir.
                Toast.makeText(this,"Giriş yapılan mail : "+firebaseUser.getEmail(),Toast.LENGTH_LONG).show();

                //giriş yapıldıysa butonu aktif et.
                btnSignOut.setEnabled(true);
            }

            else {

                Toast.makeText(this,""+response.getError().getMessage(),Toast.LENGTH_LONG).show();
            }
        }
    }
}
