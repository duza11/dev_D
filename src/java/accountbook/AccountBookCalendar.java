package accountbook;

import java.util.ArrayList;
import java.util.List;

public class AccountBookCalendar {
    private int year;
    private int month;
    private int day;
    private List<DailyData> dayList;
    
    public void setYear(int year) {
        this.year = year;
    }
    
    public void setMonth(int month) {
        this.month = month;
    }
    
    public void setDay(int day) {
        this.day = day;
    }
    
    public void setDayList(List<DailyData> dayList) {
        this.dayList = dayList;
    }
    
    public int getYear() {
        return year;
    }
    
    public int getMonth() {
        return month;
    }
    
    public int getDay() {
        return day;
    }
    
    public List<DailyData> getDayList() {
        return dayList;
    }
}