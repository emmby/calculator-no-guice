package roboguice.calculator.util;

import com.xtremelabs.robolectric.RobolectricTestRunner;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import roboguice.RoboGuice;

import android.app.Activity;

import com.google.inject.Inject;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

@RunWith(RobolectricTestRunner.class)
public class RpnStackTest {
    
    Activity context = new Activity();

    @Inject RpnStack stack;

    @Before
    public void setup() {
        RoboGuice.injectMembers(context,this);
    }

    @Test
    public void testAccumulator() {
        assertThat(stack.getDigitAccumulator(), equalTo(""));
        stack.setDigitAccumulator("foo");
        assertThat(stack.getDigitAccumulator(), equalTo("foo"));
    }
}
