package com.yzh.importTest.requestEntity;


import com.yzh.dao.Mobj;
import com.yzh.dao.exportModel.AbstractObject;
import onegis.psde.model.ModelDef;

/**
 * @ Author        :  yuyazhou
 * @ CreateDate    :  2021/1/12 15:00
 */
public class ModelEntity extends AbstractObject {
    private ModelDef mdef;
    private int pLanguage;
    private Mobj mobj;


    public ModelDef getMdef() {
        return mdef;
    }

    public void setMdef(ModelDef mdef) {
        this.mdef = mdef;
    }

    public Integer getpLanguage() {
        return pLanguage;
    }

    public void setpLanguage(Integer pLanguage) {
        this.pLanguage = pLanguage;
    }

    public Mobj getMobj() {
        return mobj;
    }

    public void setMobj(Mobj mobj) {
        this.mobj = mobj;
    }
}
