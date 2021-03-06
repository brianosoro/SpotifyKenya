/*
This is a fun project to demonstrate how to interact with the Spotify APIs now that it lauched in Kenya

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

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

public class SpotifyKenya extends Application {

    SharedPreferences sharedpreferences;

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        sharedpreferences = getSharedPreferences(Constants.MY_PREFS, Context.MODE_PRIVATE);
        if(tokenExists()){
            Intent i = new Intent(this, MainActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(i);
        }else{
            Intent i = new Intent(this, Login.class);
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(i);
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
