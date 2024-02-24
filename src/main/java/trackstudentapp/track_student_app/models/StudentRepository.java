package trackstudentapp.track_student_app.models;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
//interface are set of operation or method to fall under a category
// the repositry acts as an intermediate between the sql and java 
public interface StudentRepository extends JpaRepository<student, Integer>{
    //allows interfacing with the table
    //find student by uid
    student findByUid(int uid);
}