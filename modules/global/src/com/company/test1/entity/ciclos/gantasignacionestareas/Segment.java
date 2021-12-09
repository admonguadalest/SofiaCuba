package com.company.test1.entity.ciclos.gantasignacionestareas;

import com.haulmont.chile.core.annotations.MetaClass;
import com.haulmont.chile.core.annotations.MetaProperty;
import com.haulmont.cuba.core.entity.BaseUuidEntity;
import org.apache.commons.lang3.time.DateUtils;

import java.util.Date;

@MetaClass(name = "test1_Segment")
public class Segment extends BaseUuidEntity {
    private static final long serialVersionUID = -1340871364797272195L;

    @MetaProperty
    private Date start;

    @MetaProperty
    private Integer duration;

    @MetaProperty
    private String color = "red";

    @MetaProperty
    private String task;

    @MetaProperty
    private String category;

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    @MetaProperty
    public Date getEnd() {
        if (getStart()!=null){
            return DateUtils.addDays(getStart(),getDuration());
        }else{
            return null;
        }

    }

    public void setStart(Date start) {
        this.start = start;
    }

    public Date getStart() {
        return start;
    }

    public String getTask() {
        return task;
    }

    public void setTask(String task) {
        this.task = task;
    }

    public String getColor() {

        return color;
    }

    public void setColor(String color) {
        if (color!=null) {
            this.color = color;
        }
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

}