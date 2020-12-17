package com.yzh.dao;

import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * @author Yzh
 * @create 2020-12-17 8:54
 */
public class Mobj {
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String script;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String source;

    public Mobj() {
    }

    public String getScript() {
        return this.script;
    }

    public void setScript(String script) {
        this.script = script;
    }

    public String getSource() {
        return this.source;
    }

    public void setSource(String source) {
        this.source = source;
    }
}
