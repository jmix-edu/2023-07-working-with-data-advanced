package com.company.jmixpm.app;

import com.company.jmixpm.entity.Project;
import com.company.jmixpm.entity.User;
import io.jmix.core.FetchPlan;
import io.jmix.core.FetchPlans;
import io.jmix.data.PersistenceHints;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Component
public class UsersService {
    private final FetchPlans fetchPlans;
    @PersistenceContext
    private EntityManager entityManager;

    public UsersService(FetchPlans fetchPlans) {
        this.fetchPlans = fetchPlans;
    }

    @Transactional
    public List<User> getUsersNotInProject(Project project, int firstResult, int maxResult) {
        FetchPlan fetchPlan = fetchPlans.builder(User.class)
                .add("username")
                .partial()
                .build();

        return entityManager.createQuery(
                "select e from User e" +
                        " where e.id NOT IN " +
                        "(select u.id from User u " +
                        "INNER JOIN u.projects pul WHERE pul.id = ?1)", User.class)
                .setParameter(1, project.getId())
                .setFirstResult(firstResult)
                .setMaxResults(maxResult)
//                .setHint(PersistenceHints.FETCH_PLAN, fetchPlan)
                .getResultList();
    }
}