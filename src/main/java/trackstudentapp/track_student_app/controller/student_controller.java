package trackstudentapp.track_student_app.controller;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.view.RedirectView;
import org.springframework.ui.Model;
import jakarta.servlet.http.*;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import trackstudentapp.track_student_app.models.student;
import trackstudentapp.track_student_app.models.StudentRepository;
import java.awt.Color;
// marks a Java class as a controller, responsible for handling HTTP requests and generating responses.
@Controller
public class student_controller {
    public static boolean isShadeOfWhite(Color color) {
        // Get the red, green, and blue components of the color
        int red = color.getRed();
        int green = color.getGreen();
        int blue = color.getBlue();
        // Check if the color is a shade of white or a really light color
        // check if the color is very close to white by ensuring each color component is above a certain threshold
        int threshold = 230; 
        return red >= threshold && green >= threshold && blue >= threshold;
    }
    //"@Autowired" injects an instance of "StudentRepository" into the field "studentrepo".
    @Autowired
    private StudentRepository studentrepo;

    //Get endpoint that allows user to view all data in the student database data
    @GetMapping("/view")
    // A Model is a holder of the context data passed by a Controller to be displayed on a View, 
    // like it allows us to pass our data onto thymeleaf
    public String getAllUsers(Model model ){
        //gets all student sorted by alphabetical order in student name
        List<student> users = studentrepo.findAll(Sort.by(Sort.Direction.ASC, "name"));
        // adds the students attribute to the model for it to be displayed on to thymeleaf
        model.addAttribute("us", users);
        // go to the showall webpage
        return "Student/showAll";
    }
    //Default get endpoint that directs user to the view get endpoint, where they can see student data from
    // the database
    @GetMapping("/")
    public RedirectView process(){
        return new RedirectView("view");
    }

    //A get endpoint that directs user to the add student webpage where they can add new student
    @GetMapping("/addStudent")
    public String redirectToAddPage() {
        return "Student/add";
    }
    
    // A post endpoint that connects the form the from the addstudent webpage to the server
    // this is the endpoint that will store data onto the table
    @PostMapping("/Student/additem")
    public String addUser(@RequestParam Map<String, String> newstudent, HttpServletResponse response, RedirectAttributes redirectAttributes){
        // gets the colors from the input box from the form
        Color rgbColor = Color.decode(newstudent.get("haircolor"));
        Color rgbColor_2 = Color.decode(newstudent.get("favouritecolor"));
        // checks if the color from the input box is white or very close to white, if so, redirect to add student
        if(isShadeOfWhite(rgbColor) || isShadeOfWhite(rgbColor_2) ){
            redirectAttributes.addFlashAttribute("message", "One of your color is a shade of white, please choose a darker color." + 
            " If all 3 RGB values are the same and above 230, the color is considered a shade of white.");
            redirectAttributes.addFlashAttribute("flash", "alert-danger");
            return "redirect:/addStudent";
        }
        // checks if non of the values from the input boxes are empty
        if(!newstudent.get("name").isEmpty() && !newstudent.get("haircolor").isEmpty()
         && !newstudent.get("height").isEmpty() 
        && !newstudent.get("weight").isEmpty() && !newstudent.get("gpa").isEmpty()
        && !newstudent.get("favouritecolor").isEmpty())
        {
            //gets the attributes from the input boxes are converts to their correct type
            String newName = newstudent.get("name");
            String haircolor = newstudent.get("haircolor");
            String favouritecolor = newstudent.get("favouritecolor");
            int height = Integer.parseInt(newstudent.get("height"));
            int weight = Integer.parseInt(newstudent.get("weight"));
            Float gpa = Float.parseFloat(newstudent.get("gpa"));
            // generates a new student into the database with the attributes recieved above
            studentrepo.save(new student(newName, weight, height, haircolor, gpa, favouritecolor));
            // indicate that we successfully added a newstudent
            redirectAttributes.addFlashAttribute("message", "student added successfully!");
            redirectAttributes.addFlashAttribute("flash", "alert-success");

        } else{
            // indicate that we did not sucessfully added a new student
            redirectAttributes.addFlashAttribute("message", "Failed to add student!");
            redirectAttributes.addFlashAttribute("flash", "alert-danger");
            
        }
        // redirect back to student add page
        return "redirect:/addStudent";
    } 

