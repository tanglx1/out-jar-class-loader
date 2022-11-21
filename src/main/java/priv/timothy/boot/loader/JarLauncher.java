package priv.timothy.boot.loader;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.List;

/**
 * Hello world!
 */
public class JarLauncher {
    static final String BOOT_INF_CLASSES = "D:\\workspace\\rabbit/lib";

    static final String BOOT_INF_LIB = "D:\\workspace\\rabbit/";

    public static void main(String[] args) throws Exception {
        System.out.println("start");

        new JarLauncher().launch(args);

    }

    private void launch(String[] args) throws Exception {
        ClassLoader classLoader = createClassLoader(getClassPathArchives());
        launch(args, getMainClass(), classLoader);
    }

    private List<URL> getClassPathArchives() throws Exception {
        List<URL> jarFileUrl = new JarFileArchive().getUrl(BOOT_INF_LIB);
        List<URL> explodeUrl = new ExplodeArchive().getUrl(BOOT_INF_CLASSES);
        jarFileUrl.addAll(explodeUrl);
        return jarFileUrl;
    }


    private ClassLoader createClassLoader(List<URL> urls) {
        return new LaunchedURLClassLoader(urls.toArray(new URL[0]), getClass().getClassLoader());
    }

    private String getMainClass() throws Exception {
        return "priv.timothy.App";
    }

    private void launch(String[] args, String mainClass, ClassLoader classLoader) throws Exception {
        Thread.currentThread().setContextClassLoader(classLoader);
        new MainMethodRunner(mainClass, args).run();

    }


}
