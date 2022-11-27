package com.aperepair.aperepair.application.controller;

import com.aperepair.aperepair.application.dto.response.ProposalResponseDto;
import com.aperepair.aperepair.domain.exception.NoContentException;
import com.aperepair.aperepair.domain.exception.NotFoundException;
import com.aperepair.aperepair.domain.service.MatchService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/matches")
public class MatchController {
//TODO(melhoria a ser pensada): validar UF para provider poder realizar uma proposta
    @Autowired
    private MatchService matchService;

    @GetMapping("/available-proposals/{orderId}")
    @ResponseStatus(HttpStatus.OK)
    public List<ProposalResponseDto> getProposalsForOrder(@PathVariable Integer orderId) throws NotFoundException, NoContentException {
        logger.info("Calling MatchService to get proposals for an order");

        return matchService.getProposalsForOrder(orderId);
    }

    private static final Logger logger = LogManager.getLogger(MatchController.class.getName());
}