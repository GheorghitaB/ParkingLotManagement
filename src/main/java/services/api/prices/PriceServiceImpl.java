package services.api.prices;

import com.fasterxml.jackson.core.JsonProcessingException;
import models.parkings.spots.ParkingSpotType;
import models.prices.Currency;
import models.prices.Price;
import models.users.UserType;
import models.vehicles.VehicleType;
import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import services.properties.AppProperty;
import services.readers.JsonReader;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static utils.Constants.SERVICE_GET_PRICE_URL;

public class PriceServiceImpl implements PriceService {
    private final Logger logger = LoggerFactory.getLogger(PriceServiceImpl.class);

    @Override
    public Optional<Price> getPrice(int parkingTimeInMinutes, UserType userType, VehicleType vehicleType,
                                   ParkingSpotType parkingSpotType, Currency toCurrency) {

        List<NameValuePair> parameters = new ArrayList<>();
        parameters.add(new BasicNameValuePair("vehicleType", vehicleType.name()));
        parameters.add(new BasicNameValuePair("userType", userType.name()));
        parameters.add(new BasicNameValuePair("parkingSpotType", parkingSpotType.name()));
        parameters.add(new BasicNameValuePair("parkingTimeInMinutes", String.valueOf(parkingTimeInMinutes)));
        parameters.add(new BasicNameValuePair("toCurrency", toCurrency.name()));

        HttpRequest httpRequest;
        try {
            httpRequest = createRequest(AppProperty.getProperty(SERVICE_GET_PRICE_URL), parameters);
            logger.info("Request created. " + httpRequest.toString());
        } catch (URISyntaxException e) {
            logger.error("URL: " + AppProperty.getProperty(SERVICE_GET_PRICE_URL));
            logger.error("URISyntaxException: " + e.getMessage());
            return Optional.empty();
        }

        HttpResponse<String> response;
        try {
            response = HttpClient.newHttpClient().send(httpRequest, HttpResponse.BodyHandlers.ofString());
            logger.info("Response status code: " + response.statusCode());
        } catch (IOException | InterruptedException e) {
            logger.error("HttpResponse error: " + e.getMessage());
            return Optional.empty();
        }

        Price resultPrice;
        try {
            resultPrice = new JsonReader<Price>().readJson(response.body(), Price.class);
        } catch (JsonProcessingException e) {
            logger.error("JsonProcessingException: " + e.getMessage());
            return Optional.empty();
        }

        return Optional.of(resultPrice);
    }

    private HttpRequest createRequest(String url, List<NameValuePair> parameters) throws URISyntaxException {
        return HttpRequest.newBuilder()
                .uri(new URIBuilder(url).addParameters(parameters).build())
                .method("GET", HttpRequest.BodyPublishers.noBody())
                .build();
    }
}
