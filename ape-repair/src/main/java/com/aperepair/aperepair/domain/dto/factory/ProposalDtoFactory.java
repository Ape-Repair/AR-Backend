package com.aperepair.aperepair.domain.dto.factory;

import com.aperepair.aperepair.application.dto.request.CreateProposalRequestDto;
import com.aperepair.aperepair.application.dto.response.ProposalResponseDto;
import com.aperepair.aperepair.domain.model.CustomerOrder;
import com.aperepair.aperepair.domain.model.Proposal;

import java.time.LocalDateTime;

public class ProposalDtoFactory {

    public static Proposal toEntity(CreateProposalRequestDto request, CustomerOrder customerOrder) {

        return new Proposal(
                customerOrder.getServiceType(),
                customerOrder.getDescription(),
                request.getAmount(),
                false,
                LocalDateTime.now()
        );
    }

    public static ProposalResponseDto toResponseDto(Proposal proposal) {

        return new ProposalResponseDto(
                proposal.getId(),
                proposal.getOrderId().getId(),
                proposal.getProviderId().getId(),
                proposal.getServiceType(),
                proposal.getDescription(),
                proposal.getAmount(),
                proposal.isAccepted(),
                LocalDateTime.now()
        );
    }
}