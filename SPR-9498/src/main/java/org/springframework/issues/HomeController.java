package org.springframework.issues;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author Dave Syer
 */
@Controller
public class HomeController {

	private Foo context;

	public void setFoo(Foo context) {
		this.context = context;
	}

	@RequestMapping("/home")
	public String home(Model model) throws Exception {
		model.addAttribute("parameters", context.getAccessTokenRequest());
		return "home";
	}

}