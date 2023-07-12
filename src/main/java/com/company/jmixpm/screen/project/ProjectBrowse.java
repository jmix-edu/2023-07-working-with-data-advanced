package com.company.jmixpm.screen.project;

import com.company.jmixpm.entity.Project;
import io.jmix.core.DataManager;
import io.jmix.ui.Notifications;
import io.jmix.ui.component.Button;
import io.jmix.ui.screen.*;
import org.springframework.beans.factory.annotation.Autowired;

@UiController("Project.browse")
@UiDescriptor("project-browse.xml")
@LookupComponent("projectsTable")
public class ProjectBrowse extends StandardLookup<Project> {
    @Autowired
    private DataManager dataManager;
    @Autowired
    private Notifications notifications;

    @Subscribe
    public void onBeforeShow(BeforeShowEvent event) {
        Integer newProjectsCount = dataManager.loadValue("select count(e) from Project e" +
                " where :session_isManager = TRUE and e.status = 10" +
                " and e.manager.id = :current_user_id", Integer.class).one();

        if (newProjectsCount > 0) {
            notifications.create(Notifications.NotificationType.TRAY)
                    .withPosition(Notifications.Position.TOP_RIGHT)
                    .withCaption("New projects")
                    .withDescription("Project with NEW status: " + newProjectsCount)
                    .show();
        }
    }

/*    @Subscribe("createCustomBtn")
    public void onCreateCustomBtnClick(Button.ClickEvent event) {
        Project project = dataManager.create(Project.class);
        dataManager.save(project);
    }*/


}