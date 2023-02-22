package algonquin.cst2335.yang0212;


import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.Espresso.pressBack;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withParent;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;

import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import androidx.test.espresso.ViewInteraction;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class MainActivityTest {

    @Rule
    public ActivityScenarioRule<MainActivity> mActivityScenarioRule =
            new ActivityScenarioRule<>(MainActivity.class);

    /**
     * General test that the application shows "You shall not pass!" on the TextView when entered
     * "12345" in the EditText
     */
    @Test
    public void mainActivityTest() {

        ViewInteraction appCompatEditText = onView(withId(R.id.password));
        appCompatEditText.perform( replaceText( "12345"), closeSoftKeyboard() );

        ViewInteraction materialButton = onView(withId(R.id.loginButton));
        materialButton.perform(click());

        ViewInteraction textView = onView(withId(R.id.textView));
        textView.check(matches(withText("You shall not pass!")));

    }

    /**
     * Testing a password which is only missing an upper case letter
     */
    @Test
    public void findMissingUpperCaseTest() {
        // finds the EditText
        ViewInteraction appCompatEditText = onView(withId(R.id.password));
        // type in "password123#$*"
        appCompatEditText.perform( replaceText( "password123#$*"), closeSoftKeyboard() );

        //find the button
        ViewInteraction materialButton = onView(withId(R.id.loginButton));
        //click the button
        materialButton.perform(click());

        //find the TextView
        ViewInteraction textView = onView(withId(R.id.textView));
        //check that the text matches "You shall not pass!"
        textView.check(matches(withText("You shall not pass!")));

    }

    /**
     * Testing a password which is only missing a lower case letter
     */
    @Test
    public void findMissingLowerCaseTest() {
        // finds the EditText
        ViewInteraction appCompatEditText = onView(withId(R.id.password));
        // type in "PSW123#$*"
        appCompatEditText.perform( replaceText( "PSW123#$*"), closeSoftKeyboard() );

        //find the button
        ViewInteraction materialButton = onView(withId(R.id.loginButton));
        //click the button
        materialButton.perform(click());

        //find the TextView
        ViewInteraction textView = onView(withId(R.id.textView));
        //check that the text matches "You shall not pass!"
        textView.check(matches(withText("You shall not pass!")));

    }

    /**
     * Testing a password which is only missing a number
     */
    @Test
    public void findMissingNumberTest() {
        // finds the EditText
        ViewInteraction appCompatEditText = onView(withId(R.id.password));
        // type in "Password#"
        appCompatEditText.perform( replaceText( "Password#"), closeSoftKeyboard() );

        //find the button
        ViewInteraction materialButton = onView(withId(R.id.loginButton));
        //click the button
        materialButton.perform(click());

        //find the TextView
        ViewInteraction textView = onView(withId(R.id.textView));
        //check that the text matches "You shall not pass!"
        textView.check(matches(withText("You shall not pass!")));

    }

    /**
     * Testing a password which is only missing a special symbol
     */
    @Test
    public void findMissingSpecialSymbolTest() {
        // finds the EditText
        ViewInteraction appCompatEditText = onView(withId(R.id.password));
        // type in "Password123"
        appCompatEditText.perform( replaceText( "Password123"), closeSoftKeyboard() );

        //find the button
        ViewInteraction materialButton = onView(withId(R.id.loginButton));
        //click the button
        materialButton.perform(click());

        //find the TextView
        ViewInteraction textView = onView(withId(R.id.textView));
        //check that the text matches "You shall not pass!"
        textView.check(matches(withText("You shall not pass!")));

    }

    /**
     * Testing a password that has all of these requirements
     */
    @Test
    public void successfulTest() {
        // finds the EditText
        ViewInteraction appCompatEditText = onView(withId(R.id.password));
        // type in "Password123#"
        appCompatEditText.perform( replaceText( "Password123#"), closeSoftKeyboard() );

        //find the button
        ViewInteraction materialButton = onView(withId(R.id.loginButton));
        //click the button
        materialButton.perform(click());

        //find the TextView
        ViewInteraction textView = onView(withId(R.id.textView));
        //check that the text matches "Your password meets the requirements"
        textView.check(matches(withText("Your password meets the requirements")));

    }



    private static Matcher<View> childAtPosition(
            final Matcher<View> parentMatcher, final int position) {

        return new TypeSafeMatcher<View>() {
            @Override
            public void describeTo(Description description) {
                description.appendText("Child at position " + position + " in parent ");
                parentMatcher.describeTo(description);
            }

            @Override
            public boolean matchesSafely(View view) {
                ViewParent parent = view.getParent();
                return parent instanceof ViewGroup && parentMatcher.matches(parent)
                        && view.equals(((ViewGroup) parent).getChildAt(position));
            }
        };
    }
}
