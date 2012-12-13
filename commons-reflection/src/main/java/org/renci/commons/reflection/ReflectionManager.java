package org.renci.commons.reflection;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.net.JarURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;

/**
 * 
 * @author jdr0887
 */
public class ReflectionManager {

    private static ReflectionManager instance;

    public static ReflectionManager getInstance() {
        if (instance == null) {
            instance = new ReflectionManager();
        }
        return (instance);
    }

    private ReflectionManager() {
    }

    /**
     * 
     * @param pkg
     * @param regex
     * @param filteredAnnotation
     * @return
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public List<Class<?>> lookupClassList(String pkg, String regex, Class filteredAnnotation) {
        List<Class<?>> ret = new ArrayList<Class<?>>();
        List<Class<?>> classList = new ArrayList<Class<?>>();

        try {
            getClasses(classList, pkg);

            Map<String, List<Class<?>>> classesByPackageMap = new HashMap<String, List<Class<?>>>();

            for (Class<?> clazz : classList) {
                String fullPackage = clazz.getPackage().toString();
                classesByPackageMap.put(fullPackage, new ArrayList<Class<?>>());
            }

            for (Class<?> clazz : classList) {
                String fullPackage = clazz.getPackage().toString();
                classesByPackageMap.get(fullPackage).add(clazz);
            }

            for (String key : classesByPackageMap.keySet()) {
                List<Class<?>> classes = classesByPackageMap.get(key);

                for (Class<?> c : classes) {

                    if (filteredAnnotation != null) {

                        if (!c.isEnum() && c.isAnnotationPresent(filteredAnnotation)) {

                            if (StringUtils.isNotEmpty(regex)) {
                                Pattern p = Pattern.compile(regex);
                                Matcher m = p.matcher(c.getName());
                                if (m.matches()) {
                                    ret.add(c);
                                }
                            } else {
                                ret.add(c);
                            }

                        }

                    } else {
                        ret.add(c);
                    }

                }

            }

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return ret;
    }

    /**
     * 
     * @param pkg
     * @param regex
     * @return
     */
    public List<Class<?>> lookupClassList(String pkg, String regex) {
        return lookupClassList(pkg, regex, null);
    }

    private void getClasses(List<Class<?>> cl, String pkgName) throws ClassNotFoundException {
        // Get a File object for the package
        List<URL> jarList = new ArrayList<URL>();
        String path = pkgName.replace('.', '/');
        try {
            ClassLoader cld = Thread.currentThread().getContextClassLoader();
            if (cld == null) {
                throw new ClassNotFoundException("Can't get class loader.");
            }
            Enumeration<URL> resources = cld.getResources(path);

            while (resources.hasMoreElements()) {

                URL resource = resources.nextElement();
                String resourcePath = resource.getPath();
                System.out.println("resourcePath = " + resourcePath);
                if (resourcePath.indexOf(".jar") != -1) {
                    jarList.add(resource);
                } else {
                    File resourceFile = new File(resourcePath.replace("file:", ""));
                    File[] files = resourceFile.listFiles(new FilenameFilter() {
                        public boolean accept(File dir, String name) {
                            if (name.endsWith(".class") && name.indexOf("$") == -1) {
                                return true;
                            }
                            return false;
                        }
                    });
                    for (File f : files) {
                        cl.add(Class.forName(pkgName + "." + f.getName().replace(".class", "")));
                    }
                }

            }

            if (jarList != null && jarList.size() > 0) {

                for (URL url : jarList) {

                    JarURLConnection conn = (JarURLConnection) url.openConnection();
                    JarFile jar = conn.getJarFile();
                    for (JarEntry jarEntry : Collections.list(jar.entries())) {
                        if (jarEntry.getName().length() > path.length()
                                && jarEntry.getName().substring(0, path.length()).equals(path)
                                && jarEntry.getName().endsWith(".class")) {
                            cl.add(Class.forName(jarEntry.getName().replaceAll("/", ".").replace(".class", "")));
                        } else {
                            getClasses(cl, pkgName + "." + jarEntry.getName());
                        }
                    }
                }

            }

        } catch (IOException e) {
            throw new ClassNotFoundException("No resource for " + path);
        } catch (NullPointerException x) {
            throw new ClassNotFoundException(pkgName + " does not appear to be a valid package");
        }
    }

}
