package priv.timothy.boot.loader;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;


/**
 * 普通资源文件的URl路径为
 * file:/D:/workspace/rabbit/lib/amqp-client-5.5.0/com/rabbitmq/client/impl/nio/xx.class
 *
 *
 *
 *
 */
public class ExplodeArchive {

    /**
     * 获取lib目录下面所有的包文件的URL 如file:/D:/workspace/rabbit/lib/amqp-client-5.5.0/com/rabbitmq/client/impl/nio/
     * @param lib
     * @return
     * @throws Exception
     */
    public List<URL> getUrl(String lib) throws Exception {
        File file = new File(lib);
        List<URL> urls = new ArrayList<>();
        urls.addAll(getAllFileUrl(file));
        System.out.println(urls);
        return urls;
    }


    private List<URL> getAllFileUrl(File file) throws Exception {
        List<URL> urls = new ArrayList<>();

        File[] files = file.listFiles();

        for (File fileTemp : files) {
            if(fileTemp.isDirectory()){
                urls.addAll(getAllFileUrl(fileTemp));
                urls.add(fileTemp.toURI().toURL());
            }else if(fileTemp.getName().endsWith("jar")){
                urls.addAll(new JarFileArchive().getUrl(fileTemp.getPath()));
            }
        }

        return urls;

    }





}
