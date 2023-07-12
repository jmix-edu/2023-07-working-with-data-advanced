package com.company.jmixpm.app;

import com.company.jmixpm.entity.Project;
import com.company.jmixpm.entity.ProjectStats;
import com.company.jmixpm.entity.Task;
import io.jmix.core.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class ProjectStatsService {
    private static final Logger log = LoggerFactory.getLogger(ProjectStatsService.class);

    private DataManager dataManager;
    private final FetchPlans fetchPlans;
    private final EntityStates entityStates;
    private final FetchPlanRepository fetchPlanRepository;

    public ProjectStatsService(DataManager dataManager, FetchPlans fetchPlans, EntityStates entityStates, FetchPlanRepository fetchPlanRepository) {
        this.dataManager = dataManager;
        this.fetchPlans = fetchPlans;
        this.entityStates = entityStates;
        this.fetchPlanRepository = fetchPlanRepository;
    }

    public List<ProjectStats> fetchProjectStatistics() {
        List<Project> projects = dataManager.load(Project.class)
                .all()
//                .fetchPlan(fetchPlanRepository.getFetchPlan(Project.class, "project-with-tasks"))
                .list();

        List<ProjectStats> projectStats = projects.stream()
                .map(project -> {
                    log.info("Tasks is loaded: " + entityStates.isLoaded(project, "tasks"));

                    ProjectStats stats = dataManager.create(ProjectStats.class);
                    stats.setProjectName(project.getName());

                    List<Task> tasks = project.getTasks();
                    stats.setTaskCount(tasks.size());

                    Integer estimatedEfforts = tasks.stream()
                            .map(task -> task.getEstimatedEfforts() == null ? 0 : task.getEstimatedEfforts())
                            .reduce(0, Integer::sum);
                    stats.setPlannedEfforts(estimatedEfforts);
                    stats.setActualEfforts(getActualEfforts(project.getId()));
                    return stats;
                })
                .collect(Collectors.toList());
        return projectStats;
    }

    private Integer getActualEfforts(UUID projectId) {
        Integer actualEfforts = dataManager.loadValue("select sum(te.timeSpent) from TimeEntry te " +
                        "where te.task.project.id = :projectId", Integer.class)
                .parameter("projectId", projectId)
                .one();
        return actualEfforts;
    }

    private FetchPlan createFetchPlanWithTasks() {
        return fetchPlans.builder(Project.class)
                .addFetchPlan(FetchPlan.INSTANCE_NAME)
                .add("tasks", builder ->
                        builder.add("estimatedEfforts")
                                .add("startDate"))
                .build();
    }
}