package algonquin.cst2335.yang0212;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import algonquin.cst2335.yang0212.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    private static String TAG = "MainActivity";
    private ActivityMainBinding binding;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.w( "MainActivity", "In onCreate() - Loading Widgets" );
        Log.d( TAG, "Message");

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        SharedPreferences prefs = getSharedPreferences("MyData", Context.MODE_PRIVATE);
        String emailAddress = prefs.getString("LoginName", "");
        binding.editTextEmail.setText(emailAddress);

        binding.loginButton.setOnClickListener(btn -> {
            String login = binding.editTextEmail.getText().toString();
            Intent nextPage = new Intent(MainActivity.this, SecondActivity.class);
            nextPage.putExtra( "EmailAddress", login );
            startActivity(nextPage);

            SharedPreferences.Editor editor = prefs.edit();
            editor.putString( "LoginName",login );
            editor.apply();
        });
    }







    @Override
    protected void onStart() {
        super.onStart();
        Log.w( "MainActivity", "In onStart() - The application is now visible on screen." );
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.w( "MainActivity", "In onResume() - The application is now responding to user input" );
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.w( "MainActivity", "In onPause() - The application no longer responds to user input" );
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.w( "MainActivity", "In onStop() - The application is no longer visible." );
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.w( "MainActivity", "In onDestroy() - Any memory used by the application is freed" );
    }
}