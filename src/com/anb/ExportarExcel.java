package com.anb;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

/**

 * Time: 11:28 PM
 * To change this template use File | Settings | File Templates.
 */
public class ExportarExcel {

    public enum RECORD_TYPE {
        SPECIFICATIONS,
        PRICING,
        SERIAL_NUMBER  ,
        NORMAL  ,
        IGNORE  ,
        MCM,
        MARCA ,
        CLASE ,
        MODELO  , PAGINA
    }

    public static void main(String args[])
    {
        System.out.println(" Exportar Excel");

        Workbook wb = new XSSFWorkbook();
        Sheet sheet1 = wb.createSheet("Maquinaria");


        int currentLineNumber = 0;
        BufferedReader reader = null;
        String line;

        try{

            String source = "/Users/davidolori/Documents/Personal/anb/capturas/Equip_guide_2014.TXT";
            String target = "/Users/davidolori/Documents/Personal/anb/capturas/Equip_guide_2014.xlsx";
            FileOutputStream fileOut = new FileOutputStream(target);

            int row_number = 0;
            Row row = sheet1.createRow(row_number);
            row.createCell(1).setCellValue("MARCA");
            row.createCell(2).setCellValue("CLASE");
            row.createCell(3).setCellValue("MODELO");
            row.createCell(4).setCellValue("ENGINE");
            row.createCell(5).setCellValue("HP");
            row.createCell(6).setCellValue("CARRIER");
            row.createCell(7).setCellValue("TRANS");
            row.createCell(8).setCellValue("SN_YEAR");
            row.createCell(9).setCellValue("SN BEGINING");
            row.createCell(10).setCellValue("PRICING_YEAR");
            row.createCell(11).setCellValue("PRICING_RETAIL");




            InputStreamReader ir = new InputStreamReader(new FileInputStream(source));
            reader = new BufferedReader(ir);
            RECORD_TYPE CurrentType = RECORD_TYPE.IGNORE;
            String Marca= "";
            String Clase= "";
            String Modelo= "";

            Specification       specification    = new Specification();
            List<SerialNumber>  lstSerialNumbers = new ArrayList<SerialNumber>();
            List<Pricing>       lstPricing       = new ArrayList<Pricing>();


            if(!reader.markSupported())
            {
                System.out.println(" ERROR FATAL: NO soporta el metodo mark for InputStream");


            }



            while ((line = reader.readLine()) != null) {

                currentLineNumber++;
                if (line.trim().length() == 0) {
                    System.out.println(" Posible linea vacia  en pagina " + currentLineNumber);
                    continue;  // skip empty lines
                }

                if(StringUtils.contains(line,"Pagina ")){

                    String strPagina = StringUtils.remove(line,"Pagina ");

                }

                /// VERIFY IF IT IS MARCA MODELO AND CLASE
                if(getRecordType(line)==RECORD_TYPE.MCM){
                    reader.mark(500);
                    String next1=  reader.readLine();
                    String next2=  reader.readLine();

                    if(getRecordType(next1) == RECORD_TYPE.MCM && getRecordType(next2) == RECORD_TYPE.MCM )
                    {

                        CurrentType=RECORD_TYPE.MARCA;
                        Marca=line;
                    }



                    if(getRecordType(next1) == RECORD_TYPE.MCM && getRecordType(next2) != RECORD_TYPE.MCM )
                    {
                        CurrentType=RECORD_TYPE.CLASE;
                        Clase=line;

                    }


                    if(getRecordType(next1) != RECORD_TYPE.MCM && getRecordType(next2) != RECORD_TYPE.MCM )
                    {
                        CurrentType=RECORD_TYPE.MODELO;
                        Modelo=line;
                    }

                    reader.reset();

                }

                // Reading Specification line
                if (getRecordType(line) == RECORD_TYPE.SPECIFICATIONS){
                    CurrentType = RECORD_TYPE.SPECIFICATIONS;
                    continue;

                }

                // VERIFY SERIAL NUMBER
                if (getRecordType(line)== RECORD_TYPE.SERIAL_NUMBER){

                    CurrentType=RECORD_TYPE.SERIAL_NUMBER;
                    currentLineNumber++;
                    line = reader.readLine(); // avanzamos una linea para evitar el titulo
                    continue;
                }

                if (getRecordType(line)==RECORD_TYPE.PRICING){

                    CurrentType=RECORD_TYPE.PRICING;
                    currentLineNumber++;
                    line = reader.readLine();  // avanzamos una linea para que la proxima vez se lean los valores
                    continue;
                }

                if(getRecordType(line)==RECORD_TYPE.IGNORE){
                    continue;
                }





                int length=line.length();


                if(CurrentType == RECORD_TYPE.SPECIFICATIONS )
                {

                    if(StringUtils.contains(line,"HP "))
                    {
                        String hptemp= line;
                        specification.setHp(StringUtils.remove(hptemp,"HP "));

                    }

                    if(StringUtils.contains(line,"ENGINE ") )
                    {
                        String engine_tmp   = line;
                        engine_tmp   =StringUtils.remove(engine_tmp,".");
                        specification.setEngine(StringUtils.remove(engine_tmp,"ENGINE "));


                    }
                    if( StringUtils.startsWith(line,"ENG "))
                    {
                        String engine_tmp   = line;
                        engine_tmp   =StringUtils.remove(engine_tmp,".");
                        specification.setEngine(StringUtils.remove(engine_tmp,"ENG "));


                    }

                    if( StringUtils.startsWith(line,"CARRIER "))
                    {
                        String carrier = line;
                        carrier = StringUtils.remove(carrier,".");
                        specification.setCarrier(StringUtils.remove(carrier,"CARRIER "));


                    }


                    if( StringUtils.startsWith(line,"TRANS "))
                    {
                        String trans = line;
                        trans = StringUtils.remove(trans,".");
                        specification.setTrans(StringUtils.remove(trans,"TRANS "));

                    }





                }


                if(CurrentType==RECORD_TYPE.SERIAL_NUMBER && getRecordType(line)!=RECORD_TYPE.PAGINA ){


                    String serial_number=line;
                    String strYear = serial_number.substring(0, 4) ;

                    String serial_number_value =StringUtils.remove(serial_number,strYear);

                    serial_number_value =StringUtils.remove(serial_number_value,".");

                    SerialNumber sn = new SerialNumber();
                    sn.setYear(Integer.valueOf(strYear));
                    sn.setValue(serial_number_value);
                    lstSerialNumbers.add(sn);

                }

                if(CurrentType==RECORD_TYPE.PRICING && getRecordType(line)!=RECORD_TYPE.PAGINA){

                    String strPrincing=line;
                    String strYear = strPrincing.substring(0, 4) ;
                    String retailvalue= getRetail(strPrincing);
                    Number numberFformat=NumberFormat.getNumberInstance(java.util.Locale.US).parse(retailvalue);

                    Pricing pricing = new Pricing();
                    pricing.setYear(Integer.valueOf(strYear));
                    pricing.setValue(numberFformat.floatValue());
                    lstPricing.add(pricing);

                }

                // VErificar que el siguiente line asera marca clase o modelo y empezar a grabar


                if(getRecordType(line)!= RECORD_TYPE.MCM ){

                    reader.mark(500);
                    String next1=  reader.readLine();



                    if(getRecordType(next1) == RECORD_TYPE.MCM)
                    {

                            List<Maquinaria> lstMaqunarias=procesarDatos(Marca,Clase,Modelo,specification,lstSerialNumbers,lstPricing);


                            for (Maquinaria maquinaria:lstMaqunarias){

                                row_number = row_number+1;
                                row = sheet1.createRow(row_number);
                                row.createCell(1).setCellValue(maquinaria.getMarca());
                                row.createCell(2).setCellValue(maquinaria.getClase());
                                row.createCell(3).setCellValue(maquinaria.getModelo());
                                row.createCell(4).setCellValue(maquinaria.getESP_ENGINE());
                                row.createCell(5).setCellValue(maquinaria.getESP_HP());
                                row.createCell(6).setCellValue(maquinaria.getESP_CARRIER());
                                row.createCell(7).setCellValue(maquinaria.getESP_TRANS());
                                row.createCell(8).setCellValue(maquinaria.getSn_year());
                                row.createCell(9).setCellValue(maquinaria.getSn_beginning());
                                row.createCell(10).setCellValue(maquinaria.getPricing_year());
                                row.createCell(11).setCellValue(maquinaria.getPricing_retail());


                            }

                        specification    = new Specification();
                        lstSerialNumbers = new ArrayList<SerialNumber>();
                        lstPricing       = new ArrayList<Pricing>();
                    }

                    reader.reset();

                }









            }



            wb.write(fileOut);

            fileOut.close();


        }catch (Exception e)
        {
            System.out.println(" ERROR: El error ocurrio en la pagina: " + currentLineNumber + " " +e.getMessage());
            e.printStackTrace();
        }




    }


