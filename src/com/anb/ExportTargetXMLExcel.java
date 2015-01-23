package com.anb;



import org.apache.commons.lang.StringEscapeUtils;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.util.CellReference;
import org.apache.poi.xssf.usermodel.*;

import java.io.*;
import java.util.Calendar;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;



/**
 * Created with IntelliJ IDEA.
 * User: davidolori
 * Date: 1/13/15
 * Time: 12:01 AM
 * To change this template use File | Settings | File Templates.
 */
public class ExportTargetXMLExcel implements ExportTarget {



        private XSSFWorkbook wb;
        private XSSFSheet sheet;
        private FileOutputStream os;
        private File tmp;
        private Writer fw;
        private OutputStream _os;
        private Map<String, XSSFCellStyle> styles;
        private String sheetRef;
        private SpreadsheetWriter sw;
        private int rownum;
        private String realPath;

        public ExportTargetXMLExcel(OutputStream os) {
            wb = new XSSFWorkbook();
            sheet = wb.createSheet();
            this.rownum=0;
            _os = os;
            this.realPath = "";
        }

        public ExportTargetXMLExcel(OutputStream os, String realPath) {
            wb = new XSSFWorkbook();
            sheet = wb.createSheet();
            this.rownum=0;
            _os = os;
            this.realPath = realPath;
        }

        public void begin(AttributeInfo[] attributes) {
            try{
                styles = createStyles(wb);
                sheetRef = sheet.getPackagePart().getPartName().getName();

                //save the template
                File file = new File(realPath+"\\scrap\\integrations\\template.xlsx");
                os = new FileOutputStream(file);
                wb.write(os);
                os.close();
                tmp = File.createTempFile("sheet", ".xml");
                fw = new FileWriter(tmp);

                sw = new SpreadsheetWriter(fw);
                sw.beginSheet();

                //insert header
                sw.insertRow(0);
                int styleIndex = styles.get("header").getIndex();
                for (short i = 0; i < attributes.length; i++){
                    sw.createCell(i,attributes[i].getDisplayName(),styleIndex);
                }
                sw.endRow();

            }catch (IOException e1){
                System.out.println(e1.getMessage());
            }
            catch (Exception e){
                e.printStackTrace();
            }
        }

        public boolean writeData(AttributeInfo[] attributes, Map data) {
            rownum++;
            try{
                sw.insertRow(rownum);
                for (int i = 0; i < attributes.length; i++){
                    Object value = data.get(attributes[i].getName());
                    if(value == null)
                        value="";
                    sw.createCell(i,value.toString());
                }
                sw.endRow();
                return true;
            }catch (Exception e){
                System.out.println(e.getMessage());
                return false;
            }
        }

        public void end() {
            try{
                sw.endSheet();
                fw.close();
                substitute(new File(realPath+"\\scrap\\integrations\\template.xlsx"), tmp, sheetRef.substring(1), _os);
                _os.close();
            }catch (Exception e){
                e.printStackTrace();
            }
        }

        /**
         * Create a library of cell styles.
         */
        private static Map<String, XSSFCellStyle> createStyles(XSSFWorkbook wb){
            Map<String, XSSFCellStyle> styles = new HashMap<String, XSSFCellStyle>();
            XSSFDataFormat fmt = wb.createDataFormat();

            XSSFCellStyle style1 = wb.createCellStyle();
            style1.setAlignment(XSSFCellStyle.ALIGN_RIGHT);
            style1.setDataFormat(fmt.getFormat("0.0%"));
            styles.put("percent", style1);

            XSSFCellStyle style2 = wb.createCellStyle();
            style2.setAlignment(XSSFCellStyle.ALIGN_CENTER);
            style2.setDataFormat(fmt.getFormat("0.0X"));
            styles.put("coeff", style2);

            XSSFCellStyle style3 = wb.createCellStyle();
            style3.setAlignment(XSSFCellStyle.ALIGN_RIGHT);
            style3.setDataFormat(fmt.getFormat("$#,##0.00"));
            styles.put("currency", style3);

            XSSFCellStyle style4 = wb.createCellStyle();
            style4.setAlignment(XSSFCellStyle.ALIGN_RIGHT);
            style4.setDataFormat(fmt.getFormat("mmm dd"));
            styles.put("date", style4);

            XSSFCellStyle style5 = wb.createCellStyle();
            XSSFFont headerFont = wb.createFont();
            headerFont.setBold(true);
            style5.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
            style5.setFillPattern(XSSFCellStyle.SOLID_FOREGROUND);
            style5.setFont(headerFont);
            styles.put("header", style5);

            return styles;
        }

