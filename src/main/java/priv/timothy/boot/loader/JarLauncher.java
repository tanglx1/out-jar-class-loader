package priv.timothy.boot.loader;

import java.io.File;
import java.net.URI;
import java.net.URL;
import java.security.CodeSource;
import java.security.ProtectionDomain;
import java.util.ArrayList;
import java.util.List;
import java.util.jar.Attributes;
import java.util.jar.JarFile;

/**
 * 目前实现了，从外部目录，自动搜索class和jar包加入类加载器；
 * jar中的jar没有实现；jar中的class的话如果是以jar运行的，那jar中的class自动成为class-path由appclassloader加载，无需自定义类加载器去加载
 * 像spring-boot-loader就实现类以jar包运行，自动加载jar包中的jar作为类路径
 */
public class JarLauncher {
    static final String POINT_CLASS = "D:\\workspace\\rabbit/lib";

    static final String POINT_JAR_LIB = "D:\\workspace\\rabbit/";

    public static void main(String[] args) throws Exception {
        System.out.println("start");

        new JarLauncher().launch(args);

    }

    private void launch(String[] args) throws Exception {
        ClassLoader classLoader = createClassLoader(getClassPathArchives());
        launch(args, getMainClass(), classLoader);
    }

    private List<URL> getClassPathArchives() throws Exception {
        List<URL> urls = new ArrayList<>();

        List<URL> jarFileUrl = new JarFileArchive().getUrl(POINT_JAR_LIB);
        List<URL> explodeUrl = new ExplodeArchive().getUrl(POINT_CLASS);

        String codeSourcePath=getCodeSourcePath();//放这主要是看如何获取源代码绝对路径
        File file = new File(codeSourcePath);
        if(file.isFile()){
            //如果是jar的话打印一下manifest.mf的信息
            Attributes manifest= new JarFile(file).getManifest().getMainAttributes();
            for(Object key: manifest.keySet()){
                System.out.println("manifestInfo: "+key+"="+manifest.getValue(key.toString()));
            }
        }


        urls.addAll(jarFileUrl);
        urls.addAll(explodeUrl);

        return urls;
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

    private String getCodeSourcePath() throws Exception{
        ProtectionDomain protectionDomain = getClass().getProtectionDomain();
        CodeSource codeSource = protectionDomain.getCodeSource();
        URI location = (codeSource != null) ? codeSource.getLocation().toURI() : null;
        String path = (location != null) ? location.getSchemeSpecificPart() : null;
        if (path == null) {
            throw new IllegalStateException("Unable to determine code source archive");
        }
        System.out.println("ownPath="+path);//idea中运行，打印/D:/workspace/innerjarloader/target/classes/

        File root = new File(path);
        if (!root.exists()) {
            throw new IllegalStateException("Unable to determine code source archive from " + root);
        }

        System.out.println("ownPath File="+root.toURI().toURL());
        //运行jar方式跑，打印 file:/D:/workspace/innerjarloader/target/inner-jar-loader-1.0-SNAPSHOT.jar

        return path;
    }

}