    public static RECORD_TYPE getRecordType(String line){


        if(line==null){

            return RECORD_TYPE.IGNORE;
        }
        if (line.equalsIgnoreCase("spECIFIC atIoNs".toUpperCase())){

            return RECORD_TYPE.SPECIFICATIONS;
        }
        if (line.equalsIgnoreCase("SPECIFICATIONS")){

            return RECORD_TYPE.SPECIFICATIONS;
        }

        if (line.equalsIgnoreCase("SPECIFICAT IONS")){

            return RECORD_TYPE.SPECIFICATIONS;
        }



        if (line.equalsIgnoreCase("prICINg")){
            return RECORD_TYPE.PRICING;
        }


        if (line.equalsIgnoreCase("sErIaL NUmBErs")){
            return RECORD_TYPE.SERIAL_NUMBER;
        }

        if(StringUtils.startsWith(line,"HP ")){

            return RECORD_TYPE.NORMAL;

        }
        if(StringUtils.contains(line,"TRANS  ")){

            return RECORD_TYPE.NORMAL;

        }

        if(StringUtils.contains(line,"STAT OR VIB ")){

            return RECORD_TYPE.NORMAL;

        }
        if(StringUtils.startsWith(line,"ENG ")){

            return RECORD_TYPE.NORMAL;

        }

        if(StringUtils.startsWith(line,"ENGINE ")){

            return RECORD_TYPE.NORMAL;

        }



        if(StringUtils.contains(line,"TRANS ")){

            return RECORD_TYPE.NORMAL;
        }

        if(StringUtils.contains(line,".")){

            if(StringUtils.contains(line,"FR120.2")
                || StringUtils.contains(line,"FR130.2")
                || StringUtils.contains(line,"FR140.2")
                || StringUtils.contains(line,"FR160.2")
                || StringUtils.contains(line,"FR180.2")
                || StringUtils.contains(line,"FR220.2")
                || StringUtils.contains(line,"B.R. LEE")
                || StringUtils.contains(line,"3.18 LC")
                    || StringUtils.contains(line,"3.21 LC")
                    || StringUtils.contains(line,"3.28 LC")

                    || StringUtils.contains(line,"300.9D")
                    || StringUtils.contains(line,"301.4C")
                    || StringUtils.contains(line,"301.5")
                    || StringUtils.contains(line,"301.6")
                    || StringUtils.contains(line,"301.7D")
                    || StringUtils.contains(line,"301.8")
                    || StringUtils.contains(line,"302.4D")
                    || StringUtils.contains(line,"302.5")
                    || StringUtils.contains(line,"302.7D")
                    || StringUtils.contains(line,"303.5")
                    || StringUtils.contains(line,"304.5")
                    || StringUtils.contains(line,"3.28 LC")





                    ){

                return RECORD_TYPE.MCM;
            }

                return RECORD_TYPE.NORMAL;
        }





        if(StringUtils.contains(line,"Pagina ")){
            return RECORD_TYPE.PAGINA;
        }


        if(StringUtils.contains(line,"Equipment Guide")){
            return RECORD_TYPE.IGNORE;
        }

        if(StringUtils.containsAny(line,"/") ){

            return RECORD_TYPE.IGNORE;

        }

        return RECORD_TYPE.MCM;

    }



