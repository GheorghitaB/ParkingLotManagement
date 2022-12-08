package models.prices;

public class Price {
    private double units;
    private Currency currency;

    public Price(double units, Currency currency) {
        this.units = units;
        this.currency = currency;
    }

    public Price(){}

    public double getUnits() {
        return units;
    }

    public void setUnits(double units) {
        this.units = units;
    }

    public Currency getCurrency() {
        return currency;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }

    @Override
    public String toString() {
        return "Price{" +
                "units=" + units +
                ", currency=" + currency +
                '}';
    }
}
