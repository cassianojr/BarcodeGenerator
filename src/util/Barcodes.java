package util;

import com.itextpdf.barcodes.Barcode128;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.xobject.PdfFormXObject;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.UnitValue;
import org.krysalis.barcode4j.impl.code128.Code128Bean;
import org.krysalis.barcode4j.output.bitmap.BitmapCanvasProvider;
import org.krysalis.barcode4j.tools.UnitConv;

import java.awt.image.BufferedImage;
import java.io.*;
import java.util.Date;

/**
 * Class that handle with the barcode generation and pdf file creation
 */
public class Barcodes {

    /**
     * Generate a .pdf file name based on the timestamp
     * @param dest
     * @return
     */
    public static String getRandomFileName(String dest){
        return dest+"barcodes-"+new Date().getTime()+".pdf";
    }

    /**
     * Creates a pdf file that contains the barcode and save to the destination file
     * @param dest the Destination File
     * @param codes a array of codes that will be generated
     * @throws Exception
     */
    public static void createPdf(String dest, String[] codes) throws Exception{

        //Create a pdf document and set the margins to 0
        PdfDocument pdfDocument = new PdfDocument(new PdfWriter(dest));
        Document doc = new Document(pdfDocument);
        doc.setMargins(0,0,0,0);

        //starts a table with 4 columns and use all available space for the width
        Table table = new Table(UnitValue.createPercentArray(4)).useAllAvailableWidth();

        //Actually generate the codes if the line is not empty
        for(String code : codes){
            if(!code.equals("")){
                table.addCell(createBarcode(String.format("%08d", Integer.parseInt(code)), pdfDocument));
            }
        }

        //close the IO operations
        doc.add(table);
        doc.close();
    }

    /**
     * Actually generate the Code128 barcode with the given code and create a cell
     * @param code
     * @param pdfDocument
     * @return The single barcode cell to the table
     */
    private static Cell createBarcode(String code, PdfDocument pdfDocument) {
        Barcode128 barcode = new Barcode128(pdfDocument);
        barcode.setCodeType(Barcode128.CODE128);
        barcode.setCode(code);

        //create barcode object to put it to the cell as image
        PdfFormXObject barcodeObject = barcode.createFormXObject(null, null, pdfDocument);
        Cell cell = new Cell().add(new Image(barcodeObject).scale(2, 2));
        cell.setPaddingLeft(10);
        cell.setPaddingTop(5);

        return cell;
    }

    /**
     * Create a single barcode and save to a single .png file
     * @param code
     * @param directory
     * @throws IOException
     */
    public static void createSingleBarcode(String code, String directory) throws IOException {
        Code128Bean bean = new Code128Bean();

        final int dpi = 150;

        bean.setModuleWidth(UnitConv.in2mm(2.5f / dpi));

        bean.doQuietZone(false);

        File outputFile = new File(directory+code +".png");

        OutputStream out = new FileOutputStream(outputFile);
        BitmapCanvasProvider canvas = new BitmapCanvasProvider(out, "image/x-png", dpi, BufferedImage.TYPE_BYTE_BINARY, false,0);

        bean.generateBarcode(canvas, code);

        canvas.finish();
        out.close();
    }
}
