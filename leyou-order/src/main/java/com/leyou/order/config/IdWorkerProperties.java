package com.leyou.order.config;

import com.leyou.common.util.IdWorker;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author zhang
 * @date 2019年04月29日 10:41
 */
@ConfigurationProperties(prefix = "leyou.worker")
public class IdWorkerProperties {
    private Long workerId;
    private Long datacenterId;

    public Long getWorkerId() {
        return workerId;
    }

    public void setWorkerId(Long workerId) {
        this.workerId = workerId;
    }

    public Long getDatacenterId() {
        return datacenterId;
    }

    public void setDatacenterId(Long datacenterId) {
        this.datacenterId = datacenterId;
    }
}
