package com.h5.game.common.tools.check.model;

import java.util.List;

/**
 * Created by PenitenceTK on 2016/9/26.
 */
public class CheckErrorResult {

    private String name;

    private List<PeopleParamCheckModel> models;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<PeopleParamCheckModel> getModels() {
        return models;
    }

    public void setModels(List<PeopleParamCheckModel> models) {
        this.models = models;
    }
}
