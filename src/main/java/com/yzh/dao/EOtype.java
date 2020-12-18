package com.yzh.dao;

import onegis.psde.attribute.Fields;
import onegis.psde.dictionary.EditTypeEnum;
import onegis.psde.form.FormStyles;
import onegis.psde.model.Models;
import onegis.psde.psdm.AObject;
import onegis.psde.psdm.DTypes;
import onegis.psde.psdm.User;
import onegis.psde.reference.SpatialReferenceSystem;
import onegis.psde.reference.TimeReferenceSystem;
import onegis.psde.relation.Connectors;

import java.io.Serializable;

/**
 * @author Yzh
 * @create 2020-12-18 14:29
 * @details 导出的类模板
 */
public class EOtype extends AObject implements Serializable {
    private String tags;
    private String des;
    private String icon;
    private String code;
    private SpatialReferenceSystem srs;
    private TimeReferenceSystem trs;
    private Fields fields;
    private FormStyles formStyles;
    private Connectors connectors;
    private Models models;
    private DTypes dTypes;
    private Integer placedes;
    private EditTypeEnum editType;
    private User user;
    private Long mtime;
}
