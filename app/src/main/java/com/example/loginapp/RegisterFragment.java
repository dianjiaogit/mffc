package com.example.loginapp;


import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;
import android.content.Context;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.security.MessageDigest;

import com.google.android.material.textfield.TextInputLayout;


/**
 * A simple {@link Fragment} subclass.
 */
public class RegisterFragment extends Fragment {

    private static final String TAG = "RegisterActivity";

    EditText name;
    EditText email;
    EditText password;
    EditText repassword;
    Button button;


    public RegisterFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_register, container, false);

        name = v.findViewById(R.id.et_name);
        email = v.findViewById(R.id.et_email);
        password = v.findViewById(R.id.et_password);
        repassword = v.findViewById(R.id.et_repassword);
        button = v.findViewById(R.id.btn_register);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                register();
            }
        });

        return v;
    }

    public void register() {
        Log.d(TAG, "I'm Registering!!!!!!!!!");

        String name_string = name.getText().toString();
        String email_string = email.getText().toString();
        String password_string = password.getText().toString();
        String repassword_string = repassword.getText().toString();

        Log.d(TAG, "name_string " + name_string);
        Log.d(TAG, "email_string " + email_string);
        Log.d(TAG, "password_string " + password_string);
        Log.d(TAG, "repassword_string" + repassword_string);

        if (userExists(email_string)) {
            userIsExistsFailed();
            return;
        }

        Log.d(TAG, "New User!!!!!!!!!!!!!!");


        if (!validate()) {
            onRegisterFailed();
            return;
        }

        Log.d(TAG, "New User Registering!!!!!!!!!");
        button.setEnabled(false);

        String hashedPassword = hashPassword(password_string);
        Log.d(TAG, "HashPassword!!!!!!!!!!!!!!!!!!!!!!!!: " + hashedPassword);

        try {
            Log.d(TAG, "Writing!!!!!!!!!!!!!!!!!!!!!!!!");
            FileOutputStream fileOutputStream = getActivity().openFileOutput("user_data.csv", Context.MODE_APPEND);
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(fileOutputStream);
            String newUserData = name_string + "," + email_string + "," + hashedPassword + "\n";
            Log.d(TAG, newUserData);
            outputStreamWriter.write(newUserData);
            outputStreamWriter.close();
            Log.d(TAG, "Finished Writing!!!!!!!!!!!!!!!!!!!!!!!!");
        } catch (Exception e) {
            // Handle any errors that might occur during file operations
            e.printStackTrace();
            button.setEnabled(true);
        }

        try {
            // Open the CSV file in the assets folder and read its contents
            Log.d(TAG, "Reprint Now!!!!!!!!!");
            InputStream inputStream = getResources().openRawResource(R.raw.user_data);
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            String line;

            // Loop through the CSV entries to find a match
            while ((line = bufferedReader.readLine()) != null) {
                String[] userData = line.split(",");
                String storedEmail = userData[1];
                String storedHashPassword = userData[2];
                Log.d(TAG, "$$$$$Stored_Email: " + storedEmail);
                Log.d(TAG, "$$$$$Stored_Hash: " + storedHashPassword);

            }

            // Close the file and return false if no match is found
            bufferedReader.close();
        } catch (Exception e) {
            // Handle any errors that might occur during file operations
            e.printStackTrace();
        }

        button.setEnabled(true);
//        new android.os.Handler().postDelayed(
//                new Runnable() {
//                    public void run() {
//                        // On complete call either onLoginSuccess or onLoginFailed
//                        onRegisterSuccess();
//                        // onLoginFailed();
//                    }
//                }, 3000);
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

    public void onRegisterSuccess() {
        button.setEnabled(true);
        getActivity().finish();
    }

    public void onRegisterFailed() {
        Toast.makeText(getActivity().getBaseContext(), "Register failed", Toast.LENGTH_LONG).show();
        button.setEnabled(true);
    }

    public void userIsExistsFailed() {
        Toast.makeText(getActivity().getBaseContext(), "Email is Already Registered", Toast.LENGTH_LONG).show();
        button.setEnabled(true);
    }

    public boolean validate() {
        boolean valid = true;

        String name_string = name.getText().toString();
        String email_string = email.getText().toString();
        String password_string = password.getText().toString();
        String repassword_string = repassword.getText().toString();

        if (!password_string.equals(repassword_string)) {
            repassword.setError("This is not same with the entered password.");
            valid = false;
        }


        if (email_string.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email_string).matches()) {
            email.setError("Enter a valid email address");
            valid = false;
        } else {
            email.setError(null);
        }

        if (password_string.isEmpty() || password_string.length() < 4 || password_string.length() > 10) {
            password.setError("Between 4 and 10 alphanumeric characters");
            valid = false;
        } else {
            password.setError(null);
        }

        return valid;
    }





}
