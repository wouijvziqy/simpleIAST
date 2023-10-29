package com.keven1z.core.monitor;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.keven1z.core.vulnerability.DetectAndReportHandler;
import com.keven1z.core.vulnerability.report.ReportMessage;

import static com.keven1z.core.hook.HookThreadLocal.REPORT_QUEUE;

public class ReportMonitor extends Monitor {


    @Override
    public String getThreadName() {
        return "SimpleIAST-Report-Thread";
    }

    @Override
    public boolean isForServer() {
        return false;
    }

    @Override
    public void doRun() throws InterruptedException, JsonProcessingException {
        ReportMessage taintMessage = null;
        try {
            taintMessage = REPORT_QUEUE.take();
            DetectAndReportHandler.doHandle(taintMessage);
            //降低漏洞处理频率，减少cpu 持续消耗
            Thread.sleep(1000);
        } finally {
            if (taintMessage != null) {
                taintMessage.clear();
            }
        }
    }
}
