package com.sjsu.edu.cmpe273.util;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.edit.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;

/**
 * 
 * @author Avadhut Thakar
 * This class contains the method to convert given string into PDF File.
 * 
 */
public class PDFFileConversion {
	/**
	 * Constructor.
	 */
	public PDFFileConversion() {
		super();
	}

	/**
	 * This method is used to convert string message to PDF file and stores the
	 * same to the path given
	 * 
	 * @param file
	 * @param message
	 * @throws Exception
	 */
	public void createPDFFile(String file, String message) throws Exception {

		PDDocument doc = null;
		try {
			doc = new PDDocument();
			PDPage page = new PDPage();
			doc.addPage(page);
			PDFont font = PDType1Font.COURIER_OBLIQUE;
			PDPageContentStream contentStream = new PDPageContentStream(doc,
					page);
			contentStream.beginText();
			contentStream.setFont(font, 15);
			contentStream.moveTextPositionByAmount(100, 700);
			// contentStream.set
			contentStream.drawString(message);
			contentStream.endText();
			contentStream.close();
			doc.save(file);
		} finally {
			if (doc != null) {
				doc.close();
			}
		}
	}

}
