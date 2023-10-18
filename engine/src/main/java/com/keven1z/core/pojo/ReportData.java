package com.keven1z.core.pojo;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class ReportData {
    private String agentId;
    private HttpRequestData requestData;
    private HttpResponseData responseData;
    private String timestamp;
    private List<FindingData> findingDataList;
    public ReportData() {
        // 获取当前时间
        LocalDateTime now = LocalDateTime.now(ZoneId.of("Asia/Shanghai"));
        // 定义时间格式
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        // 将当前时间格式化为字符串
        this.timestamp = now.format(formatter);
    }

    public String getAgentId() {
        return agentId;
    }

    public void setAgentId(String agentId) {
        this.agentId = agentId;
    }

    public HttpRequestData getRequestData() {
        return requestData;
    }

    public void setRequestData(HttpRequestData requestData) {
        this.requestData = requestData;
    }

    public HttpResponseData getResponseData() {
        return responseData;
    }

    public void setResponseData(HttpResponseData responseData) {
        this.responseData = responseData;
    }

    public List<FindingData> getFindingDataList() {
        return findingDataList;
    }

    public void setFindingDataList(List<FindingData> findingDataList) {
        this.findingDataList = findingDataList;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
}
