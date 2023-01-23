import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Main {
    public static void main(String[] args) {

        String data = getStudentsData();
        saveToCSVFile(convertDStudentsDataToRows(data));
        System.out.println();


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

    public static String convertDStudentsDataToRows(String data){
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

    public static void saveToCSVFile(String data){
        try {
            FileOutputStream fos = new FileOutputStream("test.csv");

            fos.write(data.getBytes());
            fos.close();


        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}