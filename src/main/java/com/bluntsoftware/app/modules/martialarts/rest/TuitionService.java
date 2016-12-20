package com.bluntsoftware.app.modules.martialarts.rest;



import com.bluntsoftware.lib.jpa.repository.GenericRepository;
import com.bluntsoftware.app.modules.martialarts.domain.Tuition;
import com.bluntsoftware.app.modules.martialarts.repository.TuitionRepository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
@Controller("martialartsTuitionService")
@RequestMapping(value = "/martialarts/tuition")
@Transactional
@Qualifier("martialarts")

public class TuitionService extends CustomService<Tuition,Integer, TuitionRepository> {


}
