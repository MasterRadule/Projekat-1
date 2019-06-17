package isamrs.tim1.controller;

import java.util.Map;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class SystemAdminPageController {
	@PreAuthorize("hasRole('SYSADMIN')")
	@GetMapping("/sysAdmin")
	public String showAirlineAdminPage(Map<String, Object> model){
		return "sysAdmin/index.html";
	}
	
}