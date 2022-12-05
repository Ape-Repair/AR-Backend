package com.aperepair.aperepair.domain.service.impl;

import com.aperepair.aperepair.domain.model.Dashboard;
import com.aperepair.aperepair.domain.repository.CustomerRepository;
import com.aperepair.aperepair.domain.repository.DashboardRepository;
import com.aperepair.aperepair.domain.repository.ProviderRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DashboardServiceImpl {

    @Autowired
    private DashboardRepository dashboardRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private ProviderRepository providerRepository;

    public void addToRecipe(Double amountPaid) {
        long customers = customerRepository.countActives();
        long providers = providerRepository.countActives();

        Dashboard dashboard = new Dashboard(amountPaid, providers, customers);

        dashboardRepository.save(dashboard);
        logger.info(String.format("New value added to recipe - [%.2f]", amountPaid));
    }

    public Dashboard allAnalytics() {
        long customers = customerRepository.countActives();
        long providers = providerRepository.countActives();
        Double amountPaid = dashboardRepository.sumRecipe();

        logger.info("Query executed with success!");

        return new Dashboard(amountPaid, providers, customers);
    }

    private static final Logger logger = LogManager.getLogger(DashboardServiceImpl.class.getName());
}