package priv.timothy.boot.loader;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * JDK原始的JarFile URL的定义
 * 原始的JarFile URL是这样子的：
 * jar:file:/tmp/target/demo-0.0.1-SNAPSHOT.jar!/
 * jar包里的资源的URL：
 * jar:file:/tmp/target/demo-0.0.1-SNAPSHOT.jar!/com/example/SpringBootDemoApplication.class
 */
public class JarFileArchive {

    /**
     * 获取lib下面一级的所有的jar包URL ，如jar:file:/D:/workspace/rabbit/rabbit-1.0.jar!/
     * @param lib
     * @return
     * @throws Exception
     */
    public List<URL> getUrl(String lib) throws Exception {
        File file = new File(lib);
        List<URL> urls = new ArrayList<>();

        if (file.isDirectory()) {
            File[] files = file.listFiles((dir, name) -> name.endsWith("jar"));
            for (File fileTemp : files) {
                String jarFilePath = fileTemp.toURI() + "!/";
                jarFilePath = jarFilePath.replace("file:////", "file://"); // Fix UNC paths
                URL url = new URL("jar", "", -1, jarFilePath);//jar:file:/D:/workspace/rabbit/amqp-client-5.5.0.jar!/
                urls.add(url);
            }
        }
        System.out.println(urls);
        return urls;
    }
}
