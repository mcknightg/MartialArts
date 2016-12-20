package com.bluntsoftware.app.modules.martialarts.rest;



import com.bluntsoftware.lib.jpa.repository.GenericRepository;
import com.bluntsoftware.app.modules.martialarts.domain.Address;
import com.bluntsoftware.app.modules.martialarts.repository.AddressRepository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
@Controller("martialartsAddressService")
@RequestMapping(value = "/martialarts/address")
@Transactional
@Qualifier("martialarts")

public class AddressService extends CustomService<Address,Integer, AddressRepository> {


}
