package inits.parkingspots;

import com.github.stefanbirkner.systemlambda.SystemLambda;
import org.junit.jupiter.api.Test;
import parkingspots.ParkingSpot;
import parkingspots.ParkingSpotType;

import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

class ParkingSpotsInitIntegrationTest {
    private static final String PARKING_SPOTS_INIT_TEST1_FILEPATH = "inits/parkingspots/test1.init";
    private static final String PARKING_SPOTS_INIT_TEST2_FILEPATH = "inits/parkingspots/test2.init";
    private static final String PARKING_SPOTS_INIT_TEST3_FILEPATH = "inits/parkingspots/test3.init";
    private static final String PARKING_SPOTS_INIT_TEST4_FILEPATH = "inits/parkingspots/test4.init";
    private static final String PARKING_SPOTS_INIT_TEST5_FILEPATH = "inits/parkingspots/test5.init";
    private static final String PARKING_SPOTS_INIT_TEST6_FILEPATH = "inits/parkingspots/test6.init";
    private static final String PARKING_SPOTS_INIT_TEST7_FILEPATH = "inits/parkingspots/test7.init";

    @Test
    void getListOfParkingSpotsFromResource_ShouldReturnListWithTenSmallParkingSpotsWithECAndTwoMediumParkingSpotsWithoutECWhenThisInformationIsFoundInTheFile(){
        List<ParkingSpot> resultList = ParkingSpotsInit.getListOfParkingSpotsFromResource(PARKING_SPOTS_INIT_TEST1_FILEPATH);
        assertEquals(12, resultList.size());
        List<ParkingSpot> smallParkingSpots = resultList.stream().filter(e->e.getParkingSpotType().equals(ParkingSpotType.SMALL)).collect(Collectors.toList());
        List<ParkingSpot> mediumParkingSpots = resultList.stream().filter(e->e.getParkingSpotType().equals(ParkingSpotType.MEDIUM)).collect(Collectors.toList());
        smallParkingSpots.forEach(e -> assertTrue(e.hasElectricCharger()));
        mediumParkingSpots.forEach(e -> assertFalse(e.hasElectricCharger()));
    }

    @Test
    void getListOfParkingSpotsFromResource_ShouldCloseApplicationWithStatusMinusOneWhenTheNumberOfParkingSpotsIsNotValid() throws Exception {
        int status = SystemLambda.catchSystemExit( () -> ParkingSpotsInit.getListOfParkingSpotsFromResource(PARKING_SPOTS_INIT_TEST2_FILEPATH));
        assertEquals(-1, status);
    }

    @Test
    void getListOfParkingSpotsFromResource_ShouldCloseApplicationWithStatusMinusOneWhenElectricChargerArgumentIsMissing() throws Exception {
        int status = SystemLambda.catchSystemExit( () -> ParkingSpotsInit.getListOfParkingSpotsFromResource(PARKING_SPOTS_INIT_TEST3_FILEPATH));
        assertEquals(-1, status);
    }

    @Test
    void getListOfParkingSpotsFromResource_ShouldCloseApplicationWithStatusMinusOneWhenElectricChargerArgumentIsNotValid() throws Exception {
        int status = SystemLambda.catchSystemExit( () -> ParkingSpotsInit.getListOfParkingSpotsFromResource(PARKING_SPOTS_INIT_TEST4_FILEPATH));
        assertEquals(-1, status);
    }

    @Test
    void getListOfParkingSpotsFromResource_ShouldCloseApplicationWithStatusMinusOneWhenParkingSpotTypeArgumentIsNotValid() throws Exception {
        int status = SystemLambda.catchSystemExit( () -> ParkingSpotsInit.getListOfParkingSpotsFromResource(PARKING_SPOTS_INIT_TEST5_FILEPATH));
        assertEquals(-1, status);
    }

    @Test
    void getListOfParkingSpotsFromResource_ShouldReturnEmptyListWhenAllLinesInTheFileAreCommented() {
        List<ParkingSpot> resultList = ParkingSpotsInit.getListOfParkingSpotsFromResource(PARKING_SPOTS_INIT_TEST6_FILEPATH);
        assertTrue(resultList.isEmpty());
    }

    @Test
    void getListOfParkingSpotsFromResource_ShouldReturnListWithOneLargeParkingSpotWithElectricChargerWhenThereIsALineWithThisInformationWithLeadingAndTrailingWhiteSpaceAndLowercase() {
        List<ParkingSpot> resultList = ParkingSpotsInit.getListOfParkingSpotsFromResource(PARKING_SPOTS_INIT_TEST7_FILEPATH);

        assertEquals(1, resultList.size());
        ParkingSpot parkingSpot = resultList.get(0);
        assertEquals(ParkingSpotType.LARGE, parkingSpot.getParkingSpotType());
        assertTrue(parkingSpot.hasElectricCharger());
    }
}