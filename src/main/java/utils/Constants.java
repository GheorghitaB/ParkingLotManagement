package utils;

public class Constants {
    public static final String REGULAR_USER_STRATEGY_FILEPATH_PROPERTY = "regular-user-strategy-file-path";
    public static final String VIP_USER_STRATEGY_FILEPATH_PROPERTY = "vip-user-strategy-file-path";
    public static final String PARKING_SPOTS_FILEPATH_PROPERTY = "parking-spots-file-path";
    public static final String APP_PROPERTIES_FILEPATH = "properties/app-properties.init";
    public static final String TICKET_SAVING_LOCATION = "tickets-saving-location";

    public static final double BASE_PRICE_FOR_VIP_USER = 0.5;
    public static final double BASE_PRICE_FOR_REGULAR_USER = 1.0;

    public static final double BASE_PRICE_FOR_MOTORCYCLE_TYPE = 0.5;
    public static final double BASE_PRICE_FOR_CAR_TYPE = 0.15;
    public static final double BASE_PRICE_FOR_TRUCK_TYPE = 0.20;

    public static final double BASE_PRICE_FOR_SMALL_PS_TYPE = 0.1;
    public static final double BASE_PRICE_FOR_MEDIUM_PS_TYPE = 0.2;
    public static final double BASE_PRICE_FOR_LARGE_PS_TYPE = 0.3;

    public static final double VIP_USER_DISCOUNT = 0.50;
    public static final double REGULAR_USER_DISCOUNT = 0.25;

    public static final int DISCOUNT_AVAILABLE_AFTER_THIS_TIME = 180; // 3 hours
}
