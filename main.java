package org.example;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.NotFoundException;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import java.util.logging.Level;
import java.util.logging.Logger;

//Class 1
class Haiku {
    private String line1;
    private String line2;
    private String line3;
    private String title;
    private int[] numSyllables;

    public Haiku() {
        this.line1 = "I wish you would have";
        this.line2 = "Typed something so that this would";
        this.line3 = "Not be as boring.";
        this.title = "Untitled";
    }

    public Haiku(String line1, String line2, String line3, String title) {
        this.line1 = line1;
        this.line2 = line2;
        this.line3 = line3;
        this.title = title;
    }

    public String getLine1() {
        return line1;
    }

    public void setLine1(String line1) {
        this.line1 = line1;
    }

    public String getLine2() {
        return line2;
    }

    public void setLine2(String line2) {
        this.line2 = line2;
    }

    public String getLine3() {
        return line3;
    }

    public void setLine3(String line3) {
        this.line3 = line3;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int[] getNumSyllables() {
        return numSyllables;
    }

    public void setNumSyllables(int[] arr) {
        this.numSyllables = arr;
    }

    public void inputLines() {
        Scanner input = new Scanner(System.in);
        System.out.println("Enter line 1: ");
        setLine1(input.nextLine());
        System.out.println("\nEnter line 2: ");
        setLine2(input.nextLine());
        System.out.println("\nEnter line 3: ");
        setLine3(input.nextLine());
        System.out.println("\nEnter a Title for your Haiku: ");
        setTitle(input.nextLine());
    }

    public boolean isValidFormat() {
        if(this.numSyllables[0] == 5 && this.numSyllables[1] == 7 && this.numSyllables[2] == 5)
            return true;
        return false;
    }

    public static String[] Convert(String str) {

        String[] words = str.split("[^A-Za-z']+");

        return words;
    }

    public static int countSyllables(String str) {
        int count = 0;
        //creates matcher to find regex expression, algorithm explained in paper
        Matcher m = Pattern.compile("(?i)[aiouy][aeiouy]*|e[aeiouy]*(?!d?s?\\b)").matcher(str);

        while (m.find()) {
            count++;
        }
        //returns a minimum of 1 (for words like the
        return Math.max(count, 1);
    }

    public static int NumberOfSyllables(String[] str) {
        int num = 0;

        for(int i = 0; i < str.length; i++)
            num += countSyllables(str[i]);

        return num;
    }

    //recursive function to add syllables per line
    public static int[] SyllablesPerLine(int[] arr, int i, String line1, String line2, String line3) {
        if(i > arr.length)
            return arr;
        else if(i == 0)
        {
            arr[i] = Haiku.NumberOfSyllables(Haiku.Convert(line1));
            return Haiku.SyllablesPerLine(arr, (i+1), line1, line2, line3);
        }
        else if (i == 1)
        {
            arr[i] = Haiku.NumberOfSyllables(Haiku.Convert(line2));
            return Haiku.SyllablesPerLine(arr, (i+1), line1, line2, line3);
        }
        else if (i == 2)
        {
            arr[i] = Haiku.NumberOfSyllables(Haiku.Convert(line3));
            return Haiku.SyllablesPerLine(arr, (i+1), line1, line2, line3);
        }
        return arr;
    }

    public void printHaiku() {
        System.out.println("\nGreat Success! Here is your correctly formatted haiku!\n");
        System.out.println("''" + this.title + "''");
        System.out.println(this.line1);
        System.out.println(this.line2);
        System.out.println(this.line3);
        System.out.println("");
    }
}
//Class 2
class QRClass {
    private String data;
    private String fileName;
    private String fileType;
    private String charset;
    private int height;
    private int width;

    public QRClass() {
        this.data = null;
        this.fileName = null;
        this.fileType = ".png";
        this.charset = "UTF-8";
        this.height = 200;
        this.width = 200;
    }

    public QRClass(String content, String name) {
        this.data = content;
        this.fileName = name + this.fileType;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getCharSet() {
        return charset;
    }

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }

    public static Map encodeMap() {
        Map<EncodeHintType, ErrorCorrectionLevel> hashMap
                = new HashMap<EncodeHintType,
                ErrorCorrectionLevel>();

        hashMap.put(EncodeHintType.ERROR_CORRECTION,
                ErrorCorrectionLevel.L);

        return hashMap;
    }

    public void createQR(String data, String path,
                         String charset, Map hashMap,
                         int height, int width)
            throws WriterException, IOException
    {
        BitMatrix matrix = new MultiFormatWriter().encode(
                new String(data.getBytes(charset), charset),
                BarcodeFormat.QR_CODE, width, height);

        MatrixToImageWriter.writeToFile(
                matrix,
                path.substring(path.lastIndexOf('.') + 1),
                new File(path));
    }

}
//Main
public class Main {
    public static void main(String[] args) throws WriterException {
        String choice, qrChoice;
        Scanner input = new Scanner(System.in);
        int[] syllablesPerLine = new int[3];

        System.out.println("Welcome to Haiku4u"
                + "\nA Haiku is form of poetry originating in Japan"
                + "\nIt consists of 17 syllables in three lines of five, seven, five"
                + "\nAnd evokes images of the natural world."
                + "\nAt Haiku 4 U, we're here to help you craft haikus."
                + "\nPlease enter in your Haiku line by line and we will"
                + "\nCheck to make sure you are following the correct format.");
        do {
            Haiku haiku = new Haiku();
            haiku.inputLines();
            haiku.SyllablesPerLine(syllablesPerLine, 0, haiku.getLine1(), haiku.getLine2(), haiku.getLine3());
            haiku.setNumSyllables(syllablesPerLine);

            if (haiku.isValidFormat())
            {
                haiku.printHaiku();
                System.out.println("Do you wish to print a QR Code of your Haiku? (Y/y)");
                qrChoice = input.nextLine();
                if("Y".equals(qrChoice) || "y".equals(qrChoice))
                {
                    QRClass qr = new QRClass();
                    String content = haiku.getLine1();
                    content += "\n" + haiku.getLine2();
                    content += "\n" + haiku.getLine3();
                    String filename = haiku.getTitle();
                    filename += ".png";
                    try {
                        qr.createQR(content, filename, "UTF-8", QRClass.encodeMap(), 200, 200);
                    } catch (IOException ex) {
                        Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
            else
            {
                System.out.println("Sorry, your haiku has an invalid format.");
                System.out.println("Please check your haiku and try again.");
                System.out.println("Here is the line structure of your haiku: ");
                System.out.println(Arrays.toString(haiku.getNumSyllables()));
            }

            System.out.println("Do you wish to enter another haiku? (Y/y)");
            choice = input.nextLine();
        }while("Y".equals(choice) || "y".equals(choice));
    }
}
