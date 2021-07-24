package com.mountain.library.helper;

import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.model.ZipParameters;
import net.lingala.zip4j.util.Zip4jConstants;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.ImageType;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class Utils {

    private final static Locale LOCALE = new Locale("in", "ID");
    private static final Logger LOGGER = LoggerFactory.getLogger(Utils.class);

    public static String generateToken(){
        return generateToken(7);
    }

    public static String generateToken(int length){
        return RandomStringUtils.randomAlphanumeric(length);
    }

    public static String base64Encode(String str){
        return Base64.encodeBase64String(str.getBytes());
    }

    public static String base64EncodeURLSafe(String str){
        return Base64.encodeBase64URLSafeString(str.getBytes());
    }

    public static String base64Decode(String str){
        return new String(Base64.decodeBase64(str));
    }

    public static boolean isImageFileType(String ext){
        return ext.equalsIgnoreCase("png") || ext.equalsIgnoreCase("PNG") ||
                ext.equalsIgnoreCase("jpg") || ext.equalsIgnoreCase("JPG") ||
                ext.equalsIgnoreCase("jpeg") || ext.equalsIgnoreCase("JPEG");
    }

    public static String stringArrayToString(String[] data, String delimited){
        String result = "";
        if(data == null){
            return result;
        }

        for (String str : data) {
            result += str+delimited;
        }

        return result.substring(0, result.length() - 1);
    }

    public static long getTotalPage(long totalRecords, int MAX_ROW){
        return totalRecords == 0? 1 : (long) Math.ceil((double)totalRecords/ MAX_ROW);
    }

    public static File unZipIt(String zipFile, String outputFolder){
        byte[] buffer = new byte[1024];

        try{
            File folderFile = null;
            //create output directory is not exists
            File folder = new File(outputFolder);
            if(!folder.exists()){
                folder.mkdir();
            }

            //get the zip file content
            ZipInputStream zis = new ZipInputStream(new FileInputStream(zipFile));
            //get the zipped file list entry
            ZipEntry ze = zis.getNextEntry();

            while(ze!=null){

                String fileName = ze.getName();
                File newFile = new File(outputFolder + File.separator + fileName);
                String extension = FilenameUtils.getExtension(fileName);

                //create all non exists folders
                //else you will hit FileNotFoundException for compressed folder
                if(extension != null && extension.trim().isEmpty()){
                    newFile.mkdir();
                    folderFile  = newFile;

                } else{

                    FileOutputStream fos = new FileOutputStream(newFile);
                    int len;
                    while ((len = zis.read(buffer)) > 0) {
                        fos.write(buffer, 0, len);
                    }

                    fos.close();
                }

                ze = zis.getNextEntry();
            }

            zis.closeEntry();
            zis.close();

            File newFolderName = Paths.get(outputFolder, FilenameUtils.getBaseName(zipFile)).toFile();
            //rename template folder
            if(folderFile.exists()){
                folderFile.renameTo(newFolderName);

                //delete zip file
                Paths.get(zipFile).toFile().delete();

            }

            return newFolderName;

        }catch(IOException ex){
            ex.printStackTrace();
        }
        return null;
    }

    public static void convertPdfToImage(File srcFile, File destFile) throws FileNotFoundException, IOException{
        if(!srcFile.exists()){
            throw new FileNotFoundException("File tida ada.");
        }

        PDDocument document = PDDocument.load(srcFile);
        PDFRenderer pdfRenderer = new PDFRenderer(document);
        BufferedImage bim = pdfRenderer.renderImageWithDPI(0, 300, ImageType.RGB);
        ImageIO.write(bim, "png", destFile);
    }

    public static int totalRow(Sheet sheet){
        return sheet.getLastRowNum() + 1;
    }

    public static void writeErrorRecordToExcellFile(File errorRecord, Workbook wb){
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(errorRecord);
            wb.write(fos);
            fos.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            try {
                fos.close();
            } catch (IOException ex) {
                LOGGER.warn(ex.getMessage());
            }
        }
    }

    public static void writeStringToTextFile(String filePath, String str){
        try{
            try (BufferedWriter buf = Files.newBufferedWriter(Paths.get(filePath)); StringReader sr = new StringReader(str); BufferedReader br = new BufferedReader(sr)) {
                buf.append("BARIS | KETERANGAN ERROR");
                buf.newLine();
                buf.append("==============================================================");
                buf.newLine();
                buf.flush();
                String readLine = null;
                while ((readLine = br.readLine()) != null) {
                    buf.append(readLine);
                    buf.newLine();
                    buf.flush();
                }
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public static List<String> extractExcellKolom(File file) {
        List<String> daftarKolom = new LinkedList<>();
        Workbook wb = null;
        try {
            wb = WorkbookFactory.create(file);
            Sheet sheet = wb.getSheetAt(0);
            for (Row row : sheet) {
                if(row.getRowNum() == 0){
                    for (Cell cell : row) {
                        String kolom = getCellValue(cell) + "";
                        daftarKolom.add(kolom);
                    }
                    break;
                }
            }
            wb.close();
        } catch (IOException | InvalidFormatException | EncryptedDocumentException ex) {
        }finally{
            if(wb != null){
                try {
                    wb.close();
                } catch (IOException ex) {}
            }
        }
        return daftarKolom;
    }

    public static String getCellStringValue(Row row, String namaKolom){
        Object value = getCellValue(row, namaKolom);
        return value == null ? ""  : value + "";
    }

    public static int getCellIntValue(Row row, String namaKolom){
        String value = getCellStringValue(row, namaKolom);
        return NumberUtils.parseIntOrZero(value);
    }

    public static BigDecimal getCellBigDecimalValue(Row row, String namaKolom){
        String value = getCellStringValue(row, namaKolom);
        return NumberUtils.parseBigDecimalOrZero(value);
    }

    public static LocalDate getCellLocalDateValue(Row row, String namaKolom, String pattern){
        String value = getCellStringValue(row, namaKolom);
        return DateUtils.parseDateOrNull(value, pattern);
    }

    public static Object getCellValue(Row row, String namaKolom){
        if(row == null){
            return "";
        }

        Cell cell = row.getCell(NumberUtils.parseIntOrZero(namaKolom));
        return getCellValue(cell);
    }

    public static Object getCellValue(Row row, String namaKolom, FormulaEvaluator evaluator){
        if(row == null){
            return "";
        }

        Cell cell = row.getCell(NumberUtils.parseIntOrZero(namaKolom));
        return getCellValue( evaluator.evaluateInCell(cell) );
    }

    public static Object getCellValue(Cell cell){
        if(cell == null){
            return "";
        }

        switch (cell.getCellType()) {
            case Cell.CELL_TYPE_STRING:
                return cell.getRichStringCellValue().getString();
            case Cell.CELL_TYPE_NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    Date dateCellValue = cell.getDateCellValue();
                    String format = DateFormatUtils.format(dateCellValue, "dd/MM/yyyy");
                    return format;
                } else {
                    cell.setCellType(Cell.CELL_TYPE_STRING);
                    return cell.getRichStringCellValue().getString();
                }
            case Cell.CELL_TYPE_BOOLEAN:
                cell.setCellType(Cell.CELL_TYPE_STRING);
                return cell.getRichStringCellValue().getString();
        }

        return "";
    }

    public static void copyExcellRow(Workbook src, Workbook dest, int sheetIndex, int rowIndex, boolean copyHeader){
        Sheet activeSheet = src.getSheetAt(sheetIndex);
        Sheet sheet = dest.getSheet(activeSheet.getSheetName());

        int maxColumnWidth = 0;
        if(sheet == null){
            sheet = dest.createSheet(activeSheet.getSheetName());

            //copy header
            if(copyHeader){
                Row rowHeader = activeSheet.getRow(0);
                Row newRowHeader = sheet.createRow(0);
                for (Cell cell : rowHeader) {
                    int columnIndex = cell.getColumnIndex();
                    maxColumnWidth = activeSheet.getColumnWidth(columnIndex);
                    Cell newCellHeader = newRowHeader.createCell(cell.getColumnIndex(), Cell.CELL_TYPE_STRING);
                    newCellHeader.setCellValue(getCellValue(cell) + "");
                    sheet.setColumnWidth(columnIndex, maxColumnWidth);
                }
            }
        }

        Row row = activeSheet.getRow(rowIndex);
        Row newRow = sheet.createRow(sheet.getLastRowNum() + 1);
        for (Cell cell : row) {
            int columnIndex = cell.getColumnIndex();
            int columnWidth = activeSheet.getColumnWidth(columnIndex);
            Cell newCell = newRow.createCell(cell.getColumnIndex(), Cell.CELL_TYPE_STRING);
            newCell.setCellValue(getCellValue(cell) + "");
            if(maxColumnWidth < columnWidth){
                maxColumnWidth = columnWidth;
                sheet.setColumnWidth(columnIndex, columnWidth);
            }
        }

        //autosize column
        short lastCellNum = row.getLastCellNum();
        for (int i = 0; i < lastCellNum; i++) {
            sheet.autoSizeColumn(i);
        }
    }

    public static String constructErrorMessage(int row, String message ){
        StringBuilder sb = new StringBuilder();
        String baris = row + "";
        sb.append( StringUtils.leftPad(baris, 6, " "));
        sb.append( "|" );
        sb.append( message );
        return sb.toString();
    }

    public static String constructErrorMessage(String message ){
        StringBuilder sb = new StringBuilder();
        sb.append( message );
        return sb.toString();
    }

    public static void zipFolder(File folder, File zipDestPath) throws Exception{
        if(folder == null || !folder.exists()){
            throw new NullPointerException("Folder tidak tersedia.");
        }

        if(!folder.isDirectory()){
            throw new Exception("Argument bukan sebuah folder.");
        }

        // Initiate ZipFile object with the path/name of the zip file.
        ZipFile zipFile = new ZipFile(zipDestPath.getAbsolutePath());

        // Initiate Zip Parameters which define various properties such
        // as compression method, etc.
        ZipParameters parameters = new ZipParameters();

        // set compression method to store compression
        parameters.setCompressionMethod(Zip4jConstants.COMP_DEFLATE);

        // Set the compression level
        parameters.setCompressionLevel(Zip4jConstants.DEFLATE_LEVEL_NORMAL);

        // Add folder to the zip file
        zipFile.addFolder(folder, parameters);
    }

    public static void unzipFolder(File zip, File destFolder) throws Exception{
        if( zip == null || !zip.exists()){
            throw new NullPointerException("File zip tidak tersedia.");
        }

        // Initiate ZipFile object with the path/name of the zip file.
        ZipFile zipFile = new ZipFile(zip.getAbsolutePath());

        // Extracts all files to the path specified
        zipFile.extractAll(destFolder.getAbsolutePath());

    }

    public static boolean isStartWith(String str, String[] prefix){
        for (String s : prefix) {
            if(str.startsWith(s)){
                return true;
            }
        }
        return false;
    }

    public static String validasiBarisanNilai(int[] nilai, String[] label, int tolerance){
        for (int i = 0; i < nilai.length; i++) {
            int n1 = nilai[i];
            String l1 = label[i];

            if(n1 == -1){
                continue;
            }

            for (int j = 1+i; j < nilai.length; j++) {
                int n2 = nilai[j];
                String l2 = label[j];
                if(n2 == -1){
                    continue;
                }
                if(n1 >= n2){
                    return "Nilai kriteria "+ l1 +" tidak boleh >= dari "+ l2;
                }
            }
        }
        return "";
    }

    public static boolean isAnyEmptyString(String... str){
        return Arrays.asList(str)
                .stream()
                .anyMatch( s -> StringUtils.isEmpty(s));
    }

    public static void isAnyEmptyString(String[][] daftarField){
        for (String[] f : daftarField) {
            String namaField = f[0];
            String field = f[1];
            if(StringUtils.isEmpty(field)){
                throw new NullPointerException(namaField + " tidak boleh kosong.");
            }
        }
    }

    public static String randomKode(int length, String tipeData){
        String token = "";
        switch (tipeData) {
            case "number":
                token = RandomStringUtils.randomNumeric(length).toUpperCase();
                break;
            case "random":
                token = RandomStringUtils.randomAlphanumeric(length).toUpperCase();
                break;
            case "string":
            default:
                token = RandomStringUtils.randomAlphabetic(length).toUpperCase();
        }

        return token;
    }

    public static void mkdirIfNotExist(File dir){
        if( !dir.exists() || dir.isFile()){
            dir.mkdir();
            LOGGER.info("Direcotry didn't exist, create one at {}.", dir.getAbsolutePath());
        }
    }

    public static void copyResourceToFile(Resource resource, File savedFile) throws IOException{
        if(resource == null){
            return;
        }

        InputStream inputStream = resource.getInputStream();
        FileUtils.copyInputStreamToFile(inputStream, savedFile);
    }
}