    //A get endpoint that directs user to the update student webpage where they can update existing students
    @GetMapping("/updateStudent")
    public String redirectToupdate_multi(Model model) {
        //gets all student sorted by alphabetical order in student name
        List<student> users = studentrepo.findAll(Sort.by(Sort.Direction.ASC, "name"));
        // adds the students attribute to the model for it to be displayed on to thymeleaf
        model.addAttribute("us", users);
        // directs student to update page
        return "Student/update_multi";
    }

    //function that compares floating point numbers up to 2 decimals
    public static boolean compareDecimals(float a, float b) {
        int intA = Math.round(a * 100);
        int intB = Math.round(b * 100);
        return intA == intB;
    }

    // A post endpoint that connects the form the from the update student webpage to the server
    // this is the endpoint that will modify existing data on the table
    @PostMapping("/Student/updated")
    public String Updaitng_Multi_user(@RequestParam Map<String, String> newstudent, HttpServletRequest request, 
    RedirectAttributes redirectAttributes) {
        //gets all student sorted by alphabetical order in student name
        List<student> users = studentrepo.findAll(Sort.by(Sort.Direction.ASC, "name"));
        // gets length of current database for students
        int length = studentrepo.findAll().size();
        // if length is 0, // indicate that we did not sucessfully updated any student, and redirect to update page
        if(length ==0 ){
            redirectAttributes.addFlashAttribute("message", "Failed to update student(s)!");
            redirectAttributes.addFlashAttribute("flash", "alert-danger");
            return "redirect:/updateStudent";
        } 
        //iterates:
        for(int i =0; i < length; i++){
            // gets the colors from the input box from the form
            Color rgbColor = Color.decode(newstudent.get("haircolor"+Integer.toString(i)));
            Color rgbColor_2 = Color.decode(newstudent.get("favouritecolor"+Integer.toString(i)));
            // checks if the color from the input box is white or very close to white, if so, redirect to add student
            if(isShadeOfWhite(rgbColor) || isShadeOfWhite(rgbColor_2)){
                redirectAttributes.addFlashAttribute("message", "One or more color is similar to shade of white," +
                " please choose a darker color. If all 3 RGB values are the same and above 230, the color is considered a shade of white.");
                redirectAttributes.addFlashAttribute("flash", "alert-danger");
                return "redirect:/updateStudent";
            }
        }
        //flag to check whether at least one student was successfully updated
        Boolean flag = true;
        //itterate:
        for (int i = 0; i < length; i++) {
            //gets the attributes from the input boxes and converts them to string type
            String name = newstudent.get("name"+Integer.toString(i));
            String haircolor = newstudent.get("haircolor"+Integer.toString(i));
            String gpas = newstudent.get("gpa"+Integer.toString(i));
            String uid = newstudent.get("uid_info"+Integer.toString(i));
            String weights = newstudent.get("weight"+Integer.toString(i));
            String heights = newstudent.get("height"+Integer.toString(i));
            String favouritecolor = newstudent.get("favouritecolor"+Integer.toString(i));
            //get an existing student by the uid we extracted from the input boxes
            Optional<student> existingUser = studentrepo.findById(Integer.parseInt(uid));
            //check if the existing student with that uid exists, and compare 
            // if the uid from the list of student is the same as the uid we got from the
            // inputboxes (this prevents user from inspect elementing and modifying another users data and submitting
            // it into the database)
            if(existingUser.isPresent() && users.get(i).getUid() == Integer.parseInt(uid)){
                //get the current student and their attributes
                student curr = existingUser.get();
                //convert attributes aquired from the inputboxes into their correspodning attributes
                Float gpa = Float.parseFloat(gpas);
                int weight = Integer.parseInt(weights);
                int height = Integer.parseInt(heights);
                // check to make sure that the current student and their attribute does not all equal
                // the same as the attributes we got from the inputboxes 
                if(!curr.getName().equals(name) || !curr.getHaircolor().equals(haircolor) || !curr.getFavouritecolor().equals(favouritecolor)
                 || !compareDecimals(curr.getGpa(), gpa) ||curr.getWeight()!=weight || curr.getHeight()!=height){
                    //updates existing user with their new updated attributes we aquired from the form
                    curr.setName(name);
                    curr.setHaircolor(haircolor);
                    curr.setGpa(gpa);
                    curr.setWeight(weight);
                    curr.setHeight(height);
                    curr.setFavouritecolor(favouritecolor);
                    studentrepo.save(curr);
                    // indicate that we successfully updated a student
                    if(flag==true){
                        redirectAttributes.addFlashAttribute("message", "Student(s) update successfully!");
                        redirectAttributes.addFlashAttribute("flash", "alert-success");
                        flag=false;
                    }
                } 
            }
        }
        // indicate that we have not successfully updated a student
        if(flag==true){
            redirectAttributes.addFlashAttribute("message", "Failed to update student(s)!");
            redirectAttributes.addFlashAttribute("flash", "alert-danger");
        }
        return "redirect:/updateStudent";
    }
    
