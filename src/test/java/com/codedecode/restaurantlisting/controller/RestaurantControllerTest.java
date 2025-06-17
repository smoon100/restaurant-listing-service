package com.codedecode.restaurantlisting.controller;

import com.codedecode.restaurantlisting.dto.RestaurantDTO;
import com.codedecode.restaurantlisting.service.RestaurantService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class RestaurantControllerTest {

    @InjectMocks
    RestaurantController restaurantController;

    @Mock
    RestaurantService restaurantService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this); // Initialize mocks before each test
    }


    @Test
    public void testGetAllRestaurants() {

        List<RestaurantDTO> mockRestaurants = Arrays.asList(
                new RestaurantDTO(1, "Restaurant 1", "Address 1", "City 1", "Description 1"),
                new RestaurantDTO( 2, "Restaurant 2", "Address 2", "City 2", "Description 2")
        );

        when(restaurantService.findAllRestaurants()).thenReturn(mockRestaurants);

        // Call the controller method
        ResponseEntity<List<RestaurantDTO>> response = restaurantController.getAllRestaurants();

        // Verify the response
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(mockRestaurants, response.getBody());

        // verify that the service method was called
        verify(restaurantService, times(1)).findAllRestaurants();

    }

    @Test
    public void testSaveRestaurant() {
        // arrange
        // create a mock restaurantDTO to be saved
        RestaurantDTO mockRestaurantDTO = new RestaurantDTO(1, "Restaurant 1", "Address 1", "City 1", "Description 1");

        // mock the service behavior
        when(restaurantService.addRestaurantInDB(mockRestaurantDTO)).thenReturn(mockRestaurantDTO);

        //act
        ResponseEntity<RestaurantDTO> response = restaurantController.saveRestaurant(mockRestaurantDTO);

        //assert
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(mockRestaurantDTO, response.getBody());

        verify(restaurantService, times(1)).addRestaurantInDB(mockRestaurantDTO);


    }

    @Test
    public void testFetchRestaurantById() {
        // Arrange
        Integer mockId = 1;
        RestaurantDTO mockRestaurant = new RestaurantDTO(1, "Restaurant 1", "Address 1", "City 1", "Description 1");
        when(restaurantService.fetchRestaurantById(mockId)).thenReturn(new ResponseEntity<>(mockRestaurant, HttpStatus.OK));

        // Act
        ResponseEntity<RestaurantDTO> response = restaurantController.fetchRestaurantById(mockId);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(mockRestaurant, response.getBody());

        verify(restaurantService, times(1)).fetchRestaurantById(mockId);
    }

}
