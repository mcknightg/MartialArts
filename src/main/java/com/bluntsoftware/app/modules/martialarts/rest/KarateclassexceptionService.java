package com.bluntsoftware.app.modules.martialarts.rest;



import com.bluntsoftware.lib.jpa.repository.GenericRepository;
import com.bluntsoftware.app.modules.martialarts.domain.Karateclassexception;
import com.bluntsoftware.app.modules.martialarts.repository.KarateclassexceptionRepository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
@Controller("martialartsKarateclassexceptionService")
@RequestMapping(value = "/martialarts/karateclassexception")
@Transactional
@Qualifier("martialarts")

public class KarateclassexceptionService extends CustomService<Karateclassexception,Integer, KarateclassexceptionRepository> {


}
