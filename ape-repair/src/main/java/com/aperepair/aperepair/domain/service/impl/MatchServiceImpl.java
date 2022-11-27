package com.aperepair.aperepair.domain.service.impl;

import com.aperepair.aperepair.application.dto.response.ProposalResponseDto;
import com.aperepair.aperepair.domain.dto.factory.ProposalDtoFactory;
import com.aperepair.aperepair.domain.exception.NoContentException;
import com.aperepair.aperepair.domain.exception.NotFoundException;
import com.aperepair.aperepair.domain.model.Proposal;
import com.aperepair.aperepair.domain.repository.OrderRepository;
import com.aperepair.aperepair.domain.repository.ProposalRepository;
import com.aperepair.aperepair.domain.service.MatchService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class MatchServiceImpl implements MatchService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ProposalRepository proposalRepository;

    @Override
    public List<ProposalResponseDto> getProposalsForOrder(Integer orderId) throws NoContentException, NotFoundException {
        if (orderRepository.existsById(orderId)) {
            logger.info(String.format("Looking for proposals for the order of id [%d]", orderId));

            List<Proposal> proposals = proposalRepository.getAllByCustomerOrderId(orderId);

            if (proposals.isEmpty()) {
                logger.info("There are no proposals for this order");

                throw new NoContentException("There are no proposals for this order");
            }

            List<ProposalResponseDto> proposalResponseDtos = new ArrayList();

            for (Proposal proposal : proposals) {
                ProposalResponseDto proposalResponseDto = ProposalDtoFactory.toResponseDto(proposal);
                proposalResponseDtos.add(proposalResponseDto);
            }

            return proposalResponseDtos;
        }

        logger.error(String.format("Order with id: [%d] not found", orderId));
        throw new NotFoundException(String.format("Order with id [%d] not found!", orderId));
    }

    private static final Logger logger = LogManager.getLogger(MatchServiceImpl.class.getName());
}