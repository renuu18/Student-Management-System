package web.management.bootstrap;

import lombok.AllArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import web.management.entity.Student;
import web.management.repository.StudentRepository;
import com.github.javafaker.Faker;

@AllArgsConstructor
public class BootstrapData implements CommandLineRunner {
    private final StudentRepository studentRepository;
    @Override
    public void run(String... args) throws Exception {
        Faker faker = new Faker();

        if (studentRepository.count() > 0) {
            return;
        }

        for (int i = 0; i < 50; i++) {
            Student student = new Student();

            student.setFirstName(faker.leagueOfLegends().champion());
            student.setLastName(faker.leagueOfLegends().rank());
            student.setEmail(faker.internet().emailAddress());
            student.setAge(faker.number().numberBetween(18, 30));
            student.setPhotoUrl("https://avatars.dicebear.com/api/open-peeps/" + faker.name().firstName() + ".svg");

            studentRepository.save(student);
        }

        System.out.println("Bootstrap data loaded. Students registered: " + studentRepository.count() + ".");
    }
}
