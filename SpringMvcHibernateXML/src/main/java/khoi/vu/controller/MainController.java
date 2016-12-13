package khoi.vu.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class MainController {

	@RequestMapping(value = { "/" }, method = RequestMethod.GET)
	public String LoginPage() {
		return "login";
	}

	@RequestMapping(value = "/login", method = RequestMethod.GET)
	public ModelAndView login(@RequestParam(value = "error", required = false) String error,
			@RequestParam(value = "logout", required = false) String logout) {

		ModelAndView model = new ModelAndView();
		if (error != null) {
			model.addObject("error", "Invalid username and password!");
		}

		if (logout != null) {
			model.addObject("msg", "You've been logged out successfully.");
		}
		model.setViewName("login");

		return model;

	}
	// @RequestMapping(value = "/login", method = RequestMethod.GET)
	// public ModelAndView login(@RequestParam(value = "error", required =
	// false) String error,
	// @RequestParam(value = "logout", required = false) String logout) {
	//
	// ModelAndView model = new ModelAndView();
	// if (error != null) {
	// model.addObject("error", "Invalid username and password!");
	// }
	//
	// if (logout != null) {
	// model.addObject("msg", "You've been logged out successfully.");
	// }
	// model.setViewName("login");
	//
	// return model;
	//
	// }

	@RequestMapping(value = { "/home" }, method = RequestMethod.GET)
	public String homePage() {
		return "home";
	}

	@RequestMapping(value = { "/admin/dashboard" }, method = RequestMethod.GET)
	public String dashBoardPage() {
		return "home";
	}
}
