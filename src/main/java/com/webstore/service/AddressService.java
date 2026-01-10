package com.webstore.service;

import com.webstore.model.User;
import com.webstore.payload.AddressDTO;

public interface AddressService {

    AddressDTO createAddress(AddressDTO addressDTO, User user);
}
