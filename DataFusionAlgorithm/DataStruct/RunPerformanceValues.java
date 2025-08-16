package com.algorithm.DataStruct;

public class RunPerformanceValues {
    private String name ;
    private Double performanceValues;

    public RunPerformanceValues() {
    }

    public RunPerformanceValues(String name) {
        this.name = name;
    }

    public RunPerformanceValues(String name, Double performanceValues) {
        this.name = name;
        this.performanceValues = performanceValues;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getPerformanceValues() {
        return performanceValues;
    }

    public void setPerformanceValues(Double performanceValues) {
        this.performanceValues = performanceValues;
    }
}
