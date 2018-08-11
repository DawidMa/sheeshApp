package de.dhkarlsruhe.it.sheeshapp.test;

import android.app.Activity;
import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.ViewInteraction;
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
import de.dhkarlsruhe.it.sheeshapp.sheeshapp.R;


import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.pressImeActionButton;
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

    @Given("^App is launched$")
    public void appLaunched() {
        Context appContext = InstrumentationRegistry.getTargetContext();
        assertEquals("de.dhkarlsruhe.it.sheeshapp.sheeshapp", appContext.getPackageName());

    }

    @And("^I am signed up$")
    public void signingIn() {
        ViewInteraction appCompatButton = onView(
                allOf(withId(R.id.btnWelSignUp), withText("SignUp"),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                2),
                        isDisplayed()));
        appCompatButton.perform(click());

        ViewInteraction appCompatEditText = onView(
                allOf(withId(R.id.edTUsername),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                3),
                        isDisplayed()));
        appCompatEditText.perform(click());

        ViewInteraction appCompatEditText2 = onView(
                allOf(withId(R.id.edTUsername),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                3),
                        isDisplayed()));
        appCompatEditText2.perform(replaceText("test"), closeSoftKeyboard());

        ViewInteraction appCompatEditText3 = onView(
                allOf(withId(R.id.edTEmail),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                4),
                        isDisplayed()));
        appCompatEditText3.perform(replaceText("t@t.de"), closeSoftKeyboard());

        ViewInteraction appCompatEditText4 = onView(
                allOf(withId(R.id.edTPassword),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                5),
                        isDisplayed()));
        appCompatEditText4.perform(replaceText("a@111111"), closeSoftKeyboard());

        ViewInteraction appCompatEditText5 = onView(
                allOf(withId(R.id.edTPasswordRepeat),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                6),
                        isDisplayed()));
        appCompatEditText5.perform(replaceText("a@111111"), closeSoftKeyboard());

        ViewInteraction appCompatEditText6 = onView(
                allOf(withId(R.id.edTPasswordRepeat), withText("a@111111"),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                6),
                        isDisplayed()));
        appCompatEditText6.perform(pressImeActionButton());

        ViewInteraction appCompatButton2 = onView(
                allOf(withId(R.id.btnSigHomescreen), withText("submit"),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                2),
                        isDisplayed()));
        appCompatButton2.perform(click());
    }
    @And("^I am logged in$")
    public void loggingIn() {
        ViewInteraction appCompatButton3 = onView(
                allOf(withId(R.id.btnWelLogin), withText("LogIn"),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                3),
                        isDisplayed()));
        appCompatButton3.perform(click());

        ViewInteraction appCompatButton4 = onView(
                allOf(withId(R.id.btnLogLogin), withText("Login"),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                3),
                        isDisplayed()));
        appCompatButton4.perform(click());
    }
    @Then("^I start AddFriendActivity$")
    public void startAddfriend() {
        ViewInteraction floatingActionButton = onView(
                allOf(withId(R.id.fab),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.drawer_layout),
                                        0),
                                2),
                        isDisplayed()));
        floatingActionButton.perform(click());
    }


    @And("^I insert a name into text field$")
    public void iInsertName() {
        ViewInteraction appCompatEditText9 = onView(
                allOf(withId(R.id.addEtName),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                1),
                        isDisplayed()));
        appCompatEditText9.perform(click());
        ViewInteraction appCompatEditText10 = onView(
                allOf(withId(R.id.addEtName),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                1),
                        isDisplayed()));
        appCompatEditText10.perform(replaceText("manu"), closeSoftKeyboard());
    }

    @And("^I click the FAB$")
    public void iClickedFab() {
        ViewInteraction floatingActionButton2 = onView(
                allOf(withId(R.id.addFabAdd),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                2),
                        isDisplayed()));
        floatingActionButton2.perform(click());
    }

    @Then("^I see friend in list at MainActivity$")
    public void iSeeFriend()  {
        ViewInteraction textView = onView(
                allOf(withId(R.id.liChooseFriendName), withText("manu"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.lvFragFriList),
                                        0),
                                0),
                        isDisplayed()));
        textView.check(matches(withText("manu")));
    }

    @Then("^text field is deleted$")
    public void textFieldIsEmpty() {
        ViewInteraction editText = onView(
                allOf(withId(R.id.addEtName), withText("Name"),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                1),
                        isDisplayed()));
        editText.check(matches(withText("Name")));
    }

    @And("^I stay in the AddFriendActivity$")
        public void stayInAddfriend() {
        ViewInteraction textView = onView(
                allOf(withId(R.id.addTvTitle), withText("Wie heißt dein Kumpel?"),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                0),
                        isDisplayed()));
        textView.check(matches(isDisplayed()));

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
