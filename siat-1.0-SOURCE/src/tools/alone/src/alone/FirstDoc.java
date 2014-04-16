//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package alone;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import com.lowagie.text.DocumentException;


public class FirstDoc {
    
    public static void main(String[] args) 
            throws IOException, DocumentException {
        String inputFile = "sample.html";
        String url = new File(inputFile).toURI().toURL().toString();
        String outputFile = "firstdoc.pdf";
        OutputStream os = new FileOutputStream(outputFile);
        
//        ITextRenderer renderer = new ITextRenderer();
//        renderer.setDocument(url);
//        renderer.layout();
//        renderer.createPDF(os);
        
        os.close();
    }
}