package servlets;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.xhtmlrenderer.pdf.ITextRenderer;
import org.xhtmlrenderer.resource.FSEntityResolver;
import org.xml.sax.SAXException;

import com.itextpdf.text.DocumentException;


@WebServlet("/hello")
public class Calc extends HttpServlet {
	public Calc() {
        super();
    }

    private static final long serialVersionUID = 1L;
    private String preapreResponse(String typRat, int iloscRat, float oprocentowanie, float oplata, float kwota) {
        StringBuilder stringBuilder = new StringBuilder();
        float kapital = (kwota / iloscRat);
        float odsetki = 0;
        float ckr = 0;
        if (typRat.contentEquals("malejaca")) {
        	stringBuilder.append("<table border='1'>");

            for (int i = 1; i <= iloscRat; i++) {

                ckr = (kwota / iloscRat) * ((1 + ((iloscRat - i + 1) * ((oprocentowanie / 100) / 12)))) + oplata;
                odsetki = ckr - kapital - oplata;
                stringBuilder.append("<tr><th>Rata nr: " + i + "</th><th>" +
                    "Kwota kapitalu: " + Math.ceil(kapital) + "</th><th>" +
                    "Kwota odsetek: " + Math.ceil(odsetki) + "</th><th>" +
                    "Oplata stala: " + oplata + "</th><th>" +
                    "Calkowita rata: " + Math.ceil(ckr) + "</th>" +
                    "</tr>");
            }
            stringBuilder.append("</table>");
        } else {
        	stringBuilder.append("<table border='1'>");
            oprocentowanie = (oprocentowanie / 100);
            for (int i = 1; i <= iloscRat; i++) {
                ckr = (float)(
                    (float)(((kwota + oplata) * (Math.pow((1 + (oprocentowanie / 12)), iloscRat))) *
                        ((1 + (oprocentowanie / 12)) - 1)) /
                    (Math.pow(((1 + (oprocentowanie / 12))), (iloscRat)) - 1)
                );
                odsetki = ckr - kapital;
                stringBuilder.append("<tr><th>Rata nr: " + i + "</th><th>" +
                    "Kwota kapitalu: " + Math.ceil(kapital) + "</th><th>" +
                    "Kwota odsetek: " + Math.ceil(odsetki) + "</th><th>" +
                    "Oplata stala: " + oplata + "</th><th>" +
                    "Calkowita rata: " + Math.ceil(ckr) + "</th>" +
                    "</tr>");
            }
            stringBuilder.append("</table>");        } 

        return stringBuilder.toString();
    }
    private void generatePdf(String html, HttpServletResponse response) throws IOException {
    	String pre = "<!DOCTYPE html> <html> <head> <title>Harmonogram</title> </head> <body>  <center><h1>Harmonogram splaty</h1></center>";
    	String post = "</body></html>";
    	html = pre + html + post;
    	response.setHeader("Cache-Control", "no-cache");


    	response.setContentType("application/pdf"); 

    	response.setHeader("Content-Disposition", "attachment; filename=\"" + "Harmonogram splaty.pdf\"");
    	response.setHeader("Cache-Control", "no-cache");
    	OutputStream os = response.getOutputStream();
    	final DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
    	documentBuilderFactory.setValidating(true);
    	DocumentBuilder builder;
    	try {
    	    builder = documentBuilderFactory.newDocumentBuilder();
    	    builder.setEntityResolver(FSEntityResolver.instance());
    	    org.w3c.dom.Document document;
    	    document = builder.parse(new ByteArrayInputStream(html.getBytes())); 

    	    ITextRenderer itxtrenderer = new ITextRenderer();
    	    itxtrenderer.setDocument(document, null);
    	    itxtrenderer.layout();
    	    itxtrenderer.createPDF(os, true); 
    	} catch (ParserConfigurationException e) {    	   
    	    e.printStackTrace();

    	} catch (SAXException e) {    	    
    	    e.printStackTrace();
    	} catch (DocumentException e) {
    	    e.printStackTrace();
    	}
    	os.flush();
    	os.close();
    }
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String kwota = request.getParameter("kwota");
        String iloscRat = request.getParameter("iloscRat");
        String oprocentowanie = request.getParameter("oprocentowanie");
        String oplata = request.getParameter("oplata");
        String typRat = request.getParameter("typRat");
        String akcja = request.getParameter("akcja");
        Boolean dalej = true;
        
        if (kwota == null || kwota.equals("") ||
            iloscRat == null || iloscRat.equals("") ||
            oprocentowanie == null || oprocentowanie.equals("") ||
            oplata == null || oplata.equals("")) {
            response.sendRedirect("/");
            dalej = false;
        }
        if(dalej == true) {
	        float iKwota = Float.parseFloat(kwota);
	        int iIloscRat = Integer.parseInt(iloscRat);
	        float iOprocentowanie = Float.parseFloat(oprocentowanie);
	        float iOplata = Float.parseFloat(oplata);
	
	        if (iKwota < 0 || iIloscRat < 0 || iOprocentowanie < 0 || iOplata < 0) {
	            response.sendRedirect("/");
	            dalej = false;
	        }
	        if (dalej == true) {
		        if (akcja.contentEquals("pdf")) {
		        	generatePdf(preapreResponse(typRat, iIloscRat, iOprocentowanie, iOplata, iKwota),response);
		        } else {
		        	response.setContentType("text/html");
			        response.getWriter().println(preapreResponse(typRat, iIloscRat, iOprocentowanie, iOplata, iKwota));	                	
		        }
	        }
        }
    }

}