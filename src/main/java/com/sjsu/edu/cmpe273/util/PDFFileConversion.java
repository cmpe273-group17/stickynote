/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements. See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
