package com.aperepair.aperepair.application.dto.response;

import com.aperepair.aperepair.domain.model.Proposal;

import javax.validation.constraints.NotNull;

public class ProposalResponseDto {

    @NotNull
    private Proposal proposal;

    public ProposalResponseDto(Proposal proposal) {
        this.proposal = proposal;
    }

    public Proposal getProposal() {
        return proposal;
    }

    public void setProposal(Proposal proposal) {
        this.proposal = proposal;
    }
}