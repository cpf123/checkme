package 类加载;

import java.io.*;
import java.lang.reflect.Method;
import java.util.HashSet;

//https://valleylord.github.io/post/201409-jvm-classloader/
/*
 *  实现热替换，自定义ClassLoader，加载的是.class
 */
class HowswapCL extends ClassLoader {
    private String basedir; // 需要该类加载器直接加载的类文件的基目录
    private HashSet<String> dynaclazns; // 需要由该类加载器直接加载的类名

    public HowswapCL(String basedir, String[] clazns) {
        super(null); // 指定父类加载器为 null
        this.basedir = basedir;
        dynaclazns = new HashSet<String>();
        loadClassByMe(clazns);
    }

    private void loadClassByMe(String[] clazns) {
        for (int i = 0; i < clazns.length; i++) {
            loadDirectly(clazns[i]);
            dynaclazns.add(clazns[i]);
        }
    }

    private Class<?> loadDirectly(String name) {
        Class<?> cls = null;
        StringBuffer sb = new StringBuffer(basedir);
        String classname = name.replace('.', File.separatorChar) + ".class";
        sb.append(File.separator + classname);
        File classF = new File(sb.toString());
        try {
            cls = instantiateClass(name, new FileInputStream(classF), classF.length());
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return cls;
    }

    private Class<?> instantiateClass(String name, InputStream fin, long len) {
        byte[] raw = new byte[(int) len];
        try {
            fin.read(raw);
            fin.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return defineClass(name, raw, 0, raw.length);
    }

    /**
     * 热加载简单来说就是在程序运行时可以重新加载之前经过编译转换后的类，Java并不支持热加载，
     * 因为我们编写的代码文件，也就是.java文件在加载前首先被Java编译器编译成.class文件，
     * 当程序执行需要使用到这个类时，会将它的.class文件加载到JVM里，通过类加载器读取这些.class文件后，
     * 转化成类实例，即可以生成类的对象。对于同一个类来说，JVM只会把它加载一次，加载完成后也不能把它删除掉，
     * 如果我们想要在运行过程中删掉这个类，替换一个新版本的class类，类加载器显然无法帮我们实现，
     * 但是，想要实现类热加载，可以重写ClassLoader，在里面写我们自己的替换逻辑。
     *
     * @param name
     * @param
     * @return
     * @throws ClassNotFoundException
     */

//    方法1
 /*   @Override
    protected Class<?> loadClass(String name, boolean resolve) throws ClassNotFoundException {
        Class<?> cls = null;
        cls = findLoadedClass(name);
        if (!this.dynaclazns.contains(name) && cls == null) {
            cls = getSystemClassLoader().loadClass(name);
        }
        if (cls == null) {
            throw new ClassNotFoundException(name);
        }
        if (resolve) {
            resolveClass(cls);
        }
        return cls;
    }*/

    // 方法2
    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        byte[] data = this.loadClassData(name);
        return this.defineClass(name, data, 0, data.length);
    }

    private byte[] loadClassData(String name) {
        try {
            // 传进来是带包名的
            name = name.replace(".", "//");
            FileInputStream inputStream = new FileInputStream(new File(basedir + name + ".class")); // 传进指定class
            // 定义字节数组输出流
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            int b = 0;
            while ((b = inputStream.read()) != -1) {
                baos.write(b);
            }
            inputStream.close();
            return baos.toByteArray();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


}

/*
 * 每隔2s运行一次，不断加载class
 */

class Multirun implements Runnable {
    @Override
    public void run() {
        try {
            while (true) {
                // 每次都创建出一个新的类加载器
                // class需要放在自己package名字的文件夹下
                String url = "/Users/Bloomberg/IdeaProjects/DataCheck/target/scala-2.11/classes"; //System.getProperty("user.dir") + "/bin";// "/bin/problem1/GetInfo.class";
                HowswapCL cl = new HowswapCL(url, new String[]{"类加载.GetInfo"});
                Class<?> cls = cl.loadClass("类加载.GetInfo");
                Object foo = cls.newInstance();
                // 被调用函数的参数
                Method m = foo.getClass().getMethod("Output", new Class[]{});
                m.invoke(foo, new Object[]{});
                Thread.sleep(2000);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}

