package com.company.test1.web.screens.flexreports;

import com.haulmont.cuba.gui.screen.*;
import com.company.test1.entity.reportsyplantillas.FlexReport;

@UiController("test1_FlexReport.edit")
@UiDescriptor("flex-report-edit.xml")
@EditedEntityContainer("flexReportDc")
@LoadDataBeforeShow
public class FlexReportEdit extends StandardEditor<FlexReport> {
}