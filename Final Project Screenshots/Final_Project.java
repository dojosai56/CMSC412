// Name: Sairam Soundararajan
// Date: 5-4-22
// Course: CMSC412: Operating Systems
// Prof. Schmeelk

import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class Final_Project {

    //Global variables
    //Most will be used by most methods.
    static Scanner inputFile = new Scanner(System.in);
    static int numFrames, entries;
    static int numArray[][];
    static int[] framesArray, pointArray, freqArray;
    static String[] faultArray;

    //Main method just takes the argument as assigns it to numFrames
    //Then starts the menu loop.
    public static void main(String args[]) {

        try{
            System.out.println("Please enter the number of frames as an argument.");
            numFrames = Integer.parseInt(String.valueOf(4));
            System.out.println(numFrames + " Frames");
            menu();
        }catch(NumberFormatException e){
            System.out.println("The number format is NOT valid");
        }catch(ArrayIndexOutOfBoundsException e){
            System.out.println("Input is NOT valid");
        }
    }

    //method will loop through the options that the user can run.
    private static void menu() {
        //Loop through the menu choices
        while (true){
            //printMenu() will print the menu in text form.
            printMenu();
            String selection = inputFile.next();

            try{
                //Attempt to convert user inputFile to an int.
                int selNum = Integer.parseInt(selection);
                if (selNum < 10 && selNum >=0){
                    //If it passes the tests, run switchcase on it.
                    switchcase(selNum);
                }
                else{
                    throw new NumberFormatException();
                }
            }catch(NumberFormatException e){
                //If it doesn't pass the tests, display this error.
                System.out.println("Please enter a valid number.");
                //pressEnter() is a method to pause until the user presses enter.
                pressEnter();
            }
        }

    }

    private static void pressEnter() {
        System.out.println("\nPress enter to continue");
        Scanner pressEnter = new Scanner(System.in);
        String blank = pressEnter.nextLine();
        pressEnter = null;
    }

    //switchcase method takes the user entered number, and runs the appropriate method.
    private static void switchcase(int num) {
        switch(num){
            case 0: exit(); break;
            case 1: manualEntry(); break;
            case 2: randomEntry(); break;
            case 3: displayEnter(); break;
            case 4: fifo(); break;
            case 5: opt(); break;
            case 6: lru(); break;
            case 7: lfu(); break;

        }

    }

    //manual entry method asks the user to inputFile a number without spaces.
    //It will then split that number into an array of one digit numbers.
    //It will also validate the inputFile to ensure it can be used.
    private static void manualEntry(){
        System.out.println("Please enter the series of frames.\nDo not include spaces.");
        try{
            //Take in the inputFile and split into a String Array
            String entry = inputFile.next();
            String[] entryArray = entry.split("");

            //entryArray[0] is empty, so int array will be 1 less
            framesArray = new int[entryArray.length-1];

            System.out.println("You have entered: ");

            //skip entryArray[0], and the rest convert to integer array.
            for(int i=1; i<entryArray.length; i++){
                framesArray[i-1] = Integer.parseInt(entryArray[i]);
                System.out.print(framesArray[i-1] + " ");
            }
            //set the entries value to be the array length
            //entries variable is to be used by other methods
            entries = framesArray.length;
            pressEnter();
        }catch(NumberFormatException e){
            //If it is an error, display error
            System.out.println("Error, Please enter numbers with no spaces.");
            pressEnter();
        }
    }

    //RandomEntry creates an array of 20 one-digit variables.
    private static void randomEntry(){
        //Declare the array and random class
        framesArray = new int[20];
        Random rand = new Random();
        //Fill the array with random digits 0 through 9.
        for(int i=0; i<20; i++){
            framesArray[i] = rand.nextInt(10);
        }
        entries = 20;
        displayEnter();
    }

    //This method displays the current reference string.
    //Return true if referrence string is set.
    //Return false if the ref string isn't set yet.
    private static boolean displayEnter(){
        System.out.print("The current referrence string is: ");
        try{
            if(framesArray == null){
                //If the array is blank, then the user never
                //entered a ref string.  Throw error.
                throw new NumberFormatException();
            }
            //Display the current ref string one digit at a time.
            for(int i=0; i<framesArray.length; i++){
                System.out.print(framesArray[i] + " ");

            }
            pressEnter();
            return true;
        }catch(NumberFormatException e){
            System.out.println("Reference String not Set.");
            pressEnter();
            return false;
        }
    }

    //FIFO will replace the oldest physical memory used.
    private static void fifo(){
        System.out.println("Beginning FIFO algorithm.");

        //If the reference string is true.
        if(displayEnter()){

            //initArray will initialize all the arrays and set default values.
            initArray();

            //for each place in the entries array, test where the reference string value can go.
            for(int j=0; j<entries; j++){

                //Short statement to copy last steps results to current step.
                if(j>0){
                    for(int i=0; i<numFrames; i++){
                        numArray[i][j] = numArray[i][j-1];
                    }
                }

                //If it is a hit, do nothing.
                //Else, do a fault
                if(!isHit(j)){

                    //Figure out with frame to replace
                    //Get the frame with the highest points.
                    //(They get 2 points each time their frame isn't replaced.
                    //if there frame is replaced, there points go to zero.)
                    int tempHighest = 0;
                    for(int k=0; k<numFrames; k++){
                        if(pointArray[k]>pointArray[tempHighest]){
                            tempHighest = k;
                        }

                        //Increment the point array by 1.
                        pointArray[k] = (pointArray[k])+1;
                    }

                    //Now the frame to replace is stored as tempHighest.
                    //Set that frames points to zero
                    pointArray[tempHighest] = 0;

                    //replace that frame with the user entry for the step
                    numArray[tempHighest][j] = framesArray[j];
                    faultArray[j] = "F";

                }

            }

            //printStep() method will print out the
            // results array one step at a time.
            printStep();
        }
    }

    //LFU replaces the least frequently used frame.
    //To do this, it uses the freqArray[10] variables which
    //has 0-9 and keeps the number of times it is used in the array.
    //Which ever frame has the lowest points in freqArray, gets replaced.
    private static void lfu(){
        System.out.println("Beginning LFU algorithm.");

        //If the reference string is true.
        if(displayEnter()){

            //initArray will initialize all the arrays and set default values.
            initArray();

            //for each place in the entries array, test where the reference string value can go.
            for(int j=0; j<entries; j++){

                //Short statement to copy last steps results to current step.
                if(j>0){
                    for(int i=0; i<numFrames; i++){
                        numArray[i][j] = numArray[i][j-1];
                    }
                }

                //If it is a hit, reset physical frame.
                if(!isHit(j)){

                    //Figure out with frame to replace
                    //Get the frame with the lowest frequency.
                    // if all freq are the same get the one with the most points (FIFO)
                    //(They get 2 points each time their frame isn't replaced.
                    //if there frame is replaced, there points go to zero.)
                    int freqLowest = 0;
                    for(int k = 0; k < numFrames; k++)
                    {
                        if(freqArray[k] < freqArray[freqLowest])
                        {
                            freqLowest = freqArray[k];
                        }
                        //Increment the point array by 1.
                        pointArray[k] = (pointArray[k])+1;
                    }

                    // find all frames with lowest frequency & replace the
                    // one with the highest points
                    int tempHighest = 0;
                    for(int k=0; k<numFrames; k++){
                        if(freqArray[k] == freqLowest) {
                            if (pointArray[k] > pointArray[tempHighest]) {
                                tempHighest = k;
                            }
                        }
                        //Increment the point array by 1.
                        pointArray[k] = (pointArray[k])+1;
                    }

                    //Now the frame to replace is stored as tempHighest.
                    //Set that frames points to zero
                    pointArray[tempHighest] = 0;
                    // Set that frames freq to 0
                    freqArray[tempHighest] = 0;

                    //replace that frame with the user entry for the step
                    numArray[tempHighest][j] = framesArray[j];
                    faultArray[j] = "F";

                }
                else{
                    // increment everything and reset the one that was hit
                    // that way the frame with the least amount of points
                    // was the one most recently used
                    int frameHit = 0;
                    for(int i = 0; i < numFrames; i++)
                    {
                        if(numArray[i][j] == framesArray[j])
                        {
                            frameHit = i;
                        }
                    }
                    // increment frequency array for the one that was hit
                    freqArray[frameHit] += 1;
                } //else


            }

            //printStep() method will print out the results array one step at a time.
            printStep();
        }


    }

    //OPT goes through the physical frames to find how far away it is to getting a hit.
    //Then replaces the furthest out one.
    private static void opt() {
        System.out.println("Beginning OPT algorithm.");

        // If the reference string is true.
        if (displayEnter()) {

            // initArray will initialize all the arrays and set default values.
            initArray();

            // for each place in the entries array, test where the reference string value
            // can go.
            for (int j = 0; j < entries; j++) {

                // Short statement to copy last steps results to current step.
                if (j > 0) {
                    for (int i = 0; i < numFrames; i++) {
                        numArray[i][j] = numArray[i][j - 1];
                    }
                }

                // If it is a hit, do nothing.
                // Else, do a fault
                if (!isHit(j)) {

                    // loop through refrence frames
                    // when a future frame is found add it to the framesToExclude list
                    // until framesToExclude size == numFrames -1 or we have searched through all refrence
                    // frames
                    // this way the farthest frame can be replaced. Or if multiple frames will never be hit in the future
                    // LIFO will decide which of those frames get replaced
                    ArrayList<Integer> framesToExclude = new ArrayList<Integer>();
                    int count = j;
                    while(count < framesArray.length && framesToExclude.size() < numFrames-1) {
                        for(int i = 0; i < numFrames;i++) {
                            if(framesArray[count] == numArray[i][j]) {
                                framesToExclude.add(numArray[i][j]);
                            }
                        }
                        count++;
                    }

                    // Figure out with frame to replace
                    // Excluding frames in the framesToExclude list
                    // Get the frame with the highest points.
                    // (They get 2 points each time their frame isn't replaced.
                    // if there frame is replaced, there points go to zero.)
                    int tempHighest = 0;
                    for (int k = 0; k < numFrames; k++) {
                        // Increment the point array by 1.
                        pointArray[k] = (pointArray[k]) + 1;

                        if(framesToExclude.contains(numArray[k][j]))
                            continue; // skip frame if it is in framesToExclude list

                        // otherwise find oldest frame
                        if (pointArray[k] > pointArray[tempHighest]) {
                            tempHighest = k;
                        }

                    }

                    // Now the frame to replace is stored as tempHighest.
                    // Set that frames points to zero
                    pointArray[tempHighest] = 0;

                    // replace that frame with the user entry for the step
                    numArray[tempHighest][j] = framesArray[j];
                    faultArray[j] = "F";

                }

            }

            // printStep() method will print out the results array one step at a time.
            printStep();
        }

    }



    //LRU is like FIFO, except it resets the physical frames points to zero if it is a hit.
    private static void lru(){

        System.out.println("Beginning LRU algorithm.");

        //If the reference string is true.
        if(displayEnter()){

            //initArray will initialize all the arrays and set default values.
            initArray();

            //for each place in the entries array, test where the reference string value can go.
            for(int j=0; j<entries; j++){

                //Short statement to copy last steps results to current step.
                if(j>0){
                    for(int i=0; i<numFrames; i++){
                        numArray[i][j] = numArray[i][j-1];
                    }
                }

                //If it is a hit, reset physical frame.
                if(!isHit(j)){

                    //Figure out with frame to replace
                    //Get the frame with the highest points.
                    //(They get 2 points each time their frame isn't replaced.
                    //if there frame is replaced, there points go to zero.)
                    int tempHighest = 0;
                    for(int k=0; k<numFrames; k++){
                        if(pointArray[k]>pointArray[tempHighest]){
                            tempHighest = k;
                        }

                        //Increment the point array by 1.
                        pointArray[k] = (pointArray[k])+1;
                    }

                    //Now the frame to replace is stored as tempHighest.
                    //Set that frames points to zero
                    pointArray[tempHighest] = 0;

                    //replace that frame with the user entry for the step
                    numArray[tempHighest][j] = framesArray[j];
                    faultArray[j] = "F";

                }
                else{
                    // increment everything and reset the one that was hit
                    // that way the frame with the least amount of points
                    // was the one most recently used
                    int frameHit = 0;
                    for(int i = 0; i < numFrames; i++)
                    {
                        pointArray[i] += (pointArray[i]) + 1;
                        if(numArray[i][j] == framesArray[j])
                        {
                            frameHit = i;
                        }
                    }
                    // reset the one that was hit ('used')
                    pointArray[frameHit] = 0;
                } //else


            }

            //printStep() method will print out the results array one step at a time.
            printStep();
        }

    }


    //Short method to see if it is a hit.
    //Returns true if it is a hit.
    //Returns false if it is a fault.
    private static boolean isHit(int j){
        for(int i=0; i<numFrames; i++){
            if(framesArray[j] == numArray[i][j]){
                return true;
            }
        }
        return false;
    }

    //Initialize the arrays with default values.
    private static void initArray(){
        numArray = new int [numFrames][entries];
        for(int i=0; i<numFrames; i++){
            for(int j=0; j<entries; j++){
                numArray[i][j] = -1;
            }
        }
        faultArray = new String[entries];
        for(int k=0; k<entries; k++){
            faultArray[k] = " ";
        }

        pointArray = new int[numFrames];
        for(int i=0; i<numFrames; i++){
            pointArray[i] = 1;
        }

        freqArray = new int[10];
        for(int i=0; i<10; i++){
            freqArray[i] = 0;
        }

    }

    //Prints out the numArray one step at a time.
    //Per the instructions.
    private static void printStep(){
        for(int h=0; h<entries; h++){
            System.out.println("Step number " + (h+1));

            //Print out the reference string above the results.
            System.out.print("Reference:");
            for(int k=0; k<entries; k++){
                System.out.print(framesArray[k] + " ");
            }
            System.out.print("\n");

            //Then print the numArray below only up to the current step.
            for(int i=0; i<numFrames; i++){
                System.out.print("Frame #" + i + ": ");
                for(int j=0; j<=h; j++){
                    if(numArray[i][j] == -1){
                        System.out.print("  ");
                    }
                    else{
                        System.out.print(numArray[i][j] + " ");
                    }
                }
                System.out.print("\n");
            }

            //Then print out if it is a fault below that.
            System.out.print("Faults:   ");
            for(int l=0; l<=h; l++){
                System.out.print(faultArray[l] + " ");
            }
            System.out.print("\n");

            //Finally, pause after every step.
            pressEnter();
        }

        //After everything is complete, print out the total number of faults.
        int totalFaults = 0;
        for(int l=0; l<entries; l++){
            if(faultArray[l] == "F"){
                totalFaults += 1;
            }
        }
        System.out.println("Total number of Faults is : " + totalFaults);

    }

    //Print the menu string.
    //This method was just to make it easier to write the menu() method without taking up much space.
    private static void printMenu() {
        String line1 = "Please make a selection: \n";
        String line2 = "0 : Exit\n";
        String line3 = "1 : Enter a reference string\n";
        String line4 = "2 : Generate a random reference string\n";
        String line5 = "3 : Display current referance string\n";
        String line6 = "4 : Simulate FIFO\n";
        String line7 = "5 : Simulate OPT\n";
        String line8 = "6 : Simulate LRU\n";
        String line9 = "7 : Simulate LFU\n";
        String line10 = "Please enter your selection:\n";

        String total = line1 + line2 + line3 + line4 + line5 + line6 + line7 + line8 + line9 + line10;
        System.out.println(total);

    }

    //The exit method.  Closes scanner and then exits.
    public static void exit(){
        inputFile.close();
        System.out.println("Good bye.");
        System.exit(0);
    }
}
