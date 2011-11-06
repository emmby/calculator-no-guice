package roboguice.calculator.activity;

import roboguice.activity.RoboActivity;
import roboguice.activity.event.OnPauseEvent;
import roboguice.activity.event.OnResumeEvent;
import roboguice.calculator.R;
import roboguice.event.Observes;
import roboguice.inject.InjectView;
import roboguice.util.Ln;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.inject.Inject;

import java.util.Stack;

public class CalculatorActivity extends RoboActivity {

    @InjectView(R.id.tape) TextView tapeView;
    @InjectView(R.id.enter) Button enterButton;

    @Inject RpnStack stack;

    String digitAccumulator = "";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        updateDisplay();
    }


    public void onDigitClicked( View digit ) {
        digitAccumulator += (Integer.valueOf(((TextView) digit).getText().toString()));
        updateDisplay();
    }
    
    public void onOperationClicked( View operation ) {

        // Any operator will automatically push the current digits onto the stack as if the user hit 'enter'
        if( digitAccumulator.length()>0 ) {
            stack.push(Integer.valueOf(digitAccumulator));
            digitAccumulator="";
        }
                
        switch( operation.getId() ) {
            case R.id.plus:
                if( stack.size()<2 ) break;
                stack.push(stack.pop() + stack.pop());
                break;

            case R.id.minus:
                if( stack.size()<2 ) break;
                stack.push(-1 * (stack.pop() - stack.pop()));
                break;
        }

        updateDisplay();
    }

    protected void updateDisplay() {
        Stack<Integer> lines = new Stack<Integer>();

        if( digitAccumulator.length()>0 )
            lines.push(Integer.valueOf(digitAccumulator));

        for( int i=0; lines.size()<=3 && i<stack.size(); ++i)
            lines.push(stack.get(stack.size()-i-1));

        String text = "";
        for( int i=0; i<3 && i<lines.size(); ++i )
            text = lines.get(i) + "\n" + text + "\n";

        tapeView.setText( text.trim() );
        enterButton.setEnabled(digitAccumulator.length() > 0);
    }


    @Override
    protected void onPause() {
        super.onPause();
        Ln.d("onPause");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Ln.d("onResume");
    }
}


class RpnStack extends Stack<Integer> {
    @Inject SharedPreferences prefs;

    /**
     * Save to prefs automatically on pause
     */
    protected void onPause( @Observes OnPauseEvent ignored) {
        Ln.d("RpnStack.onPause");
        final SharedPreferences.Editor edit = prefs.edit();
        
        for( int i=0; i<size(); ++i )
            edit.putInt(String.valueOf(i), get(i));

        edit.commit();
    }

    /**
     * Restore from prefs automatically on resume
     */
    protected void onResume( @Observes OnResumeEvent ignored ) {
        Ln.d("RpnStack.onResume");
        for( int i=0; prefs.contains(String.valueOf(i)); ++i)
            insertElementAt(prefs.getInt(String.valueOf(i),0), i);
    }
}
