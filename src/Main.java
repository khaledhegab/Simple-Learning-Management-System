import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;

public class Main {
    public static void main(String[] args) {

        String studentsData = getStudentsData();
        saveToCSVFile("student_data.csv", convertStudentsDataToRows(studentsData));
        saveToCSVFile("course_data.csv", convertCourseDataToRows());
        printStudentData();

    }

    public static String getStudentsData(){
        String path = "student_data.txt";
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

    public static void saveToCSVFile(String fileName,String data){
        try {
            FileOutputStream fos = new FileOutputStream(fileName);

            fos.write(data.getBytes());
            fos.close();


        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void printStudentData(){
        System.out.println("-------------------------------\nCurrent Student List\n-------------------------------");
        System.out.println(getStudentDataFromCSV());
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

    public static String convertCourseDataToRows(){
        String data = "id,Course Name,Instructor,Course duration,Course time,Location\n";
        try {
            File inputFile = new File("course_data.txt");
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(inputFile);
            doc.getDocumentElement().normalize();
            NodeList nList = doc.getElementsByTagName("row");

            for (int temp = 0; temp < nList.getLength(); temp++) {
                Node nNode = nList.item(temp);

                if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element eElement = (Element) nNode;
                    data += eElement.getElementsByTagName("id")
                            .item(0)
                            .getTextContent()
                            + ","
                            + eElement
                            .getElementsByTagName("CourseName")
                            .item(0)
                            .getTextContent()
                            + ","
                            + eElement
                            .getElementsByTagName("Instructor")
                            .item(0)
                            .getTextContent()
                            + ","
                            + eElement
                            .getElementsByTagName("Courseduration")
                            .item(0)
                            .getTextContent()
                            + ","
                            + eElement
                            .getElementsByTagName("Coursetime")
                            .item(0)
                            .getTextContent()
                            + ","
                            + eElement
                            .getElementsByTagName("Location")
                            .item(0)
                            .getTextContent() + "\n";


//                    System.out.println("id: "
//                            + eElement.getElementsByTagName("id")
//                            .item(0)
//                            .getTextContent());
//                    System.out.println("Course Name : "
//                            + eElement
//                            .getElementsByTagName("CourseName")
//                            .item(0)
//                            .getTextContent());
//                    System.out.println("Instructor : "
//                            + eElement
//                            .getElementsByTagName("Instructor")
//                            .item(0)
//                            .getTextContent());
//                    System.out.println("Course duration : "
//                            + eElement
//                            .getElementsByTagName("Courseduration")
//                            .item(0)
//                            .getTextContent());
//                    System.out.println("Course time : "
//                            + eElement
//                            .getElementsByTagName("Coursetime")
//                            .item(0)
//                            .getTextContent());
//                    System.out.println("Location : "
//                            + eElement
//                            .getElementsByTagName("Location")
//                            .item(0)
//                            .getTextContent());
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        data = data.substring(0,data.length()-1);
        return data;
    }



}