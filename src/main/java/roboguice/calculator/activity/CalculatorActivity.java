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

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.Stack;

public class CalculatorActivity extends RoboActivity {

    @InjectView(R.id.tape)      TextView tapeView;
    @InjectView(R.id.enter)     Button enterButton;
    @InjectView(R.id.delete)    Button deleteButton;
    @InjectView(R.id.plus)      Button plusButton;
    @InjectView(R.id.minus)     Button minusButton;
    @InjectView(R.id.multiply)  Button multiplyButton;
    @InjectView(R.id.divide)    Button divideButton;

    @Inject RpnStack stack;

    String digitAccumulator = "";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateDisplay();
    }



    public void onDigitClicked( View digit ) {
        digitAccumulator += ((TextView) digit).getText();
        updateDisplay();
    }
    
    public void onOperationClicked( View operation ) {

        // Any operator will automatically push the current digits onto the stack as if the user hit 'enter'
        if( digitAccumulator.length()>0 ) {
            stack.push(new BigDecimal(digitAccumulator));
            digitAccumulator="";
        }
                
        BigDecimal tmp;
        switch( operation.getId() ) {
            case R.id.plus:
                if( stack.size()<2 ) break;
                stack.push(stack.pop().add(stack.pop()));
                break;

            case R.id.minus:
                if( stack.size()<2 ) break;
                tmp = stack.pop();
                stack.push( stack.pop().subtract(tmp) );
                break;

            case R.id.multiply:
                if( stack.size()<2 ) break;
                stack.push(stack.pop().multiply( stack.pop()));
                break;

            case R.id.divide:
                if( stack.size()<2 ) break;
                tmp = stack.pop();
                stack.push(stack.pop().divide(tmp, MathContext.DECIMAL128 ));
                break;
            
            case R.id.delete:
                if( stack.size()>=1 )
                    stack.pop();
                break;
            
        }

        updateDisplay();
    }

    protected void updateDisplay() {
        Stack<String> lines = new Stack<String>();

        if( digitAccumulator.length()>0 )
            lines.push(digitAccumulator);

        for( int i=0; lines.size()<=3 && i<stack.size(); ++i)
            lines.push(stack.get(stack.size()-i-1).toString());

        String text = "";
        for( int i=0; i<3 && i<lines.size(); ++i )
            text = lines.get(i) + "\n" + text + "\n";

        boolean linesHasAtLeastTwoItems = lines.size()>1;

        tapeView.setText( text.trim() );
        enterButton.setEnabled(digitAccumulator.length() > 0);
        deleteButton.setEnabled(stack.size() > 0 || digitAccumulator.length() > 0);
        plusButton.setEnabled(linesHasAtLeastTwoItems);
        minusButton.setEnabled(linesHasAtLeastTwoItems);
        divideButton.setEnabled(linesHasAtLeastTwoItems);
        multiplyButton.setEnabled(linesHasAtLeastTwoItems);
    }


}


class RpnStack extends Stack<BigDecimal> {
    @Inject SharedPreferences prefs;

    /**
     * Save to prefs automatically on pause
     */
    protected void onPause( @Observes OnPauseEvent ignored) {
        Ln.d("RpnStack.onPause");
        final SharedPreferences.Editor edit = prefs.edit();
        
        edit.clear();
        
        for( int i=0; i<size(); ++i )
            edit.putString(String.valueOf(i), get(i).toString());

        edit.commit();
    }

    /**
     * Restore from prefs automatically on resume
     */
    protected void onResume( @Observes OnResumeEvent ignored ) {
        Ln.d("RpnStack.onResume");
        for( int i=0; prefs.contains(String.valueOf(i)); ++i)
            insertElementAt( new BigDecimal(prefs.getString(String.valueOf(i),null)), i);
    }
}
