package com.bluntsoftware.app.modules.martialarts.rest;



import com.bluntsoftware.lib.jpa.repository.GenericRepository;
import com.bluntsoftware.app.modules.martialarts.domain.Student;
import com.bluntsoftware.app.modules.martialarts.repository.StudentRepository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
@Controller("martialartsStudentService")
@RequestMapping(value = "/martialarts/student")
@Transactional
@Qualifier("martialarts")

public class StudentService extends CustomService<Student,Integer, StudentRepository> {


}
