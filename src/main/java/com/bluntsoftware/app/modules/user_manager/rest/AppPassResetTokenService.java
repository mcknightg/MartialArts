package com.bluntsoftware.app.modules.user_manager.rest;



import com.bluntsoftware.lib.jpa.repository.GenericRepository;
import com.bluntsoftware.app.modules.user_manager.domain.AppPassResetToken;
import com.bluntsoftware.app.modules.user_manager.repository.AppPassResetTokenRepository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
@Controller("user_managerAppPassResetTokenService")
@RequestMapping(value = "/user_manager/appPassResetToken")
@Transactional
@Qualifier("user_manager")

public class AppPassResetTokenService extends CustomService<AppPassResetToken,Integer, AppPassResetTokenRepository> {


}
