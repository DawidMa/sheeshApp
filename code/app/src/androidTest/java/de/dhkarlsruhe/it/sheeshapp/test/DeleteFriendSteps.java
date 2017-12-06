package de.dhkarlsruhe.it.sheeshapp.test;

import android.app.Activity;
import android.content.Context;
import android.test.ActivityInstrumentationTestCase2;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

import cucumber.api.java.Before;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import de.dhkarlsruhe.it.sheeshapp.sheeshapp.MainActivity;

/**
 * Created by Informatik on 06.12.2017.
 */

public class DeleteFriendSteps extends ActivityInstrumentationTestCase2<MainActivity> {

    private Activity activity;
    private Context instrumentationContext;
    private Context appContext;

    public DeleteFriendSteps() {
        super(MainActivity.class);
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

    @Before
    public void setUp() throws Exception {
        super.setUp();
        instrumentationContext = getInstrumentation().getContext();
        appContext = getInstrumentation().getTargetContext();
        activity = getActivity();
        assertNotNull(activity);
    }
}

