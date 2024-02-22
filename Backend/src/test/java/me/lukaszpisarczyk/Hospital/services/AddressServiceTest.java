package me.lukaszpisarczyk.Hospital.services;

import me.lukaszpisarczyk.Hospital.dto.SignupDoctorRequest;
import me.lukaszpisarczyk.Hospital.dto.SignupRequest;
import me.lukaszpisarczyk.Hospital.models.Address;
import me.lukaszpisarczyk.Hospital.repositories.AddressRepository;
import me.lukaszpisarczyk.Hospital.services.implementation.AddressServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AddressServiceTest {

    @InjectMocks
    private AddressServiceImpl addressService;

    @Mock
    private AddressRepository addressRepository;

    @Test
    public void testSaveAddressWithSignupRequest() {
        SignupRequest signupRequest = new SignupRequest();
        signupRequest.setStreetAddress("123 Main St");
        signupRequest.setHouse("1A");
        signupRequest.setApartment("2B");
        signupRequest.setCity("New York");
        signupRequest.setPostalCode("12345");

        Address expectedAddress = new Address(
                signupRequest.getStreetAddress(),
                signupRequest.getHouse(),
                signupRequest.getApartment(),
                signupRequest.getCity(),
                signupRequest.getPostalCode()
        );

        when(addressRepository.save(any(Address.class))).thenReturn(expectedAddress);

        Address savedAddress = addressService.saveAddress(signupRequest);

        verify(addressRepository, times(1)).save(any(Address.class));
        assertEquals(expectedAddress, savedAddress);
    }

    @Test
    public void testSaveAddressWithSignupDoctorRequest() {
        SignupDoctorRequest signupDoctorRequest = new SignupDoctorRequest();
        signupDoctorRequest.setStreetAddress("456 Elm St");
        signupDoctorRequest.setHouse("2B");
        signupDoctorRequest.setApartment("3C");
        signupDoctorRequest.setCity("Los Angeles");
        signupDoctorRequest.setPostalCode("54321");

        Address expectedAddress = new Address(
                signupDoctorRequest.getStreetAddress(),
                signupDoctorRequest.getHouse(),
                signupDoctorRequest.getApartment(),
                signupDoctorRequest.getCity(),
                signupDoctorRequest.getPostalCode()
        );

        when(addressRepository.save(any(Address.class))).thenReturn(expectedAddress);

        Address savedAddress = addressService.saveAddress(signupDoctorRequest);

        verify(addressRepository, times(1)).save(any(Address.class));
        assertEquals(expectedAddress, savedAddress);
    }
}