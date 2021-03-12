package com.stackroute.keepnote.aspectj;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/* Annotate this class with @Aspect and @Component */
@Aspect
@Component
public class LoggingAspect {
	/*
	 * Write loggers for each of the methods of Note controller, any particular method
	 * will have all the four aspectJ annotation
	 * (@Before, @After, @AfterReturning, @AfterThrowing).
	 */
	private static final Logger logger = LoggerFactory.getLogger(LoggingAspect.class);
	@Before("execution (* com.stackroute.keepnote.controller.NoteController.*(..)) ")
	public void messagesbeforeservicemethods(JoinPoint joinPoint) {
		
		logger.info("Inside the Before method"+joinPoint.getSignature().getName());
	}

	@After("execution (* com.stackroute.keepnote.controller.NoteController.*(..)) ")
	public void messagesafterservicemethods(JoinPoint joinPoint) {
		logger.info("Inside the After method"+joinPoint.getSignature().getName());
	}

	@AfterReturning("execution (* com.stackroute.keepnote.controller.NoteController.*(..)) ")
	public void messagesafterreturningservicemethods(JoinPoint joinPoint) {
		logger.info("Inside the After returning method"+joinPoint.getSignature().getName());
	}

	@AfterThrowing("execution (* com.stackroute.keepnote.controller.NoteController.*(..)) ")
	public void messagesafterthrowingservicemethods(JoinPoint joinPoint) {
		logger.info("Inside the After throwing method"+joinPoint.getSignature().getName());
	}

}
