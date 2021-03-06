package com.example.crud.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.example.crud.bean.StudentBean;
import com.example.crud.bean.PageCountBean;
import com.example.crud.service.UserServiceIMPL;


/*
 * Controller acts as bridge between database/bean and webpage, It accepts the url and connect with repective function with the help of RequestMapping annotation.
 */
@RestController
public class UserController {
	
	@Autowired
	UserServiceIMPL service;
	
	PageCountBean pcb = new PageCountBean();
	@RequestMapping(value="/login", method=RequestMethod.GET)
	public ModelAndView login() {
		return new ModelAndView("demohome");
	}
	
	@RequestMapping(value="/demostudentdetail",method=RequestMethod.POST)
	public ModelAndView changeCount(@ModelAttribute("pcb") PageCountBean page) {
		pcb.setCount(page.getCount());
		return studentreport(0);
	}
	
	public int getPageCount() {
		return pcb.getCount();
	}
	
	@RequestMapping(value="/demostudentdetails{no}",method= {RequestMethod.GET,RequestMethod.POST})
	public ModelAndView studentreport(@PathVariable("no") int no) {
		ModelAndView modelandview = new ModelAndView();
		List<StudentBean> sbean = service.findAll(no,getPageCount());
		int totalpages = service.findAll().size()/getPageCount();
		modelandview.addObject("pcb", new PageCountBean());
		modelandview.addObject("stubean",sbean);
		modelandview.addObject("total", totalpages);
		modelandview.setViewName("demoStudentReport");
		return modelandview;
	}
	
	@RequestMapping(value="/democreatestudent",method=RequestMethod.GET)
	public ModelAndView studentCreation() {
		return new ModelAndView("demoCreationTab","studentbean",new StudentBean());
	}
	
	// Enrollment form validation
	@RequestMapping(value="/demosuccess",method=RequestMethod.POST)
	public ModelAndView successfulCreation(@Valid @ModelAttribute("studentbean") StudentBean sbean ,BindingResult b) throws Exception {
		ModelAndView modelandview = new ModelAndView();
		if(b.hasErrors()) {
			modelandview.setViewName("demoCreationTab");
		}
		else {
		String s = service.save(sbean);
		modelandview.addObject("message",s);
		modelandview.setViewName("demoSuccessPage");
		}
		return modelandview;
	}
	
	@RequestMapping(value="/demodeletestudent{Id}",method=RequestMethod.GET)
	public ModelAndView deleteStudent(@PathVariable("Id") int studentID)
	{
		String s = service.delete(studentID);
		return new ModelAndView("demoDelete","message",s);
	}
	
	@RequestMapping(value="/demoeditstudent{Id}",method=RequestMethod.GET)
	public ModelAndView editStudent(@PathVariable("Id") int studentID)
	{
		ModelAndView modelandview = new ModelAndView();
		List<StudentBean> sbean = service.update(studentID);
		for(StudentBean sBean:sbean){
			modelandview.setViewName("demoUpdate");
			modelandview.addObject("studentbean",sBean);
		}
		modelandview.addObject("stbean",sbean);
		return modelandview;
	}
	
	@RequestMapping(value="/demoedited",method=RequestMethod.POST)
	public ModelAndView editedsuccessful(@Valid @ModelAttribute("studentbean") StudentBean sbean ,BindingResult b) throws Exception {
		ModelAndView modelandview = new ModelAndView();
		int id = service.getId();
		String s = service.edited(sbean,id);
		modelandview.addObject("message",s);
		modelandview.setViewName("demoSuccessPage");
		return modelandview;
	}
	
	@RequestMapping(value="/demosearch{s}",method=RequestMethod.GET)
	public ModelAndView findStudent(@PathVariable("s") String value) {
		ModelAndView mv = new ModelAndView();
		List<StudentBean> sbean = service.search(value);
		mv.addObject("pcb", new PageCountBean());
		mv.addObject("stubean",sbean);
		mv.setViewName("demoStudentReport");
		return mv;
	}

}
