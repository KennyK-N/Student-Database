package trackstudentapp.track_student_app.models;
import jakarta.persistence.*;

@Entity
@Table(name="students")
public class student {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private int uid;
    private String name;
    private Float gpa;
    private int weight;
    private int height;
    private String haircolor;
    public student(){
    }
    public student(String name, int weight, int height, String haircolor, Float gpa){
        this.name = name;
        this.weight = weight;
        this.height = height;
        this.haircolor = haircolor;
        this.gpa = gpa;
    }
    public int getUid() {
        return uid;
    }
    public void setUid(int uid) {
        this.uid = uid;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public Float getGpa() {
        return gpa;
    }
    public void setGpa(Float gpa) {
        this.gpa = gpa;
    }
    public int getWeight() {
        return weight;
    }
    public void setWeight(int weight) {
        this.weight = weight;
    }
    public int getHeight() {
        return height;
    }
    public void setHeight(int height) {
        this.height = height;
    }
    public String getHaircolor() {
        return haircolor;
    }
    public void setHaircolor(String haircolor) {
        this.haircolor = haircolor;
    }
    
    
}
