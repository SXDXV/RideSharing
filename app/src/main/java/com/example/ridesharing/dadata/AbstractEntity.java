package com.example.ridesharing.dadata;

import com.google.gson.annotations.SerializedName;

/**
 * Абстрактный класс для определения вида обращения к DaData
 */
public abstract class AbstractEntity {
    @SerializedName("source")
    private String source;

    @SerializedName("qc")
    private int qc;

    public String getSource() {
        return source;
    }

    public int getQc() {
        return qc;
    }
}
