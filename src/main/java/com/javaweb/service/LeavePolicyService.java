package com.javaweb.service;
import com.javaweb.entity.*;
import com.javaweb.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.*;
import java.util.*;

@Service
public class LeavePolicyService {
 private final LeaveTypeRepository types;
 private final HolidayRepository holidays;
 private final LeaveBalanceRepository balances;
 private final LeaveRequestRepository leaves;
 public LeavePolicyService(LeaveTypeRepository t,HolidayRepository h,LeaveBalanceRepository b,LeaveRequestRepository l){types=t;holidays=h;balances=b;leaves=l;}
 @Transactional
 public void seedTypes(){
  seed("ANNUAL","Nghỉ phép năm",true);
  seed("SICK","Nghỉ ốm",false);
  seed("UNPAID","Nghỉ không lương",false);
  seed("PERSONAL","Nghỉ việc riêng",false);
 }
 private void seed(String code,String name,boolean deduct){if(types.findByCode(code)==null){LeaveTypeEntity t=new LeaveTypeEntity();t.setCode(code);t.setName(name);t.setDeductBalance(deduct);types.save(t);}}
 public List<LeaveTypeEntity> types(){return types.findAll();}
 public LeaveTypeEntity type(Long id){return types.findById(id).orElseThrow(()->new IllegalArgumentException("Loại phép không hợp lệ"));}
 public int workingDays(LocalDate start,LocalDate end){
  return WorkdayCalculator.count(start,end,holidays::existsByDate);
 }

 @Transactional
 public LeaveBalanceEntity balance(UserEntity user,int year){
  LeaveBalanceEntity b=balances.findByUserIdAndYear(user.getId(),year);
  if(b==null){b=new LeaveBalanceEntity();b.setUser(user);b.setYear(year);b.setAllocatedDays(12);b=balances.save(b);}return b;
 }
 public int reservedAnnualDays(UserEntity user,int year){
  return leaves.findByUserIdAndStatusIn(user.getId(),List.of("PENDING","MANAGER_PENDING","APPROVED")).stream()
   .filter(l->(l.getLeaveType()==null || l.getLeaveType().isDeductBalance())&&l.getStartDate()!=null&&l.getStartDate().getYear()==year)
   .mapToInt(l->l.getWorkDays()==null?workingDays(l.getStartDate(),l.getEndDate()):l.getWorkDays()).sum();
 }
 @Transactional public int remaining(UserEntity user,int year){return balance(user,year).getAllocatedDays()-reservedAnnualDays(user,year);}
 @Transactional public void allocate(UserEntity user,int year,int days){if(days<0||days>365)throw new IllegalArgumentException("Ngày phép cấp phải trong khoảng 0–365");LeaveBalanceEntity b=balance(user,year);b.setAllocatedDays(days);balances.save(b);}
 public List<HolidayEntity> holidays(){return holidays.findAll(org.springframework.data.domain.Sort.by("date"));}
 @Transactional public void deleteHoliday(Long id){holidays.deleteById(id);}
 @Transactional public void addHoliday(LocalDate date,String name){if(date==null||name==null||name.isBlank())throw new IllegalArgumentException("Ngày lễ hoặc tên không hợp lệ");if(!holidays.existsByDate(date)){HolidayEntity h=new HolidayEntity();h.setDate(date);h.setName(name.trim());holidays.save(h);}}
}
