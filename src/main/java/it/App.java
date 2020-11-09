package it;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import it.gui.GUI;

public class App {
    public static void main(String[] args) {
        ApplicationContext applicationContext=new AnnotationConfigApplicationContext(ApplicationConfiguration.class);
        GUI gui=applicationContext.getBean(GUI.class);
        gui.startMainLoop();
    }

}
