package com.tutorialspoint;

import java.io.PrintWriter;
import java.util.Random;
import java.util.concurrent.RecursiveAction;

public class WeightAnimals extends RecursiveAction {

    private PrintWriter out;

    private int[] animalWeight;

    private int start;

    private int end;

    public WeightAnimals(PrintWriter out, int[] animalWeight) {
        this.out = out;
        this.animalWeight = animalWeight;
    }

    public WeightAnimals(PrintWriter out, int[] animalWeight, int start, int end) {
        this.out = out;
        this.animalWeight = animalWeight;
        this.start = start;
        this.end = end;
    }

    @Override
    protected void compute() {
       if(end - start < 3) {
           for(int i = 0; i < end - start; i++) {
               out.println("<h2>compute weight for " + i + "</h2>");
               out.println("");
               animalWeight[i + start] = new Random().nextInt(100);
           }
       } else {
           int middle = (end + start)/2;
           invokeAll(new WeightAnimals(out, animalWeight, start, middle), new WeightAnimals(out, animalWeight, middle, end));
       }
    }
}
