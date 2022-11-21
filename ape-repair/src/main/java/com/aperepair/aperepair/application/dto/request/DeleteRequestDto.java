package com.aperepair.aperepair.application.dto.request;

import javax.validation.constraints.NotNull;

public class DeleteRequestDto {

    @NotNull
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

    @Override
    public String toString() {
        return "DeleteRequestDto{" +
                "id=" + id +
                '}';
    }
}