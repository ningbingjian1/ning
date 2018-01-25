package com.ning.yarn;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.net.NetUtils;
import org.apache.hadoop.yarn.api.ApplicationConstants;
import org.apache.hadoop.yarn.api.protocolrecords.RegisterApplicationMasterResponse;
import org.apache.hadoop.yarn.api.records.*;
import org.apache.hadoop.yarn.client.api.AMRMClient;
import org.apache.hadoop.yarn.client.api.async.AMRMClientAsync;
import org.apache.hadoop.yarn.client.api.async.NMClientAsync;
import org.apache.hadoop.yarn.client.api.async.impl.NMClientAsyncImpl;
import org.apache.hadoop.yarn.exceptions.YarnException;
import org.apache.hadoop.yarn.util.ConverterUtils;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by zhaoshufen
 * User:  zhaoshufen
 * Date: 2018/1/18
 * Time: 21:18
 * To change this setting on:Preferences->editor->File and Code Templates->Include->File Header
 */
public class ApplicationMaster {
    private static final Log LOG = LogFactory.getLog(ApplicationMaster.class);
    @SuppressWarnings("rawtypes")
    AMRMClientAsync amRMClient = null;
    NMClientAsyncImpl amNMClient = null;
    Configuration conf = new Configuration() ;
    Map<ContainerId, Container> runningContainers = new ConcurrentHashMap<ContainerId, Container>();
    AtomicInteger numCompletedConatiners = new AtomicInteger(0);
    ExecutorService exeService = Executors.newCachedThreadPool();
    AtomicInteger numTotalContainers = new AtomicInteger(2);

    public void run() throws Exception{
        logInformation();
        // 1. create amRMClient
        createAmRmClient();
        // 2. register with RM and this will heartbeating to RM
        registerAM();

        // 4. Request containers
        reqContainers();
    }

    void waitComplete() throws YarnException, IOException {
        while(numTotalContainers.get() != numCompletedConatiners.get()){
            try{
                Thread.sleep(1000);
                LOG.info("waitComplete" +
                        ", numTotalContainers=" + numTotalContainers.get() +
                        ", numCompletedConatiners=" + numCompletedConatiners.get());
            } catch (InterruptedException ex){}
        }
        LOG.info("ShutDown exeService Start");
        exeService.shutdown();
        LOG.info("ShutDown exeService Complete");
        amNMClient.stop();
        LOG.info("amNMClient  stop  Complete");
        amRMClient.unregisterApplicationMaster(FinalApplicationStatus.SUCCEEDED, "dummy Message", null);
        LOG.info("unregisterApplicationMaster  Complete");
        amRMClient.stop();
        LOG.info("amRMClient  stop Complete");
    }

    private void reqContainers() {
        for (int i = 0; i < numTotalContainers.get(); i++) {
            AMRMClient.ContainerRequest containerAsk = new AMRMClient.ContainerRequest(
                    //100*10M + 1vcpu
                    Resource.newInstance(100, 1), null, null,
                    Priority.newInstance(0));
            amRMClient.addContainerRequest(containerAsk);
        }
    }

    private void registerAM() throws org.apache.hadoop.yarn.exceptions.YarnException, IOException {
        RegisterApplicationMasterResponse response = amRMClient
                .registerApplicationMaster(NetUtils.getHostname(), -1, "");
        response.getContainersFromPreviousAttempts();
    }

    void logInformation(){
        System.out.println("This is System.out.println");
        System.err.println("This is System.err.println");

        String containerIdStr = System
                .getenv(ApplicationConstants.Environment.CONTAINER_ID.name());
        LOG.info("containerIdStr " + containerIdStr);
        ContainerId containerId = ConverterUtils.toContainerId(containerIdStr);

    }

    private void createAmRmClient() {
        amRMClient = AMRMClientAsync.createAMRMClientAsync(
                1000, new RMCallbackHandler());
        amRMClient.init(conf);
        amRMClient.start();
        // 2. Create nmClientAsync
        amNMClient = new NMClientAsyncImpl(new NMCallbackHandler());
        amNMClient.init(conf);
        amNMClient.start();
    }
    private class RMCallbackHandler implements AMRMClientAsync.CallbackHandler{

        @Override
        public void onContainersCompleted(List<ContainerStatus> statuses) {
            for (ContainerStatus status :statuses ){
                LOG.info("Container Completed: " + status.getContainerId().toString()
                        + " exitStatus="+ status.getExitStatus());
                if (status.getExitStatus() != 0) {
                    // restart
                }
                ContainerId id = status.getContainerId();
                runningContainers.remove(id);
                numCompletedConatiners.addAndGet(1);
            }

        }
        public void onContainersAllocated(List<Container> containers) {
            for (Container c : containers) {
                LOG.info("Container Allocated"
                        + ", id=" + c.getId()
                        + ", containerNode=" + c.getNodeId());
                exeService.submit(new LaunchContainerTask(c));
                runningContainers.put(c.getId(), c);
            }
        }

        public void onShutdownRequest() {
        }

        public void onNodesUpdated(List<NodeReport> updatedNodes) {

        }

        public float getProgress() {
            float progress = 0;
            return progress;
        }

        public void onError(Throwable e) {
            amRMClient.stop();
        }
    }
    private class NMCallbackHandler implements NMClientAsync.CallbackHandler {

        public void onContainerStarted(ContainerId containerId,
                                       Map<String, ByteBuffer> allServiceResponse) {
            LOG.info("Container Stared " + containerId.toString());

        }

        public void onContainerStatusReceived(ContainerId containerId,
                                              ContainerStatus containerStatus) {

        }

        public void onContainerStopped(ContainerId containerId) {
            // TODO Auto-generated method stub

        }

        public void onStartContainerError(ContainerId containerId, Throwable t) {
            // TODO Auto-generated method stub

        }

        public void onGetContainerStatusError(ContainerId containerId,
                                              Throwable t) {
            // TODO Auto-generated method stub

        }

        public void onStopContainerError(ContainerId containerId, Throwable t) {
            // TODO Auto-generated method stub

        }

    }
    private class LaunchContainerTask implements Runnable{
        private Container container ;
        public LaunchContainerTask(Container container){
            this.container = container ;
        }
        @Override
        public void run() {
            List<String> commands = new LinkedList<String>();
            //commands.add("echo "  +  new Random().nextInt() +" >> " +System.getProperty("java.io.tmpdir") + "/echo.txt");
            commands.add("sleep 20  >> " +System.getProperty("java.io.tmpdir") + "/echo.txt");
            ContainerLaunchContext ctx = ContainerLaunchContext.newInstance(
                    null, null, commands, null, null, null);
            amNMClient.startContainerAsync(container, ctx);
        }
    }

    public static void main(String[] args) throws Exception{
        ApplicationMaster am = new ApplicationMaster();
        am.run();
        am.waitComplete();
    }
}
