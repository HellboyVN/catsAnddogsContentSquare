package com.example.catsanddogs.sdk;


import android.content.pm.ActivityInfo;

import androidx.test.rule.ActivityTestRule;
import androidx.test.runner.AndroidJUnit4;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.scrollTo;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition;
import static androidx.test.espresso.matcher.RootMatchers.withDecorView;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import com.example.catsanddogs.MainActivity;
import com.example.catsanddogs.R;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;


import java.io.File;
import java.util.HashMap;
import java.util.SortedSet;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@RunWith(AndroidJUnit4.class)
public class CsAnalyticsTest {

    private  MainActivity activity;
    private CsAnalytics csAnalytics;

    private static final int POSITION_TO_TEST = 10;
    private static final int PET_SIZE = 200;
    private static final String CAT = "cat";
    private static final String DOG = "dog";
    private static final String fileName = "cat_and_dog.txt";

    @Rule
    public ActivityTestRule<MainActivity> activityTestRule =
            new ActivityTestRule<>(MainActivity.class);

    @Before
    public void before() {
        activity = activityTestRule.getActivity();
        csAnalytics = activity.getAdapter().getCsAnalytics();
    }

    @Test
    public void clickOnSpecificPositionTest() {
        // Get filled pet list and check its size
        String[] mPetList = activity.getAdapter().getmPetList();
        assertEquals(mPetList.length, PET_SIZE);

        // Count how many pets are the same with the pet at position ITEM_TO_TEST, include itself
        int petCount = 1;
        // Save the last position that have the same pet with the pet at position ITEM_TO_TEST
        int checkPoint = -1;

        for (int i = 0; i < POSITION_TO_TEST; i++) {
            if (mPetList[i].equals(mPetList[POSITION_TO_TEST])) {
                petCount++;
                checkPoint = i;
            }

        }
        // Perform test on position POSITION_TO_TEST
        checkToast(POSITION_TO_TEST, mPetList[POSITION_TO_TEST], petCount, csAnalytics);
        // make enough time for next perform
        sleep(2500);
        // Perform test again on position checkPoint
        if (checkPoint != -1) {
            checkToast(checkPoint, mPetList[checkPoint], petCount - 1, csAnalytics);
        } else {
            // checkPoint = -1 means that all the pets from position 0 to position (POSITION_TO_TEST - 1)
            // are different with the pet at position POSITION_TO_TEST
            checkPoint = POSITION_TO_TEST - 1;
            checkToast(checkPoint, mPetList[checkPoint], POSITION_TO_TEST, csAnalytics);
        }
    }

    @Test
    public void dataSurviveOnScreenRotationTest() {
        String[] mPetList_before = activity.getAdapter().getmPetList();
        assertEquals(mPetList_before.length, PET_SIZE);
        // Perform screen rotation
        if (activity != null) {
            sleep(500);
            activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
            sleep(500);
            activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            sleep(500);
        }
        String[] mPetList_after = activity.getAdapter().getmPetList();
        assertEquals(mPetList_after.length, PET_SIZE);

        assertArrayEquals(mPetList_before, mPetList_after);
    }

    @Test
    public void comparePetTrackerTest() {
        /*
         * Get the pet tracker on CsAnalytics and compare each position of the pet tracker with
         * corresponding position on the list of pets of MainActivity.
         */
        HashMap<String, SortedSet<Integer>> petMap;
        String[] mPetList = activity.getAdapter().getmPetList();
        assertEquals(mPetList.length, PET_SIZE);

        onView(withId(R.id.recycler_view)).perform(actionOnItemAtPosition(POSITION_TO_TEST, scrollTo()));

        if (csAnalytics != null) {
            petMap = csAnalytics.getPetMap();
            // get set of position of each pet
            SortedSet<Integer> cats = petMap.get(CAT);
            SortedSet<Integer> dogs = petMap.get(DOG);

            for (int cat : cats) {
                assertEquals(mPetList[cat], CAT);
            }

            for (int dog : dogs) {
                assertEquals(mPetList[dog], DOG);
            }
        }

    }

    public void checkToast(int position, String pet, int petCount, CsAnalytics csAnalytics) {
        if (activity != null && csAnalytics != null) {
            // clear log file
            csAnalytics.clear();
            assertFalse(isFileExist());
            // Scroll to specific position
            onView(withId(R.id.recycler_view)).perform(actionOnItemAtPosition(position, scrollTo()));
            sleep(100);
            // Perform item click to count the pet and display the toast message, then write to file, then read file to display content
            // We click continuously 2 times to make sure the debounce work well
            onView(withId(R.id.recycler_view)).perform(actionOnItemAtPosition(position, click()));
            onView(withId(R.id.recycler_view)).perform(actionOnItemAtPosition(position, click()));

            String resultToast = "Position " + position + ". There are " + petCount + " " + pet + "s.";

            // Check the displayed toast messages when we click on recycleView item at position ITEM_TO_TEST
            onView(withText(resultToast)).inRoot(withDecorView(not(is(activity.getWindow().getDecorView())))).check(matches(isDisplayed()));

            // Right now the result file has been created
            assertTrue(isFileExist());
            // Get result from file and check if it is the same with what we wrote and although we clicked 2 times, but only one result should be written
            FileUpdateReceiver fileUpdateReceiver = new FileUpdateReceiver();
            assertEquals(fileUpdateReceiver.readFileAsString(activity.getApplicationContext(), fileName), resultToast + "\n");
        }
    }

    public void sleep(long time) {
        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public boolean isFileExist() {
        if (activity != null) {
            File file = new File(activity.getApplicationContext().getFilesDir(), fileName);
            return file.exists();
        }
        return  false;
    }

}