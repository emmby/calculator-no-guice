package roboguice.calculator.activity;

import roboguice.activity.RoboActivity;
import roboguice.calculator.R;
import roboguice.util.Ln;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.inject.Inject;

import java.util.Stack;

public class CalculatorActivity extends RoboActivity {
    
    //@InjectView(R.id.one) Button button1;
    //@InjectView(R.id.two) Button button2;
    //@InjectView(R.id.plus) Button buttonPlus;

    @Inject RpnStack stack;

    String digitAccumulator = "";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

    }


    public void onDigitClicked( View digit ) {
        digitAccumulator += (Integer.valueOf(((TextView) digit).getText().toString()));
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
    }


}



class RpnStack extends Stack<Integer> {
    @Override
    public Integer push(Integer object) {
        Ln.d("Pushing %s", object);
        return super.push(object);
    }
}
