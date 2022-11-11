package inits.parkingspots;

import Utils.TextArgumentParser;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import parkingspots.ParkingSpot;

import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


class ParkingSpotsInitTest {

    @Test
    void getListOfParkingSpotsFromResource_ShouldReturnListWithTenSmallParkingSpotsWithECAndThreeMediumParkingSpotsWithoutEC(){
        String resourcePath = "mockResourcePath";
        String line1 = "SMALL 10 1";
        String line2 = "MEDIUM 3 0";
        List<String> givenLines = List.of(line1, line2);
        String[] argumentsLine1 = {"SMALL", "10", "1"};
        String[] argumentsLine2 = {"MEDIUM", "3", "0"};

        try(MockedStatic<TextArgumentParser> textArgumentParserMockedStatic = Mockito.mockStatic(TextArgumentParser.class)){
            textArgumentParserMockedStatic.when( () -> TextArgumentParser.readLines(resourcePath)).thenReturn(givenLines);
            textArgumentParserMockedStatic.when( () -> TextArgumentParser.prepareLine(givenLines.get(0))).thenReturn(line1);
            textArgumentParserMockedStatic.when( () -> TextArgumentParser.prepareLine(givenLines.get(1))).thenReturn(line2);
            textArgumentParserMockedStatic.when( () -> TextArgumentParser.getArgumentsFromLine(givenLines.get(0))).thenReturn(argumentsLine1);
            textArgumentParserMockedStatic.when( () -> TextArgumentParser.getArgumentsFromLine(givenLines.get(1))).thenReturn(argumentsLine2);
            textArgumentParserMockedStatic.when( () -> TextArgumentParser.notComment(givenLines.get(0))).thenReturn(true);
            textArgumentParserMockedStatic.when( () -> TextArgumentParser.notComment(givenLines.get(1))).thenReturn(true);
            textArgumentParserMockedStatic.when( () -> TextArgumentParser.prepareArguments(argumentsLine1)).thenReturn(argumentsLine1);
            textArgumentParserMockedStatic.when( () -> TextArgumentParser.prepareArguments(argumentsLine2)).thenReturn(argumentsLine2);

            List<ParkingSpot> parkingSpotsList = ParkingSpotsInit.getListOfParkingSpotsFromResource(resourcePath);
            textArgumentParserMockedStatic.verify( () -> TextArgumentParser.readLines(resourcePath), times(1));
            textArgumentParserMockedStatic.verify( () -> TextArgumentParser.prepareLine(givenLines.get(0)), times(1));
            textArgumentParserMockedStatic.verify( () -> TextArgumentParser.prepareLine(givenLines.get(1)), times(1));
            textArgumentParserMockedStatic.verify( () -> TextArgumentParser.getArgumentsFromLine(givenLines.get(0)), times(1));
            textArgumentParserMockedStatic.verify( () -> TextArgumentParser.getArgumentsFromLine(givenLines.get(1)), times(1));
            textArgumentParserMockedStatic.verify( () -> TextArgumentParser.notComment(givenLines.get(0)), times(1));
            textArgumentParserMockedStatic.verify( () -> TextArgumentParser.notComment(givenLines.get(1)), times(1));
            textArgumentParserMockedStatic.verify( () -> TextArgumentParser.prepareArguments(argumentsLine1), times(1));
            textArgumentParserMockedStatic.verify( () -> TextArgumentParser.prepareArguments(argumentsLine2), times(1));

            List<ParkingSpot> smallParkingSpots = parkingSpotsList.stream()
                    .filter(e -> e.getParkingSpotType().name().equals("SMALL"))
                    .collect(Collectors.toList());
            List<ParkingSpot> mediumParkingSpots = parkingSpotsList.stream()
                    .filter(e -> e.getParkingSpotType().name().equals("MEDIUM"))
                    .collect(Collectors.toList());

            assertEquals(Integer.parseInt(argumentsLine1[1]) + Integer.parseInt(argumentsLine2[1]), parkingSpotsList.size());
            assertEquals(Integer.parseInt(argumentsLine1[1]), smallParkingSpots.size());
            assertEquals(Integer.parseInt(argumentsLine2[1]), mediumParkingSpots.size());
            smallParkingSpots.forEach(e -> assertTrue(e.hasElectricCharger()));
            mediumParkingSpots.forEach(e -> assertFalse(e.hasElectricCharger()));
        }
    }
}
