import java.util.Scanner;
import org.json.JSONArray;
import org.json.JSONObject;
import static Course.Course.*;
import static Student.Student.*;

public class Main {
    static Scanner in = new Scanner(System.in);
    public static void main(String[] args) {

        String studentsData = readFromText("student_data.txt");
        saveToFile("student_data.csv", convertStudentsDataToRows(studentsData));
        saveToFile("course_data.csv", convertCourseDataToRows());

        System.out.println("Welcome to LMS\n" +
                "created by {Khaled Hegab_1/24/2023}\n" +
                "====================================================================================");

        while (true){
            homePage();
        }

    }

    private static void print(String input){
        System.out.print(input);
    }
    private static void homePage(){
     System.out.println("Home page\n" +
             "====================================================================================");
        printStudentData();
        System.out.println("------------------------------------------------------------------------------------");
        System.out.print("Please select the required student: ");
        try {
            String input = in.next();
            int studentID= Integer.valueOf(input);

            String [] studentData = getStudentData(studentID);

            if(studentData[0] == null){
                System.out.println("Student ID does not exist, Please choose from the list");
                return;
            }
            studentDetailedPage(studentData);
        }
        catch (Exception ex){
            print(ex.toString());
            System.out.println("Invalid strudent ID, Please enter numbers only");
        }
    }


    private static void studentDetailedPage(String[] studentData){

        print("====================================================================================\n" +
                "Student Details page\n" +
                "====================================================================================\n");
        int studentID = Integer.valueOf(studentData[0]);
        String name  = studentData[1];
        String grade = studentData[2];
        String email = studentData[3];

        print("Name:" + name + " \tGrade: " + grade + " \tEmail: " + email + "\n");

        int [] enrolledStudentCourses= getStudentEnrolledCourses(studentID);
        if(enrolledStudentCourses[0] == 0){
            print("This student hasn't enrolled in any courses\n");
        }
        else{

            print("Enrolled courses.\n");
            for(int i=0;i<enrolledStudentCourses.length;i++){
                if(enrolledStudentCourses[i]==0) break;
                String[] courseData = getCourseData(enrolledStudentCourses[i]);
                print((i+1) + "-\t" + courseData[0] + "\t" + courseData[1] + "\t" + courseData[2] + "\t" + courseData[3]+ "\t" + courseData[4]+ "\t" + courseData[5] + "\n");
            }
        }
        boolean cont = true;

        while (cont) {

            print("------------------------------------------------------------------------------------\n" +
                    "Please choose from the following:\n" +
                    "a - Enroll in a course\n" +
                    "d - Unenrollfrom an existing course\n" +
                    "r - Replacing an existing course\n" +
                    "b - Back to the main page\n" +
                    "please select the required action:");

            String input = in.next();
            switch (input) {
                case "a":
                    print("Enrollment page\n" +
                            "====================================================================================================\n");
                    print(getCourseDataFromCSV() + "\n");
                    enroll(studentID);
                    break;
                case "d":
                    unenroll(studentID);
                    break;
                case "r":
                    replace(studentID);
                    break;
                case "b":
                    cont = false;
                    break;
                default:
                    print("Wrong choice, please choose from the list");
            }
        }


    }

    private static void enroll(int studentID){


        while (true) {
            String jsonString = readFromText("student_course_details.json");
            JSONObject obj = new JSONObject(jsonString);
            JSONArray arr = new JSONArray();

            if (obj.has(String.valueOf(studentID))) {
                arr= obj.getJSONArray(String.valueOf(studentID));
                if (arr.length() == 6) {

                    print("The student has reached the maximum number of course enrollment!\n");
                    return;
                }


            }
            print("Enter the course id that you want to enroll the student to\n" +
                    "Enter b to go back to the home page: ");
            String input = in.next();

            if (input.equals("b")) {
                return;
            }

            try {

                int courseID = Integer.valueOf(input);
                String[] courseData = getCourseData(courseID);
                if (courseData[0] == null) {
                    System.out.println("Failed to enroll: The course with id = " + courseID + " is not exist");
                    return;
                }
                int cID=0;
                for(int i=0;i<arr.length();i++){
                    cID = arr.getInt(i);
                    if(cID == courseID)
                        break;
                }
                if(cID != courseID)
                obj.append(String.valueOf(studentID), courseID);
                else{
                    print("The student has enrolled in this course previously.\n");
                    return;
                }

                String courseName = courseData[1];
                saveToFile("student_course_details.json",obj.toString());
                print("The student is Enrolled Successfully in the " + courseName + " course\n");
            } catch (Exception ex) {
                print("Failed to enroll: please write a valid course id\n");
            }
        }

    }

