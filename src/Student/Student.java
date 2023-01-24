package Student;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Student {

    public static String convertStudentsDataToRows(String data){
        String valueSeparator= "#";
        String recordSeparator= "\\$";
        String allRows="";
        System.out.println(recordSeparator);

        List<String> rows = Arrays.asList(data.split(recordSeparator));
        rows.set(0,"id," + rows.get(0));
        int id=0;
        for(String row : rows){
            List<String> values= new ArrayList<>();

            if(id>0) {
                values.add(0, String.valueOf(id));
            }
            values.addAll(Arrays.asList(row.split(valueSeparator)));
            for(String value : values){
                allRows += value + ",";
            }
            allRows = allRows.substring(0,allRows.length()-1);
            allRows +="\n";
            id++;
        }

        return allRows;
    }
    public static String readFromText(String path){

        String data = "";
        try {
            FileInputStream studentDataFIS= new FileInputStream(path);
            int size = studentDataFIS.available();
            byte[] b = new byte[size];
            studentDataFIS.read(b);
            data = new String(b);
            studentDataFIS.close();

        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return data;
    }
    public static String getStudentDataFromCSV(){
        String path = "student_data.csv";
        String data = "";
        String allRows="";
        try {
            FileInputStream studentDataFIS= new FileInputStream(path);
            int size = studentDataFIS.available();
            byte[] b = new byte[size];
            studentDataFIS.read(b);
            data = new String(b);

            String valueSeparator= ",";
            String recordSeparator= "\n";

            List<String> rows = Arrays.asList(data.split(recordSeparator));

            for(String row : rows){
                List<String> values= new ArrayList<>();

                values.addAll(Arrays.asList(row.split(valueSeparator)));
                for(String value : values){
                    allRows += value + "\t";
                }
                allRows = allRows.substring(0,allRows.length()-1);
                allRows +="\n";

            }

            studentDataFIS.close();

        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return allRows;
    }
    public static void printStudentData(){
        System.out.println("-------------------------------\nCurrent Student List\n-------------------------------");
        System.out.println(getStudentDataFromCSV());

    }
    public static String[] getStudentData(int studentID) {
        String [] studentData = new String[7];

        String valueSeparator= ",";
        String recordSeparator= "\n";
        String data = readFromText("student_data.csv");
        data = data.substring(44,data.length()); // ignore first row

        List<String> rows = Arrays.asList(data.split(recordSeparator));
        for(String row : rows){
            List<String> elements = Arrays.asList(row.split(valueSeparator));
            String id = elements.get(0);
            String name = elements.get(1);
            String grade = elements.get(2);
            String email = elements.get(3);
            String address = elements.get(4);
            String region = elements.get(5);
            String country = elements.get(6);

            if(id.equals(String.valueOf(studentID))){
                studentData[0]= id;
                studentData[1]= name;
                studentData[2]= grade;
                studentData[3]= email;
                studentData[4]= address;
                studentData[5]= region;
                studentData[6]= country;
                break;
            }
        }

        return  studentData;
    }
    public static int[] getStudentEnrolledCourses(int studentID) {

        String jsonString = readFromText("student_course_details.json");
        JSONObject obj = new JSONObject(jsonString);

        int enrolledCourses[] = new int[6];
        try{

            JSONArray arr = obj.getJSONArray(String.valueOf(studentID));
            for (int i = 0; i < arr.length(); i++) {
                int enrolledCourse = arr.getInt(i);

                enrolledCourses[i] = enrolledCourse;

            }

        }
        catch (Exception ex){

        }
        return enrolledCourses;
    }
}
