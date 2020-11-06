package root.model;

public class Student implements Comparable<Student> {
    private String firstName;
    private String lastname;
    private int transcriptNumber;

    public Student(String firstName, String lastname, int transcriptNumber) {
        this.firstName = firstName;
        this.lastname = lastname;
        this.transcriptNumber = transcriptNumber;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastname() {
        return lastname;
    }

    public int getTranscriptNumber() {
        return transcriptNumber;
    }

    @Override
    public int compareTo(Student o) {
        int result;
        result = this.firstName.compareToIgnoreCase(o.firstName);
        if (result == 0) {
            result = this.lastname.compareToIgnoreCase(o.lastname);
            if (result == 0) {
                return this.transcriptNumber - o.transcriptNumber;
            } else {
                return result;
            }
        } else {
            return result;
        }
    }

    @Override
    public String toString() {
        return this.firstName + " " + this.lastname + " " + this.transcriptNumber;
    }
}
