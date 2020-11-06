package root;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import root.database.IRegisterRepository;
import root.database.RegisterRepositoryImpl;
import root.gui.GUI;

@Configuration
public class ApplicationConfiguration {
    @Bean
    public RegisterRepositoryImpl registerRepository() {
        return RegisterRepositoryImpl.getInstance();
    }

    @Bean
    public GUI gui(IRegisterRepository registerRepository) {
        GUI gui = new GUI();
        gui.setRegisterRepository(registerRepository);
        return gui;
    }
}
