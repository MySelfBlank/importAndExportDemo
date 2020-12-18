package com.yzh.utilts;

import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson.JSON;
import com.yzh.dao.EField;
import onegis.psde.attribute.Field;
import onegis.psde.attribute.Fields;
import org.springframework.util.StringUtils;

import java.util.*;

/**
 * @author Yzh
 * @create 2020-12-17 14:22
 * @Details 字段处理
 */
public class FieldUtils {
    /**
     *
     * @param fields Json 字段项
     * @return
     */
    public static List<EField> dsField2Field(Fields fields){
        List<EField> eFields = new ArrayList<>();
        //判断字段是否为空 为空直接返回
        if(ObjectUtil.isNull(fields)&&ObjectUtil.isEmpty(fields)){
            return eFields;
        }
        List<Field> fieldList = new ArrayList<>();
        if (!ObjectUtil.isNull(fields.getFields())&&!ObjectUtil.isEmpty(fields.getFields())){
            fieldList.addAll(fields.getFields());
            //处理每一个字段信息
            for (Field field : fieldList) {
                eFields.add(field2EField(field));
            }
        }
        return eFields;
    }
    public static EField field2EField(Field field){
        EField eField = new EField();
        eField.setId(field.getId());
        eField.setCaption(field.getCaption());
        eField.setDesc(field.getDes());
        eField.setName(field.getName());
        //值域处理
        Map<String, Object> domain = new HashMap<>();
        if (!StringUtils.isEmpty(field.getDomain())){
            //值域存储的为JSON串
            Map<String,Object> map = JSON.parseObject(field.getDomain(), Map.class);
            map.forEach((k,v)->{
                if (k.equals("Range")){
                    domain.put("type","list");
                }else {
                    domain.put("type","range");
                }
                if (v instanceof List){
                    List value = (List) v;
                    //移除空的集合
                    ((List<?>) v).removeAll(Collections.singleton(null));
                    if (ObjectUtil.isEmpty(v)&&((List<?>) v).size()==0){
                        domain.remove("type");
                        //结束本次循环
                        return;
                    }
                    domain.put("value", value);
                }else {
                    domain.put("value", v);
                }
            });

        }
        if (domain.size() > 0) {
            eField.setDomain(domain);
        }
        //类型处理
        if (field.getType().getName().equalsIgnoreCase("date")){
            eField.setType("datetime");
        }else {
            eField.setType(field.getType().getName());
        }

        //默认值处理
        if (ObjectUtil.isNotNull(field.getDefaultValue())){
            eField.setDefaultValue(field.getDefaultValue().toString());
        }
        return eField;
    }
}
