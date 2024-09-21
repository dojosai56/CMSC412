// Name: Sairam Soundararajan
// Date: 4-8-22
// Course: CMSC412: Operating Systems
// Homework 4: Banker's Algorithm

package SairamS;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.io.File;

public class BankersAlgorithm {
    private int need[][], allocation[][],max[][],available[][];
    private int numProcess,numResource;
    private String fileName;

    private void input(){

        List<String> lines = null;
        Scanner scanner = new Scanner(System.in);

        System.out.print("Please enter path of input file: ");
        String fileName = scanner.nextLine();

        try {
            lines = Files.readAllLines(Paths.get(fileName));
        } catch (IOException e) {
            System.out.println("Could not read file: " + fileName);
            System.exit(1);
        }

        numProcess = Integer.parseInt(lines.get(0));
        numResource = Integer.parseInt(lines.get(1));

        need = new int[numProcess][numResource];
        max = new int[numProcess][numResource];
        need = new int[numProcess][numResource];
        allocation = new int[numProcess][numResource];
        available = new int[1][numResource];

        for (int i = 0; i < numProcess; i++) {
            max[i] = stringToIntegerArray(lines.get(i + 2));
            allocation[i] = stringToIntegerArray(
                    lines.get(numProcess + 2 + i));
        }

        for (int i = 0; i < allocation.length; i++) {
            for (int j = 0; j < allocation[i].length; j++) {
                need[i][j] = max[i][j] - allocation[i][j];
            }
        }

        available[0] = stringToIntegerArray(lines.get(lines.size() - 1));

        scanner.close();
    }


    public static int[] stringToIntegerArray(String string) {
        String[] stringArray = string.split(" ");

        int[] intArray = new int[stringArray.length];

        for (int i = 0; i < stringArray.length; i++) {
            intArray[i] = Integer.parseInt(stringArray[i]);
        }

        return intArray;
    }

    private int[][] calc_need()
    {
        for(int counter = 0; counter < numProcess; counter++) {
            for (int count = 0; count < numResource; count++) {
                need[counter][count] = max[counter][count] - allocation[counter][count];
            }}
        return need;
    }

    private boolean check(int i)
    {
        for(int counter = 0; counter < numResource; counter++)
            if(available[0][counter] < need[i][counter])
                return false;

        return true;
    }

    public void isSafe() {
        input();
        calc_need();

        boolean complete[] = new boolean[numProcess];

        int count = 0;

        while(count<numProcess) {
            boolean allocated = false;

            for(int counter = 0; counter < numProcess; counter++) {
                if (!complete[counter] && check(counter)) {
                    for (int k = 0; k < numResource; k++) {
                        available[0][k] = available[0][k] + allocation[counter][k];
                    }
                    System.out.println("Allocated process : " + counter);
                    allocated = complete[counter] = true;
                    count++;

                    if (!allocated)
                        break;
                }
            }
        }

        if(count==numProcess) {
            System.out.println("\nSafely allocated");
        } else {
            System.out.println("All process cannot be allocated safely");
        }
    }



       public static void main(String[] args)
    {

        BankersAlgorithm bankers = new BankersAlgorithm();
        bankers.isSafe();
        bankers.calc_need();
        System.out.println(Arrays.deepToString(bankers.need));

    }
}
