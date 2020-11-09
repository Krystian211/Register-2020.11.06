package it;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import it.database.IRegisterRepository;
import it.database.RegisterRepositoryImpl;
import it.gui.GUI;

@Configuration
public class ApplicationConfiguration {
    @Bean
    public RegisterRepositoryImpl registerRepository() {
        return new RegisterRepositoryImpl();
    }

    @Bean
    public GUI gui(IRegisterRepository registerRepository) {
        GUI gui = new GUI();
        gui.setRegisterRepository(registerRepository);
        return gui;
    }
}
