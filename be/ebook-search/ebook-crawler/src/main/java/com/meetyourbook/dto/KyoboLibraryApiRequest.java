package com.meetyourbook.dto;

import jakarta.validation.constraints.NotNull;

public record KyoboLibraryApiRequest(

    @NotNull
    String url

) {

}
