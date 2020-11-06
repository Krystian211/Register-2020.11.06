package root.database;

import io.bretty.console.table.Alignment;
import io.bretty.console.table.ColumnFormatter;
import io.bretty.console.table.Precision;
import io.bretty.console.table.Table;
import org.apache.commons.codec.digest.DigestUtils;
import root.model.Lesson;
import root.model.Student;
import root.model.User;

import java.time.LocalDate;
import java.util.*;

public class RegisterRepositoryImpl implements IRegisterRepository {
    private List<User> usersList = new ArrayList<>();
    private List<Student> studentsList = new ArrayList<>();
    private List<Lesson> lessonsList = new ArrayList<>();
    private Lesson presentLesson;

    private RegisterRepositoryImpl() {
        addDefaultUser();
        addDefaultStudents();
        addDefaultLessons();
    }

    private void addDefaultUser() {
        addNewUser("admin", "admin");
    }

    private void addDefaultStudents() {
        addNewStudent("Adam", "Ziencikiewicz", 25023);
        addNewStudent("Zuzanna", "Bryk", 25030);
        addNewStudent("Marian", "Udar", 25033);
        addNewStudent("Joanna", "Kruk", 25050);
    }

    private void addDefaultLessons() {
        Map<Student, Boolean> exampleAttendanceList = new TreeMap<>();

        for (Student student : studentsList) {
            exampleAttendanceList.put(student, true);
        }
        //Wprowadzenie jednej nieobecnosci
        exampleAttendanceList.put(new Student("Zuzanna", "Bryk", 25030), false);

        lessonsList.add(new Lesson("Polimorfizm",
                LocalDate.of(2020, 10, 31),
                exampleAttendanceList));

        for (Student student : studentsList) {
            exampleAttendanceList.put(student, true);
        }

        lessonsList.add(new Lesson("Typy generyczne",
                LocalDate.of(2020, 11, 5),
                exampleAttendanceList));

        Collections.sort(lessonsList);
    }

    public static RegisterRepositoryImpl getInstance() {
        return SingletonHolder.INSTANCE;
    }

