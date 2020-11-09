package it.database;

import it.model.Lesson;

public interface IRegisterRepository {
    boolean authenticate(String username, String password);
    void addNewUser(String username, String password);
    boolean checkUsernameAvailability(String username);
    String generateAttendanceTable();
    void addNewLesson(String subject);
    boolean checkTranscriptNumberAvailability(int transcriptNumber);
    void addNewStudent(String firstName,String lastName, int transcriptNumber);
    String generateStudentsTable();
    Lesson getPresentLesson();
    String generateLessonsList();
    void changePresentLesson(int index);
    int getLessonListLength();
    boolean removeStudent(int transcriptNumber);
    String generateUsersTable();
    boolean removeUser(String username);
    String generateLessonsTable();
    boolean checkLessonAvailability(String subject);
}
