package de.dhkarlsruhe.it.sheeshapp.test;

import android.app.Activity;
import android.content.Context;
import android.support.test.espresso.ViewInteraction;
import android.test.ActivityInstrumentationTestCase2;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

import cucumber.api.java.After;
import cucumber.api.java.Before;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import de.dhkarlsruhe.it.sheeshapp.sheeshapp.AddFriendActivity;
import de.dhkarlsruhe.it.sheeshapp.sheeshapp.R;


import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.replaceText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;

public class AddFriendActivitySteps extends ActivityInstrumentationTestCase2<AddFriendActivity> {

    private Activity activity;
    private Context instrumentationContext;
    private Context appContext;

    public AddFriendActivitySteps() {
        super(AddFriendActivity.class);
    }

    @Before
    public void setUp() throws Exception {
        super.setUp();
        instrumentationContext = getInstrumentation().getContext();
        appContext = getInstrumentation().getTargetContext();
        activity = getActivity();
        assertNotNull(activity);
    }

    @Given("I am on the Add Friend Tab")
    public void i_am_on_friend_screen() {

        assertEquals(getActivity().getLocalClassName(), (AddFriendActivity.class.getCanonicalName()));
    }

    @And("I insert a name into text field")
    public void i_insert_name() {
        ViewInteraction appCompatEditText2 = onView(
                allOf(withId(R.id.addEtName),
                        isDisplayed()));
        appCompatEditText2.perform(replaceText("manu"), closeSoftKeyboard());
    }

    @Given("^I have internet$")
    public void i_have_internet() {
    }

    @When("^I tap \"(.+)\"$")
    public void i_tap_element(final String text) throws IllegalAccessException {
        Driver.tapElement(text);
    }

    @Then("^I see text \"(.+)\"$")
    public void i_see_text(final String text) {
        onView(withText(text)).check(matches(isDisplayed()));
    }

    @Then("^Success$")
    public void success() {
    }

    @After
    public void tearDown() throws Exception {
        ActivityFinisher.finishOpenActivities();
        getActivity().finish();
        super.tearDown();
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
