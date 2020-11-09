package it.gui;

import it.database.IRegisterRepository;
import it.model.Lesson;
import it.model.Student;

import java.util.Map;
import java.util.Scanner;

public class GUI {
    private static final Scanner scanner = new Scanner(System.in);
    private static IRegisterRepository registerRepository;
    private static final String INVALID_CHARACTER_MESSAGE = "Wprowadzono niepoprawny znak, proszę wprowadzić liczbę.";
    private static final String ABORT_OPERATION = "Anulowano operację.";

    public void setRegisterRepository(IRegisterRepository registerRepository) {
        GUI.registerRepository = registerRepository;
    }

    public void startMainLoop() {
        System.out.println("------------------------\n---Dziennik obecności---\n------------------------");
        System.out.println("Domyślny użytkownik: \"admin\", hasło: \"admin\".");
        boolean loggedIn = false;
        do {
            if (!loggedIn) {
                starLoginLoop();
                loggedIn = true;
            } else {
                System.out.println(Options.MainMenuOptions.generateString());
                try {
                    switch (Options.MainMenuOptions.intToOption(Integer.parseInt(scanner.nextLine()))) {
                        case EXIT:
                            closeApplication();
                            break;
                        case LOGOUT:
                            loggedIn = !logOut();
                            break;
                        case ADD_NEW_LESSON:
                            addNewLesson();
                            break;
                        case SHOW_ATTENDANCE:
                            showAttendance();
                            break;
                        case SHOW_STUDENTS_LIST:
                            showStudentsList();
                            break;
                        case CHECK_ATTENDANCE:
                            checkAttendenceList();
                            break;
                        case SHOW_LESSONS_HISTORY:
                            showLessonsHistory();
                            break;
                        case REGISTER_MANAGEMENT:
                            startRegisterManagementLoop();
                            break;
                    }
                } catch (NumberFormatException e) {
                    System.out.println(INVALID_CHARACTER_MESSAGE);
                } catch (IllegalArgumentException e) {
                    System.out.println(e.getMessage());
                }
            }
        } while (true);
    }

    private void starLoginLoop() {
        boolean loginSuccesfull = false;
        do {
            System.out.println(Options.LoginMenuOptions.generateString());
            try {
                switch (Options.LoginMenuOptions.intToOption(Integer.parseInt(scanner.nextLine()))) {
                    case EXIT:
                        closeApplication();
                        break;
                    case LOGIN:
                        if (logInToSystem()) {
                            loginSuccesfull = true;
                        }
                        break;
                }
            } catch (NumberFormatException e) {
                System.out.println(INVALID_CHARACTER_MESSAGE);
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
            }
        } while (!loginSuccesfull);
    }

    private void startRegisterManagementLoop() {
        boolean backToMainMenu = false;
        do {
            System.out.println(Options.RegisterManagementOptions.generateString());
            try {
                switch (Options.RegisterManagementOptions.intToOption(Integer.parseInt(scanner.nextLine()))) {
                    case RETURN_TO_MAIN_MENU:
                        backToMainMenu = true;
                        break;
                    case ADD_NEW_STUDENT:
                        addNewStudent();
                        break;
                    case ADD_NEW_USER:
                        addNewUser();
                        break;
                    case SHOW_USERS:
                        showUsers();
                        break;
                    case REMOVE_USER:
                        removeUser();
                        break;
                    case CHANGE_PRESENT_LESSON:
                        changePresentLesson();
                        break;
                    case REMOVE_STUDENT:
                        removeStudent();
                        break;
                }
            } catch (NumberFormatException e) {
                System.out.println(INVALID_CHARACTER_MESSAGE);
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
            }
        } while (!backToMainMenu);
    }

    private void showLessonsHistory() {
        System.out.println("\n---Historia zajęć---");
        System.out.println(registerRepository.generateLessonsTable());
    }

    private void showStudentsList() {
        System.out.println("\n---Lista studentów---");
        System.out.println(registerRepository.generateStudentsTable());
    }

    private void showAttendance() {
        System.out.println("\n---Lista obecności---");
        System.out.println(registerRepository.getPresentLesson());
        System.out.println(registerRepository.generateAttendanceTable());
    }

    private boolean logOut() {
        System.out.println("Wylogowano.");
        return true;
    }

    private void showUsers() {
        System.out.println("\n---Lista użytkowników---");
        System.out.println(registerRepository.generateUsersTable());
    }

    private void removeUser() {
        String username;
        boolean exitLoop = false;
        System.out.println("\n---Usuwanie użytkownika");
        System.out.println(registerRepository.generateUsersTable());
        do {
            username = getStringFromUser("Wprowadz nazwę użytkownika do usunięcia."
                    , "Wprowadzono pusty znak, proszę spróbować ponownie."
                    , "\\S+");
            if (username.equalsIgnoreCase("exit")) {
                System.out.println(ABORT_OPERATION);
                exitLoop = true;
            } else {
                if (registerRepository.removeUser(username)) {
                    System.out.println("Użytkownik usunięty.");
                    exitLoop = true;
                } else {
                    System.out.println("Użytkownik o podanej nazwie nie istnieje, proszę spróbować jeszcze raz.");
                }
            }
        } while (!exitLoop);
    }

