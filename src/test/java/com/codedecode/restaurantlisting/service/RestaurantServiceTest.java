package com.codedecode.restaurantlisting.service;

import com.codedecode.restaurantlisting.dto.RestaurantDTO;
import com.codedecode.restaurantlisting.entity.Restaurant;
import com.codedecode.restaurantlisting.mapper.RestaurantMapper;
import com.codedecode.restaurantlisting.repository.RestaurantRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class RestaurantServiceTest {

    @InjectMocks
    RestaurantService restaurantService;

    @Mock
    RestaurantRepo restaurantRepo;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this); // Initialize mocks before each test
    }

    @Test
    public void testfindAllRestaurants() {
        // Arrange
        List<Restaurant> mockRestaurants = Arrays.asList(
                new Restaurant(1, "Restaurant 1", "Address 1", "City 1", "Description 1"),
                new Restaurant(2, "Restaurant 2", "Address 2", "City 2", "Description 2")
        );

        when(restaurantRepo.findAll()).thenReturn(mockRestaurants);

        // Act
        List<RestaurantDTO> restaurantDTOList = restaurantService.findAllRestaurants();

        // Assert
        assertEquals(mockRestaurants.size(), restaurantDTOList.size());

        for (int i = 0; i < mockRestaurants.size(); i++) {
            RestaurantDTO expectedRestaurantDTO = RestaurantMapper.INSTANCE.mapToRestaurantToRestaurantDTO(mockRestaurants.get(i));
            assertEquals(expectedRestaurantDTO, restaurantDTOList.get(i));
        }

        verify(restaurantRepo, times(1)).findAll();
    }

    @Test
    public void testaddRestaurantInDB() {
        // Arrange
//        Restaurant mockRestaurant = new Restaurant(1, "Restaurant 1", "Address 1", "City 1", "Description 1");
        RestaurantDTO mockRestaurantDTO = new RestaurantDTO(1, "Restaurant 1", "Address 1", "City 1", "Description 1");
        Restaurant mockRestaurant = RestaurantMapper.INSTANCE.mapToRestaurantDTOToRestaurant(mockRestaurantDTO);

        when(restaurantRepo.save(mockRestaurant)).thenReturn(mockRestaurant);

        //act

        RestaurantDTO actualRestaurantDTO = restaurantService.addRestaurantInDB(mockRestaurantDTO);
       // RestaurantDTO expectedRestaurantDTO = RestaurantMapper.INSTANCE.mapToRestaurantToRestaurantDTO(mockRestaurant);
        //assert
        assertEquals(mockRestaurantDTO, actualRestaurantDTO);
        verify(restaurantRepo, times(1)).save(mockRestaurant);
    }

    @Test
    public void testfetchRestaurantById_ExistingId() {
        // Arrange
        Integer mockId = 1;
        Restaurant mockRestaurant = new Restaurant(1, "Restaurant 1", "Address 1", "City 1", "Description 1");

        when(restaurantRepo.findById(mockId)).thenReturn(Optional.of(mockRestaurant));

        // Act
        ResponseEntity<RestaurantDTO> response = restaurantService.fetchRestaurantById(mockId);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(mockId, response.getBody().getId());

        verify(restaurantRepo, times(1)).findById(mockId);
    }


    @Test
    public void testfetchRestaurantById_NonexistentId() {
        // Arrange
        Integer mockId = 1;

        when(restaurantRepo.findById(mockId)).thenReturn(Optional.empty());

        // Act
        ResponseEntity<RestaurantDTO> response = restaurantService.fetchRestaurantById(mockId);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals(null, response.getBody());


        verify(restaurantRepo, times(1)).findById(mockId);
    }
}
