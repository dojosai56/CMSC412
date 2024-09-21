import java.io.*;
import java.io.IOException;
import java.nio.file.*;
import java.util.Scanner;

public class HW6 {

    private boolean endFlag;
    private boolean hw6Flag;
    private File file = null;
    Scanner scan;

    public void setEndFlag(boolean endFlag) {
        this.endFlag = endFlag;
    }

    public static void main(String[] args) throws IOException {

        HW6 hw6 = new HW6();
        hw6.scan = new Scanner(System.in);
        hw6.setEndFlag(false);

        do {
            hw6.listOptions();
            System.out.println("Select option:");
            String input = hw6.scan.next();
            int valueInput;

            try {
                valueInput = Integer.parseInt(input);
                if (valueInput >= 0 && valueInput <= 7) {
                    hw6.optionSelect(valueInput);
                } else {
                    System.out.println("Input is not 0 to 7.");
                }
            } catch (NumberFormatException ne) {
                System.out.println("Input not a number, please continue.");
            }
        } while (!hw6.endFlag);
    }

    public void optionSelect(int option) throws IOException {
        switch (option) {
            case 0:
                setEndFlag(true);
                break;
            case 1:
                selectFolder();
                break;
            case 2:
                readFolder(file);
                break;
            case 3:
                readFolderAll(file, false, "");
                break;
            case 4:
                fileToDelete();
                break;
            case 5:
                readFileAsHex();
                break;
            case 6:
                fileToBytes(6);
                break;
            case 7:
                fileToBytes(7);
                break;
            default:
                System.out.println("Choose value from 0 to 7");
                break;
        }
    }

    public void listOptions() {

    }

    public void selectFolder() {

    }

    public void readFolderAll(File f, boolean dirSub, String tab) throws IOException {

    }

    public void readFolder(File f) throws IOException {
        if (!hw6Flag) {
            System.out.println("You must select a hw6 first.");
            return;
        }
        File[] files = f.listFiles();

        for (File file : files) {
            if (file.isFolder()) {
                System.out.print("hw6: ");
            } else {
                System.out.print("-file: ");
            }
            System.out.println(file.getCanonicalPath());
        }
    }

    public void fileToDelete() throws IOException {
        System.out.println("Input file to delete: ");
        String filename = scan.next();

        if (hw6Flag) {

            File fileToDelete = new File(file.getAbsolutePath() + "\\"
                    + filename);

            if (fileToDelete.delete()) {
                System.out.println(filename + " was deleted sucessfully.");
            } else {
                System.out.println("Failed to delete.");
            }
        } else {
            System.out.println("Please Select a hw6 using option 2 "
                    + "before attempting other steps.");
        }
    }

    public void readFileAsHex() throws IOException {
        FileInputStream readFile;
        String filename = scan.next();
        if (hw6Flag) {
            try {
                readFile = new FileInputStream(file.getAbsolutePath() + "\\" + filename);
                int len;
                byte data[] = new byte[16];

                do {
                    len = readFile.read(data);
                    for (int j = 0; j < len; j++) {
                        if (j % 20 == 0) {
                            System.out.print("\n");
                        }
                        System.out.printf("%02X ", data[j]);
                    }
                } while (len != -1);
            } catch (FileNotFoundException e) {
                System.out.println("Select a hw6 using Option 2 "
                        + "before attempting another step.");
            }
            System.out.print("\n\n");
        } else {
            System.out.println("Select a folder using Option 2 "
                    + "before attempting another step.");
        }
    }

    public void fileToBytes(int optionSelection) throws IOException {
        System.out.println("Please input filename:");

        String filename = scan.next();

        if (hw6Flag) {
            try {
                Path path = Paths.get(file.getAbsolutePath() + "\\" + filename);
                byte[] byteArray = Files.readAllBytes(path);
                System.out.println(filename + " found.");

                String password;

                do {
                    System.out.println("Enter password:");
                    password = scan.next();
                    if (password.getBytes().length > 256) {
                        System.out.println("Password can not be more than 256 bytes.");
                    }
                } while (password.getBytes().length > 256);
                byte[] passwordArray = password.getBytes();
                if (optionSelection == 6) {
                    encryptFile(byteArray, passwordArray, filename);
                } else {
                    decryptFile(byteArray, passwordArray, filename);
                }
            } catch (FileNotFoundException e) {
                System.out.println("The file was not found... Returning to menu.");
            }
        } else {
            System.out.println("A folder was not selected.");
        }

    }

    public void encryptFile(byte[] byteArray, byte[] passwordArray, String filename)
            throws IOException {
        try {
            int j = 0;

            for (int i = 0; i < byteArray.length; i++) {
                if (j > passwordArray.length - 1) {
                    j = 0;
                }
                byteArray[i] = (byte) (byteArray[i] ^ passwordArray[j]);
                j++;
            }

            FileOutputStream stream = new FileOutputStream(file.getAbsolutePath()
                    + "\\" + "ENC-" + filename);
            try {
                stream.write(byteArray);
            } finally {
                stream.close();
            }
        } catch (IOException e) {
            System.out.println("Your file was not found, returning to menu.");
        }
    }

    public void decryptFile(byte[] byteArray, byte[] passwordArray, String filename) throws IOException {
        try {
            int j = 0;
            for (int i = 0; i < byteArray.length; i++) {
                if (j > passwordArray.length - 1) {
                    j = 0;
                }
                byteArray[i] = (byte) (passwordArray[j] ^ byteArray[i]);
                j++;
            }
            FileOutputStream stream = new FileOutputStream(file.getAbsolutePath() + "\\" + filename);
            try {
                stream.write(byteArray);
            } finally {
                stream.close();
            }
        } catch (IOException e) {
            System.out.println("Your file was not found, returning to menu.");
        }
    }

}
