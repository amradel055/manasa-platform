/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.dto;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author ahmed
 */
public class ChartDTO {

    private List<ChartDataDTO> chartDataDTOList;
    private String legendPosition;
    private String title;
    private String xLabel;
    private String yLabel;
    private Object min;
    private Object max;
    private List<Number> pieValues;
    private List<String> pieLabels;
    private List<String> pieColers;

    /**
     * @return the chartDataDTOList
     */
    public List<ChartDataDTO> getChartDataDTOList() {
        return chartDataDTOList;
    }

    /**
     * @param chartDataDTOList the chartDataDTOList to set
     */
    public void setChartDataDTOList(List<ChartDataDTO> chartDataDTOList) {
        this.chartDataDTOList = chartDataDTOList;
    }

    /**
     * @return the legendPosition
     */
    public String getLegendPosition() {
        return legendPosition;
    }

    /**
     * @param legendPosition the legendPosition to set
     */
    public void setLegendPosition(String legendPosition) {
        this.legendPosition = legendPosition;
    }

    /**
     * @return the title
     */
    public String getTitle() {
        return title;
    }

    /**
     * @param title the title to set
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * @return the xLabel
     */
    public String getxLabel() {
        return xLabel;
    }

    /**
     * @param xLabel the xLabel to set
     */
    public void setxLabel(String xLabel) {
        this.xLabel = xLabel;
    }

    /**
     * @return the yLabel
     */
    public String getyLabel() {
        return yLabel;
    }

    /**
     * @param yLabel the yLabel to set
     */
    public void setyLabel(String yLabel) {
        this.yLabel = yLabel;
    }

    /**
     * @return the min
     */
    public Object getMin() {
        return min;
    }

    /**
     * @param min the min to set
     */
    public void setMin(Object min) {
        this.min = min;
    }

    /**
     * @return the max
     */
    public Object getMax() {
        return max;
    }

    /**
     * @param max the max to set
     */
    public void setMax(Object max) {
        this.max = max;
    }

    /**
     * @return the pieValues
     */
    public List<Number> getPieValues() {
        if (pieValues == null || pieValues.isEmpty()) {
            pieValues = new ArrayList<>();
        }
        return pieValues;
    }

    /**
     * @param pieValues the pieValues to set
     */
    public void setPieValues(List<Number> pieValues) {
        this.pieValues = pieValues;
    }

    /**
     * @return the pieLabels
     */
    public List<String> getPieLabels() {
        if (pieLabels == null || pieLabels.isEmpty()) {
            pieLabels = new ArrayList<>();
        }
        return pieLabels;
    }

    /**
     * @param pieLabels the pieLabels to set
     */
    public void setPieLabels(List<String> pieLabels) {
        this.pieLabels = pieLabels;
    }

    /**
     * @return the pieColers
     */
    public List<String> getPieColers() {
        if (pieColers == null || pieColers.isEmpty()) {
            pieColers = new ArrayList<>();
        }
        return pieColers;
    }

    /**
     * @param pieColers the pieColers to set
     */
    public void setPieColers(List<String> pieColers) {
        this.pieColers = pieColers;
    }
}
