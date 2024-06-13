package accounts.web;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

@Component
@Aspect
public class AccountAspect {

  private final Counter counter;

  public AccountAspect(MeterRegistry registry) {
    this.counter = registry.counter("account.fetch", "type", "fromAspect");
  }

  @Before("execution(public * accounts.web.AccountController.accountSummary(..))")
  public void accountSummaryCount(JoinPoint joinPoint) {
    counter.increment();
  }
}
