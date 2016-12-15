package com.bluntsoftware.app.modules.user_manager.rest;



import com.bluntsoftware.lib.jpa.repository.GenericRepository;
import com.bluntsoftware.app.modules.user_manager.domain.ApplicationUser;
import com.bluntsoftware.app.modules.user_manager.repository.ApplicationUserRepository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
@Controller("user_managerApplicationUserService")
@RequestMapping(value = "/user_manager/applicationUser")
@Transactional
@Qualifier("user_manager")

public class ApplicationUserService extends CustomService<ApplicationUser,Integer, ApplicationUserRepository> {


}
