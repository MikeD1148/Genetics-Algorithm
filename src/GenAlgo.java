import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

class GeneticAlgo {

    static ArrayList<Double> dataset;
    static double finalFitness = 0;


    static class Individual{

        public ArrayList<Double> chromosome;
        public ArrayList<Integer> binary;
        double fitness;

        public Individual() {

            ArrayList<Double> chromosome = new ArrayList<>();
            ArrayList<Integer> binary = new ArrayList<>();
                // add data set

            for (int i = 0; i <  25; i++) {
                int leftOrRight = (int)Math.round(Math.random());
                binary.add(leftOrRight);
                chromosome.add(dataset.get(i));
            }

            this.chromosome = chromosome;
            this.binary = binary;
            setFitness();
        }

        public void setFitness () {
            double res = 0, l = 0, r = 0;
            int size = dataset.size();
            for (int i = 0; i < size - 1; i++){
                if (binary.get(i) == 0)
                    l -= chromosome.get(i);
                if (binary.get(i) == 1)
                    r += chromosome.get(i);
            }
            res = l+r;
            res = Math.abs(res);
            res = (double)Math.round(res * 100000d) / 100000d;
            this.fitness = res;
        }

        public Individual copyGenes(Individual ind) {

            Individual res = new Individual();

            for (int i=0; i<ind.chromosome.size(); i++) {
                res.chromosome.set(i, ind.chromosome.get(i));
            }

            return res;
        }

        public void printChromosome () {
            System.out.print("\n"+binary+" "+fitness+"\n");
        }

    }

    class Population{

        public ArrayList<Individual> population = new ArrayList<Individual>();

        public Population (int popSize, int chromosomeSize) {

            for (int i=0; i<popSize; i++) {
                Individual ind = new Individual();
                this.population.add(ind);
            }
        }

        public void printPop() {

            for (Individual individual : population) {
                System.out.print(individual.binary + "\t");
                System.out.println(individual.fitness);
            }
        }

        public Individual crossOver (Individual p1, Individual p2, double rate) {

            int point = (int)(p1.binary.size()*rate);

            Individual res = new Individual();

            for (int i=0; i<point; i++) {
                res.binary.set(i, p1.binary.get(i)); //copy some genes from the parent
            }

            //For cross over I want to loop through (the size of the array multiplied by the cross over rate)
            for(int i = point; i < p2.binary.size(); i++) {
                res.binary.set(i, p2.binary.get(i)); //copy some genes from the parent
            }
            return res;
        }

        public Individual mutate (Individual p1, double rate) {
            Individual res = p1.copyGenes(p1); //we copy the parent's genes first

            //now we mutate the genes, using small change (swap genes by random)
            Random r = new Random();
            for(int n = 0; n != Math.round(p1.chromosome.size()*rate); n++) {
                int i = r.nextInt(p1.chromosome.size());
                int j = r.nextInt(p1.chromosome.size());

                //to avoid getting the same gene
                while (i == j) {
                    j = r.nextInt(p1.chromosome.size());
                }
                res.binary.set(i, p1.binary.get(j));
                res.binary.set(j, p1.binary.get(i));
            }
            res.setFitness();
            return res;
        }
    }

    public static void runGA() {

        //create a population object and parameters
        GeneticAlgo ga = new GeneticAlgo();
        int numGeneration = 30;
        int popSize = 3;
        double crossOverRate = 0.6;
        double mutationRate = 0.3;

        //prepare dataset
        String file = "data.csv";
        dataset = Data.readFile(file);
        int chromosomeSize = dataset.size();

        //initialise the population
        Population pop = ga.new Population(popSize, chromosomeSize); //create 10 candidates, each candidates has 5 genes (5 nodes), pass dataset to calculate fitness

        //We sort the candidates by fitness in ascending order, the least the better in this example (TSP)
        Collections.sort(pop.population,new CompareFitness()); //sorting the population by fitness (asc)
        System.out.println("====Before Search====");
        pop.printPop();

        for (int gen=0; gen<numGeneration; gen++) {

            System.out.println("Generation : "+gen);

            //get the parents - top 2 from the list
            Individual p1 = pop.population.get(0);
            Individual p2 = pop.population.get(1);

            //get 2 new children
            Individual ch1 = pop.crossOver(p1, p2,crossOverRate);
            ch1.setFitness();
            Individual ch2 = pop.crossOver(p2,p1, crossOverRate);
            ch2.setFitness();

            System.out.println("\nChild 1 : ");
            ch1.printChromosome();
            System.out.println("\nChild 2 : ");
            ch2.printChromosome();

            //get a mutate child
            Individual ch3 = pop.mutate(p1, mutationRate);
            ch3.setFitness();
            System.out.println("\nChild 3 : ");
            ch3.printChromosome();
            System.out.println("\n");

            //add these new children to the population
            pop.population.add(ch1);
            pop.population.add(ch2);
            pop.population.add(ch3);

            //sort them
            Collections.sort(pop.population,new CompareFitness()); //sorting the population by fitness

            //remove the weak candidate
            pop.population.remove(popSize);
            pop.population.remove(popSize);
            pop.population.remove(popSize);

            pop.printPop();

            String fileName = "C:\\Users\\micha\\OneDrive\\Documents\\Computer Science Degree Files\\Year 2\\Algorithms and Data Structures\\Assessment\\Project 5\\"+gen+".csv";

            Data.writeResult(fileName, pop);
            finalFitness = pop.population.get(0).fitness;

        }
        System.out.println("Generation : " + numGeneration + "\n\n====Result====");
        pop.printPop();
    }

    public static void main(String[] args) {
        runGA();
    }

}