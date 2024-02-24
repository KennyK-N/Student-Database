package trackstudentapp.track_student_app.models;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface StudentRepository extends JpaRepository<student, Integer>{

    List<student> findByGpa(Float gpa);
    List<student> findByHeight(int height);
    List<student> findByWeight(int weight);
    List<student> findByName(String name);
    //IT DOESNT NOT LIKE name1_name2 for member variable FOR POSTGREL WHEN USING FINDBY 
    List<student> findByHaircolor(String haircolor);
    student findByUid(int uid);
}