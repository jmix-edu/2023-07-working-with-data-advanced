package com.company.jmixpm.screen.user;

import com.company.jmixpm.app.UsersService;
import com.company.jmixpm.entity.Project;
import com.company.jmixpm.entity.User;
import com.company.jmixpm.screen.userprojectsdialog.UserProjectsDialog;
import io.jmix.core.DataManager;
import io.jmix.core.LoadContext;
import io.jmix.ui.ScreenBuilders;
import io.jmix.ui.action.Action;
import io.jmix.ui.component.Filter;
import io.jmix.ui.component.GroupTable;
import io.jmix.ui.navigation.Route;
import io.jmix.ui.screen.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@UiController("User.browse")
@UiDescriptor("user-browse.xml")
@LookupComponent("usersTable")
@Route("users")
public class UserBrowse extends StandardLookup<User> {
    @Autowired
    private DataManager dataManager;
    @Autowired
    private Filter filter;
    @Autowired
    private ScreenBuilders screenBuilders;
    @Autowired
    private UsersService usersService;
    @Autowired
    private GroupTable<User> usersTable;
    private Project projectFilter;

    @Install(to = "usersDl", target = Target.DATA_LOADER)
    private List<User> usersDlLoadDelegate(LoadContext<User> loadContext) {
        LoadContext.Query query = loadContext.getQuery();
        if (projectFilter != null && query != null) {
            return usersService.getUsersNotInProject(projectFilter, query.getFirstResult(), query.getMaxResults());
        }
        return dataManager.loadList(loadContext);
    }

    public void setProjectFilter(Project projectFilter) {
        this.projectFilter = projectFilter;
        filter.setVisible(false);
    }

    @Subscribe("usersTable.showUserProjects")
    public void onUsersTableShowUserProjects(Action.ActionPerformedEvent event) {
        User user = usersTable.getSingleSelected();
        if (user == null) {
            return;
        }

        screenBuilders.screen(this)
                .withScreenClass(UserProjectsDialog.class)
                .build()
                .withUser(user)
                .show();
    }
}