package Course;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static Student.Student.readFromText;

public class Course {

    public static String[] getCourseData(int courseID) {
        String [] courseData = new String[6];

        String valueSeparator= ",";
        String recordSeparator= "\n";
        String data = readFromText("course_data.csv");
        data = data.substring(63,data.length()); // ignore first row

        List<String> rows = Arrays.asList(data.split(recordSeparator));
        for(String row : rows){
            List<String> elements = Arrays.asList(row.split(valueSeparator));
            String id = elements.get(0);
            String courseName = elements.get(1);
            String instructor = elements.get(2);
            String courseDuration = elements.get(3);
            String courseTime = elements.get(4);
            String location = elements.get(5);


            if(id.equals(String.valueOf(courseID))){
                courseData[0]= id;
                courseData[1]= courseName;
                courseData[2]= instructor;
                courseData[3]= courseDuration;
                courseData[4]= courseTime;
                courseData[5]= location;
                break;
            }
        }

        return  courseData;
    }





    public static void saveToFile(String fileName,String data){
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
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        data = data.substring(0,data.length()-1);
        return data;
    }


    public static String getCourseDataFromCSV(){
        String path = "course_data.csv";
        String data = "";
        String allRows="";
        try {
            FileInputStream courseDataFIS= new FileInputStream(path);
            int size = courseDataFIS.available();
            byte[] b = new byte[size];
            courseDataFIS.read(b);
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

            courseDataFIS.close();

        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return allRows;
    }
}