    private void removeStudent() {
        boolean exitLoop = false;
        System.out.println("\n---Usuwanie studentów---");
        System.out.println(registerRepository.generateStudentsTable());
        do {
            try {
                System.out.println("Podaj numer indeksu studenta do usunięcia.");
                if (registerRepository.removeStudent(Integer.parseInt(scanner.nextLine()))) {
                    System.out.println("Usunięto studenta.");
                    exitLoop = true;
                } else {
                    System.out.println("Student o podanym numerze indeksu nie istnieje.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Wprowadzono ciąg liter zamiast numeru indeksu, proszę spróbować ponownie.");
            }
        } while (!exitLoop);

    }

    private String getStringFromUser(String initialMessage, String failureMessage, String regex) {
        String result;
        boolean exitLoop = false;
        System.out.println(initialMessage + "\nWpisz \"exit\" aby anulować.");
        do {
            result = scanner.nextLine();
            if (result.equalsIgnoreCase("exit") || result.matches(regex)) {
                exitLoop = true;
            } else {
                System.out.println(failureMessage);
            }
        } while (!exitLoop);
        return result;
    }

    private void changePresentLesson() {
        String lessonNumber;
        System.out.println("\n---Historia zajęć ---");
        System.out.println(registerRepository.generateLessonsList());
        lessonNumber = getLessonNumber();
        if (lessonNumber.equalsIgnoreCase("exit")) {
            System.out.println(ABORT_OPERATION);
        } else {
            registerRepository.changePresentLesson(Integer.parseInt(lessonNumber) - 1);
            System.out.println("Dokonano zmiany zajęć");
        }
    }

    private String getLessonNumber() {
        String number;
        boolean exitLoop = false;
        System.out.println("Podaj numer odpowiadający zajęciom.\nWpisz \"exit\" aby anulować.");
        do {
            number = scanner.nextLine();
            if (number.equalsIgnoreCase("exit")
                    || ((Integer.parseInt(number) >= 1) && (Integer.parseInt(number) <= registerRepository.getLessonListLength()))) {
                exitLoop = true;
            } else {
                System.out.println("Niepoprawny numer zajęć, prosze spróbować ponownie.");
            }
        } while (!exitLoop);
        return number;
    }

    private void checkAttendenceList() {
        Lesson presentLesson = registerRepository.getPresentLesson();
        String attendance;
        if (presentLesson == null) {
            System.out.println("Błąd! Proszę najpierw utworzyć nową lub wybrać wcześniejszą lekcję.");
        } else {
            System.out.println("\n---Sprawdzanie listy obecności---\n" + presentLesson);
            for (Map.Entry<Student, Boolean> entry : presentLesson.getAttendanceList().entrySet()) {
                System.out.println((entry.getKey().getFirstName()
                        + " " + entry.getKey().getLastname()
                        + " " + entry.getKey().getTranscriptNumber() + " obecny/na?"));
                attendance = getAttendance();
                if (attendance.equalsIgnoreCase("exit")) {
                    System.out.println(ABORT_OPERATION);
                    return;
                } else {
                    entry.setValue(attendance.equals("+"));
                }
            }
            System.out.println("Koniec listy.");
        }
    }

    private String getAttendance() {
        String attendance;
        boolean exitLoop = false;
        do {
            System.out.println("\"+\"-obecny/na, \"-\"-nieobecny/na, \"exit\"-wyjdź");
            attendance = scanner.nextLine();
            if (attendance.equals("+") || attendance.equals("-") || attendance.equalsIgnoreCase("exit")) {
                exitLoop = true;
            } else {
                System.out.println("Niepoprawny format obecności, prosze spróbować ponownie");
            }
        } while (!exitLoop);
        return attendance;
    }

    private void addNewLesson() {
        boolean exitLoop = false;
        String subject;
        System.out.println("\n---Dodawanie nowej lekcji---");
        do {
            subject = getSubject();
            if (subject.equalsIgnoreCase("exit")) {
                exitLoop = true;
                System.out.println(ABORT_OPERATION);
            } else {
                if (registerRepository.checkLessonAvailability(subject)) {
                    registerRepository.addNewLesson(subject);
                    System.out.println("Dodano nową lekcję.");
                    exitLoop = true;
                } else {
                    System.out.println("Istnieją już zajęcia ze wskazanym tematem i dzisiejszą datą, proszę spróbować ponownie.");
                }
            }
        } while (!exitLoop);
    }

    private String getSubject() {
        return getStringFromUser("Podaj temat lekcji (niedozwolene białe znaki).\nMinimalna długość 8 znaków, maksymalna 60."
                , "Niepoprawny temat zajęć, proszę spróbować ponownie."
                , ".{8,60}");
    }

    private void closeApplication() {
        System.out.println("\n---Zamykanie aplikacji---");
        scanner.close();
        System.exit(0);
    }

    private static boolean logInToSystem() {
        String username;
        String password;

        System.out.println("\n---Logowanie---");
        System.out.println("Podaj nazwę użytkownika.");
        username = scanner.nextLine();
        System.out.println("Podaj hasło.");
        password = scanner.nextLine();
        if (registerRepository.authenticate(username, password)) {
            System.out.println("Zalogowano.");
            return true;
        } else {
            System.out.println("Błędna nazwa użytkownika lub hasło.");
            return false;
        }
    }

    private void addNewUser() {
        String username;
        String password;
        System.out.println("\n---Dodawanie nowego użytkownika---");
        username = getUsername();
        if (!username.equalsIgnoreCase("exit")) {
            password = getPassword();
            if (!password.equalsIgnoreCase("exit")) {
                registerRepository.addNewUser(username, password);
                System.out.println("Dodano nowego użytkownika.");
            } else {
                System.out.println(ABORT_OPERATION);
            }
        } else {
            System.out.println(ABORT_OPERATION);
        }
    }

    private String getUsername() {
        String username;
        boolean exitLoop = false;
        boolean usernameAvailable;
        System.out.println("Podaj nazwę użytkownika (dozwolone znaki: a-z,A-Z,0-9, brak polskich znaków).\n" +
                "Minimalna długość 8 znaków, maksymalna 25, małe i duże litery są traktowane tak samo.\n" +
                "Wpisz \"exit\" aby anulować");
        do {
            username = scanner.nextLine();
            usernameAvailable = registerRepository.checkUsernameAvailability(username);
            if ((username.equalsIgnoreCase("exit") || username.matches("[A-Za-z0-9]{8,25}")) && usernameAvailable) {
                exitLoop = true;
            } else if (!usernameAvailable) {
                System.out.println("Użytkownik o podanej nazwie już istnieje, proszę podać inną nazwę.");
            } else {
                System.out.println("Niepoprawna nazwa użytkownika, proszę spróbować ponownie.");
            }
        } while (!exitLoop);
        return username.toLowerCase();
    }

    private String getPassword() {
        return getStringFromUser("Podaj hasło (niedozwolene białe znaki).\nMinimalna długość 8 znaków."
                , "Niepoprawny format hasła, proszę spróbować ponownie."
                , "\\S{8,}");
    }

    private void addNewStudent() {
        String firstName;
        String lastName;
        String transcriptNumber;
        System.out.println("---Dodawanie nowego studenta---");
        firstName = getFirstName();
        if (!firstName.equalsIgnoreCase("exit")) {
            lastName = getLastName();
            if (!lastName.equalsIgnoreCase("exit")) {
                transcriptNumber = getTranscriptNumber();
                if (!transcriptNumber.equalsIgnoreCase("exit")) {
                    registerRepository.addNewStudent(firstName, lastName, Integer.parseInt(transcriptNumber));
                    System.out.println("Dodano nowego studenta.");
                } else {
                    System.out.println(ABORT_OPERATION);
                }
            } else {
                System.out.println(ABORT_OPERATION);
            }
        } else {
            System.out.println(ABORT_OPERATION);
        }
    }

    private String getFirstName() {
        return getStringFromUser("Podaj imię. Maksymalnie 25 znaków."
                , "Niepoprawy format imienia, proszę spróbować ponownie."
                , "[A-ZĄĘĆŻŹŁÓŃŚ][a-ząęćżźłóńś]{2,25}");
    }

    private String getLastName() {
        return getStringFromUser("Podaj nazwisko. Maksymalnie 30 znaków."
                , "Niepoprawy format nazwiska, proszę spróbować ponownie."
                , "[A-ZĄĘĆŻŹŁÓŃŚ][a-ząęćżźłóńś]{2,30}(-[A-ZĄĘĆŻŹŁÓŃŚ][a-ząęćżźłóńś]*)*");
    }

    private String getTranscriptNumber() {
        String transcriptNumber;
        boolean exitLoop = false;
        boolean numberAvailable;
        System.out.println("Podaj numer indeksu. Minimum 1 znak, maksymalnie 10 znaków.\nWpisz \"exit\" aby anulować");
        do {
            transcriptNumber = scanner.nextLine();
            numberAvailable = registerRepository.checkTranscriptNumberAvailability(Integer.parseInt(transcriptNumber));
            if ((transcriptNumber.equalsIgnoreCase("exit") || transcriptNumber.matches("[A-Za-z0-9]{1,10}")) && numberAvailable) {
                exitLoop = true;
            } else if (!numberAvailable) {
                System.out.println("Wskazany numer indeksu jest już zajęty.");
            } else {
                System.out.println("Niepoprawny numer indeksu, proszę spróbować ponownie.");
            }
        } while (!exitLoop);
        return transcriptNumber;
    }
}
