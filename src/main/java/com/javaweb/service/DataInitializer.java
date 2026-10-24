package com.javaweb.service;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
@Component
public class DataInitializer {
 private final LeavePolicyService policy;
 public DataInitializer(LeavePolicyService policy){this.policy=policy;}
 @EventListener(ContextRefreshedEvent.class)
 public void ready(){policy.seedTypes();}
}
