package web.management.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import web.management.entity.Student;

import java.util.Optional;


public interface StudentRepository extends JpaRepository<Student, Long> {
    @Query(
            "SELECT p FROM Student p " +
            "WHERE LOWER(p.firstName) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "OR LOWER(p.lastName) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "OR LOWER(p.email) LIKE LOWER(CONCAT('%', :keyword, '%')) "
    )
    Page<Student> search(String keyword, Pageable pageable);
    Optional<Student> findByEmail(String email);
}
