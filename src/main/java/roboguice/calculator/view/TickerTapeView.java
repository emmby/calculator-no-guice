package roboguice.calculator.view;

import roboguice.RoboGuice;
import roboguice.calculator.util.RpnStack;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

import java.util.Stack;

/**
 * A simple TextView that knows how to read an RpnStack
 * and display it.
 *
 * @see {@link #refresh()}
 */
public class TickerTapeView extends TextView {
    RpnStack stack;

    public TickerTapeView(Context context, AttributeSet attrs) {
        super(context, attrs);
        RoboGuice.injectMembers(context, this);
    }

    public void setStack(RpnStack stack) {
        this.stack = stack;
    }

    public void refresh() {
        Stack<String> lines = new Stack<String>();
        String digitAccumulator = stack.getDigitAccumulator();

        // Include the current number that the user is typing in the display
        if( digitAccumulator.length()>0 )
            lines.push(digitAccumulator);

        // Include up to 3 lines of stack
        for( int i=0; lines.size()<3 && i<stack.size(); ++i)
            lines.push(stack.get(stack.size()-i-1).toString());

        // Convert to text
        String text = "";
        for( String line : lines)
            text = line + "\n" + text + "\n";

        setText(text.trim());
    }
}
