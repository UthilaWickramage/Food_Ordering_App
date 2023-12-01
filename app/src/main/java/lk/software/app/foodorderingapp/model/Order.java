package lk.software.app.foodorderingapp.model;

public class Order {
    private String documentId;

    private String currentSaveDate, currentSaveTime;

    private double totalOrderPrice;

    private OrderStatusEnum orderStatus = OrderStatusEnum.PENDING;

    public Order(String documentId, String currentSaveDate, String currentSaveTime, double totalOrderPrice) {
        this.documentId = documentId;
        this.currentSaveDate = currentSaveDate;
        this.currentSaveTime = currentSaveTime;
        this.totalOrderPrice = totalOrderPrice;
    }

    public Order() {
    }

    public String getDocumentId() {
        return documentId;
    }

    public void setDocumentId(String documentId) {
        this.documentId = documentId;
    }

    public String getCurrentSaveDate() {
        return currentSaveDate;
    }

    public void setCurrentSaveDate(String currentSaveDate) {
        this.currentSaveDate = currentSaveDate;
    }

    public String getCurrentSaveTime() {
        return currentSaveTime;
    }

    public void setCurrentSaveTime(String currentSaveTime) {
        this.currentSaveTime = currentSaveTime;
    }

    public double getTotalOrderPrice() {
        return totalOrderPrice;
    }

    public void setTotalOrderPrice(double totalOrderPrice) {
        this.totalOrderPrice = totalOrderPrice;
    }

    public OrderStatusEnum getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(OrderStatusEnum orderStatus) {
        this.orderStatus = orderStatus;
    }
}
