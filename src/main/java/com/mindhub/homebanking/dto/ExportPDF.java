package com.mindhub.homebanking.dto;

import com.lowagie.text.*;
import com.lowagie.text.Font;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;

import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.io.IOException;
import java.util.List;

public class ExportPDF {
    private List<TransactionDTO> transactions;

    public ExportPDF() {}
    public ExportPDF(List<TransactionDTO> transactions)
        {this.transactions = transactions;}

    public List<TransactionDTO> getTransactions() {return transactions;}
    public void setTransactions(List<TransactionDTO> transactions) {this.transactions = transactions;}

    public void HeaderWrite(PdfPTable pdfTable){
        PdfPCell cell=new PdfPCell();
        cell.setBackgroundColor(Color.darkGray);
        cell.setPadding(3);
        Font font= FontFactory.getFont(FontFactory.HELVETICA_BOLD);
        font.setColor(Color.WHITE);

        cell.setPhrase(new Phrase("Date",font));
        pdfTable.addCell(cell);

        cell.setPhrase(new Phrase("Description",font));
        pdfTable.addCell(cell);

        cell.setPhrase(new Phrase("Amount",font));
        pdfTable.addCell(cell);
    }

    public void DataWrite(PdfPTable pdfPTable){
        for (TransactionDTO transaction: transactions){
            pdfPTable.addCell(transaction.getCreationDate().toString());
            pdfPTable.addCell(transaction.getDescription());
            pdfPTable.addCell("$"+transaction.getBalance());
        }
    }

    public void export(HttpServletResponse response){
        try(Document document=new Document(PageSize.A4)){
            PdfWriter.getInstance(document,response.getOutputStream());
            document.open();

            Font font=FontFactory.getFont(FontFactory.HELVETICA);
            font.setSize(20);
            font.setColor(Color.BLUE);

            //title characteristics
            Paragraph paragraph=new Paragraph("Transaction List",font);
            paragraph.setAlignment(Paragraph.ALIGN_CENTER);
            document.add(paragraph);

            PdfPTable table=new PdfPTable(3);
            table.setWidthPercentage(60);
            table.setWidths(new float[]{2.5f,4.5f,2.0f});
            table.setSpacingBefore(10);

            HeaderWrite(table);
            DataWrite(table);

            document.add(table);
            document.close();
        } catch (IOException e) {
            System.err.println("error"+e);
        }
    }
}
