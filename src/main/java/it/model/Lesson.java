package it.model;

import java.time.LocalDate;
import java.util.Map;

public class Lesson implements Comparable<Lesson> {
    private String subject;
    private LocalDate date;
    private Map<Student, Boolean> attendanceList;

    public Lesson(String subject, LocalDate date, Map<Student, Boolean> attendanceList) {
        this.subject = subject;
        this.date = date;
        this.attendanceList = attendanceList;
    }

    public String getSubject() {
        return subject;
    }

    public LocalDate getDate() {
        return date;
    }

    public Map<Student, Boolean> getAttendanceList() {
        return attendanceList;
    }

    @Override
    public int compareTo(Lesson o) {
        int result=(this.date.compareTo(o.date))*(-1);
        if (result==0){
            return this.subject.compareToIgnoreCase(o.subject);
        }else {
            return result;
        }
    }

    @Override
    public String toString() {
        return "Temat zajęć: " + this.subject + ", Data: " + this.date + "\n";
    }
}
