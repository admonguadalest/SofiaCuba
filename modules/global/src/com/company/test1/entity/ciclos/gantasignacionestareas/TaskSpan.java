package com.company.test1.entity.ciclos.gantasignacionestareas;

import com.haulmont.chile.core.annotations.MetaClass;
import com.haulmont.chile.core.annotations.MetaProperty;
import com.haulmont.cuba.core.entity.BaseUuidEntity;

import java.util.ArrayList;
import java.util.List;

@MetaClass(name = "test1_TaskSpan")
public class TaskSpan extends BaseUuidEntity {
    private static final long serialVersionUID = -6887116914541399234L;


    @MetaProperty
    private String category;

    @MetaProperty
    private String color = "red";

    @MetaProperty
    private List<Segment> segments = new ArrayList<>();

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        if (color!=null) {
            this.color = color;
        }
    }

    public void setSegments(List<Segment> segments) {
        this.segments = segments;
    }

    public List<Segment> getSegments() {
        return segments;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}