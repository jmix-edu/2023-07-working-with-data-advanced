package com.company.jmixpm.screen.workloadinfo;

import io.jmix.core.entity.KeyValueEntity;
import io.jmix.ui.component.Button;
import io.jmix.ui.model.KeyValueContainer;
import io.jmix.ui.screen.Screen;
import io.jmix.ui.screen.Subscribe;
import io.jmix.ui.screen.UiController;
import io.jmix.ui.screen.UiDescriptor;
import org.springframework.beans.factory.annotation.Autowired;

@UiController("WorkloadInfoScreen")
@UiDescriptor("workload-info-screen.xml")
public class WorkloadInfoScreen extends Screen {
    @Autowired
    private KeyValueContainer workloadDc;

    public WorkloadInfoScreen withItem(KeyValueEntity item) {
        workloadDc.setItem(item);
        return this;
    }

    @Subscribe("closeBtn")
    public void onCloseBtnClick(Button.ClickEvent event) {
        closeWithDefaultAction();
    }

}