    private static void unenroll(int studentID){
        String jsonString = readFromText("student_course_details.json");
        JSONObject obj = new JSONObject(jsonString);
        JSONArray arr = new JSONArray();

        if (obj.has(String.valueOf(studentID))) {
            arr= obj.getJSONArray(String.valueOf(studentID));
            if (arr.length() == 1) {

                print("Faild to unenroll: The student as only one or no courses to unenroll from\n");
            }
            else{
                print("Please enter course id: ");
                String input = in.next();
                try{
                    int courseID = Integer.valueOf(input);
                    boolean isCourseExist= false;
                    for(int i=0;i<arr.length();i++){
                        if(courseID == arr.getInt(i)){
                            isCourseExist= true;
                            break;
                        }
                    }
                    if(isCourseExist == true){
                        obj.remove(String.valueOf(studentID));
                        for(int i=0;i<arr.length();i++){
                            if(arr.getInt(i) != courseID)
                                obj.append(String.valueOf(studentID),arr.getInt(i));
                        }
                        print("Unenrolled successfully from the Algorithms course\n");
                        saveToFile("student_course_details.json",obj.toString());
                    }
                    else{
                        print("The student does not enroll previously in this course!\n");
                        return;
                    }


                }
                catch (Exception ex){

                }
            }


        }
        else{
            print("Faild to unenroll: The student as only one or no courses to unenroll from\n");
        }
    }

    private static void replace(int studentID){
        String jsonString = readFromText("student_course_details.json");
        JSONObject obj = new JSONObject(jsonString);
        JSONArray arr = new JSONArray();

        if (obj.has(String.valueOf(studentID))) {
            arr= obj.getJSONArray(String.valueOf(studentID));
            if (arr.length() == 0) {

                print("Faild to replace: The student doesn't enroll to any courses yet!\n");
            }
            else{
                print("Please enter course id to be replaced: ");
                String input1 = in.next();
                print("Available courses\n");
                print(getCourseDataFromCSV() + "\n");
                print("Please enter the required course id to replace: ");
                String input2 = in.next();
                try{
                    int courseID = Integer.valueOf(input1);
                    int courseID2 = Integer.valueOf(input2);
                    boolean isCourseExist= false;
                    boolean isCourse2Exist= false;
                    for(int i=0;i<arr.length();i++){
                        if(courseID == arr.getInt(i)){
                            isCourseExist= true;
                            break;
                        }
                    }

                    for(int i=0;i<arr.length();i++){
                        if(courseID2 == arr.getInt(i)){
                            isCourse2Exist= true;
                            break;
                        }
                    }

                    if(isCourseExist == true && isCourse2Exist==true){
                        print("The student already enrolled in these courses");
                        return;
                    }
                    else if(isCourseExist == false){
                        String []course = getCourseData(courseID);
                        print("the student doesn't enroll in " + course[1] + " course");
                        return;
                    }
                    else{
                        String []course1 = getCourseData(courseID);
                        String []course2 = getCourseData(courseID2);
                        obj.remove(String.valueOf(studentID));
                        for(int i=0;i<arr.length();i++){
                            if(arr.getInt(i) != courseID)
                                obj.append(String.valueOf(studentID),arr.getInt(i));
                        }

                        obj.append(String.valueOf(studentID),courseID2);

                        print("Courses replaced successfully from the " + course1[1] +" course to " + course2[1] + " course\n");

                        //print(obj.toString());
                        saveToFile("student_course_details.json",obj.toString());
                    }



                }
                catch (Exception ex){
                    print("Please enter numbers only");
                }
            }


        }
        else{
            print("Faild to unenroll: The student as only one or no courses to unenroll from\n");
        }
    }

}