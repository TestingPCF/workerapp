package com.hcl.cloud.workerapp.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CustomMessageBean {

    private String skuCode;
    private long quantity;
    
    
    public CustomMessageBean() {
    }
    public CustomMessageBean(@JsonProperty String skuCode,@JsonProperty long quantity) {
        super();
        this.skuCode = skuCode;
        this.quantity = quantity;
    }
    public String getSkuCode() {
        return skuCode;
    }
    public void setSkuCode(String skuCode) {
        this.skuCode = skuCode;
    }
    public long getQuantity() {
        return quantity;
    }
    public void setQuantity(long quantity) {
        this.quantity = quantity;
    }
    @Override
    public String toString() {
        return "CustomMessageBean [skuCode=" + skuCode + ", quantity=" + quantity + "]";
    }
    
}
