package com.bluntsoftware.app.modules.martialarts.rest;



import com.bluntsoftware.lib.jpa.repository.GenericRepository;
import com.bluntsoftware.app.modules.martialarts.domain.School;
import com.bluntsoftware.app.modules.martialarts.repository.SchoolRepository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
@Controller("martialartsSchoolService")
@RequestMapping(value = "/martialarts/school")
@Transactional
@Qualifier("martialarts")

public class SchoolService extends CustomService<School,Integer, SchoolRepository> {


}
