package com.app.first_arrival.entities.dto;

import lombok.Data;

import java.util.List;

@Data
public class Dataset {
    private String label;
    private List<Integer> data;
    private List<String> backgroundColor;
}
