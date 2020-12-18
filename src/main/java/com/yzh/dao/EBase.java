package com.yzh.dao;

import com.fasterxml.jackson.annotation.JsonInclude;
import onegis.psde.psdm.OBase;
import onegis.psde.reference.SpatialReferenceSystem;
import onegis.psde.reference.TimeReferenceSystem;

import java.util.List;

/**
 * @ Author        :  yuyazhou
 * @ CreateDate    :  2020/12/17 9:24
 */
public class EBase {
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String code;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<OBase> parentList;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private TimeReferenceSystem trs;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private SpatialReferenceSystem srs;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public List<OBase> getParentList() {
        return parentList;
    }

    public void setParentList(List<OBase> parentList) {
        this.parentList = parentList;
    }

    public TimeReferenceSystem getTrs() {
        return trs;
    }

    public void setTrs(TimeReferenceSystem trs) {
        this.trs = trs;
    }

    public SpatialReferenceSystem getSrs() {
        return srs;
    }

    public void setSrs(SpatialReferenceSystem srs) {
        this.srs = srs;
    }

    @Override
    public String toString() {
        return "EBase{" +
                "code='" + code + '\'' +
                ", parentList=" + parentList +
                ", trs=" + trs +
                ", srs=" + srs +
                '}';
    }
}
