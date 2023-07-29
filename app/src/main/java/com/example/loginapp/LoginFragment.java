package com.example.loginapp;


import android.app.ProgressDialog;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.MessageDigest;


/**
 * A simple {@link Fragment} subclass.
 */
public class LoginFragment extends Fragment {
    private static final String TAG = "LoginActivity";

    EditText email;
    TextInputLayout password;
    Button button;

    public LoginFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_login, container, false);
        email = v.findViewById(R.id.et_email);
        password = v.findViewById(R.id.et_password);
        button = v.findViewById(R.id.btn_login);


        Log.d(TAG, "Hello!!!!!");

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });
        return v;
    }

    public void login() {
        Log.d(TAG, "I'm Loging!!!!!!!!!");

        String email_string = email.getText().toString();
        String password_string = password.getEditText().getText().toString();

        button.setEnabled(false);

        Log.d(TAG, "email_string " + email_string);
        Log.d(TAG, "password_string " + password_string);

//        if (!validate()) {
//            onLoginFailed();
//            return;
//        }

        if (!userExists(email_string)) {
            userNotExistsFailed();
            return;
        }

        if (passwordCorrect(email_string, password_string)) {
            onLoginSuccessed();
            return;
        }
        onLoginFailed();


        // TODO: Implement authentication logic

//        new android.os.Handler().postDelayed(
//                new Runnable() {
//                    public void run() {
//                        // On complete call either onLoginSuccess or onLoginFailed
//                        onLoginSuccess();
//                        // onLoginFailed();
//                    }
//                }, 3000);
    }

    public boolean userExists(String email_string) {
        try {
            // Open the CSV file in the assets folder and read its contents
            Log.d(TAG, "Checking Exist!!!!!!!!!!!!!!!!");
            InputStream inputStream = getResources().openRawResource(R.raw.user_data);
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            String line;

            // Loop through the CSV entries to find a match
            while ((line = bufferedReader.readLine()) != null) {
                String[] userData = line.split(",");
                String storedEmail = userData[1];
                Log.d(TAG, "****************Stored_Email: "+ storedEmail);

                // Check if the provided email matches any entry
                if (storedEmail.equals(email_string)) {
                    bufferedReader.close();
                    return true;
                }
            }

            // Close the file and return false if no match is found
            bufferedReader.close();
            return false;
        } catch (Exception e) {
            // Handle any errors that might occur during file operations
            e.printStackTrace();
            return false;
        }
    }

    public boolean passwordCorrect(String email_string, String password_string) {
        String hashedPassword = hashPassword(password_string);

        try {
            // Open the CSV file in the assets folder and read its contents
            Log.d(TAG, "Checking Password!!!!!!!!!!!!!!!!");
            InputStream inputStream = getResources().openRawResource(R.raw.user_data);
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            String line;

            // Loop through the CSV entries to find a match
            while ((line = bufferedReader.readLine()) != null) {
                String[] userData = line.split(",");
                String storedEmail = userData[1];
                String storedHashPassword = userData[2];
                Log.d(TAG, "****************Stored_Email: " + storedEmail);
                Log.d(TAG, "****************Stored_Hash: " + storedHashPassword);
                Log.d(TAG, "****************Email: " + email_string);
                Log.d(TAG, "****************Password: " + hashedPassword);

                // Check if the provided email matches any entry
                if (storedEmail.equals(email_string)) {
                    if (storedHashPassword.equals(hashedPassword)) {
                        bufferedReader.close();
                        return true;
                    }
                }
            }

            // Close the file and return false if no match is found
            bufferedReader.close();
            return false;
        } catch (Exception e) {
            // Handle any errors that might occur during file operations
            e.printStackTrace();
            return false;
        }
    }

    private static String hashPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest(password.getBytes("UTF-8"));
            StringBuilder hexString = new StringBuilder();

            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }

            return hexString.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public void onLoginSuccess() {
        button.setEnabled(true);
        getActivity().finish();
    }

    public void userNotExistsFailed() {
        Toast.makeText(getActivity().getBaseContext(), "Email is not Registered", Toast.LENGTH_LONG).show();
        button.setEnabled(true);
    }

    public void onLoginFailed() {
        Toast.makeText(getActivity().getBaseContext(), "Login failed", Toast.LENGTH_LONG).show();

        button.setEnabled(true);
    }

    public void onLoginSuccessed() {
        Log.d(TAG, "@@@@@@@Login Successed@@@@@@@@@@@");
    }

    public boolean validate() {
        boolean valid = true;

        String email_string = email.getText().toString();
        String password_string = password.getEditText().getText().toString();

        if (email_string.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email_string).matches()) {
            email.setError("enter a valid email address");
            valid = false;
        } else {
            email.setError(null);
        }

        if (password_string.isEmpty() || password_string.length() < 4 || password_string.length() > 10) {
            password.setError("between 4 and 10 alphanumeric characters");
            valid = false;
        } else {
            password.setError(null);
        }

        return valid;
    }

}
