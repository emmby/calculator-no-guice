package roboguice.calculator.activity;

import roboguice.activity.RoboActivity;
import roboguice.calculator.R;
import roboguice.inject.InjectView;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.inject.Inject;

import java.util.Stack;

public class CalculatorActivity extends RoboActivity {

    @InjectView(R.id.tape) TextView tapeView;
    @InjectView(R.id.enter) Button enterButton;

    @Inject Stack<Integer> stack;

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

}
