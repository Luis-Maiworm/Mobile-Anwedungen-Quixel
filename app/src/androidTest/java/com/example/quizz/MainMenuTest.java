package com.example.quizz;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.isRoot;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.junit.Assert.assertEquals;
import android.app.Activity;
import androidx.test.core.app.ActivityScenario;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.intent.Intents;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import com.example.quizz.view.ChooseGamemodeActivity;
import com.example.quizz.view.MainMenuActivity;
import com.example.quizz.view.StatisticsActivity;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class MainMenuTest {

    @Rule
    public ActivityScenarioRule<MainMenuActivity> mainMenuActivityActivityScenarioRule
            = new ActivityScenarioRule<>(MainMenuActivity.class);

    @Test
    public void openStatisticsScreenUI() {
        Intents.init();
        onView(ViewMatchers.withId(R.id.statsBtn)).perform(click());
        intended(hasComponent(StatisticsActivity.class.getName()));
        Intents.release();


    }
    @Test
    public void openSingleplayerScreenUI() {
        Intents.init();
        onView(ViewMatchers.withId(R.id.singleplayerBtn)).perform(click());
        intended(hasComponent(ChooseGamemodeActivity.class.getName()));
        Intents.release();
    }
    @Test
    public void showQuitDialogAlertOnBackPressedUI() {
        onView(isRoot()).perform(ViewActions.pressBack());
        onView(withText("Do you really want to quit?")).check(matches(isDisplayed()));

    }
    @Test
    public void destroyMainMenuWithAlertDialog() {
        ActivityScenario<MainMenuActivity> mainMenuActivityActivityScenario = ActivityScenario.launch(MainMenuActivity.class);
        onView(isRoot()).perform(ViewActions.pressBack());
        onView(withId(android.R.id.button1)).perform(click());
        assertEquals(mainMenuActivityActivityScenario.getResult().getResultCode(), Activity.RESULT_CANCELED);




    }



}
