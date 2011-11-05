package roboguice.calculator.activity;

import roboguice.activity.RoboActivity;
import roboguice.calculator.R;
import roboguice.inject.InjectView;
import roboguice.util.Ln;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.google.inject.Inject;

import java.util.Stack;

public class CalculatorActivity extends RoboActivity {
    
    @InjectView(R.id.one) Button button1;
    @InjectView(R.id.two) Button button2;
    @InjectView(R.id.plus) Button buttonPlus;

    @Inject RpnStack stack;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        button1.setOnClickListener( new NumberButtonListener(1) );
        button2.setOnClickListener( new NumberButtonListener(2) );
    }


    
    public void onOperationClicked( View operation ) {
        switch( ((Button)operation).getText().charAt(0) ) {
            case '+':
                if( stack.size()<2 ) break;
                stack.push( stack.pop() + stack.pop() );
                break;

        }
    }


    class NumberButtonListener implements OnClickListener {
        
        Integer number;

        NumberButtonListener(Integer number) {
            this.number = number;
        }

        public void onClick(View view) {
            stack.push( number );
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
