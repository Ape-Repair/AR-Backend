package com.aperepair.aperepair.domain.repository;

import com.aperepair.aperepair.domain.model.Dashboard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface DashboardRepository extends JpaRepository<Dashboard, Integer> {

    @Query("SELECT SUM(d.amountPaid) FROM Dashboard d")
    Double sumRecipe();
}