        /**
         *
         * @param zipfile the template file
         * @param tmpfile the XML file with the sheet data
         * @param entry the name of the sheet entry to substitute, e.g. xl/worksheets/sheet1.xml
         * @param out the stream to write the result to
         */
        private static void substitute(File zipfile, File tmpfile, String entry, OutputStream out) throws IOException {
        /*System.out.println("PATH2: "+zipfile.getAbsolutePath());*/
            ZipFile zip = new ZipFile(zipfile);

            ZipOutputStream zos = new ZipOutputStream(out);

            @SuppressWarnings("unchecked")
            Enumeration<ZipEntry> en = (Enumeration<ZipEntry>) zip.entries();
            while (en.hasMoreElements()) {
                ZipEntry ze = en.nextElement();
                if(!ze.getName().equals(entry)){
                    zos.putNextEntry(new ZipEntry(ze.getName()));
                    InputStream is = zip.getInputStream(ze);
                    copyStream(is, zos);
                    is.close();
                }
            }
            zos.putNextEntry(new ZipEntry(entry));
            InputStream is = new FileInputStream(tmpfile);
            copyStream(is, zos);
            is.close();

            zos.close();
        }

        private static void copyStream(InputStream in, OutputStream out) throws IOException {
            byte[] chunk = new byte[1024];
            int count;
            while ((count = in.read(chunk)) >=0 ) {
                out.write(chunk,0,count);
            }
        }

        /**
         * Writes spreadsheet data in a Writer.
         * (YK: in future it may evolve in a full-featured API for streaming data in Excel)
         */
        public static class SpreadsheetWriter {
            private final Writer _out;
            private int _rownum;

            public SpreadsheetWriter(Writer out){
                _out = out;
            }

            public void beginSheet() throws IOException {
                _out.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
                        "<worksheet xmlns=\"http://schemas.openxmlformats.org/spreadsheetml/2006/main\">" );
                _out.write("<sheetData>\n");
            }

            public void endSheet() throws IOException {
                _out.write("</sheetData>");
                _out.write("</worksheet>");
            }

            /**
             * Insert a new row
             *
             * @param rownum 0-based row number
             */
            public void insertRow(int rownum) throws IOException {
                _out.write("<row r=\""+(rownum+1)+"\">\n");
                this._rownum = rownum;
            }

            /**
             * Insert row end marker
             */
            public void endRow() throws IOException {
                _out.write("</row>\n");
            }

            public void createCell(int columnIndex, String value, int styleIndex) throws IOException {
                value = value.replaceAll("\u000E"," ");  // escape unicode characters make a method if have more cases or set encoding in out file
                value = StringEscapeUtils.escapeXml(value);
                String ref = new CellReference(_rownum, columnIndex).formatAsString();
                _out.write("<c r=\""+ref+"\" t=\"inlineStr\"");
                if(styleIndex != -1)
                    _out.write(" s=\""+styleIndex+"\"");
                _out.write(">");
                _out.write("<is><t>"+value+"</t></is>");
                _out.write("</c>");
            }

            public void createCell(int columnIndex, String value) throws IOException {
                createCell(columnIndex, value, -1);
            }

            public void createCell(int columnIndex, double value, int styleIndex) throws IOException {
                String ref = new CellReference(_rownum, columnIndex).formatAsString();
                _out.write("<c r=\""+ref+"\" t=\"n\"");
                if(styleIndex != -1)
                    _out.write(" s=\""+styleIndex+"\"");
                _out.write(">");
                _out.write("<v>"+value+"</v>");
                _out.write("</c>");
            }

            public void createCell(int columnIndex, double value) throws IOException {
                createCell(columnIndex, value, -1);
            }

            public void createCell(int columnIndex, Calendar value, int styleIndex) throws IOException {
                createCell(columnIndex, DateUtil.getExcelDate(value, false), styleIndex);
            }
        }
    }


