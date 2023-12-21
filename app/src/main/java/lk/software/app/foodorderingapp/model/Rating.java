package lk.software.app.foodorderingapp.model;

public class Rating {
    private double rating;
    private String review;

    private String  customerId;

    public Rating() {
    }

    public String getReview() {
        return review;
    }


    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public void setReview(String review) {
        this.review = review;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }
}
