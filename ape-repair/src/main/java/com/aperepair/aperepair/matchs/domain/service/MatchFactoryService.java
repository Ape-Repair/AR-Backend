package com.aperepair.aperepair.matchs.domain.service;

import com.aperepair.aperepair.matchs.domain.model.Solicitation;
import org.springframework.http.ResponseEntity;

public interface MatchFactoryService {

    ResponseEntity requestMatch(Solicitation solicitation);
}
