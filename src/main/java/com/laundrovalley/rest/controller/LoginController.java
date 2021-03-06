package com.laundrovalley.rest.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;


import org.springframework.web.bind.annotation.SessionAttributes;

import org.springframework.web.servlet.ModelAndView;

import com.laundrovalley.rest.model.Staff;
import com.laundrovalley.rest.model.Student;
import com.laundrovalley.rest.model.User;
import com.laundrovalley.rest.service.UserService;

@Controller
@SessionAttributes("stud")
public class LoginController {
	
	@Autowired
	UserService userService;
	
	
	@ModelAttribute("stud")
	   public Student getStudent() {
	      return new Student();
	   }
	
	
	@PostMapping("doLogin")

	public String doLogin(HttpSession session,@ModelAttribute User user,HttpServletRequest request,Model model) {
		
		if(user.getId().startsWith("S")) {
			Staff staff=new Staff();
			staff.setId(user.getId());
			staff.setPassword(user.getPassword());
			System.out.println(staff.getId());
			if(userService.loginStaff(staff)!=null) {
			
			request.setAttribute("mode", "MODE_STAFF_HOME");
			return "home-staff";
			}
			
			request.setAttribute("error", "Invalid Username password");
			request.setAttribute("mode", "MODE_LOGIN");
			return "home";
			
		}
	

		Student stud1=new Student();
		stud1.setId(user.getId());
		stud1.setPassword(user.getPassword());
		System.out.println(user.getId());
		Student student=userService.loginUser(stud1);
		System.out.println(student.getId());
		System.out.println(student);
		if(student.getId().isEmpty()) {
			
			request.setAttribute("error", "Invalid Username password");
			request.setAttribute("mode", "MODE_LOGIN");
			
		}
		
		else {
			stud1.setId(user.getId());
			stud1.setPassword(user.getPassword());
			model.addAttribute("stud",stud1);
			session.setAttribute("stud", stud1);
			request.setAttribute("mode", "MODE_HOME");
		}
		
		return "home";

		}

	
	
	@RequestMapping("login")
	public ModelAndView login() {
		
		ModelAndView mav = new ModelAndView();
		mav.addObject("mode", "MODE_LOGIN");
		mav.setViewName("index");
		return mav;
	}
	
	@RequestMapping("logout")
	public String logout(HttpSession session,HttpServletRequest request) {
		request.setAttribute("mode", "MODE_LOGIN");
		session.invalidate();
		return "index";
	}
}
			