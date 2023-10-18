package com.keven1z;

import java.io.Closeable;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Enumeration;

/**
 * @author keven1z
 * @date 2023/02/23
 * 参考 @link <a href="https://github.com/alibaba/jvm-sandbox/blob/master/sandbox-agent/src/main/java/com/alibaba/jvm/sandbox/agent/SandboxClassLoader.java">jvm-sandbox</a>
 */
public class SimpleIASTClassLoader extends URLClassLoader {

    private final String toString;

    SimpleIASTClassLoader(
            final String engineJarFilePath) throws MalformedURLException {
        super(new URL[]{new URL("file:" + engineJarFilePath)});
        this.toString = String.format("SimpleIASTClassLoader[path=%s;]", engineJarFilePath);
    }

    @Override
    public URL getResource(String name) {
        URL url = findResource(name);
        if (null != url) {
            return url;
        }
        url = super.getResource(name);
        return url;
    }

    @Override
    public Enumeration<URL> getResources(String name) throws IOException {
        Enumeration<URL> urls = findResources(name);
        if (null != urls) {
            return urls;
        }
        urls = super.getResources(name);
        return urls;
    }

    @Override
    protected synchronized Class<?> loadClass(String name, boolean resolve) throws ClassNotFoundException {
        final Class<?> loadedClass = findLoadedClass(name);
        if (loadedClass != null) {
            return loadedClass;
        }
        try {
            Class<?> aClass = findClass(name);
            if (resolve) {
                resolveClass(aClass);
            }
            return aClass;
        } catch (Exception e) {
            return super.loadClass(name, resolve);
        }
    }

    @Override
    public String toString() {
        return toString;
    }


    /**
     * 尽可能关闭ClassLoader
     * <p>
     * URLClassLoader会打开指定的URL资源，在SANDBOX中则是对应的Jar文件，如果不在shutdown的时候关闭ClassLoader，会导致下次再次加载
     * 的时候，依然会访问到上次所打开的文件（底层被缓存起来了）
     * <p>
     * 在JDK1.7版本中，URLClassLoader提供了{@code close()}方法来完成这件事；但在JDK1.6版本就要下点手段了；
     * <p>
     * 该方法将会被{@code EngineController#shutdown}通过反射调用，
     * 请保持方法声明一致
     */
    @SuppressWarnings("unused")
    public void closeIfPossible() {
        try {
            ((Closeable) this).close();
        } catch (Exception cause) {
            // ignore...
        }
    }
}