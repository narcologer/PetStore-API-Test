package org.example.model;

import com.google.gson.annotations.SerializedName;

public class Order {
    @SerializedName("id")
    private int id;

    @SerializedName("petId")
    private int petId;

    @SerializedName("quantity")
    private int quantity;

    @SerializedName("shipDate")
    private String shipDate;

    @SerializedName("status")
    private String status;

    @SerializedName("complete")
    private Boolean complete;

    public Order(int id, int petId, int quantity, String shipDate, String status, Boolean complete) {
        this.id = id;
        this.petId = petId;
        this.quantity = quantity;
        this.shipDate = shipDate;
        this.status = status;
        this.complete = complete;
    }

    public String getStatus(){
        return this.status;
    }

    @Override
    public String toString() {
        return "Order{" +
                "id=" + id +
                ", petId=" + petId +
                ", quantity=" + quantity +
                ", shipDate='" + shipDate + '\'' +
                ", status='" + status + '\'' +
                ", complete=" + complete +
                '}';
    }
}
