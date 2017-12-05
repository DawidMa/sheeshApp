package  de.dhkarlsruhe.it.sheeshapp.test;

import java.lang.reflect.Field;
import de.dhkarlsruhe.it.sheeshapp.test.Constants;


import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

class Driver {
    static void tapElement(String text) throws IllegalAccessException {
        Field field = findElement(text);
        if (field != null)
            onView(withId(field.getInt(new Constants()))).perform(click());
        else
            onView(withText(text)).perform(click());
    }

    static Field findElement(String text) {
        try {
            return Constants.class.getDeclaredField(text);
        } catch (NoSuchFieldException e) {
            return null;
        }
    }
}
