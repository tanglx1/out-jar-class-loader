package priv.timothy.boot.loader;

import java.lang.reflect.Method;

public class MainMethodRunner {
    private final String mainClassName;

    private final String[] args;

    public MainMethodRunner(String mainClass, String[] args) {
        this.mainClassName = mainClass;
        this.args = (args != null) ? args.clone() : null;
    }

    public void run() throws Exception {
        Class<?> mainClass = Thread.currentThread().getContextClassLoader().loadClass(this.mainClassName);
        Method mainMethod = mainClass.getDeclaredMethod("main", String[].class);
        mainMethod.invoke(null, new Object[] { this.args });
    }
}
