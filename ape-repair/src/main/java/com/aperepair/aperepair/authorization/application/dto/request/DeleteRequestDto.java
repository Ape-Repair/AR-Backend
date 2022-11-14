package com.aperepair.aperepair.authorization.application.dto.request;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

public class DeleteRequestDto {

    @NotNull
    @Min(1)
    private Integer id;

    public DeleteRequestDto(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}
