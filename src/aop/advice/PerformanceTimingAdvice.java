package aop.advice;

import org.aspectj.lang.ProceedingJoinPoint;

public class PerformanceTimingAdvice {

	//For Around type advice
	public Object recordTiming(ProceedingJoinPoint jp) throws Throwable{
		double timeNow = System.nanoTime();
		
			try {
				Object returnValue = jp.proceed();
				return returnValue;
			}
	
			finally {
				double timeAfter = System.nanoTime();
				
				double timeTaken = timeAfter - timeNow;
				double timeInMilliSeconds = timeTaken/1000000;
				System.out.println("Time taken by the method " + jp.getSignature().getName() + " is " + timeInMilliSeconds);
			}
		
	}
	
}
