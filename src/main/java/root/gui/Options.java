package root.gui;

public class Options {
    private interface IOptionGetters {
        String getDescription();

        int getOptionNumber();
    }

    private static class EnumEnhancer<E extends Enum<E> & IOptionGetters> {
        private String generateString(E[] values) {
            StringBuilder stringBuilder = new StringBuilder();
            for (E value : values) {
                stringBuilder.append(value.getOptionNumber())
                        .append(". ")
                        .append(value.getDescription())
                        .append("\n");
            }
            return stringBuilder.toString();
        }

        private E intToEnum(E[] values, int optionNumber) {
            for (E value : values) {
                if (value.getOptionNumber() == optionNumber) {
                    return value;
                }
            }
            throw new IllegalArgumentException("Wybrano opcję spoza zakresu.");
        }
    }

    public enum MainMenuOptions implements IOptionGetters {
        EXIT("Wyjdź z programu", 0),
        LOGOUT("Wyloguj się", 1),
        ADD_NEW_LESSON("Dodaj nowe zajęcia", 2),
        CHECK_ATTENDANCE("Sprawdź listę obecności", 3),
        SHOW_STUDENTS_LIST("Wyświetl listę studentów", 4),
        SHOW_ATTENDANCE("Wyświetl listę obecności", 5),
        SHOW_LESSONS_HISTORY("Wyświetl historię zajęć",6),
        REGISTER_MANAGEMENT("Zarządzaj dziennikiem", 7);

        private final String description;
        private final int optionNumber;
        private final static EnumEnhancer<MainMenuOptions> ENUM_ENHANCER = new EnumEnhancer<>();

        MainMenuOptions(String description, int optionNumber) {
            this.description = description;
            this.optionNumber = optionNumber;
        }

        @Override
        public String getDescription() {
            return this.description;
        }

        @Override
        public int getOptionNumber() {
            return this.optionNumber;
        }

        public static String generateString() {
            return "\n---Menu główne---\n" + ENUM_ENHANCER.generateString(values());
        }

        public static MainMenuOptions intToOption(int optionNumber) {
            return ENUM_ENHANCER.intToEnum(values(), optionNumber);
        }
    }

    public enum LoginMenuOptions implements IOptionGetters {
        EXIT("Wyjdź z programu", 0),
        LOGIN("Zaloguj się", 1);

        private final String description;
        private final int optionNumber;
        private static final EnumEnhancer<LoginMenuOptions> ENUM_ENHANCER = new EnumEnhancer<>();

        LoginMenuOptions(String description, int optionNumber) {
            this.description = description;
            this.optionNumber = optionNumber;
        }

        @Override
        public String getDescription() {
            return this.description;
        }

        @Override
        public int getOptionNumber() {
            return this.optionNumber;
        }

        public static String generateString() {
            return "\n---Menu Logowania---\n" + ENUM_ENHANCER.generateString(values());
        }

        public static LoginMenuOptions intToOption(int optionNumber) {
            return ENUM_ENHANCER.intToEnum(values(), optionNumber);
        }
    }

    public enum RegisterManagementOptions implements IOptionGetters {
        RETURN_TO_MAIN_MENU("Powrót do menu głównego", 0),
        ADD_NEW_USER("Dodaj nowego użytkownika", 1),
        SHOW_USERS("Wyświetl listę użytkowników",2),
        REMOVE_USER("Usuń użytkownika",3),
        ADD_NEW_STUDENT("Dodaj nowego ucznia", 4),
        CHANGE_PRESENT_LESSON("Wybierz wcześniejsze zajęcia",5),
        REMOVE_STUDENT("Usuń studenta",6);

        private String description;
        private int optionNumber;
        private final static EnumEnhancer<RegisterManagementOptions> ENUM_ENHANCER = new EnumEnhancer<>();

        RegisterManagementOptions(String description, int optionNumber) {
            this.description = description;
            this.optionNumber = optionNumber;
        }

        @Override
        public String getDescription() {
            return this.description;
        }

        @Override
        public int getOptionNumber() {
            return this.optionNumber;
        }

        public static String generateString() {
            return "\n---Menu zarządzania dziennikiem---\n" + ENUM_ENHANCER.generateString(values());
        }

        public static RegisterManagementOptions intToOption(int optionNumber) {
            return ENUM_ENHANCER.intToEnum(values(), optionNumber);
        }

    }
}
