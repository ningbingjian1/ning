/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.ning.util;

import com.ning.functions.ZeroOperator;
import org.apache.hadoop.fs.FileSystem;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.PriorityQueue;
import java.util.Set;


public class NingShutdownHookManager {
    static int DEFAULT_SHUTDOWN_PRIORITY = 100;

    /**
     * The shutdown priority of the SparkContext instance. This is lower than the default
     * priority, so that by default hooks are run before the context is shut down.
     */
    static int SPARK_CONTEXT_SHUTDOWN_PRIORITY = 50;

    /**
     * The shutdown priority of temp directory must be lower than the SparkContext shutdown
     * priority. Otherwise cleaning the temp directories while Spark jobs are running can
     * throw undesirable errors at the time of shutdown.
     */
    static int TEMP_DIR_SHUTDOWN_PRIORITY = 25;


    static NingShutdownHookManager shutdownHooks = null;
    private static Set<String> shutdownDeletePaths = new HashSet<>();


    static {
        shutdownHooks = new NingShutdownHookManager();
        shutdownHooks.install();
        // Add a shutdown hook to delete the temp dirs when the JVM exits
        addShutdownHook(TEMP_DIR_SHUTDOWN_PRIORITY, () ->{
            shutdownDeletePaths.forEach(dirPath ->{
                try {
                    Utils.INSTANCE.deleteRecursively(new File(dirPath));
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
        });
    }
/*    () -> {
        System.out.println("Shutdown hook called");
        // we need to materialize the paths to delete because deleteRecursively removes items from
        // shutdownDeletePaths as we are traversing through it.
        shutdownDeletePaths.forEach( dirPath -> {
            try {
                System.out.println("Deleting directory " + dirPath);
                //Utils.deleteRecursively(new File(dirPath))
            } catch( Exception e) {
                System.out.println("Exception while deleting Spark temp dir: $dirPath" +  e.getMessage());
            }
        });
    }*/




    // Register the path to be deleted via shutdown hook
    public static boolean registerShutdownDeleteDir(File file) {
        String absolutePath = file.getAbsolutePath();
        synchronized(shutdownDeletePaths){
            return shutdownDeletePaths.add( absolutePath);
        }
    }

    // Remove the path to be deleted via shutdown hook
    public static boolean removeShutdownDeleteDir(File file ) {
        String absolutePath = file.getAbsolutePath();
        synchronized(shutdownDeletePaths) {
            return shutdownDeletePaths.remove(absolutePath);
        }
    }

    // Is the path already registered to be deleted via a shutdown hook ?
    public static boolean  hasShutdownDeleteDir(File file) {
        String absolutePath = file.getAbsolutePath();
        synchronized(shutdownDeletePaths) {
            return shutdownDeletePaths.contains(absolutePath);
        }
    }


    // Note: if file is child of some registered path, while not equal to it, then return true;
    // else false. This is to ensure that two shutdown hooks do not try to delete each others
    // paths - resulting in IOException and incomplete cleanup.
    public static boolean hasRootAsShutdownDeleteDir(File file) {
        String absolutePath = file.getAbsolutePath();
        boolean retval = false;
        synchronized(shutdownDeletePaths) {
            for(String path : shutdownDeletePaths){
                retval = !absolutePath.equals(path) && absolutePath.startsWith(path);
            }
        }
        if (retval) {
            System.out.println("path = " + file + ", already present as root for deletion.");
        }
        return retval;
    }

    /**
     * Detect whether this thread might be executing a shutdown hook. Will always return true if
     * the current thread is a running a shutdown hook but may spuriously return true otherwise (e.g.
     * if System.exit was just called by a concurrent thread).
     *
     * Currently, this detects whether the JVM is shutting down by Runtime#addShutdownHook throwing
     * an IllegalStateException.
     */
    public static boolean inShutdown() {
        try {
            Thread hook = new Thread(){
                public void run(){
                }
            };
            Runtime.getRuntime().addShutdownHook(hook);
            Runtime.getRuntime().removeShutdownHook(hook);
        } catch(IllegalStateException ise) {
            return true ;
        }
        return false;
    }


    /**
     * Adds a shutdown hook with default priority.
     *
     * @param hook The code to run during shutdown.
     * @return A handle that can be used to unregister the shutdown hook.
     */
    public static Object addShutdownHook(ZeroOperator hook) {
        return addShutdownHook(DEFAULT_SHUTDOWN_PRIORITY,hook);
    }


    /**
     * Adds a shutdown hook with the given priority. Hooks with lower priority values run
     * first.
     *
     * @param hook The code to run during shutdown.
     * @return A handle that can be used to unregister the shutdown hook.
     */
    public static Object addShutdownHook(int priority,ZeroOperator hook) {
        return shutdownHooks.add(priority, hook);
    }

    /**
     * Remove a previously installed shutdown hook.
     *
     * @param ref A handle returned by `addShutdownHook`.
     * @return Whether the hook was removed.
     */
    public static boolean removeShutdownHook(Object ref) {
        return shutdownHooks.remove(ref);
    }


    private  volatile boolean shuttingDown = false;
    private PriorityQueue<NingShutdownHook> hooks = new PriorityQueue<>();

    /**
     * Install a hook to run at shutdown and run all registered hooks in order. Hadoop 1.x does not
     * have `ShutdownHookManager`, so in that case we just use the JVM's `Runtime` object and hope for
     * the best.
     */
    public void install(){
        Runnable hookTask = () ->  runAll();
        Try<Class> try1 =  Try.apply(
                () -> Utils.INSTANCE.classForName("org.apache.hadoop.util.ShutdownHookManager")
        );
        if(try1 instanceof Try.Success) {
            Try.Success<Class> success = (Try.Success<Class>)try1;
            try {
                int fsPriority = (Integer) FileSystem.class.getField("SHUTDOWN_HOOK_PRIORITY")
                        .get(null);
                Object shm = success.get().getMethod("get").invoke(null);
                shm.getClass().getMethod("addShutdownHook",Runnable.class, int.class)
                .invoke(shm, hookTask, Integer.valueOf(fsPriority + 30));

            } catch (Exception e) {
                e.printStackTrace();
            }

        }else if(try1 instanceof Try.Failure) {
            Runtime.getRuntime().addShutdownHook(new Thread(hookTask, "Spark Shutdown Hook"));
        }else{
            throw new RuntimeException("exception in find a shutdownHook ");
        }
    }
    public void runAll() {
        shuttingDown = true;
        NingShutdownHook nextHook = null;
        while (true) {
             synchronized(hooks) {
                 nextHook = hooks.poll() ;
            }
            if(nextHook != null) {
                nextHook.run();
            }else{
                 break;
            }

        }
    }
    public Object add(int priority, ZeroOperator operator) {
        synchronized(hooks) {
            if (shuttingDown) {
                throw new IllegalStateException("Shutdown hooks cannot be modified during shutdown.");
            }
            NingShutdownHook hookRef = new NingShutdownHook(priority, operator);
            hooks.add(hookRef);
            return hookRef;
        }
    }

    public boolean remove(Object obj ){
        synchronized(hooks) {
            return hooks.remove(obj) ;
        }
    }
}
class NingShutdownHook implements Comparable<NingShutdownHook> {
    private int priority;
    private ZeroOperator hook;
    public NingShutdownHook(int priority,ZeroOperator hook){
        this.hook = hook;
    }
    public  int compareTo(NingShutdownHook other) {
        return other.priority - priority ;
    }

    public void run(){
        try {
            hook.op();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}

