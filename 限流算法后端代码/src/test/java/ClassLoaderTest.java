import lombok.SneakyThrows;

import java.io.*;
import java.sql.SQLOutput;
import java.util.Arrays;

/**
 * @Author qxc
 * @Date 2023 2023/8/31 20:04
 * @Version 1.0
 * @PACKAGE PACKAGE_NAME
 */
public class ClassLoaderTest {

    public static void main(String[] args) throws Exception {
        Class clazz = Class.forName("java.lang.Object");
        final ClassLoader classLoader = clazz.getClassLoader();
        System.out.println(classLoader);


        Class clazz2 = Class.forName("com.qxc.databasecentral.pojo.Result");
        final ClassLoader classLoader2 = clazz2.getClassLoader();
        System.out.println(classLoader2);
        System.out.println(clazz2);
    }
}


class myClassLoader extends ClassLoader {
    myClassLoader() {
    }

    /**
     * 读取
     *
     * @param name
     * @return
     * @throws IOException
     */
    private byte[] loadClassData(String name) throws IOException {
        name = "target/classes/" + name.replace('.', File.separatorChar) + ".class";
        try (FileInputStream fileInputStream = new FileInputStream(name)) {
            return fileInputStream.readAllBytes();
        }
    }

    @SneakyThrows
    @Override
    protected Class<?> findClass(String name) {
        byte[] b = loadClassData(name);
        return defineClass(name, b, 0, b.length);
    }



    public static void main(String[] args) throws IOException, ClassNotFoundException {
        myClassLoader myClassLoader = new myClassLoader();
        final Class<?> aClass = myClassLoader.loadClass("com.qxc.T");
        System.out.println(aClass);
        System.out.println(aClass.getClassLoader());
    }
}

