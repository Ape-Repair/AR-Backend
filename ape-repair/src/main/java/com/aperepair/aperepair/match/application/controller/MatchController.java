package com.aperepair.aperepair.match.application.controller;

import com.aperepair.aperepair.match.application.dto.request.CreateSolicitationRequestDto;
import com.aperepair.aperepair.match.application.dto.response.CreateSolicitationResponseDto;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import javax.validation.Valid;

@RestController
@RequestMapping("/matches")
public class MatchController {

    /* public CreateSolicitationResponseDto createSolicitation(
            @Valid @RequestBody CreateSolicitationRequestDto solicitation) {
        logger.info("");
    } */

    private static final Logger logger = LogManager.getLogger(MatchController.class.getName());
}