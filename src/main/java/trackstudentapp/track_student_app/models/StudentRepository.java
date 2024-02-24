package trackstudentapp.track_student_app.models;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
//interface are set of operation or method to fall under a category
// the repositry acts as an intermediate between the sql and java 
public interface StudentRepository extends JpaRepository<student, Integer>{
    //allows interfacing with the table
    //find student by gpa
    List<student> findByGpa(Float gpa);
    //find student by height
    List<student> findByHeight(int height);
    //find student by weight
    List<student> findByWeight(int weight);
    //find student by name
    List<student> findByName(String name);
    //find student by haircolor
    List<student> findByHaircolor(String haircolor);
    //find student by uid
    student findByUid(int uid);
}