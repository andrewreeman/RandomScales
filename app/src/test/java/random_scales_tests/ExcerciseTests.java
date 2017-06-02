package random_scales_tests;

import android.util.Log;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import com.stepwise.random_scales.Exercise;

import java.util.Random;

@RunWith(PowerMockRunner.class)
@PrepareForTest({ Exercise.class, Log.class})
public class ExcerciseTests {

    @Before
    public void setup() {
        PowerMockito.mockStatic(Log.class);
    }

    @Test
    public void canCreateExcerciseForEveryKey(){

        final String[] keys = new String[]{"c", "c#", "d", "d#", "e", "f", "f#", "g", "g#", "a", "a#", "b", "db", "eb", "gb", "ab", "bb"};
        final String exerciseName = "test key";
        final String exerciseDescription = "This is an exercise";

        for (String key : keys) {
            Exercise.ExerciseType randomType = generateRandomExerciseType();
            try {
                Exercise exercise = new Exercise(key, exerciseName, randomType, exerciseDescription);

                Assert.assertTrue("Exercise type does not match type passed in ctr", randomType == exercise.getType());
                Assert.assertTrue(exerciseName.equals(exercise.getName()));
                Assert.assertTrue(exerciseDescription.equals(exercise.getHint()));
            } catch (Exercise.InvalidKeyException invalidKeyException) {
                Assert.fail("Invalid key found: " + key);
            }
        }




        Assert.assertThat("Thing", new Matcher<String>() {
            @Override
            public boolean matches(Object item) {
                return true;
            }

            @Override
            public void describeMismatch(Object item, Description mismatchDescription) {

            }

            @Override
            public void _dont_implement_Matcher___instead_extend_BaseMatcher_() {

            }

            @Override
            public void describeTo(Description description) {

            }
        });
    }

    private Exercise.ExerciseType generateRandomExerciseType() {
        Random randomGen = new Random();
        int randomNumber = randomGen.nextInt(2);

        if(randomNumber == 0){
            return Exercise.ExerciseType.SCALE;
        }
        else {
            return Exercise.ExerciseType.ARPEGGIO;
        }
    }

}
