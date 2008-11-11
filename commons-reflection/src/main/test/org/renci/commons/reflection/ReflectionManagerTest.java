package org.renci.commons.reflection;

import java.util.List;

import org.junit.Test;
import org.renci.sp.annotations.Generator;

public class ReflectionManagerTest {

    @Test
    public void testReflectionManager() {
        ReflectionManager rm = ReflectionManager.getInstance();
        List<Class<?>> classList = rm.lookupClassList("org.renci.sp.applications", null, Generator.class);
        System.out.println(classList.size());
    }
    
}
