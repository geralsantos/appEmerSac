package com.example.myapplication.data;

import android.util.Log;
import android.widget.Toast;

import com.example.myapplication.Config.Config;
import com.example.myapplication.Interface.ApiService;
import com.example.myapplication.data.model.LoggedInUser;

import java.io.IOException;
import java.util.Random;
import java.util.UUID;

import okhttp3.Interceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Class that handles authentication w/ login credentials and retrieves user information.
 */
public class LoginDataSource {
    public Result<LoggedInUser> login(String username, String password) {

        try {

            LoggedInUser fakeUser =  new LoggedInUser((new Random().nextInt(61) + 20),username, username,password);
            return new Result.Success<>(fakeUser);
        } catch (Exception e) {
            return new Result.Error(new IOException("Error logging in", e));
        }
    }

    public void logout() {
        // TODO: revoke authentication

    }
}
