package com.javaweb.service;
import java.time.*;
import java.util.function.Predicate;

/** Pure calculation logic; testable without database or web server. */
public final class WorkdayCalculator {
 private WorkdayCalculator(){}
 public static int count(LocalDate from,LocalDate to,Predicate<LocalDate> isHoliday){
  if(from==null||to==null||to.isBefore(from)||from.plusDays(366).isBefore(to))throw new IllegalArgumentException("Khoảng ngày không hợp lệ hoặc quá 366 ngày");
  int n=0;for(LocalDate d=from;!d.isAfter(to);d=d.plusDays(1))
    if(d.getDayOfWeek()!=DayOfWeek.SATURDAY&&d.getDayOfWeek()!=DayOfWeek.SUNDAY&&!isHoliday.test(d))n++;
  return n;
 }
}
