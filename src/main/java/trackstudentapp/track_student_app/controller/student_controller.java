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

@Controller
public class student_controller {
    @Autowired
    private StudentRepository studentrepo;
    
    @GetMapping("/view")
    public String getAllUsers(Model model ){
        List<student> users = studentrepo.findAll(Sort.by(Sort.Direction.ASC, "name"));
        model.addAttribute("us", users);
        return "Student/showAll";
    }

    @GetMapping("/")
    public RedirectView process(){
        return new RedirectView("view");
    }

    @GetMapping("/addStudent")
    public String redirectToAddPage() {
        return "Student/add";
    }
    @PostMapping("/Student/additem")
    public String addUser(@RequestParam Map<String, String> newstudent, HttpServletResponse response, RedirectAttributes redirectAttributes){
        if(!newstudent.get("name").isEmpty() && !newstudent.get("haircolor").isEmpty()
         && !newstudent.get("height").isEmpty() 
        && !newstudent.get("weight").isEmpty() && !newstudent.get("gpa").isEmpty())
        {
            String newName = newstudent.get("name");
            String haircolor = newstudent.get("haircolor");
            int height = Integer.parseInt(newstudent.get("height"));
            int weight = Integer.parseInt(newstudent.get("weight"));
            Float gpa = Float.parseFloat(newstudent.get("gpa"));
            studentrepo.save(new student(newName, weight, height, haircolor, gpa));
            response.setStatus(201);
            redirectAttributes.addFlashAttribute("message", "student added successfully!");
            redirectAttributes.addFlashAttribute("flash", "alert-success");

        } else{
            redirectAttributes.addFlashAttribute("message", "Failed to add student!");
            redirectAttributes.addFlashAttribute("flash", "alert-danger");
            
        }
        return "redirect:/addStudent";
    } 

    @GetMapping("/updateStudent")
    public String redirectToupdate_multi(Model model) {
        List<student> users = studentrepo.findAll(Sort.by(Sort.Direction.ASC, "name"));
        model.addAttribute("us", users);
        return "Student/update_multi";
    }

    public static boolean compareDecimals(float a, float b) {
        int intA = Math.round(a * 100);
        int intB = Math.round(b * 100);
        return intA == intB;
    }

    @PostMapping("/Student/updated")
    public String Updaitng_Multi_user(@RequestParam Map<String, String> newuser, HttpServletRequest request, 
    RedirectAttributes redirectAttributes) {
        Boolean flag = true;
        int length = studentrepo.findAll().size();
        if(length ==0 ){
            redirectAttributes.addFlashAttribute("message", "Failed to update student(s)!");
            redirectAttributes.addFlashAttribute("flash", "alert-danger");
            return "redirect:/updateStudent";
        } 
        List<student> users = studentrepo.findAll(Sort.by(Sort.Direction.ASC, "name"));
        for (int i = 0; i < length; i++) {
            String name = newuser.get("name"+Integer.toString(i));
            String haircolor = newuser.get("haircolor"+Integer.toString(i));
            String gpas = newuser.get("gpa"+Integer.toString(i));
            String uid = newuser.get("uid_info"+Integer.toString(i));
            String weights = newuser.get("weight"+Integer.toString(i));
            String heights = newuser.get("height"+Integer.toString(i));
            Optional<student> existingUser = studentrepo.findById(Integer.parseInt(uid));
            if(existingUser.isPresent() && users.get(i).getUid() == Integer.parseInt(uid)){
                student curr = existingUser.get();
                Float gpa = Float.parseFloat(gpas);
                int weight = Integer.parseInt(weights);
                int height = Integer.parseInt(heights);
                if(!curr.getName().equals(name) || !curr.getHaircolor().equals(haircolor)
                 || !compareDecimals(curr.getGpa(), gpa) ||curr.getWeight()!=weight || curr.getHeight()!=height){
                    curr.setName(name);
                    curr.setHaircolor(haircolor);
                    curr.setGpa(gpa);
                    curr.setWeight(weight);
                    curr.setHeight(height);
                    studentrepo.save(curr);
                    if(flag==true){
                        redirectAttributes.addFlashAttribute("message", "Student(s) update successfully!");
                        redirectAttributes.addFlashAttribute("flash", "alert-success");
                        flag=false;
                    }
                } 
            }
        }
        if(flag==true){
            redirectAttributes.addFlashAttribute("message", "Failed to update student(s)!");
            redirectAttributes.addFlashAttribute("flash", "alert-danger");
        }
        return "redirect:/updateStudent";
    }

    @GetMapping("/deleteStudent")
    public String delete_soon_students(Model model ){
        List<student> users = studentrepo.findAll(Sort.by(Sort.Direction.ASC, "name"));
        model.addAttribute("us", users);
        return "Student/delete_students";
    }
    @PostMapping("/Student/deleted_multi")
    public String deleteUser_multi_student(
    @RequestParam(name = "userCheckbox", required = false) String[] selectedUserIds,
    HttpServletResponse response, RedirectAttributes redirectAttributes) 
    {
        Boolean flag = true;
        if (selectedUserIds != null) {
            List<student> users = studentrepo.findAll(Sort.by(Sort.Direction.ASC, "name"));
            for (int i = 0; i < selectedUserIds.length; i++) {
                String[] parts = selectedUserIds[i].split(" ");
                if (parts.length == 2) {
                    int userId = Integer.parseInt(parts[0]);
                    int j = Integer.parseInt(parts[1]);
                    if(users.get(j).getUid() == userId) {
                        studentrepo.deleteById(userId);
                        if(flag==true){
                            redirectAttributes.addFlashAttribute("message", "Student(s) deleted successfully!");
                            redirectAttributes.addFlashAttribute("flash", "alert-success");
                            flag=false;
                        }
                    }
                }
            }
        }
        if(flag==true){
            redirectAttributes.addFlashAttribute("message", "Failed to delete student(s)!");
            redirectAttributes.addFlashAttribute("flash", "alert-danger");
        }
        return"redirect:/deleteStudent";
    }  

    @RequestMapping("/{randomPath}")
    public RedirectView redirectToSpecificUrl(@PathVariable String randomPath) {
        String[] excludedPaths = {"addStudent", "view", "deleteStudent", "updateStudent"};
        for (String excludedPath : excludedPaths) {
            if (randomPath.equals(excludedPath)) {
                return null;
            }
        }
        return new RedirectView("view"); 
    }  
}
