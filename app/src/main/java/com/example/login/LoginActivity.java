package com.example.login;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity {

    private EditText etEmail, etPassword;
    private CheckBox cbxRemember;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        Button btnLogin = (Button) findViewById(R.id.btnLogin);
        Button btnSignup = (Button) findViewById(R.id.btnSignup);

        etEmail = (EditText) findViewById(R.id.etEmail);
        etPassword = (EditText) findViewById(R.id.etPassword);
        cbxRemember = (CheckBox) findViewById(R.id.cbxRemember);

        SharedPreferences pref = getSharedPreferences("LOGIN_PREFERENCES", Activity.MODE_PRIVATE);

        if(pref.contains("email")){
            loadMain();
        } else {
            btnLogin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    login();
                }
            });

            btnSignup.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(LoginActivity.this, SignupActivity.class);
                    startActivity(intent);
//                remove the finish() here
                }
            });
        }
    }

    public void login() {
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = getString(R.string.API_URL)+"/login";
        System.out.println(url);

        JSONObject logininfo = new JSONObject();
        try {
            logininfo.put("email", etEmail.getText().toString());
            logininfo.put("password", etPassword.getText().toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        System.out.println(logininfo);

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, logininfo, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                SharedPreferences pref = getSharedPreferences("LOGIN_PREFERENCES", Activity.MODE_PRIVATE);
                SharedPreferences.Editor editor = pref.edit();
//               TRAPPINGS FOR WRONG EMAIL AND PASSWORD
                System.out.println(response.toString());
                if(response.has("message")){
                    MainActivity.customToast(LoginActivity.this,"Invalid Credentials", R.drawable.ic_baseline_account_circle_24);
                } else {
                    if(cbxRemember.isChecked() == true){
                        editor.putString("email",etEmail.getText().toString());
                        editor.putString("password",etPassword.getText().toString());
                    } else {
                        editor.putString("email","guest");
                        editor.putString("password","guest");
                    }
                    try {
                        editor.putString("access_token",response.getString("access_token"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    //change to commit
                    editor.commit();
                    loadMain();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });

        queue.add(request);
    }

    public void loadMain(){
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}