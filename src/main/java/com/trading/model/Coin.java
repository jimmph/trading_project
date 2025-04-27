package com.trading.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

@Entity
@Data
public class Coin {

    @Id
    @JsonProperty("id")
    private String id;

    @JsonProperty("symbol")
    private String symbol;

    @JsonProperty("name")
    private String name;

    @JsonProperty("image")
    private String image;

    @JsonProperty("currentPrice")
    private double currentPrice;

    @JsonProperty("marketCap")
    private long marketCap;

    @JsonProperty("marketCapRank")
    private int marketCapRank;

    @JsonProperty("fullyDilutedValuation")
    private long fullyDilutedValuation;

    @JsonProperty("totalVolume")
    private long totalVolume;

    @JsonProperty("high24h")
    private double high24h;

    @JsonProperty("low24h")
    private double low24h;
    
    @JsonProperty("priceChange24h")
    private double priceChange24h;

    @JsonProperty("priceChangePercentage24h")
    private double priceChangePercentage24h;

    @JsonProperty("marketCapChange24h")
    private long marketCapChange24h;

    @JsonProperty("marketCapChangePercentage24h")
    private double marketCapChangePercentage24h;

    @JsonProperty("circulatingSupply")
    private long circulatingSupply;
    
    @JsonProperty("totalSupply")
    private long totalSupply;

    @JsonProperty("maxSupply")
    private long maxSupply;

    @JsonProperty("ath")
    private double ath;

    @JsonProperty("athChangePercentage")
    private double athChangePercentage;
    
    @JsonProperty("athDate")
    private Date athDate;

    @JsonProperty("atl")
    private double atl;

    @JsonProperty("atlChangePercentage")
    private double atlChangePercentage;

    @JsonProperty("atlDate")
    private Date atlDate;

    @JsonProperty("roi")
    @JsonIgnore
    private String roi;

    @JsonProperty("last_updated")
    private Date lastUpdated;
}