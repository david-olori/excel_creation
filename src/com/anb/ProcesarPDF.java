package com.anb;

import com.itextpdf.text.pdf.PdfName;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.parser.*;

import java.io.FileOutputStream;
import java.io.PrintWriter;

/**
 * Created with IntelliJ IDEA.
 * User: davidolori
 * Date: 1/11/15
 * Time: 11:54 AM
 * To change this template use File | Settings | File Templates.
 */
public class ProcesarPDF {


    public static void main(String args[])
    {
       try{

           System.out.println("Encodign ="+System.getProperty("file.encoding"));
           //System.setProperty("file.encoding","Cp1252") ;

           System.setProperty("file.encoding","WinAnsiEncoding");

           String pdf="/Volumes/Hybrid/temp/aduana/GUIDE_2014.pdf";
           String txt="/Volumes/Hybrid/temp/aduana/TXT_GUIDE_2014.txt";

           PdfReader reader = new PdfReader(pdf);
           PdfReaderContentParser parser = new PdfReaderContentParser(reader);
           PrintWriter out = new PrintWriter(new FileOutputStream(txt));
           TextExtractionStrategy strategy;
           System.out.println("=======PDF PROCESOR========");
           System.out.println("NRO pages PDF a procesar  = " + reader.getNumberOfPages());

/*
           strategy = parser.processContent(86, new SimpleTextExtractionStrategy());
           out.println(strategy.getResultantText()); */

           //for (int i = 82; i <= 1415; i++) {

           //String original_content = PdfTextExtractor.getTextFromPage(reader, 82, new SimpleTextExtractionStrategy());
           //System.out.println(" original content = " + original_content);

           for (int i = 81; i <= 82; i++) {

               strategy = parser.processContent(i, new MaquinariaTextExtractionStrategy());


               //SimpleTextExtractionStrategy rs = new SimpleTextExtractionStrategy();

               //strategy = parser.processContent(i, new LocationTextExtractionStrategy());
               //out.println(PdfTextExtractor.getTextFromPage(reader, i));
               out.println(strategy.getResultantText());

           }
           out.flush();
           out.close();
           reader.close();

       }catch(Exception e){

           System.out.print(" ERROR PDF = "  + e.getMessage());
           e.printStackTrace();

       }



    }
}
