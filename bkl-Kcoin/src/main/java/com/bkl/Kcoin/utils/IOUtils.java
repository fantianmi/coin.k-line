package com.bkl.Kcoin.utils;

import java.io.Closeable;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.nio.charset.Charset;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public final class IOUtils {
    public static final Charset UTF8_CHARSET = Charset.forName("UTF-8");
    public static final Charset ANSI_CHARSET = Charset.forName("ISO8859-1");
    
	public static void main(String[] args) throws Exception{
		String dir = "D:/eclipse/eclipse-jee-indigo-SR2-win32/workplace/.metadata/.plugins/org.eclipse.wst.server.core/tmp0/wtpwebapps/weiqiche/upload/files";
		createDirRecursive(dir);
	}
	

    private IOUtils() {
    }

    public static String path(String parent, String child) {
        File f = new File(parent, child);
        return f.getAbsolutePath();
    }

    public static void closeIO(Object obj) {
        if (obj == null) {
            return;
        }
        if (obj instanceof Closeable) {
            try {
                ((Closeable) obj).close();
            } catch (IOException e) {
            }
            return;
        }
        if (obj instanceof ResultSet) {
            try {
                ((ResultSet) obj).close();
            } catch (Exception e) {
            }
            return;
        }

        if (obj instanceof Statement) {
            try {
                ((Statement) obj).close();
            } catch (Exception e) {
            }
            return;
        }

        if (obj instanceof Connection) {
            try {
                ((Connection) obj).close();
            } catch (Exception e) {
            }
            return;
        }
        throw new IllegalArgumentException("unsupported close obj:" + obj.getClass());
    }

    
    
   
    public static void delete(String f) throws IOException {
        delete(new File(f));
    }

    public static void delete(File f) throws IOException {
        if (!f.exists()) {
            return;
        }
        if (f.isFile()) {
            if (!f.delete()) {
                throw new IOException("failed to delete the file:" + f.getAbsolutePath());
            }
            return;
        }
        if (f.isDirectory()) {
            File[] files = f.listFiles();
            for (File child : files) {
                delete(child);
            }
            if (!f.delete()) {
                throw new IOException("failed to delete the dir:" + f.getAbsolutePath());
            }
        }
    }

    public static void createFile(File f, boolean overwrite) throws IOException {
        String path = f.getAbsolutePath();
        if (f.exists()) {
            if (overwrite) {
                delete(f);
            } else if (f.isDirectory()) {
                throw new IOException("the named file exists:" + path);
            } else {
                return;
            }
        }
        File parent = f.getParentFile();
        parent.mkdirs();
        if (!f.createNewFile()) {
            throw new IOException("failed to create file:" + path);
        }
    }

    public static File createFile(String f, boolean overwrite) throws IOException {
        File file = new File(f);
        IOUtils.createFile(file, true);
        return file;
    }

    public static File createDir(String dir, boolean overwrite) throws IOException {
        File fdir = new File(dir);
        createDir(fdir, overwrite);
        return fdir;
    }

    public static void createDir(File dir, boolean overwrite) throws IOException {
        String path = dir.getAbsolutePath();
        if (dir.exists()) {
            if (overwrite) {
                delete(dir);
            } else if (dir.isFile()) {
                throw new IOException("the named file exists:" + path);
            } else {
                return;
            }
        }
        dir.mkdirs();

    }
    public static void createDirRecursive(File dir) throws IOException {
    	if (dir == null || dir.exists()) {
    		return;
    	}
    	
    	File parentFile = dir.getParentFile();
    	if (parentFile != null && !parentFile.isDirectory()) {
    		createDirRecursive(parentFile);
    	}
    	dir.mkdir();
    }
  

    public static void createDirRecursive(String dir) throws IOException {
    	createDirRecursive(new File(dir));
    }
   
    
    public static List<File> listFile(File f,boolean recursion)throws IOException{
        List<File > flist = new ArrayList<File>();
        if(!f.isDirectory()){
            return flist;
        }

        File[] files = f.listFiles();
        for(File file :files){
            if(file.isFile()){
                flist.add(file);
            }else if(file.isDirectory()&& recursion){
                flist.addAll(listFile(file, true));
            }
        }
        return flist;
    }
    
    public static void echo(File f, String str, String charset, boolean sync) throws IOException {
        createFile(f, false);

        FileOutputStream out = null;
        try {
            out = new FileOutputStream(f);
            OutputStreamWriter outW = (charset != null ? new OutputStreamWriter(out, charset) : new OutputStreamWriter(
                    out));
            PrintWriter w = new PrintWriter(outW);
            w.write(str);
            w.flush();
            if (sync) {
                out.getFD().sync();
            }
        } finally {
            IOUtils.closeIO(out);
        }
    }
    
    public static String getPrefixFileName(String fileName) {
    	if (fileName == null) {
    		return null;
    	}
    	
    	int dotIndex = fileName.lastIndexOf(".");
    	if (dotIndex == -1) {
    		return fileName;
    	}
    	String prefix = fileName.substring(0, dotIndex);
    	return prefix;
    	
    }
    
    public static String getSuffixFileName(String fileName) {
    	if (fileName == null) {
    		return null;
    	}
    	
    	int dotIndex = fileName.lastIndexOf(".");
    	if (dotIndex == -1) {
    		return "";
    	}
    	String suffix = fileName.substring(dotIndex+1);
    	return suffix;
    }
}
