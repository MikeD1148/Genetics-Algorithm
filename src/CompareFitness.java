
public class CompareFitness implements java.util.Comparator {

    public int compare(Object a, Object b)
    {
        if (((GeneticAlgo.Individual)a).fitness < ((GeneticAlgo.Individual)b).fitness) {
            return(-1);
        }
        if (((GeneticAlgo.Individual)a).fitness > ((GeneticAlgo.Individual)b).fitness) {
            return (1);
        } else {
            return(0);
        }
    }

}

