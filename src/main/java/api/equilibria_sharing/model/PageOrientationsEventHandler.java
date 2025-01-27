package api.equilibria_sharing.model;


import com.itextpdf.kernel.events.Event;
import com.itextpdf.kernel.events.IEventHandler;
import com.itextpdf.kernel.events.PdfDocumentEvent;
import com.itextpdf.kernel.pdf.PdfName;
import com.itextpdf.kernel.pdf.PdfNumber;
import com.itextpdf.kernel.pdf.PdfPage;


public class PageOrientationsEventHandler implements IEventHandler {

    private PdfNumber orientation = new PdfNumber(0);

    public void setPortrait() {
        orientation = new PdfNumber(0);
    }

    public void setLandscape() {
        orientation = new PdfNumber(90);
    }

    public void setSeascape() {
        orientation = new PdfNumber(270);
    }

    @Override
    public void handleEvent(Event currentEvent) {
        if (currentEvent instanceof PdfDocumentEvent) {
            PdfDocumentEvent pdfDocumentEvent = (PdfDocumentEvent) currentEvent;
            PdfPage page = pdfDocumentEvent.getPage();
            page.put(PdfName.Rotate, orientation);
        }
    }
}

