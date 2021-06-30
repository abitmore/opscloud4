package com.baiyi.caesar.datasource.kubernetes.handler;

import com.baiyi.caesar.common.datasource.config.DsKubernetesConfig;
import com.baiyi.caesar.datasource.kubernetes.client.KubeClient;
import io.fabric8.kubernetes.api.model.Pod;
import io.fabric8.kubernetes.api.model.PodList;
import io.fabric8.kubernetes.client.dsl.ExecListener;
import io.fabric8.kubernetes.client.dsl.ExecWatch;
import io.fabric8.kubernetes.client.dsl.LogWatch;
import lombok.Data;
import okhttp3.Response;
import org.springframework.util.CollectionUtils;

import java.io.OutputStream;
import java.util.Collections;
import java.util.List;

/**
 * @Author baiyi
 * @Date 2021/6/24 11:16 下午
 * @Version 1.0
 */
public class KubernetesPodHandler {

    public static List<Pod> listPod(DsKubernetesConfig.Kubernetes kubernetes) {
        PodList podList = KubeClient.build(kubernetes)
                .pods()
                .list();
        return podList.getItems();
    }

    public static List<Pod> listPod(DsKubernetesConfig.Kubernetes kubernetes, String namespace) {
        PodList podList = KubeClient.build(kubernetes)
                .pods()
                .inNamespace(namespace)
                .list();
        if (CollectionUtils.isEmpty(podList.getItems()))
            return Collections.emptyList();
        return podList.getItems();
    }

    /**
     * @param kubernetes
     * @param namespace
     * @param name       podName
     * @return
     */
    public static Pod getPod(DsKubernetesConfig.Kubernetes kubernetes, String namespace, String name) {
        return KubeClient.build(kubernetes)
                .pods()
                .inNamespace(namespace)
                .withName(name)
                .get();
    }

    public static LogWatch getPodLogWatch(DsKubernetesConfig.Kubernetes kubernetes, String namespace, String podName) {
        return KubeClient.build(kubernetes).pods()
                .inNamespace(namespace)
                .withName(podName)
                .watchLog();
    }

    public static LogWatch getPodLogWatch(DsKubernetesConfig.Kubernetes kubernetes, String namespace, String podName, String containerName) {
        return KubeClient.build(kubernetes).pods()
                .inNamespace(namespace)
                .withName(podName)
                .inContainer(containerName)
                .watchLog();
    }

    /**
     * @param kubernetes
     * @param namespace
     * @return
     */
    public static ExecWatch loginPodContainer(DsKubernetesConfig.Kubernetes kubernetes,
                                              String namespace,
                                              String podName,
                                              String containerName,
                                              SimpleListener listener,
                                              OutputStream out
    ) {
        return KubeClient.build(kubernetes).pods()
                .inNamespace(namespace)
                .withName(podName)
                .inContainer(containerName) // 如果Pod中只有一个容器，不需要指定
                .redirectingInput()
                //.redirectingOutput()
                //.redirectingError()
                //.redirectingErrorChannel()
                //.readingInput(in)
                .writingOutput(out)
                .writingError(System.err)
                .withTTY()
                .usingListener(listener)
                .exec("sh");
    }

    @Data
    public static class SimpleListener implements ExecListener {

        private boolean isClosed = false;

        @Override
        public void onOpen(Response response) {
        }

        @Override
        public void onFailure(Throwable t, Response response) {
            this.isClosed = true;
            // throw new SshRuntimeException("Kubernetes Container Failure!");
        }

        @Override
        public void onClose(int code, String reason) {
            this.isClosed = true;
            // throw new SshRuntimeException("Kubernetes Container Close!");
        }
    }
}
