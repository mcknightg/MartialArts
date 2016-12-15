package com.bluntsoftware.app.modules.martialarts.rest;



import com.bluntsoftware.lib.jpa.repository.GenericRepository;
import com.bluntsoftware.app.modules.martialarts.domain.Karateclass;
import com.bluntsoftware.app.modules.martialarts.repository.KarateclassRepository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
@Controller("martialartsKarateclassService")
@RequestMapping(value = "/martialarts/karateclass")
@Transactional
@Qualifier("martialarts")

public class KarateclassService extends CustomService<Karateclass,Integer, KarateclassRepository> {


}
