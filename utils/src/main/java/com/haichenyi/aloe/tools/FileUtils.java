package com.haichenyi.aloe.tools;

import android.content.Context;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

/**
 * @Title: FileUtils
 * @Description: 文件工具类
 * Environment.getDataDirectory():     /data
 * Environment.getRootDirectory():    /system
 * Environment.getDownloadCacheDirectory():     /cache
 * Environment.getExternalStorageDirectory():   /storage/emulated/0
 * @Author: wz
 * @Date: 2018/5/23
 * @Version: V1.0
 */
public class FileUtils {
    /**
     * 在path目录下面创建名称为name的文件夹.
     *
     * @param path 目录
     * @param name 名称
     * @return File
     */
    @SuppressWarnings("all")
    public static File createFileDirs(String path, String name) {
        return createFileDirs(path + "/" + name);
    }

    @SuppressWarnings("all")
    public static File createFileDirs(String dirPath) {
        File file = new File(dirPath);
        if (!file.exists()) {
            file.mkdirs();
        }
        return file;
    }

    /**
     * 在path目录下面新建一个名称为name的文件,存在就会删除.
     *
     * @param path 目录
     * @param name 名称（包括文件后缀）
     * @return File
     */
    @SuppressWarnings("all")
    public static File createNewFile(String path, String name) throws IOException {
        File file = new File(path + "/" + name);
        if (file.exists()) {
            file.delete();
        }
        file.createNewFile();
        return file;
    }

    /**
     * 删除文件夹下的所有内容.
     *
     * @param path 文件夹路径
     */
    public static void deleteDir(String path) {
        File dir = new File(path);
        deleteDirFile(dir);
    }

    @SuppressWarnings("all")
    private static void deleteDirFile(File dir) {
        if (dir == null || !dir.exists() || !dir.isDirectory())
            return;
        for (File file : dir.listFiles()) {
            if (file.isFile()) {
                file.delete(); // 删除所有文件
            } else if (file.isDirectory())
                deleteDirFile(file); // 递规的方式删除文件夹
        }
        dir.delete();// 删除目录本身
    }

    /**
     * 读文件（任意路径）.
     *
     * @param filePath 文件路径
     * @return String
     */
    public static String readFile(String filePath) {
        BufferedReader br = null;
        StringBuilder sb = new StringBuilder();
        try {
            br = new BufferedReader(new FileReader(new File(filePath)));
            String result = "";
            while ((result = br.readLine()) != null) {
                sb.append(result);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return sb.toString();
    }

    /**
     * 覆盖方式写文件（任意路径）.
     *
     * @param filePath 文件路径
     * @param msg      需要写的内容
     */
    public static void writeFile(String filePath, String msg) {
        FileOutputStream outStream;
        File file = new File(filePath);
        try {
            outStream = new FileOutputStream(file);
        } catch (FileNotFoundException e) {
            return;
        }
        try {
            outStream.write(msg.getBytes());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                outStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 以追加的方式写文件（任意路径）.
     *
     * @param filePath 文件路径
     * @param msg      内容
     */
    public static void writeFileAppend(String filePath, String msg) {
        BufferedWriter bw = null;
        try {
            bw = new BufferedWriter(new OutputStreamWriter(
                    new FileOutputStream(new File(filePath), true)));
            bw.write(msg);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (bw != null) {
                    bw.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 写文件，openFileOutput方式（目录限制如下）.
     * 写的文件位置：data/data/app的包名/files/自命名文件，不需要申请内存读写权限
     *
     * @param context  context
     * @param fileName 文件名称
     * @param msg      需要写的内容
     * @param type     写文件的方式：覆盖：Context.MODE_PRIVATE。追加：Context.MODE_APPEND
     */
    public static void writeFile(Context context, String fileName, String msg, int type) {
        FileOutputStream out = null;
        BufferedWriter writer = null;
        try {
            out = context.openFileOutput(fileName, type);
            writer = new BufferedWriter(new OutputStreamWriter(out));
            writer.write(msg);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (writer != null) {
                    writer.close();
                }
                if (out != null) {
                    out.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 读文件，openFileInput方式（目录限制如下）.
     * 读的文件位置：data/data/app的包名/files/自命名文件，不需要申请内存读写权限
     *
     * @param context  context
     * @param fileName 文件名称
     * @return String
     */
    private String readFile(Context context, String fileName) {
        FileInputStream in = null;
        BufferedReader br = null;
        StringBuilder sb = new StringBuilder();
        try {
            in = context.openFileInput(fileName);
            br = new BufferedReader(new InputStreamReader(in));
            String line = "";
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return sb.toString();
    }
}
