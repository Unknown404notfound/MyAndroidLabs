package algonquin.cst2335.yang0212;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

/**
 * This class is to perform a simple password checker app
 * @author Yuxi Yang
 * @version 1.0
 */
public class MainActivity extends AppCompatActivity {
    /** This holds the text at the centre of screen */
    TextView text = null;

    /** This holds the editable text at the centre of screen */
    EditText psw = null;
    /** This holds the button at the centre of screen */
    Button btn = null;
    /*
     * This function is a starting point of an Android application
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Loading the file called activity_main.xml, which is in the layout folder, in the res folder.
        setContentView(R.layout.activity_main);

        text = findViewById(R.id.textView);
        psw = findViewById(R.id.password);
        btn = findViewById(R.id.loginButton);

        /**
         * This function is to read the string in the EditText and check if the password is too
         * simple or not by clicking Login Button
         */
        btn.setOnClickListener( clk -> {
            String password = psw.getText().toString();
            //Call checkPasswordComplexity to check password
            if (checkPasswordComplexity(password)) {
                text.setText("Your password meets the requirements");
            }else {
                text.setText("You shall not pass!");


            }
        });
    }

    /**
     * This function is to check if the string of entered password has an Upper Case letter,
     * a lower case letter, a number, and a special symbol. And show a Toast message if not match
     * the requirement
     * @param pwd The String of entered password that we are checking.
     * @return Return ture if the String of password includes uppercase letter, lowercase letter,
     * number and special symbol, otherwise return false
     */
    boolean checkPasswordComplexity (String pwd) {
        boolean foundUpperCase, foundLowerCase, foundNumber, foundSpecial;
        foundUpperCase = foundLowerCase = foundNumber = foundSpecial = false;

        for (int i = 0; i < pwd.length(); i++) {
            char c = pwd.charAt(i);

            if (Character.isUpperCase(c)) {
                foundUpperCase = true;
            } else if (Character.isLowerCase(c)) {
                foundLowerCase = true;
            } else if (Character.isDigit(c)) {

                foundNumber = true;
            } else if (isSpecialCharacter(c)) {
                foundSpecial = true;
            }

        }


        if(!foundUpperCase) {
            Context context = this;
            CharSequence text = "Your password does not have an upper case letter";
            int duration = Toast.LENGTH_SHORT;
            Toast.makeText( context,text,duration ).show(); // Show Toast message if password missing a upper case letter;
            return false;
        }

        else if( !foundLowerCase) {
            Context context = this;
            CharSequence text = "Your password does not have a lower case letter";
            int duration = Toast.LENGTH_SHORT;
            Toast.makeText( context,text,duration ).show(); // Show Toast message if password missing a lower case letter;
            return false;
        }
        else if( ! foundNumber) {
            Context context = this;
            CharSequence text = "Your password does not have a number";
            int duration = Toast.LENGTH_SHORT;
            Toast.makeText( context,text,duration ).show(); // Show Toast message if password missing a number;
            return false;
        }

        else if(! foundSpecial) {
            Context context = getApplicationContext();
            CharSequence text = "Your password does not have a special character";
            int duration = Toast.LENGTH_SHORT;
            Toast.makeText( context,text,duration ).show(); // Show Toast message if password missing a special character;
            return false;
        }

        else
            return true; ////only get here if they're all true
    }

    /**
     * This function is to check if the character is a special character or not
     * @param c The entered character
     * @return Return true if character is one of the symbols list below, otherwise return false
     */
    boolean isSpecialCharacter(char c) {
        switch (c) {
            case '#':
            case '?':
            case '*':
            case '$':
            case '%':
            case '^':
            case '&':
            case '!':
            case '@':
                return true;
            default:
                return false;
        }

    }
}