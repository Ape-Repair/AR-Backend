package com.aperepair.aperepair.matchs.controller;

import com.aperepair.aperepair.matchs.domain.model.Match;
import com.aperepair.aperepair.matchs.domain.model.Solicitation;
import com.aperepair.aperepair.matchs.domain.service.impl.MatchFactoryServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/matches")
public class MatchController {

    @Autowired
    private MatchFactoryServiceImpl service;

    @PostMapping("/request")
    public ResponseEntity requestMatch(@RequestBody Solicitation solicitation) {
        return service.requestMatch(solicitation);
    }
}
