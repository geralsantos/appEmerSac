package com.example.myapplication.ui.login;

import android.app.Activity;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatActivity;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.Config.Config;
import com.example.myapplication.Interface.ApiService;
import com.example.myapplication.Interface.Message;
import com.example.myapplication.Interface.myDbAdapter;
import com.example.myapplication.MainActivity;
import com.example.myapplication.R;
import com.example.myapplication.data.model.LoggedInUser;
import com.example.myapplication.ui.login.LoginViewModel;
import com.example.myapplication.ui.login.LoginViewModelFactory;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    private LoginViewModel loginViewModel;
    private ApiService servicio = Config.getRetrofit().create(ApiService.class);
    EditText usernameEditText;
    EditText passwordEditText;
    myDbAdapter helper;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        helper = new myDbAdapter(this);
        isLogged();

        loginViewModel = ViewModelProviders.of(this, new LoginViewModelFactory())
                .get(LoginViewModel.class);

        usernameEditText =(EditText) findViewById(R.id.username);
        passwordEditText =(EditText) findViewById(R.id.password);
        final Button loginButton = findViewById(R.id.login);
        final ProgressBar loadingProgressBar = findViewById(R.id.loading);

       // String usuario = preferences.getString()
        loginViewModel.getLoginFormState().observe(this, new Observer<LoginFormState>() {
            @Override
            public void onChanged(@Nullable LoginFormState loginFormState) {
                if (loginFormState == null) {
                    return;
                }
                loginButton.setEnabled(loginFormState.isDataValid());
                if (loginFormState.getUsernameError() != null) {
                    usernameEditText.setError(getString(loginFormState.getUsernameError()));
                }
                if (loginFormState.getPasswordError() != null) {
                    passwordEditText.setError(getString(loginFormState.getPasswordError()));
                }
            }
        });

        loginViewModel.getLoginResult().observe(this, new Observer<LoginResult>() {
            @Override
            public void onChanged(@Nullable LoginResult loginResult) {
                if (loginResult == null) {
                    return;
                }
                loadingProgressBar.setVisibility(View.GONE);
                if (loginResult.getError() != null) {
                    showLoginFailed(loginResult.getError());
                }
                if (loginResult.getSuccess() != null) {
                    updateUiWithUser(loginResult.getSuccess());
                }
                setResult(Activity.RESULT_OK);

                //Complete and destroy login activity once successful
                //finish();

            }
        });

        TextWatcher afterTextChangedListener = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // ignore
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // ignore
            }

            @Override
            public void afterTextChanged(Editable s) {
                loginViewModel.loginDataChanged(usernameEditText.getText().toString(),
                        passwordEditText.getText().toString());
            }
        };
        usernameEditText.addTextChangedListener(afterTextChangedListener);
        passwordEditText.addTextChangedListener(afterTextChangedListener);
        passwordEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    login(usernameEditText.getText().toString(),
                            passwordEditText.getText().toString());
                }
                return false;
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                loadingProgressBar.setVisibility(View.VISIBLE);
                login(usernameEditText.getText().toString(),
                        passwordEditText.getText().toString());


            }
        });
    }
    public void addUser()
    {
        String t1 = usernameEditText.getText().toString();
        String t2 = passwordEditText.getText().toString();
        if(t1.isEmpty() || t2.isEmpty())
        {
            Log.d("addUser","Enter Both Name and Password");
            Message.message(getApplicationContext(),"Debe llenar el usuario/contraseña");
        }
        else
        {
            Log.d("addUser",t1+t2+"");
            long id = helper.insertData(t1,t2);
            if(id<=0)
            {
                Message.message(getApplicationContext(),"Insertion Unsuccessful");
            }
        }
    }

    public void isLogged()
    {
        String data = helper.getData();
        if(!data.isEmpty()){
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }
    }

    public void login(final String usuario, final String password){
        Call<LoggedInUser> call = servicio.Login(usuario,password);
        call.enqueue(new Callback<LoggedInUser>() {
            @Override
            public void onResponse(Call<LoggedInUser> call, Response<LoggedInUser> response) {
                Log.d("LOGIN",response+"");
                if(response.isSuccessful()){
                    Log.d("LOGIN",response.body()+"");
                    Log.d("data response",response.body().getUsuario()+"");
                    SharedPreferences preferences = getSharedPreferences("datosusuario",MODE_PRIVATE);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString("usuario",usuario);
                    editor.putString("password",password);
                    editor.putString("empleado_nombres",response.body().getNombres());
                    editor.putString("empleado_apellidos",response.body().getApellidos());
                    editor.putInt("usuario_id",response.body().getId());
                    editor.commit();

                    addUser();
                    Log.d("LOGIN",response.body()+"");
                    loginViewModel.login(response.body().getNombres(), password);
                }else{
                    loginViewModel.login("","");
                }
            }
            @Override
            public void onFailure(Call<LoggedInUser> call, Throwable t) {
                Log.d("onFailure",t.getMessage());
                loginViewModel.login("","");
            }
        });
    }
    public void logout(){
        // session.setLoggedin(false);
        finish();
        startActivity(new Intent(LoginActivity.this, LoginActivity.class));
    }
    private void updateUiWithUser(LoggedInUserView model) {
        String welcome = getString(R.string.welcome) + model.getDisplayName();
        // TODO : initiate successful logged in experience
        Toast.makeText(getApplicationContext(), welcome, Toast.LENGTH_LONG).show();
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    private void showLoginFailed(@StringRes Integer errorString) {
        Toast.makeText(getApplicationContext(), errorString, Toast.LENGTH_SHORT).show();
    }
}
