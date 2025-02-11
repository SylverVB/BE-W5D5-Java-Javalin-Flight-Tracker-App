package application;

import application.dao.FlightDAO;
import application.model.Flight;
import application.service.FlightService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

/**
 * This class tests using the dummy data inserted as part of the Application.Application.databaseSetup() method.
 * It contains the following records:
 *      (1, 'tampa', 'dallas'),
 *      (2, 'tampa', 'reston'),
 *      (3, 'reston', 'morgantown'),
 *      (4, 'morgantown', 'dallas'),
 *      (5, 'tampa', 'dallas'),
 *      (6, 'dallas', 'tampa')
 */
@ExtendWith(MockitoExtension.class)
public class FlightAppTest {
    public FlightDAO flightDAO;
    public FlightDAO mockFlightDAO;
    public FlightService flightService;

    /**
     * Set up a flightDAO and recreate the database tables and mock data.
     */
    @BeforeEach
    public void setUp() {
        Application.databaseSetup();
        flightDAO = new FlightDAO();

        mockFlightDAO = Mockito.mock(FlightDAO.class);
        flightService = new FlightService(mockFlightDAO);
    }

    /**
     * THESE TESTS ARE FOR THE FLIGHTDAO CLASS
     */

    /**
     * The flightDAO should retrieve all flights when getAllFlights is called.
     */
    @Test
    public void flightDAO_GetAllFlightsTest1() {
        List<Flight> allFlights = flightDAO.getAllFlights();
        Flight f1 = new Flight(1, "tampa", "dallas");
        Flight f2 = new Flight(2, "tampa", "reston");
        Flight f3 = new Flight(3, "reston", "morgantown");
        Flight f4 = new Flight(4, "morgantown", "dallas");
        Flight f5 = new Flight(5, "tampa", "dallas");
        Flight f6 = new Flight(6, "dallas", "tampa");
        assertTrue(allFlights.contains(f1));
        assertTrue(allFlights.contains(f2));
        assertTrue(allFlights.contains(f3));
        assertTrue(allFlights.contains(f4));
        assertTrue(allFlights.contains(f5));
        assertTrue(allFlights.contains(f6));
    }

    /**
     * The flightDAO should retrieve a flight with a specific ID when getFlightById is called.
     */
    @Test
    public void flightDAO_GetFlightByIDTest1() {
        Flight flight = flightDAO.getFlightById(6);
        if (flight == null) {
            fail();
        } else {
            Flight f6 = new Flight(6, "dallas", "tampa");
            assertEquals(flight, f6);
        }
    }

    /**
     * The flightDAO should retrieve a flight with a specific ID when getFlightById is called.
     */
    @Test
    public void flightDAO_GetFlightByIDTest2() {
        Flight flight = flightDAO.getFlightById(4);
        if (flight == null) {
            fail();
        } else {
            Flight f9012 = new Flight(4, "morgantown", "dallas");
            assertEquals(flight, f9012);
        }
    }

    /**
     * When there is one flight between two cities, getAllFlightsFromCityToCity should return a list containing
     * that flight. It should not contain other flights.
     */
    @Test
    public void flightDAO_GetFlightsFromCityToCityTest1() {
        List<Flight> flights = flightDAO.getAllFlightsFromCityToCity("reston", "morgantown");
        Flight f1 = new Flight(1, "tampa", "dallas");
        Flight f3 = new Flight(3, "reston", "morgantown");
        Flight f5 = new Flight(5, "tampa", "dallas");
        assertFalse(flights.contains(f1));
        assertTrue(flights.contains(f3));
        assertFalse(flights.contains(f5));
    }

    /**
     * When there are multiple flights between two cities, getAllFlightsFromCityToCity should return a list containing
     * both flights. It should not contain other flights.
     */
    @Test
    public void flightDAO_GetFlightsFromCityToCityTest2() {
        List<Flight> flights = flightDAO.getAllFlightsFromCityToCity("tampa", "dallas");
        Flight f1 = new Flight(1, "tampa", "dallas");
        Flight f3 = new Flight(3, "reston", "morgantown");
        Flight f5 = new Flight(5, "tampa", "dallas");
        assertTrue(flights.contains(f1));
        assertFalse(flights.contains(f3));
        assertTrue(flights.contains(f5));
    }

    /**
     * When a flight is added via the flightDAO, it should be retrievable by retrieving the flight by ID.
     */
    @Test
    public void flightDAO_InsertFlightCheckByIdTest1() {
        Flight f7 = new Flight("tampa", "morgantown");
        flightDAO.insertFlight(f7);
        Flight f7expected = new Flight(7, "tampa", "morgantown");
        Flight f7actual = flightDAO.getFlightById(7);
        assertEquals(f7expected, f7actual);
    }

    /**
     * When a flight is added via the flightDAO, it should be retrievable by retrieving all flights.
     */
    @Test
    public void flightDAO_InsertFlightCheckAllFlightsTest1() {
        Flight f7 = new Flight("tampa", "morgantown");
        flightDAO.insertFlight(f7);
        Flight f7expected = new Flight(7, "tampa", "morgantown");
        List<Flight> allFlights = flightDAO.getAllFlights();
        assertTrue(allFlights.contains(f7expected));
    }

    /**
     * When a flight is updated via the flightDAO, the updated values should be retrieved when the flight is next
     * accessed.
     */
    @Test
    public void flightDAO_UpdateFlightDAOTest1() {
        Flight f1updated = new Flight("reston", "dallas");
        flightDAO.updateFlight(1, f1updated);
        Flight f1expected = new Flight(1, "reston", "dallas");
        Flight f1actual = flightDAO.getFlightById(1);
        assertEquals(f1expected, f1actual);
    }

