package priv.timothy.boot.loader;

import sun.net.www.protocol.file.FileURLConnection;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.JarURLConnection;
import java.net.URL;
import java.net.URLClassLoader;
import java.net.URLConnection;
import java.security.AccessController;
import java.security.PrivilegedExceptionAction;
import java.util.jar.JarFile;

/**
 * URLClassLoader原理讲解
 * 支持从文件夹和jar包中加载文件，加载器实现类两个： new URLClassPath.FileLoader(basedir)   new URLClassPath.JarLoader(basedir)
 * 当第一次loadClass的时候会遍历URLClassPath来创建加载器，如果path结尾是“/”就创建FileLoader，否则创建JarLoader
 *
 * URLClassLoader的成员变量URLClassPath用来存储 文件夹和JAR的URL路径，表示从哪加载文档；
 *
 * URLClassLoader重写了findClass()的方法，当loadClass(targetFilePath)时，会遍历所有的加载器，去加载这个文件，获取二进制流;
 *
 *
 * 文件加载器加载文件过程：
 *  1、  URLClassPath.FileLoader，它的实现就是把加载器的 basedir 和 targetFilePath 结合起来
 *           target=new File(this.basedir, targetFilePath.replace('/', File.separatorChar))
 *            如果target.exist()==true 表示资源存在，二进制流就是new FileInputStream(target)
 *
 *  2、 URLClassPath.JarLoader的实现的也类似，不过它是从jar中获取二进制流，实现逻辑大概如下
 *
 *
 *
 * JarURLConnection 表示到 Java Archive (JAR) 文件或 JAR 文件中条目的 URL 连接。
 * JAR URL语法是 jar:<url>!/{entry}
 * jar:http://www.foo.com/bar/baz.jar!/COM/foo/Quux.class 此URL指jar文件中的条目
 * jar:http://www.foo.com/bar/baz.jar!/此URL指的是整个 JAR 文件
 * <p>
 * 获取jar包内的资源需要解压缩获取条目文件,JDK已经封装了一套API如下
 * <p>
 * <p>
 * URL url = new URL("jar:file:/D:/workspace/rabbit/rabbit-1.0.jar!/");
 * <p>
 * JarURLConnection jarConnection = (JarURLConnection)url.openConnection();
 * JarFile jarFile = jarConnection.getJarFile();
 * InputStream entryInputStream=jarFile.getInputStream(jarFile.getEntry("priv/timothy/App.class"))//获取jar中priv.timothy.app.class文件流
 */
public class LaunchedURLClassLoader extends URLClassLoader {

    public LaunchedURLClassLoader(URL[] urls, ClassLoader parent) {
        super(urls, parent);
    }

//    自己实现也行，不过URLClassLoader已经有更好的实现了；我们只需要把资源路径，转变成URL传给URLClassLoader就行，具体获取二进制流它内部有实现
//    @Override
//    protected Class<?> findClass(String name) throws ClassNotFoundException {
//        String classEntryName = name.replace('.', '/') + ".class";
//        for (URL url : getURLs()) {
//            try {
//                URLConnection connection = url.openConnection();
//                // openConnection url的协议是jar，但是不符合jar规范会报错；比如jar:file:/D:/workspace/rabbit/slf4j-api-1.7.25.jar
//                // 会报错 no !/ found in url spec:file:/D:/workspace/rabbit/slf4j-api-1.7.25.jar
//                if (connection instanceof JarURLConnection) {
//                    JarFile jarFile = ((JarURLConnection) connection).getJarFile();
//                    if (jarFile.getEntry(classEntryName) != null && jarFile.getManifest() != null) {
//                        InputStream in = jarFile.getInputStream(jarFile.getEntry(classEntryName));
//
//                        byte[] bytes = toBytes(in);
//
//                        return defineClass(name, bytes, 0, bytes.length);
//
//                    }
//                } else if (connection instanceof FileURLConnection) {
//
//                }
//            } catch (IOException ex) {
//                // Ignore
//            }
//        }
//
//        throw new ClassNotFoundException(name);
//    }


//    private byte[] toBytes(InputStream in) throws IOException {
//        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
//        byte[] buf = new byte[1024 * 8];
//        int len;
//        while ((len = in.read(buf)) != -1) {
//            outputStream.write(buf, 0, len);
//        }
//        return outputStream.toByteArray();
//    }

}
