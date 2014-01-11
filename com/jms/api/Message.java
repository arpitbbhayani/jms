package com.jms.api; /**
 * Created by Arpit Bhayani on 10/1/14.
 */

import java.util.HashMap;

/**
 *  Interface for com.jms.api.Message
 */
public interface Message {

    public void addHeader ( String key , String value );

    public void addProperty ( String key , String value );

    public void addBody ( Object content );

    public HashMap<String , Object> parseMessage ();
}
