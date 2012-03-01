package org.renci.common.exec;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

/**
 * 
 * @author jdr0887
 * 
 */
public abstract class AbstractExecutor implements Executor {

    public AbstractExecutor() {
        super();
    }

    /**
     * Converts an environment map to an string array
     * 
     * @return the string array
     */
    protected String[] environmentToArray(Map<String, String> env) {
        Set<String> keySet = env.keySet();
        Iterator<String> keys = keySet.iterator();
        Vector<String> envVec = new Vector<String>();
        String[] envArr = new String[0];

        while (keys.hasNext()) {
            Object key = keys.next();
            Object val = env.get(key);
            envVec.add("" + key + "=" + val + "");
        }

        envArr = (String[]) envVec.toArray(new String[0]);

        return envArr;
    }
}
