package com.company.jmixpm.app;

import com.company.jmixpm.datatype.ProjectLabels;
import com.company.jmixpm.datatype.ProjectLabelsDatatype;
import com.company.jmixpm.entity.Project;
import io.jmix.core.JmixOrder;
import io.jmix.core.metamodel.datatype.Datatype;
import io.jmix.core.metamodel.model.MetaClass;
import io.jmix.core.metamodel.model.MetaProperty;
import io.jmix.core.metamodel.model.MetaPropertyPath;
import io.jmix.core.metamodel.model.Range;
import io.jmix.ui.UiComponents;
import io.jmix.ui.component.Component;
import io.jmix.ui.component.ComponentGenerationContext;
import io.jmix.ui.component.ComponentGenerationStrategy;
import io.jmix.ui.component.TextArea;
import io.jmix.ui.sys.ValuePathHelper;
import org.springframework.core.Ordered;

import javax.annotation.Nullable;

@org.springframework.stereotype.Component("pm_ProjectFieldsGenerationStrategy")
public class ProjectFieldsGenerationStrategy implements ComponentGenerationStrategy, Ordered {
    private final UiComponents uiComponents;

    public ProjectFieldsGenerationStrategy(UiComponents uiComponents) {
        this.uiComponents = uiComponents;
    }

    @Nullable
    @Override
    public Component createComponent(ComponentGenerationContext context) {
        String[] properties = ValuePathHelper.parse(context.getProperty());
        String checkProperty = properties[properties.length - 1];
        if (!"projectLabels".equals(checkProperty)) {
            return null;
        }

        MetaClass metaClass = context.getMetaClass();
        MetaPropertyPath propertyPath = metaClass.getPropertyPath(context.getProperty());
        if (propertyPath != null) {
            metaClass = propertyPath.getMetaProperty().getDomain();
        }
        if (!metaClass.getJavaClass().equals(Project.class)) {
            return null;
        }

        MetaProperty property = metaClass.getProperty(checkProperty);
        Range range = property.getRange();
        if (range.isDatatype()) {
            Datatype datatype = range.asDatatype();
            if (datatype instanceof ProjectLabelsDatatype) {
                return create(context);
            }
        }
        return null;
    }

    private TextArea<ProjectLabels> create(ComponentGenerationContext context) {
        TextArea<ProjectLabels> textArea = uiComponents.create(TextArea.class);
        textArea.setRows(2);
        textArea.setValueSource(context.getValueSource());
        return textArea;
    }

    @Override
    public int getOrder() {
        return JmixOrder.HIGHEST_PRECEDENCE;
    }
}
