package com.yzh.importTest.requestEntity;

import com.yzh.dao.exportModel.AbstractObject;
import onegis.psde.dictionary.ModelLanguageEnum;
import onegis.psde.model.ModelDef;

/**
 * @ Author        :  yuyazhou
 * @ CreateDate    :  2021/1/12 15:00
 */
public class ModelEntity extends AbstractObject {
    private ModelDef mdef;
    private ModelLanguageEnum pLanguage;
    private String executor;

    public ModelEntity() {
        this.pLanguage = ModelLanguageEnum.JS;
        this.executor = "";
    }

    public ModelEntity(long id) {
        this.pLanguage = ModelLanguageEnum.JS;
        this.executor = "";
        this.setId(id);
    }

    public ModelDef getMdef() {
        return mdef;
    }

    public void setMdef(ModelDef mdef) {
        this.mdef = mdef;
    }

    public ModelLanguageEnum getpLanguage() {
        return pLanguage;
    }

    public void setpLanguage(ModelLanguageEnum pLanguage) {
        this.pLanguage = pLanguage;
    }

    public String getExecutor() {
        return executor;
    }

    public void setExecutor(String executor) {
        this.executor = executor;
    }
}
