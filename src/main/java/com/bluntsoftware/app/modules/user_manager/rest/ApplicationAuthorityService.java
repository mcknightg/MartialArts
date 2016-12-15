package com.bluntsoftware.app.modules.user_manager.rest;



import com.bluntsoftware.lib.jpa.repository.GenericRepository;
import com.bluntsoftware.app.modules.user_manager.domain.ApplicationAuthority;
import com.bluntsoftware.app.modules.user_manager.repository.ApplicationAuthorityRepository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
@Controller("user_managerApplicationAuthorityService")
@RequestMapping(value = "/user_manager/applicationAuthority")
@Transactional
@Qualifier("user_manager")

public class ApplicationAuthorityService extends CustomService<ApplicationAuthority,Integer, ApplicationAuthorityRepository> {


}
