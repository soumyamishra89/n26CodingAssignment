package com.n26.assignment;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
public class AssignmentApplicationTests {

	private final Logger logger = LoggerFactory.getLogger(AssignmentApplicationTests.class);

	@Autowired
	private MockMvc mvc;

	/**
	 * Tests if the transaction is not created as the timestamp is outside the time
	 * range
	 * 
	 * @throws Exception
	 */
	@Test
	public void testTransactionNoContentWhenTimeIsNotValid() throws Exception {
		logger.info("testTransactionNoContent: Start");
		mvc.perform(MockMvcRequestBuilders.post("/transactions")//
				.contentType("application/json")//
				.content(//
						"{\"amount\": 12.3," + //
								"\"timestamp\": 16342684532000}"//
		))//
				.andExpect(MockMvcResultMatchers.status().is(204));
		logger.info("testTransactionNoContent: End");
	}

	@Test
	public void testTransactionNoContentWhenBodyIsEmpty() throws Exception {
		logger.info("testTransactionNoContent: Start");
		mvc.perform(MockMvcRequestBuilders.post("/transactions")//
				.contentType("application/json")//
				.content("{}"))//
				.andExpect(MockMvcResultMatchers.status().is(204));
		logger.info("testTransactionNoContent: End");
	}

	/**
	 * Tests if the transaction is successfully created
	 * 
	 * @throws Exception
	 */
	@Test
	public void testTransactionCreated() throws Exception {
		logger.info("testTransactionCreated: Start");
		mvc.perform(MockMvcRequestBuilders.post("/transactions")//
				.contentType("application/json")//
				.content(//
						"{\"amount\": 20.4," + //
								"\"timestamp\": " + //
								System.currentTimeMillis() + //
								"}"))
				.andExpect(MockMvcResultMatchers.status().is(201));
		logger.info("testTransactionCreated: End");
	}

	@Test
	public void testOverallStatistics() throws Exception {
		logger.info("testOverallStatistics: Start");
		mvc.perform(MockMvcRequestBuilders.post("/transactions")//
				.contentType("application/json")//
				.content(//
						"{\"amount\": 20.4," + //
								"\"timestamp\": " + //
								System.currentTimeMillis() + //
								"}"))
				.andExpect(MockMvcResultMatchers.status().is(201));
		mvc.perform(MockMvcRequestBuilders.post("/transactions")//
				.contentType("application/json")//
				.content(//
						"{\"amount\": 110.9," + //
								"\"timestamp\": " + //
								System.currentTimeMillis() + //
								"}"))
				.andExpect(MockMvcResultMatchers.status().is(201));
		Thread.sleep(2000);
		mvc.perform(MockMvcRequestBuilders.post("/transactions")//
				.contentType("application/json")//
				.content(//
						"{\"amount\": 65.1," + //
								"\"timestamp\": " + //
								System.currentTimeMillis() + //
								"}"))
				.andExpect(MockMvcResultMatchers.status().is(201));

		// this transaction should not persist
		mvc.perform(MockMvcRequestBuilders.post("/transactions")//
				.contentType("application/json")//
				.content(//
						"{\"amount\": 10.3," + //
								"\"timestamp\": " + //
								(System.currentTimeMillis() - 61000l) + //
								"}"))
				.andExpect(MockMvcResultMatchers.status().is(204));
		Thread.sleep(5000);

		mvc.perform(MockMvcRequestBuilders.get("/statistics"))//
				.andExpect(MockMvcResultMatchers.status().is(200))//
				.andExpect(MockMvcResultMatchers.content().json(//
						"{" + //
								"\"sum\": 196.4," + //
								"\"avg\": 65.47," + //
								"\"max\": 110.9," + //
								"\"min\": 20.4," + //
								"\"count\": 3" + //
								"}"));

		logger.info("testOverallStatistics: End");
	}
}
