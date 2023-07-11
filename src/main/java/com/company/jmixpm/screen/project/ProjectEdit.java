package com.company.jmixpm.screen.project;

import com.company.jmixpm.app.ProjectsService;
import com.company.jmixpm.datatype.ProjectLabels;
import com.company.jmixpm.entity.Project;
import com.company.jmixpm.validation.ProjectsBeanValidationService;
import io.jmix.ui.Notifications;
import io.jmix.ui.component.Button;
import io.jmix.ui.component.TextArea;
import io.jmix.ui.component.ValidationException;
import io.jmix.ui.component.validation.Validator;
import io.jmix.ui.component.validator.BeanPropertyValidator;
import io.jmix.ui.screen.*;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.Collection;
import java.util.List;

@UiController("Project.edit")
@UiDescriptor("project-edit.xml")
@EditedEntityContainer("projectDc")
public class ProjectEdit extends StandardEditor<Project> {

    @Autowired
    private TextArea<ProjectLabels> projectLabelsField;
    @Autowired
    private ProjectsService projectsService;
    @Autowired
    private ProjectsBeanValidationService projectsBeanValidationService;
    @Autowired
    private Notifications notifications;

    @Subscribe
    public void onInit(InitEvent event) {
        /*projectLabelsField.addValidator(value -> {
           *//* throw new ValidationException("");*//*
        });
        Collection<Validator<ProjectLabels>> validators = projectLabelsField.getValidators();
        for (Validator<ProjectLabels> validator : validators) {
            if (validator instanceof BeanPropertyValidator) {
                *//*((BeanPropertyValidator) validator).accept();*//*
            }
        }*/
    }

    @Subscribe
    public void onInitEntity(final InitEntityEvent<Project> event) {
        projectLabelsField.setEditable(true);

        event.getEntity().setProjectLabels(new ProjectLabels(List.of("bug", "task", "enhancement")));
    }

    @Subscribe("commitWithBeanValidation")
    public void onCommitWithBeanValidationClick(Button.ClickEvent event) {
        try {
            projectsService.saveProject(getEditedEntity());
        } catch (ConstraintViolationException e) {
            StringBuilder sb = new StringBuilder();

            for (ConstraintViolation<?> constraintViolation : e.getConstraintViolations()) {
                sb.append(constraintViolation.getMessage()).append("\n");
            }

            notifications.create(Notifications.NotificationType.TRAY)
                    .withCaption(sb.toString())
                    .show();
        }
    }

    @Subscribe("saveWithBeanValidationSerivce")
    public void onSaveWithBeanValidationSerivceClick(Button.ClickEvent event) {
        projectsBeanValidationService.saveProject(getEditedEntity());
    }
}