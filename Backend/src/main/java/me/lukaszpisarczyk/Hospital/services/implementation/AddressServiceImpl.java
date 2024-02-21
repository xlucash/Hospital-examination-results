package me.lukaszpisarczyk.Hospital.services.implementation;

import me.lukaszpisarczyk.Hospital.dto.SignupDoctorRequest;
import me.lukaszpisarczyk.Hospital.dto.SignupRequest;
import me.lukaszpisarczyk.Hospital.models.Address;
import me.lukaszpisarczyk.Hospital.repositories.AddressRepository;
import me.lukaszpisarczyk.Hospital.services.AddressService;
import org.springframework.stereotype.Service;

@Service
public class AddressServiceImpl implements AddressService {
    private final AddressRepository addressRepository;

    public AddressServiceImpl(AddressRepository addressRepository) {
        this.addressRepository = addressRepository;
    }

    @Override
    public Address saveAddress(SignupRequest signupRequest) {
        return saveAddressInternal(
                signupRequest.getStreetAddress(),
                signupRequest.getHouse(),
                signupRequest.getApartment(),
                signupRequest.getCity(),
                signupRequest.getPostalCode());
    }

    @Override
    public Address saveAddress(SignupDoctorRequest signupRequest) {
        return saveAddressInternal(
                signupRequest.getStreetAddress(),
                signupRequest.getHouse(),
                signupRequest.getApartment(),
                signupRequest.getCity(),
                signupRequest.getPostalCode());
    }

    private Address saveAddressInternal(String streetAddress, String house, String apartment, String city, String postalCode) {
        Address address = new Address(streetAddress, house, apartment, city, postalCode);
        return addressRepository.save(address);
    }
}
