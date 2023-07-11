package com.company.jmixpm.validation;

import com.company.jmixpm.entity.Project;
import io.jmix.core.DataManager;
import io.jmix.core.validation.group.UiCrossFieldChecks;
import org.springframework.stereotype.Service;

import javax.annotation.Nullable;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import javax.validation.groups.Default;
import java.util.Set;

@Service
public class ProjectsBeanValidationService {
    private final DataManager dataManager;
    private final Validator validator;

    public ProjectsBeanValidationService(DataManager dataManager, Validator validator) {
        this.dataManager = dataManager;
        this.validator = validator;
    }

    @Nullable
    public Project saveProject(Project project) {
        if (project == null) {
            return null;
        }

        Set<ConstraintViolation<Project>> violationSet =
                validator.validate(project, Default.class, UiCrossFieldChecks.class);

        if (violationSet.isEmpty()) {
            return dataManager.save(project);
        }

        return null;
    }
}
