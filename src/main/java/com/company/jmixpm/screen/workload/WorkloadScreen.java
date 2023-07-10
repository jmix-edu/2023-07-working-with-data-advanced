package com.company.jmixpm.screen.workload;

import com.company.jmixpm.screen.workloadinfo.WorkloadInfoScreen;
import io.jmix.core.entity.KeyValueEntity;
import io.jmix.ui.ScreenBuilders;
import io.jmix.ui.component.Button;
import io.jmix.ui.component.Table;
import io.jmix.ui.screen.Screen;
import io.jmix.ui.screen.Subscribe;
import io.jmix.ui.screen.UiController;
import io.jmix.ui.screen.UiDescriptor;
import org.springframework.beans.factory.annotation.Autowired;

@UiController("WorkloadScreen")
@UiDescriptor("workload-screen.xml")
public class WorkloadScreen extends Screen {
    @Autowired
    private ScreenBuilders screenBuilders;

    @Autowired
    private Table<KeyValueEntity> workloadTable;

    @Subscribe("showInfoBtn")
    public void onShowInfoBtnClick(Button.ClickEvent event) {
        KeyValueEntity singleSelected = workloadTable.getSingleSelected();
        if (singleSelected == null) {
            return;
        }

        screenBuilders.screen(this)
                .withScreenClass(WorkloadInfoScreen.class)
                .build()
                .withItem(singleSelected)
                .show();
    }
}