package com.glovoapp.backender;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@Controller
@ComponentScan("com.glovoapp.backender")
@EnableAutoConfiguration
class API implements ErrorController{
	private final String welcomeMessage;
	private final OrderRepository orderRepository;
	private final OrdersAssignment ordersAssignment;
	
	private final static String ERROR_PATH = "/error";

	@Autowired
	API(@Value("${backender.welcome_message}") String welcomeMessage, OrderRepository orderRepository,
			OrdersAssignment ordersAssignment) {
		this.welcomeMessage = welcomeMessage;
		this.orderRepository = orderRepository;
		this.ordersAssignment = ordersAssignment;
	}

	@RequestMapping("/")
	@ResponseBody
	String root() {
		return welcomeMessage;
	}

	@RequestMapping("/orders")
	@ResponseBody
	List<OrderVM> orders() {
		return orderRepository.findAll().stream().map(order -> new OrderVM(order.getId(), order.getDescription()))
				.collect(Collectors.toList());
	}

	public static void main(String[] args) {
		SpringApplication.run(API.class);
	}

	@GetMapping("/orders/{courierId}")
	@ResponseBody
	List<OrderVM> ordersForGivenCOurier(@PathVariable String courierId){
		try {
		return ordersAssignment.getOrders(orderRepository, courierId);
		}catch(Exception e) {
			return null;
		}
		
	}
	
	@ControllerAdvice
	public class RestResponseEntityExceptionHandler 
	  extends ResponseEntityExceptionHandler {
	 
	    @ExceptionHandler(value 
	      = { IllegalArgumentException.class, IllegalStateException.class })
	    protected ResponseEntity<Object> handleConflict(
	      RuntimeException ex, WebRequest request) {
	        String bodyOfResponse = "Some problem in fetching orders for given Courier ID";
	        return handleExceptionInternal(ex, bodyOfResponse, 
	          new HttpHeaders(), HttpStatus.CONFLICT, request);
	    }
	}
	
	@RequestMapping(value = ERROR_PATH)
    public ModelAndView errorHtml() {
        return new ModelAndView("Could not fetch any orders for given Courier ID");
    }

	@Override
	public String getErrorPath() {
		return ERROR_PATH;
	}
}