    /**
     * THESE TESTS ARE FOR THE FLIGHTSERVICE CLASS
     */

    /**
     * When a flightDAO returns all flights, flightService.getAllFlights should return all flights.
     */
    @Test
    public void flightService_GetAllFlightsTest() {
        List<Flight> allFlightsReturned = new ArrayList<>();
        allFlightsReturned.add(new Flight(801, "tampa", "dallas"));
        allFlightsReturned.add(new Flight(802, "tampa", "morgantown"));
        allFlightsReturned.add(new Flight(803, "tampa", "reston"));
        allFlightsReturned.add(new Flight(804, "tampa", "dallas"));
        Mockito.when(mockFlightDAO.getAllFlights()).thenReturn(allFlightsReturned);
        assertEquals(allFlightsReturned, flightService.getAllFlights());
    }

    /**
     * When a flight is added via the flightService, it should return the added flight.
     */
    @Test
    public void flightService_AddFlightTest() {
        Flight newFlight = new Flight("dallas", "morgantown");
        Flight persistedFlight = new Flight(1, "dallas", "morgantown");
        Mockito.when(mockFlightDAO.insertFlight(newFlight)).thenReturn(persistedFlight);
        Flight actualFlight = flightService.addFlight(newFlight);
        assertEquals(persistedFlight, actualFlight);
        Mockito.verify(mockFlightDAO).insertFlight(Mockito.any());
    }

    /**
     * When flights are filtered by city pairs, flightService.getAllFlightsFromCityToCity should return only matching flights.
     */
    @Test
    public void flightService_GetFlightsFromCityToCityTest1() {
        List<Flight> allFlightsReturned = new ArrayList<>();
        allFlightsReturned.add(new Flight(801, "tampa", "dallas"));
        allFlightsReturned.add(new Flight(802, "tampa", "morgantown"));
        allFlightsReturned.add(new Flight(803, "tampa", "reston"));
        allFlightsReturned.add(new Flight(804, "tampa", "dallas"));
        allFlightsReturned.add(new Flight(805, "dallas", "morgantown"));
        Flight f801 = new Flight(801, "tampa", "dallas");
        Flight f804 = new Flight(804, "tampa", "dallas");
        // List<Flight> cityToCityFlightsReturned = new ArrayList<>();
        // cityToCityFlightsReturned.add(f801);
        // cityToCityFlightsReturned.add(f804);
        // // Mockito.when(mockFlightDAO.getAllFlightsFromCityToCity("tampa", "dallas"))
        // //         .thenReturn(cityToCityFlightsReturned);
        // Mockito.lenient().when(mockFlightDAO.getAllFlightsFromCityToCity("tampa", "dallas"))
        //            .thenReturn(cityToCityFlightsReturned);
        // // Mockito.when(mockFlightDAO.getAllFlights()).thenReturn(allFlightsReturned);
        // Mockito.lenient().when(mockFlightDAO.getAllFlights()).thenReturn(allFlightsReturned);

        // assertTrue(flightService.getAllFlightsFromCityToCity("tampa", "dallas")
        //         .contains(f801));
        // assertTrue(flightService.getAllFlightsFromCityToCity("tampa", "dallas")
        //         .contains(f804));

        // Mock DAO to return specific flights based on the city pair
        Mockito.lenient().when(mockFlightDAO.getAllFlightsFromCityToCity("tampa", "dallas"))
            .thenReturn(allFlightsReturned.stream()
                .filter(flight -> flight.getDeparture_city().equals("tampa") && flight.getArrival_city().equals("dallas"))
                .collect(Collectors.toList()));
        
        Mockito.lenient().when(mockFlightDAO.getAllFlights()).thenReturn(allFlightsReturned);

        // Ensure mock data is returned correctly
        List<Flight> flights = flightService.getAllFlightsFromCityToCity("tampa", "dallas");
        
        assertTrue(flights.contains(f801));
        assertTrue(flights.contains(f804));
        
        Mockito.verify(mockFlightDAO).getAllFlightsFromCityToCity("tampa", "dallas");
    }

    /**
     * When a flight is updated, flightService.updateFlight should return the updated flight.
     */
    @Test
    public void flightService_UpdateFlightTest1() {
        List<Flight> allFlightsReturned = new ArrayList<>();
        allFlightsReturned.add(new Flight(801, "tampa", "dallas"));
        allFlightsReturned.add(new Flight(802, "tampa", "morgantown"));
        allFlightsReturned.add(new Flight(803, "tampa", "reston"));
        allFlightsReturned.add(new Flight(804, "tampa", "dallas"));
        allFlightsReturned.add(new Flight(805, "dallas", "morgantown"));
        Flight f801 = new Flight("dallas", "morgantown");
        Flight expectedFlight = new Flight(801, "dallas", "morgantown");
        // Mockito.when(mockFlightDAO.updateFlight(801, f801)).thenReturn(expectedFlight);
        // Mock the updateFlight void method to do nothing
        // Mockito.doNothing().when(mockFlightDAO).updateFlight(801, f801);
        Mockito.doNothing().when(mockFlightDAO).updateFlight(Mockito.anyInt(), Mockito.any(Flight.class));
        // Mock the getAllFlights method to return the list of flights
        // Mockito.when(mockFlightDAO.getAllFlights()).thenReturn(allFlightsReturned);
        Mockito.lenient().when(mockFlightDAO.getAllFlights()).thenReturn(allFlightsReturned);
        // Call the method and assert the result
        assertEquals(expectedFlight, flightService.updateFlight(801, f801));
    }
}