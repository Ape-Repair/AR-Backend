package com.aperepair.aperepair.domain.dto.factory;

import com.aperepair.aperepair.application.dto.request.CreateProposalRequestDto;
import com.aperepair.aperepair.domain.model.CustomerOrder;
import com.aperepair.aperepair.domain.model.Proposal;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.time.LocalDateTime;

public class ProposalDtoFactory {

    public static Proposal toEntity(CreateProposalRequestDto request, CustomerOrder customerOrder) {
        Proposal proposal = new Proposal(
                customerOrder.getServiceType(),
                customerOrder.getDescription(),
                request.getAmount(),
                false,
                LocalDateTime.now()
        );

        logger.info("CreateProposalRequestDto transformed to Proposal entity with success!");
        return proposal;
    }

    private static final Logger logger = LogManager.getLogger(ProposalDtoFactory.class.getName());
}