/**
 * 
 */
package com.n26.assignment.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.n26.assignment.entity.OverallStatistics;
import com.n26.assignment.entity.Transaction;
import com.n26.assignment.services.AssignmentService;

/**
 * @author Soumya Mishra
 *
 */
@RestController
public class AssignmentController {

	@Autowired
	private AssignmentService service;

	@SuppressWarnings("rawtypes")
	@PostMapping(value = { "/transactions" })
	public ResponseEntity postTransaction(@RequestBody Transaction transaction) {
		if (null == transaction)
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		HttpStatus httpStatus = service.postTransaction(transaction);
		return new ResponseEntity<>(httpStatus);
	}

	@GetMapping(value = { "/statistics" })
	public ResponseEntity<OverallStatistics> getStatistics() {

		OverallStatistics stat = service.getOverallStatistics();
		return new ResponseEntity<OverallStatistics>(stat, HttpStatus.OK);
	}

}
