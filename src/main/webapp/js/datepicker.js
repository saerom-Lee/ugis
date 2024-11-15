
class DatePicker
{
    constructor(elem) {
        this.root = elem;
        this.root.setAttribute('autocomplete', 'off');
        this.calernder = document.createElement('div');
        this.calernder.classList.add('calender');
        document.body.appendChild(this.calernder);
        let template = `
            <div class="sp-cal-title">
            <a><i class="fas fa-chevron-double-left"></i></a>
            <a><i class="fas fa-chevron-left"></i></a>
            <span class="cal-caption"></span>
            <a><i class="fas fa-chevron-right"></i></a>
            <a><i class="fas fa-chevron-double-right"></i></a>
            </div>
            <div class="sp-cal-label-days">
                <span>일</span>
                <span>월</span>
                <span>화</span>
                <span>수</span>
                <span>목</span>
                <span>금</span>
                <span>토</span>
            </div>
            <div class="sp-cal-days"></div>
        `;
        this.calernder.innerHTML = template;
        this.daysDiv = this.calernder.getElementsByClassName('sp-cal-days')[0];
        this.daysList = [];
        this.isCalenderVisible = false;
        this.mouseX = 0;
        this.mouseY = 0;
        let today = new Date();
        this.month = today.getMonth();
        this.year = today.getFullYear();
        this.markedDay = 0;
        this.markedMonth = 0;
        this.markedYear = 0;
        this.root.addEventListener('focus' , e => {
            this.showCalender();
        });
        document.addEventListener('click', e => {
            if(this.isCalenderVisible) {
                this.mouseX = e.clientX;
                this.mouseY = e.clientY;
                let rect = this.calernder.getBoundingClientRect();
                if(this.mouseX > rect.left && this.mouseX < rect.right)
                    if(this.mouseY > rect.top && this.mouseY < rect.bottom)
                        return;
                this.hideCalender();
            }
        });
        this.daysDiv.addEventListener('click', e => {
            let bRect = this.daysDiv.getBoundingClientRect();
            let nRows = Math.ceil(this.daysList.length / 7);
            let dHeight = bRect.height/ nRows;
            let dWidth = bRect.width / 7;
            let yIndex = Math.floor( (e.clientY - bRect.top)/dHeight);
            let xIndex = Math.floor( (e.clientX - bRect.left)/dWidth);
            let day = this.daysList[yIndex * 7 + xIndex];
            if(day) {
                this.markedDay = day;
                this.markedMonth = this.month;
                this.markedYear = this.year;
                let d = String(day);
                let m = String(this.month + 1);
                let y = String(this.year);
                if ( d.length  == 1) d = '0' + d;
                if  (m.length == 1) m = '0' + m;
                this.root.value = `${y}-${m}-${d}`;
                this.hideCalender();
            }
        });
        let capBtns = this.calernder.getElementsByClassName('sp-cal-title')[0];
        capBtns = capBtns.getElementsByTagName('a');
        capBtns[0].addEventListener('click', e=> {
            this.year--;
            this.renderCalender();
        })
        capBtns[1].addEventListener('click', e=> {
            this.month--;
            if(this.month < 0){
                this.month = 11;
                this.year --;
            }
            this.renderCalender();
        })
        capBtns[2].addEventListener('click', e=> {
            this.month++;
            if(this.month > 11)
            {
                this.month = 0;
                this.year++;
            }
            this.renderCalender();
        })
        capBtns[3].addEventListener('click', e=> {
            this.year++;
            this.renderCalender();
        })
    }

    showCalender() {
        setTimeout(() => {
            this.isCalenderVisible = true;
        }, 200);
        this.calernder.style.visibility =  'visible';
        let {height, top, left} = this.root.getBoundingClientRect();
        let cRect = this.calernder.getBoundingClientRect();
        let y = top +  pageYOffset;
        let x = left + pageXOffset;
        let offset = 4 + height;

        if( top + cRect.height + height > window.innerHeight ) {
            offset = - 4 - cRect.height;
        }
        this.calernder.style.top = (y + offset) + 'px';
        this.calernder.style.left = x + 'px';
        this.renderCalender();
    }
    hideCalender() {
        this.isCalenderVisible = false;
        this.calernder.style.visibility = 'hidden';
    }

    renderCalender()
    {
        let cap = this.calernder.getElementsByClassName('cal-caption')[0];
        let months =[
            '1월',
            '2월',
            '3월',
            '4월',
            '5월',
            '6월',
            '7월',
            '8월',
            '9월',
            '10월',
            '11월',
            '12월'
        ];
        cap.innerText = `${months[this.month]}, ${this.year}`
        let today = new Date();
        let nDays = new Date(this.year, this.month + 1, 0).getDate();
        let startDay = new Date(this.year, this.month, 1).getDay();
        this.daysList = [];
        for(let i = 0; i < startDay - 1; i++)
        {
            this.daysList.push(0);
        }
        for(let i = 0; i <= nDays; i++)
        {
            this.daysList.push(i);
        }
        this.daysDiv.innerHTML = '';
        for(let d of this.daysList) {
            let b = document.createElement('a');
            if(this.month == today.getMonth() && d == today.getDate() &&this.year == today.getFullYear())
            {
                b.style.backgroundColor = '#ddd';
            }
            if( this.markedDay > 0 )
            {
                if(this.markedYear == this.year && this.markedMonth == this.month && d == this.markedDay)
                {

                    b.style.backgroundColor = '#2979ff';
                    b.style.color = '#fff';
                }
            }
            b.innerText = d === 0 ? '' : `${d}`;
            this.daysDiv.appendChild(b);
        }

    }
}