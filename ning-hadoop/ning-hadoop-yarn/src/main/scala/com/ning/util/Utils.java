package com.ning.util;

import com.google.common.collect.Lists;
import org.apache.commons.lang.SystemUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.Callable;

/**
 * Created by zhaoshufen
 * User:  zhaoshufen
 * Date: 2018/1/19
 * Time: 20:59
 * To change this setting on:Preferences->editor->File and Code Templates->Include->File Header
 */
public class Utils {
    private Utils(){
    }
    public static Utils INSTANCE = new Utils();

    /**
     * Whether the underlying operating system is Windows.
     */
    public static boolean isWindows = SystemUtils.IS_OS_WINDOWS;

    /**
     * Whether the underlying operating system is Mac OS X.
     */
    public static boolean isMac = SystemUtils.IS_OS_MAC_OSX;


    public File createDirectory(String root, String namePrefix) throws IOException{
        int attempts = 0 ;
        int maxAttempts = 10 ;
        File dir = null ;
        while (dir == null){
            attempts += 1;
            if(attempts > maxAttempts){
                throw new IOException("Failed to create a temp directory (under " + root + ") after " +
                        maxAttempts + " attempts!");
            }
            try{
                dir = new File(root,namePrefix + "-" + UUID.randomUUID().toString());
                if(dir.exists() || !dir.mkdirs()){
                    dir = null ;
                }
            }catch (SecurityException ex){
                dir = null ;
            }

        }
        return dir.getCanonicalFile();

    }
    public  File createTempDir(String root, String namePrefix) throws IOException{
        File dir = createDirectory(root, namePrefix);
        NingShutdownHookManager.registerShutdownDeleteDir(dir);
        return dir ;
    }

    // scalastyle:off classforname
    /** Preferred alternative to Class.forName(className) */
    public Class<?> classForName(String className) {
        try {
            return Class.forName(className, true, getContextOrTheClassLoader());
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Get the Context ClassLoader on this thread or, if not present, the ClassLoader that
     * loaded Spark.
     *
     * This should be used whenever passing a ClassLoader to Class.ForName or finding the currently
     * active loader when setting up ClassLoader delegation chains.
     */
    public  ClassLoader getContextOrTheClassLoader(){
        return Optional.of(Thread.currentThread().getContextClassLoader())
                .orElse(getTheClassLoader());
    }

    /**
     * Get the ClassLoader which loaded Spark.
     */
    public ClassLoader getTheClassLoader (){
        return getClass().getClassLoader();
    }


    /**
     * Execute the given block, logging and re-throwing any uncaught exception.
     * This is particularly useful for wrapping code that runs in a thread, to ensure
     * that exceptions are printed, and to avoid having to catch Throwable.
     */
    public <T> T logUncaughtExceptions(Callable<T> f){
        try {
            return f.call();
        } catch(Throwable ct) {
            System.out.println("Uncaught exception in thread ${Thread.currentThread().getName}" + ct.getMessage());
            throw new RuntimeException(ct);
        }
    }

    /**
     * Delete a file or directory and its contents recursively.
     * Don't follow directories if they are symlinks.
     * Throws an exception if deletion is unsuccessful.
     */
    public void deleteRecursively(File file) throws IOException {
        if (file != null) {
            try {
                if (file.isDirectory() && !isSymlink(file)) {
                    IOException savedIOException = null;
                    Collection<File> files =  listFilesSafely(file);
                    for (File child : files) {
                        try {
                            deleteRecursively(child);
                        } catch (IOException ioe){
                            // In case of multiple exceptions, only last one will be thrown
                            savedIOException = ioe;
                        }
                    }
                    if (savedIOException != null) {
                        throw savedIOException;
                    }
                    NingShutdownHookManager.removeShutdownDeleteDir(file);
                }
            } finally{
                if (!file.delete()) {
                    // Delete can also fail if the file simply did not exist
                    if (file.exists()) {
                        throw new IOException("Failed to delete: " + file.getAbsolutePath());
                    }
                }
            }
        }
    }
    /**
     * Check to see if file is a symbolic link.
     */
    public boolean isSymlink(File file) throws IOException {
        if (file == null) throw new NullPointerException("File must not be null");
        if (isWindows) {
            return false ;
        }
        File fileInCanonicalDir = null;
        if (file.getParent() == null) {
            fileInCanonicalDir = file;
        } else {
            fileInCanonicalDir = new File(file.getParentFile().getCanonicalFile(), file.getName());
        }

        return !fileInCanonicalDir.getCanonicalFile().equals(fileInCanonicalDir.getAbsoluteFile());
    }

    private Collection<File> listFilesSafely(File file) throws IOException{
        if (file.exists()) {
            File[] files = file.listFiles();
            if (files == null) {
                throw new IOException("Failed to list files for dir: " + file);
            }
            return Lists.newArrayList(files);
        } else {
            return new ArrayList<>();
        }
    }
    public static void main(String[] args) throws Exception{
        //Utils.INSTANCE.createTempDir("/tmp/ningbingjian","spark");
        System.out.println(System.getProperty("java.io.tmpdir"));
    }
}
