
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;



public class Data {


    public static ArrayList<Double> readFile (String fileName){

        ArrayList<Double> res = new ArrayList<>();

        try {

            BufferedReader br;

            String line;

            int i=0;

            br = new BufferedReader (new FileReader (fileName));

            while ((line = br.readLine()) != null)
            {
                String [] column = line.split(",");

                for (String s : column) {
                    res.add(Double.parseDouble(s));
                }

                i++;
            }

        }
        catch(Exception e) {
            System.err.print("Error");
        }
        return res;
    }

    public static void writeResult(String file, GeneticAlgo.Population result) {

        try {
            FileWriter fw = new FileWriter(file);
            BufferedWriter bw = new BufferedWriter (fw);

            for (int i=0; i<result.population.size(); i++) {
                bw.write(Double.toString(result.population.get(i).fitness));
                bw.newLine();
            }
            bw.close();
            fw.close();

        }
        catch(Exception e) {
            System.err.print("Error");
        }


    }

}
