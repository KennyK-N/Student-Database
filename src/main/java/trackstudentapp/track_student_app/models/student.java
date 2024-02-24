package trackstudentapp.track_student_app.models;
import jakarta.persistence.*;
//Annotation to tell its a table in database
//create table called students
@Entity
@Table(name="students")
public class student {
    //create serial datatype
    // The @Id annotation tells spring that this is the primary key
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    // the above notation applies to the uid field, which is a unique identifer
    private int uid;
    // store name of student
    private String name;
    // store student gpa
    private Float gpa;
    // store student weight
    private int weight;
    // store student height
    private int height;
    // store student hair color
    private String haircolor;
    // store student favourite color
    private String favouritecolor;
    // default constructor
    public student(){
    }
    //parametric contstructor to create new student
    public student(String name, int weight, int height, String haircolor, Float gpa, String favouritecolor){
        this.name = name;
        this.weight = weight;
        this.height = height;
        this.haircolor = haircolor;
        this.gpa = gpa;
        this.favouritecolor = favouritecolor;
    }
    // returns student uid
    public int getUid() {
        return uid;
    }
    //sets student uid
    public void setUid(int uid) {
        this.uid = uid;
    }
    // returns student name
    public String getName() {
        return name;
    }
    // set student name
    public void setName(String name) {
        this.name = name;
    }
    // returns student gpa
    public Float getGpa() {
        return gpa;
    }
    // set student gpa
    public void setGpa(Float gpa) {
        this.gpa = gpa;
    }
    // returns student weight
    public int getWeight() {
        return weight;
    }
    // set student weight
    public void setWeight(int weight) {
        this.weight = weight;
    }
    // returns student height
    public int getHeight() {
        return height;
    }
    // set student height
    public void setHeight(int height) {
        this.height = height;
    }
    // returns student haircolor
    public String getHaircolor() {
        return haircolor;
    }
    // set student haircolor
    public void setHaircolor(String haircolor) {
        this.haircolor = haircolor;
    }
    // returns student favouritecolor
    public String getFavouritecolor() {
        return favouritecolor;
    }
    // set student favourite colour
    public void setFavouritecolor(String favouritecolor) {
        this.favouritecolor = favouritecolor;
    }
}
