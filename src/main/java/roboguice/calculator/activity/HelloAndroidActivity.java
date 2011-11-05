package roboguice.calculator.activity;

import roboguice.activity.RoboActivity;
import roboguice.calculator.R;

import android.os.Bundle;

public class HelloAndroidActivity extends RoboActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
    }

}

