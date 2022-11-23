package com.aperepair.aperepair.domain.service;

import com.aperepair.aperepair.application.dto.response.ProposalResponseDto;
import com.aperepair.aperepair.domain.exception.NoContentException;
import com.aperepair.aperepair.domain.exception.NotFoundException;

import java.util.List;

public interface MatchService {

    List<ProposalResponseDto> getProposalsForOrder(Integer orderId) throws NoContentException, NotFoundException;
}
