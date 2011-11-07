package roboguice.calculator.view;

import com.xtremelabs.robolectric.Robolectric;
import com.xtremelabs.robolectric.RobolectricTestRunner;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import roboguice.RoboGuice;
import roboguice.calculator.util.MockRpnStack;
import roboguice.calculator.util.RpnStack;

import android.app.Activity;

import com.google.inject.AbstractModule;
import com.google.inject.util.Modules;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

@Ignore
@RunWith( RobolectricTestRunner.class )
public class TickerTapeViewTest {
    
    Activity context = new Activity();


    @Before
    public void setup() {
        RoboGuice.setBaseApplicationInjector(Robolectric.application, RoboGuice.DEFAULT_STAGE,
                Modules.override(RoboGuice.newDefaultRoboModule(Robolectric.application)).with( new MyTestModule() )
        );
    }

    
    @Test
    public void shouldDisplay123() {
        TickerTapeView v = new TickerTapeView(context,null);
        v.refresh();
        assertThat(v.getText().toString(), equalTo("1\n2\n3"));
    }



    public static class MyTestModule extends AbstractModule {
        @Override
        protected void configure() {
            bind( RpnStack.class ).to( MockRpnStack.class );
        }
    }
}
