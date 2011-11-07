package roboguice.calculator.util;

import android.content.Context;

import java.math.BigDecimal;

public class MockRpnStack extends RpnStack {

    public MockRpnStack(Context context) {
        super(context);
        add( BigDecimal.valueOf(1) );
        add( BigDecimal.valueOf(2) );
        add( BigDecimal.valueOf(3) );
    }

    @Override
    public BigDecimal pop() {
        throw new UnsupportedOperationException();
    }

    @Override
    public BigDecimal push(BigDecimal object) {
        throw new UnsupportedOperationException();
    }
}
