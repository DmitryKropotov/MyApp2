package com.tutorialspoint;

import java.io.PrintWriter;
import java.util.Random;
import java.util.concurrent.RecursiveTask;

public class CountSumWeightAnimals extends RecursiveTask<Integer> {
    private PrintWriter out;

    private int start;

    private int end;

    public CountSumWeightAnimals(PrintWriter out) {
        this.out = out;
    }

    public CountSumWeightAnimals(PrintWriter out, int start, int end) {
        this.out = out;
        this.start = start;
        this.end = end;
    }

    @Override
    protected Integer compute() {
        int result = 0;
        if(end - start < 3) {
            out.println();
            out.println("<h2>if: start is " + start + " end is " + end + "</h2>");
            for(int i = 0; i < end - start; i++) {
                out.println("<h2>compute weight for " + (i + start) + "</h2>");
                int weight = new Random().nextInt(100);
                out.println("<h2>weight is " + weight + "</h2>");
                result += weight;
                out.println("<h2>result in for loop in if is " + result + "</h2>");
            }
        } else {
            out.println();
            out.println("<h2>else: start is " + start + " end is " + end + "</h2>");
            int middle = (end + start)/2;
            RecursiveTask<Integer> otherTask = new CountSumWeightAnimals(out, start, middle);
            otherTask.fork();
            result += new CountSumWeightAnimals(out, middle, end).compute() + otherTask.join();
            out.println("<h2>result in the end of else is " + result + "</h2>");
        }
        return result;
    }
}
