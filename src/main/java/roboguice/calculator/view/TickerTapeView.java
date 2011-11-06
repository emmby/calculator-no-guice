package roboguice.calculator.view;

import roboguice.RoboGuice;
import roboguice.activity.event.OnResumeEvent;
import roboguice.calculator.util.RpnStack;
import roboguice.event.Observes;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

import com.google.inject.Inject;

import java.util.Stack;

/**
 * A simple TextView that knows how to read an RpnStack
 * and display it.
 *
 * @see {@link #refresh()}
 */
public class TickerTapeView extends TextView {
    @Inject RpnStack stack;

    public TickerTapeView(Context context, AttributeSet attrs) {
        super(context, attrs);
        RoboGuice.injectMembers(context,this);
    }

    public void refresh() {
        Stack<String> lines = new Stack<String>();
        String digitAccumulator = stack.getDigitAccumulator();

        if( digitAccumulator.length()>0 )
            lines.push(digitAccumulator);

        for( int i=0; lines.size()<=3 && i<stack.size(); ++i)
            lines.push(stack.get(stack.size()-i-1).toString());

        String text = "";
        for( int i=0; i<3 && i<lines.size(); ++i )
            text = lines.get(i) + "\n" + text + "\n";

        setText(text.trim());
    }

    protected void onResume( @Observes OnResumeEvent ignored ) {
        refresh();
    }

}