    //A getendpoint that  directs user to the delete student webpage where they can delete existing students
    @GetMapping("/deleteStudent")
    public String delete_soon_students(Model model ){
        //gets all student sorted by alphabetical order in student name
        List<student> users = studentrepo.findAll(Sort.by(Sort.Direction.ASC, "name"));
        // adds the students attribute to the model for it to be displayed on to thymeleaf
        model.addAttribute("us", users);
        // directs student to update page
        return "Student/delete_students";
    }

    // A post endpoint that connects the form the from the delete student webpage to the server
    // this is the endpoint that will modify existing data on the table
    @PostMapping("/Student/deleted_multi")
    public String deleteUser_multi_student(
    @RequestParam(name = "userCheckbox", required = false) String[] selectedUserIds,
    HttpServletResponse response, RedirectAttributes redirectAttributes) 
    {
         //flag to check whether at least one student was successfully deleted
        Boolean flag = true;
        // if at least one student was selected from deleting we proceed to delete them if we can:
        if (selectedUserIds != null) {
            //gets all student sorted by alphabetical order in student name
            List<student> users = studentrepo.findAll(Sort.by(Sort.Direction.ASC, "name"));
            //Itterate over the # of select students:
            for (int i = 0; i < selectedUserIds.length; i++) {
                // create a string array to hold 2 things: uid, and index for seraching in the database, and 
                // to do this we split the attribute into 2 things from uid (space) index into uid,index
                String[] parts = selectedUserIds[i].split(" ");
                // checks if arary is size 2 (to prevent inspect element and modiying data in an unconventional way)
                if (parts.length == 2) {
                    //convert attributes we recieved from the input boxes to their corresponding types
                    int userId = Integer.parseInt(parts[0]);
                    int j = Integer.parseInt(parts[1]);
                    // compare the current student with that uid (from the input box)  
                    // to the uid retrieved from the list of student that we aquired with using index element we aquired early and check if it 
                    //  is the same as the uid we got from the
                    // inputboxes (this prevents user from inspect elementing and modifying another users data and submitting
                    // it into the database)
                    if(users.get(j).getUid() == userId) {
                        // delete the existing user with the same uid
                        studentrepo.deleteById(userId);
                         // indicate that we successfully deleted a student
                        if(flag==true){
                            redirectAttributes.addFlashAttribute("message", "Student(s) deleted successfully!");
                            redirectAttributes.addFlashAttribute("flash", "alert-success");
                            flag=false;
                        }
                    }
                }
            }
        }
        // indicate that we have not successfully delete a student
        if(flag==true){
            redirectAttributes.addFlashAttribute("message", "Failed to delete student(s)!");
            redirectAttributes.addFlashAttribute("flash", "alert-danger");
        }
        // directs student to delete page
        return"redirect:/deleteStudent";
    }  

    // A request endpoint checks if our current url is a valid getendpoint or not
    @RequestMapping("/{randomPath}")
    public RedirectView redirectToSpecificUrl(@PathVariable String randomPath) {
        //list of Valid getendpoints we can connect to
        String[] excludedPaths = {"addStudent", "view", "deleteStudent", "updateStudent"};
        // itterate through list of valid get endpoints we can connect to
        for (String excludedPath : excludedPaths) {
            //if our url is equal to one of these valid endpoint, we dont redirect to another url through the request mapping
            if (randomPath.equals(excludedPath)) {
                return null;
            }
        }
        //otherwise if the url is not valid, we automatiaclly redict to the view getendpoint
        return new RedirectView("view"); 
    }  
}
