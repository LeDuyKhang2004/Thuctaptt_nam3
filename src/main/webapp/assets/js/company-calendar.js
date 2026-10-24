(function () {
  'use strict';
  const grid=document.getElementById('calendarGrid');
  if (!grid) return;
  let current=new Date(); current=new Date(current.getFullYear(),current.getMonth(),1);
  const holidays=Array.from(document.querySelectorAll('[data-holiday-date]'));
  const leaves=Array.from(document.querySelectorAll('[data-start][data-end]'));
  const pad=n=>String(n).padStart(2,'0');
  const iso=(year,month,day)=>year+'-'+pad(month+1)+'-'+pad(day);
  function render(){
    const year=current.getFullYear(),month=current.getMonth();
    document.getElementById('calendarLabel').textContent='Tháng '+(month+1)+' / '+year;
    grid.replaceChildren();
    ['CN','T2','T3','T4','T5','T6','T7'].forEach(label=>{
      const heading=document.createElement('strong');heading.textContent=label;grid.append(heading);
    });
    const offset=new Date(year,month,1).getDay();
    for(let i=0;i<offset;i++){const empty=document.createElement('div');empty.className='calendar-empty';grid.append(empty);}
    const total=new Date(year,month+1,0).getDate();
    for(let day=1;day<=total;day++){
      const date=iso(year,month,day),cell=document.createElement('div');cell.className='calendar-cell';
      const n=document.createElement('b');n.textContent=day;cell.append(n);
      holidays.forEach(h=>{
        if(h.dataset.holidayDate!==date)return;
        const tag=document.createElement('small');tag.className='company-day';
        tag.textContent='🏢 '+h.dataset.holidayName;cell.append(tag);
      });
      leaves.forEach(l=>{
        if(l.dataset.start<=date && date<=l.dataset.end){
          const tag=document.createElement('small');tag.className='employee-leave';
          tag.textContent=(l.dataset.person||'Nhân viên')+' (#'+l.dataset.id+')';cell.append(tag);
        }
      });
      grid.append(cell);
    }
  }
  window.moveMonth=function(n){current=new Date(current.getFullYear(),current.getMonth()+n,1);render();};
  window.goToday=function(){const d=new Date();current=new Date(d.getFullYear(),d.getMonth(),1);render();};
  render();
})();
