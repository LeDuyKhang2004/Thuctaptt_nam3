package com.javaweb.service;
import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import static org.junit.jupiter.api.Assertions.*;
class WorkdayCalculatorTest {
 @Test void removesWeekend(){assertEquals(5,WorkdayCalculator.count(LocalDate.of(2026,10,19),LocalDate.of(2026,10,25),d->false));}
 @Test void removesCustomHoliday(){assertEquals(4,WorkdayCalculator.count(LocalDate.of(2026,10,19),LocalDate.of(2026,10,25),d->d.equals(LocalDate.of(2026,10,20))));}
 @Test void rejectsReverseRange(){assertThrows(IllegalArgumentException.class,()->WorkdayCalculator.count(LocalDate.of(2026,10,20),LocalDate.of(2026,10,19),d->false));}
 @Test void aWeekendOnlyHasZeroDays(){assertEquals(0,WorkdayCalculator.count(LocalDate.of(2026,10,24),LocalDate.of(2026,10,25),d->false));}
}
