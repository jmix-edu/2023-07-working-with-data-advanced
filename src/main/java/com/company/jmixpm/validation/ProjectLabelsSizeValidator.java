package com.company.jmixpm.validation;

import com.company.jmixpm.datatype.ProjectLabels;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.List;

public class ProjectLabelsSizeValidator implements ConstraintValidator<ProjectLabelsSize, ProjectLabels> {

    protected int min = 0;
    protected int max = Integer.MAX_VALUE;

    @Override
    public void initialize(ProjectLabelsSize annotation) {
        min = annotation.min();
        max = annotation.max();
//        payloads
    }

    @Override
    public boolean isValid(ProjectLabels value, ConstraintValidatorContext context) {
        if (value == null) {
            return false;
        }

        List<String> labels = value.getLabels();

        return labels.size() >= min && labels.size() <= max;
    }
}
