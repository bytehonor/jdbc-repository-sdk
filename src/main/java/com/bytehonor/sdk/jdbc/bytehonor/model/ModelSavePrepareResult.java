package com.bytehonor.sdk.jdbc.bytehonor.model;

import java.util.ArrayList;
import java.util.List;

public class ModelSavePrepareResult {

    private List<String> columns;

    private List<Object> values;

    public ModelSavePrepareResult() {
        this.columns = new ArrayList<String>();
        this.values = new ArrayList<Object>();
    }

    public List<String> getColumns() {
        return columns;
    }

    public void setColumns(List<String> columns) {
        this.columns = columns;
    }

    public List<Object> getValues() {
        return values;
    }

    public void setValues(List<Object> values) {
        this.values = values;
    }

}
