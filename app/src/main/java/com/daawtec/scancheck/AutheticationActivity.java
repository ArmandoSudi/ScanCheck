package com.daawtec.scancheck;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.daawtec.scancheck.utils.Constant;

public class AutheticationActivity extends AppCompatActivity {

    private static final String TAG = "AutheticationActivity";

    EditText mUserNamenET, mPasswordET;
    Button mLogintBT;

    SharedPreferences mSharedPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authetication);

        mSharedPref = PreferenceManager.getDefaultSharedPreferences(this);

        initScreen();
    }

    public void initScreen(){
        mUserNamenET = findViewById(R.id.username_et);
        mPasswordET = findViewById(R.id.password_et);
        mLogintBT = findViewById(R.id.login_bt);

        mLogintBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = mUserNamenET.getText().toString();
                String password = mPasswordET.getText().toString();
                login(username, password);
            }
        });
    }

    /**
     * Pour la demo, nous allons utiliser 'default' pour mot de passe et username, et initialiser la
     * base des donnees seulement pour la premiere connexion
     * @param username
     * @param password
     */
    public void login(String username, String password) {
        if (username.equals("default") && password.equals("default")) {
            Intent intent = new Intent(AutheticationActivity.this, HomeActivity.class);
            startActivity(intent);
        } else {
            Toast.makeText(this, "Nom d'utilisateur ou mot de passe incorrect", Toast.LENGTH_SHORT).show();
        }
//        if (username.equals("default") && password.equals("default")) {
//            if(mSharedPref.getBoolean(Constant.KEY_IS_INITIALIZED, false)){
//                Toast.makeText(this, "Nom d'utilisateur ou mot de passe incorrect", Toast.LENGTH_SHORT).show();
//            } else {
//                Intent intent = new Intent(AutheticationActivity.this, HomeActivity.class);
//                startActivity(intent);
//            }
//
//        } else {
//            if (mSharedPref.getString(Constant.KEY_USERNAME, "").equals(username) &&
//                    mSharedPref.getString(Constant.KEY_PASSWORD, "").equals(password) &&
//                    !username.equals("") && !password.equals("")){
//
//                Intent intent = new Intent(AutheticationActivity.this, HomeActivity.class);
//                startActivity(intent);
//
//            } else {
//                Toast.makeText(this, "Nom d'utilisateur ou mot de passe incorrect", Toast.LENGTH_SHORT).show();
//            }
//        }
    }
}
