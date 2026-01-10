package com.webstore.service;

import com.webstore.model.User;
import com.webstore.payload.AddressDTO;

import java.util.List;

public interface AddressService {

    AddressDTO createAddress(AddressDTO addressDTO, User user);

    List<AddressDTO> getAddresses();

    AddressDTO getAddressById(Long addressId);
}