    @Override
    public boolean authenticate(String username, String password) {
        for (User user : usersList) {
            if (user.getUsername().equalsIgnoreCase(username)) {
                if (user.getPassword().equals(DigestUtils.md5Hex(password))) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public void addNewUser(String username, String password) {
        this.usersList.add(new User(username, DigestUtils.md5Hex(password)));
        Collections.sort(usersList);
    }

    @Override
    public boolean checkUsernameAvailability(String username) {
        for (User user : usersList) {
            if (user.getUsername().equals(username)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public String generateAttendanceTable() {
        if (!(presentLesson == null)) {
            int actualRow = 0;
            int rows = studentsList.size();
            String[] firstNames = new String[rows];
            String[] lastNames = new String[rows];
            Integer[] transcriptsNumbers = new Integer[rows];
            String[] attendances = new String[rows];
            for (Map.Entry<Student, Boolean> entry : presentLesson.getAttendanceList().entrySet()) {
                firstNames[actualRow] = entry.getKey().getFirstName();
                lastNames[actualRow] = entry.getKey().getLastname();
                transcriptsNumbers[actualRow] = entry.getKey().getTranscriptNumber();
                if (entry.getValue()) {
                    attendances[actualRow] = "+";
                } else {
                    attendances[actualRow] = "-";
                }
                actualRow++;
            }
            ColumnFormatter<String> firstNameFormatter = ColumnFormatter.text(Alignment.LEFT, 25);
            ColumnFormatter<String> lastNameFormatter = ColumnFormatter.text(Alignment.LEFT, 30);
            ColumnFormatter<Number> numberFormatter = ColumnFormatter.number(Alignment.LEFT, 10, Precision.ZERO);
            ColumnFormatter<String> attendanceFormater = ColumnFormatter.text(Alignment.CENTER, 8);
            Table.Builder builder = new Table.Builder("Imię", firstNames, firstNameFormatter);
            builder.addColumn("Nazwisko", lastNames, lastNameFormatter);
            builder.addColumn("Nr indeksu", transcriptsNumbers, numberFormatter);
            builder.addColumn("Obecność", attendances, attendanceFormater);
            Table table = builder.build();
            return table.toString();
        } else {
            return "Brak danych do wyświetlenia, należy najpierw utworzyć nową lub wybrać wcześniejszą lekcję.";
        }
    }

    @Override
    public void addNewLesson(String subject) {
        Map<Student, Boolean> uncheckedAttendenceList = new TreeMap<>();
        for (Student student : studentsList) {
            uncheckedAttendenceList.put(student, false);
        }
        presentLesson = new Lesson(subject, LocalDate.now(), uncheckedAttendenceList);
        lessonsList.add(presentLesson);
        Collections.sort(lessonsList);
    }

    @Override
    public boolean checkTranscriptNumberAvailability(int transcriptNumber) {
        for (Student student : studentsList) {
            if (transcriptNumber == student.getTranscriptNumber()) {
                return false;
            }
        }
        return true;
    }

    @Override
    public void addNewStudent(String firstName, String lastName, int transcriptNumber) {
        studentsList.add(new Student(firstName, lastName, transcriptNumber));
        Collections.sort(studentsList);
    }

    @Override
    public String generateStudentsTable() {
        int actualRow = 0;
        int rows = studentsList.size();
        String[] firstNames = new String[rows];
        String[] lastNames = new String[rows];
        Integer[] transcriptsNumbers = new Integer[rows];

        for (Student student : studentsList) {
            firstNames[actualRow] = student.getFirstName();
            lastNames[actualRow] = student.getLastname();
            transcriptsNumbers[actualRow] = student.getTranscriptNumber();
            actualRow++;
        }
        ColumnFormatter<String> firstNameFormatter = ColumnFormatter.text(Alignment.LEFT, 25);
        ColumnFormatter<String> lastNameFormatter = ColumnFormatter.text(Alignment.LEFT, 30);
        ColumnFormatter<Number> numberFormatter = ColumnFormatter.number(Alignment.LEFT, 10, Precision.ZERO);
        Table.Builder builder = new Table.Builder("Imię", firstNames, firstNameFormatter);
        builder.addColumn("Nazwisko", lastNames, lastNameFormatter);
        builder.addColumn("Nr indeksu", transcriptsNumbers, numberFormatter);
        Table table = builder.build();



        return table.toString();
    }

    @Override
    public Lesson getPresentLesson() {
        return this.presentLesson;
    }

    @Override
    public String generateLessonsList() {
        int number = 1;
        StringBuilder stringBuilder = new StringBuilder();
        for (Lesson lesson : lessonsList) {
            stringBuilder.append(number);
            stringBuilder.append(". ");
            stringBuilder.append(lesson);
            number++;
        }
        return stringBuilder.toString();
    }

    @Override
    public void changePresentLesson(int index) {
        presentLesson = lessonsList.get(index);
    }

    @Override
    public int getLessonListLength() {
        return lessonsList.size();
    }

    @Override
    public boolean removeStudent(int transcriptNumber) {
        for (Student student : studentsList) {
            if (student.getTranscriptNumber() == transcriptNumber) {
                studentsList.remove(student);
                return true;
            }
        }
        return false;
    }

    @Override
    public String generateUsersTable() {
        int row = 0;
        String[] users = new String[usersList.size()];
        ColumnFormatter<String> usersFormatter = ColumnFormatter.text(Alignment.LEFT, 25);
        for (User user : usersList) {
            users[row]=user.getUsername();
            row++;
        }
        Table.Builder builder=new Table.Builder("Nazwa użytkownika",users,usersFormatter);
        Table table=builder.build();
        return table.toString();
    }

    @Override
    public boolean removeUser(String username) {
        for (User user : usersList) {
            if (user.getUsername().equalsIgnoreCase(username)) {
                usersList.remove(user);
                return true;
            }
        }
        return false;
    }

    @Override
    public String generateLessonsTable() {
        int row=0;
        String[] subjects=new String[lessonsList.size()];
        String[] dates=new String[lessonsList.size()];
        for (Lesson lesson : lessonsList) {
            subjects[row]=lesson.getSubject();
            dates[row]=lesson.getDate().toString();
            row++;
        }
        ColumnFormatter<String> subjectFormatter=ColumnFormatter.text(Alignment.LEFT,60);
        ColumnFormatter<String> dateFormatter=ColumnFormatter.text(Alignment.LEFT,12);
        Table.Builder builder=new Table.Builder("Temat",subjects,subjectFormatter);
        builder.addColumn("Data",dates,dateFormatter);
        Table table=builder.build();
        return table.toString();
    }

    @Override
    public boolean checkLessonAvailability(String subject) {
        for (Lesson lesson : lessonsList) {
            if (lesson.getSubject().equals(subject)) {
                if (lesson.getDate().equals(LocalDate.now())) {
                    return false;
                }
            }
        }
        return true;
    }

    private static class SingletonHolder {
        private static final RegisterRepositoryImpl INSTANCE = new RegisterRepositoryImpl();
    }
}
