package com.example.demo.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

//import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.model.Customer;
import com.example.demo.model.CustomerDto;
import com.example.demo.BO.BOInterface;
import com.example.demo.HealthCheck.HealthCheck;
import com.example.demo.ServiceMapper.IServiceMapper;


@RestController
@RequestMapping("/backend")
public class Controller {
	
	Logger logger = LoggerFactory.getLogger(Controller.class);
	
	@Autowired
	private BOInterface bo;
	
	@Autowired
	private IServiceMapper servicemapper;
	
	@RequestMapping("/test")
	public ResponseEntity<String> test() {
		return new  ResponseEntity<String>("Yes its working!",HttpStatus.OK);
	}
	
	@PostMapping("/customer")
	public ResponseEntity<Customer> postCustomer(@Valid @RequestBody Customer customer){
		CustomerDto dto = servicemapper.customerToDTO(customer);
		bo.Add_Customer(dto);
		logger.info("Data sent to BO Layer!");
		return new ResponseEntity<Customer>(customer ,HttpStatus.OK);
	}
	
	@GetMapping("/customers")
	public ResponseEntity<List<Customer>> getAllCustomers(){
		List<CustomerDto> customers = bo.Get_Customer();
		logger.info("Received Data from BO");
		if(customers!=null && !customers.isEmpty())
			return new ResponseEntity<List<Customer>>(servicemapper.DTOTListoCustomerList(customers),HttpStatus.OK);
			else
			return new ResponseEntity("No Customers present in Database",HttpStatus.NOT_FOUND);
	}
	
	@GetMapping("/healthcheck")
	public ResponseEntity<List<HealthCheck>> checkHealth(){	
		ResponseEntity<List<HealthCheck>> value = null;
		List<HealthCheck> result = bo.HealthCheck();
		if(result!=null)
			value = new ResponseEntity<List<HealthCheck>>(result,HttpStatus.OK);
		return value;
	}

	
}
