package roboguice.calculator.activity;

import roboguice.activity.RoboActivity;
import roboguice.calculator.R;
import roboguice.calculator.util.RpnStack;
import roboguice.inject.InjectView;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.inject.Inject;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.HashMap;
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

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if( event.getAction()==KeyEvent.ACTION_UP ) {
            final Integer resourceId = keyboardShortcuts.get(keyCode);
            if( resourceId!=null ) {
                findViewById(resourceId).performClick();
                return true;
            }
        }
        return super.onKeyUp(keyCode, event);
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





    protected HashMap<Integer, Integer> keyboardShortcuts = new HashMap<Integer, Integer>() {{
        put(KeyEvent.KEYCODE_0, R.id.zero);
        put(KeyEvent.KEYCODE_1, R.id.one);
        put(KeyEvent.KEYCODE_2, R.id.two);
        put(KeyEvent.KEYCODE_3, R.id.three);
        put(KeyEvent.KEYCODE_4, R.id.four);
        put(KeyEvent.KEYCODE_5, R.id.five);
        put(KeyEvent.KEYCODE_6, R.id.six);
        put(KeyEvent.KEYCODE_7, R.id.seven);
        put(KeyEvent.KEYCODE_8, R.id.eight);
        put(KeyEvent.KEYCODE_9, R.id.nine);
        put(KeyEvent.KEYCODE_PLUS, R.id.plus);
        put(KeyEvent.KEYCODE_MINUS, R.id.minus);
        put(KeyEvent.KEYCODE_STAR, R.id.multiply);
        put(KeyEvent.KEYCODE_SLASH, R.id.divide);
        put(KeyEvent.KEYCODE_ENTER, R.id.enter);
    }};
}