    public static  List<Maquinaria> procesarDatos(String marca,String clase ,String modelo ,Specification sp,List<SerialNumber> lstSerialNumber,List<Pricing> lstPricing)
    {

        ArrayList<Maquinaria> lstMaquinaria = new ArrayList<Maquinaria>();


        if(!lstPricing.isEmpty() && !lstSerialNumber.isEmpty()){
            if(lstPricing.size()>lstSerialNumber.size())
            {
                        int sn_size=lstSerialNumber.size();
                        int sn_index=0;


                      for(Pricing pricing:lstPricing){

                          Maquinaria maquinaria = new Maquinaria();
                          maquinaria.setMarca(marca);
                          maquinaria.setClase(clase);
                          maquinaria.setModelo(modelo);
                          maquinaria.setESP_HP(sp.getHp());
                          maquinaria.setESP_TRANS(sp.getTrans());
                          maquinaria.setESP_CARRIER(sp.getCarrier());
                          maquinaria.setESP_ENGINE(sp.getEngine());
                          maquinaria.setPricing_year(String.valueOf(pricing.getYear()));
                          maquinaria.setPricing_retail(String.valueOf(pricing.getValue()));
                          maquinaria.setDescripcion( " MARCA:" + marca + " CLASE:" + clase + " MODELO:" +modelo + " " + sp.getHp());


                          if(sn_index< sn_size){
                              SerialNumber sn= lstSerialNumber.get(sn_index);

                              maquinaria.setSn_year((String.valueOf(sn.getYear())));


                              maquinaria.setSn_beginning(String.valueOf(sn.getValue()));

                              sn_index++;
                          }

                          lstMaquinaria.add(maquinaria);

                      }

            }   else{

                int price_size= lstPricing.size();
                int price_index=0;

                for(SerialNumber serialNumber:lstSerialNumber){

                    Maquinaria maquinaria = new Maquinaria();
                    maquinaria.setMarca(marca);
                    maquinaria.setClase(clase);
                    maquinaria.setModelo(modelo);
                    maquinaria.setESP_HP(sp.getHp());
                    maquinaria.setESP_ENGINE(sp.getEngine());
                    maquinaria.setESP_TRANS(sp.getTrans());
                    maquinaria.setESP_CARRIER(sp.getCarrier());


                    maquinaria.setSn_year(String.valueOf(serialNumber.getYear()));
                    maquinaria.setSn_beginning(String.valueOf(serialNumber.getValue()));

                    if(price_index< price_size){

                        maquinaria.setPricing_year(String.valueOf(((Pricing)lstPricing.get(price_index)).getYear()));
                        maquinaria.setPricing_retail(String.valueOf(((Pricing)lstPricing.get(price_index)).getValue()));
                        price_index++;
                    }

                    lstMaquinaria.add(maquinaria);

                }

            }

        }        // end if lspricing and serialnumebr != empty



        // Cuando no existe pricing ni serial number

        if(lstPricing.isEmpty() && lstSerialNumber.isEmpty())
        {

            Maquinaria maquinaria = new Maquinaria();
            maquinaria.setMarca(marca);
            maquinaria.setClase(clase);
            maquinaria.setModelo(modelo);
            maquinaria.setESP_HP(sp.getHp());
            maquinaria.setESP_ENGINE(sp.getEngine());
            maquinaria.setESP_TRANS(sp.getTrans());
            maquinaria.setESP_CARRIER(sp.getCarrier());

            lstMaquinaria.add(maquinaria);


        }

        if(!lstPricing.isEmpty() && lstSerialNumber.isEmpty()){

            for( Pricing pricing:lstPricing){

                Maquinaria maquinaria = new Maquinaria();
                maquinaria.setMarca(marca);
                maquinaria.setClase(clase);
                maquinaria.setModelo(modelo);
                maquinaria.setESP_HP(sp.getHp());
                maquinaria.setESP_ENGINE(sp.getEngine());
                maquinaria.setESP_TRANS(sp.getTrans());
                maquinaria.setESP_CARRIER(sp.getCarrier());

                maquinaria.setPricing_year(String.valueOf(pricing.getYear()));
                maquinaria.setPricing_retail(String.valueOf(pricing.getValue()));
                lstMaquinaria.add(maquinaria);
            }



        }

        if(lstPricing.isEmpty() && !lstSerialNumber.isEmpty()){


            for( SerialNumber serialNumber:lstSerialNumber){


                Maquinaria maquinaria = new Maquinaria();
                maquinaria.setMarca(marca);
                maquinaria.setClase(clase);
                maquinaria.setModelo(modelo);
                maquinaria.setESP_HP(sp.getHp());
                maquinaria.setESP_ENGINE(sp.getEngine());
                maquinaria.setESP_TRANS(sp.getTrans());
                maquinaria.setESP_CARRIER(sp.getCarrier());


                maquinaria.setSn_year(String.valueOf(serialNumber.getYear()));
                maquinaria.setSn_beginning(String.valueOf(serialNumber.getValue()));
                lstMaquinaria.add(maquinaria);
            }

        }

        return lstMaquinaria;

    }


    public static String getRetail(String line){
        String retail= "";
        int indexA=StringUtils.indexOf(line,"$");

        int indexB=StringUtils.indexOf(line, "$", indexA+1);


        retail=StringUtils.substring(line,indexA+1,indexB);
        //System.out.println("Retail=" + retail);

        return StringUtils.remove(retail,".");


    }

    //todo
//    SP60
//    YRS MFG  .  .  .  .  .  .  .  .1981-1984
//    ENGINE  .  .  .  .  .  .  .  . PERK 4 .154       punto ne engine
//    HP . . . . . . . . . . . . . . . . . . . 60

}
