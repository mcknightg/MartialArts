package com.bluntsoftware.app.modules.martialarts.rest;



import com.bluntsoftware.lib.jpa.repository.GenericRepository;
import com.bluntsoftware.app.modules.martialarts.domain.Attendance;
import com.bluntsoftware.app.modules.martialarts.repository.AttendanceRepository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
@Controller("martialartsAttendanceService")
@RequestMapping(value = "/martialarts/attendance")
@Transactional
@Qualifier("martialarts")

public class AttendanceService extends CustomService<Attendance,Integer, AttendanceRepository> {


}
