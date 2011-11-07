package roboguice.calculator.util;

public class RpnStackFactory {
    protected static RpnStack instance;
    
    public static RpnStack getInstance() {
        
        if( instance==null ) {
            synchronized (RpnStackFactory.class) {
                if( instance==null )
                    instance = new RpnStack();
            }
        }
        
        return instance;
    }
}
