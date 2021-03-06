/*

This is a fun project to demonstrate how to interact with the Spotify APIs now that it launched in Kenya.

@author Brian Osoro
@company Symatech Labs Ltd
@website https://www.symatechlabs.com/

Copyright © 2021 Symatech Labs Ltd.

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

 http://www.apache.org/licenses/LICENSE-2.0
Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.

*/

package com.symatechlabs.spotifykenya;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.spotify.sdk.android.authentication.AuthenticationClient;
import com.spotify.sdk.android.authentication.AuthenticationRequest;
import com.spotify.sdk.android.authentication.AuthenticationResponse;

import static com.symatechlabs.spotifykenya.Constants.CLIENT_ID;

public class Login extends AppCompatActivity {

    private static final int REQUEST_CODE = 1337;
    private static final String REDIRECT_URI = "yourcustomprotocol://callback";
    Button login,proceed;
    SharedPreferences sharedpreferences;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        AuthenticationRequest.Builder builder =
                new AuthenticationRequest.Builder(CLIENT_ID, AuthenticationResponse.Type.TOKEN, REDIRECT_URI);

        builder.setScopes(new String[]{"streaming"});
        AuthenticationRequest request = builder.build();



        sharedpreferences = getSharedPreferences(Constants.MY_PREFS, Context.MODE_PRIVATE);

        if(!tokenExists()){
            AuthenticationClient.openLoginActivity(this, REQUEST_CODE, request);
        }

        login = findViewById(R.id.login);
        proceed = findViewById(R.id.proceed);
        proceed.setVisibility(Button.GONE);
        login.setVisibility(Button.GONE); //When the App is starting


        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AuthenticationClient.openLoginActivity(Login.this, REQUEST_CODE, request);
            }
        });
        proceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(tokenExists()){
                    startActivity(new Intent(Login.this,MainActivity.class));
                }else{
                    Toast.makeText(Login.this , "Error occured, click the login button to retry.",
                            Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);

        // Check if result comes from the correct activity
        if (requestCode == REQUEST_CODE) {
            AuthenticationResponse response = AuthenticationClient.getResponse(resultCode, intent);
            switch (response.getType()) {

                case TOKEN:
                    /*
                    After successful authentication, we get the access token and store it on the shared preference.
                    There could be a security concern to this but they are also proposing the BBI which is more scary if you ask me.
                    */
                    Toast.makeText(Login.this , "Authentication successful",
                            Toast.LENGTH_LONG).show();
                    login.setVisibility(Button.GONE);
                    proceed.setVisibility(Button.VISIBLE);
                    SharedPreferences.Editor editor = sharedpreferences.edit();
                    editor.putString(Constants.PREF_TOKEN, response.getAccessToken());
                    editor.commit();

                    startActivity(new Intent(Login.this , MainActivity.class));
                    break;

                case ERROR:
                    login.setVisibility(Button.VISIBLE); //When an error occurs
                    Toast.makeText(Login.this , "Error occured, click the login button to retry.",
                            Toast.LENGTH_LONG).show();
                    break;
                default:
                    login.setVisibility(Button.VISIBLE); //When an error occurs
                    proceed.setVisibility(Button.VISIBLE);


            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(tokenExists()){
            startActivity(new Intent(this,MainActivity.class));
        }
    }

    public boolean tokenExists(){
        if(sharedpreferences.getString(Constants.PREF_TOKEN,"").trim().length() > 0){
           return  true;
        }else{
            return  false;
        }
    }
}


