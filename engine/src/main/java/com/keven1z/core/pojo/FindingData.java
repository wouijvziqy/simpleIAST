package com.keven1z.core.pojo;

import com.keven1z.core.model.graph.TaintData;

import java.util.LinkedList;

public class FindingData {
    private LinkedList<TaintData> flowData;
    private String vulnerableType;
    public FindingData(){

    }

    public LinkedList<TaintData> getFlowData() {
        return flowData;
    }

    public void setFlowData(LinkedList<TaintData> flowData) {
        this.flowData = flowData;
    }

    public String getVulnerableType() {
        return vulnerableType;
    }

    public void setVulnerableType(String vulnerableType) {
        this.vulnerableType = vulnerableType;
    }
}
