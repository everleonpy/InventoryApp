package py.com.softpoint.pojos;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class InventoryItems implements Serializable {

    @SerializedName("inventoryId")
    private long inventoryId;
    @SerializedName("productId")
    private long productId;
    @SerializedName("uomId")
    private long uomId;
    @SerializedName("quantity")
    private double quantity;
    @SerializedName("workerId")
    private long workerId;
    @SerializedName("usr")
    private String usr;
    @SerializedName("deviceId")
    private long deviceId;
    @SerializedName("itemStatus")
    private String itemStatus ;

    
    public long getInventoryId() {
        return inventoryId;
    }

    public void setInventoryId(long inventoryId) {
        this.inventoryId = inventoryId;
    }

    public long getProductId() {
        return productId;
    }

    public void setProductId(long productId) {
        this.productId = productId;
    }

    public long getUomId() {
        return uomId;
    }

    public void setUomId(long uomId) {
        this.uomId = uomId;
    }

    public double getQuantity() {
        return quantity;
    }

    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }

    public long getWorkerId() {
        return workerId;
    }

    public void setWorkerId(long workerId) {
        this.workerId = workerId;
    }

    public String getUsr() {
        return usr;
    }

    public void setUsr(String usr) {
        this.usr = usr;
    }

    public long getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(long deviceId) {
        this.deviceId = deviceId;
    }

    public String getItemStatus() {
        return itemStatus;
    }

    public void setItemStatus(String itemStatus) {
        this.itemStatus = itemStatus;
    }
}
