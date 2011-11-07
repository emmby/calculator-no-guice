package roboguice.calculator.util;

import android.content.Context;

public class RpnStackFactory {
    protected static RpnStack instance;
    
    public static RpnStack getInstance(Context context) {
        
        if( instance==null ) {
            synchronized (RpnStackFactory.class) {
                if( instance==null )
                    instance = new RpnStack(context);
            }
        }
        
        return instance;
    }
}
