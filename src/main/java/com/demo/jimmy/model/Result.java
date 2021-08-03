package com.demo.jimmy.model;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
public class Result {

    private long id;
    private String address;
    private int enabled = 1;
    private long dateAdded = 1627667237;
    private long dateModified = 1627667237;
    private String comment = "a";
    private String dateUpdated = null;
    private int number = 0;
    private int invalidDomains = 0;
    private int status = 0;

    public Result(long id, String address) {
        this.id = id;
        this.address = address;
    }

}
