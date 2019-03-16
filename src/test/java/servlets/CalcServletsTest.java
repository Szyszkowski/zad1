package servlets;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.Test;
import org.mockito.Mockito;

public class CalcServletsTest extends Mockito {
	@Test
	public void servlet_if_kwota_is_null() throws IOException, ServletException {
		HttpServletRequest request = mock(HttpServletRequest.class);
		HttpServletResponse response = mock(HttpServletResponse.class);
		Calc servlet = new Calc();
		
		when(request.getParameter("kwota")).thenReturn(null);
		servlet.doPost(request, response);
		verify(response).sendRedirect("/");
		
	}
	
	@Test
	public void servlet_if_kwota_is_empty() throws IOException, ServletException {
		HttpServletRequest request = mock(HttpServletRequest.class);
		HttpServletResponse response = mock(HttpServletResponse.class);
		Calc servlet = new Calc();
		
		when(request.getParameter("kwota")).thenReturn("");
		servlet.doPost(request, response);
		verify(response).sendRedirect("/");
		
	}
	
	@Test
	public void servlet_if_data_is_correct() throws IOException, ServletException {
		HttpServletRequest request = mock(HttpServletRequest.class);
		HttpServletResponse response = mock(HttpServletResponse.class);
		PrintWriter writer = mock(PrintWriter.class);		
		Calc servlet = new Calc();
		
		when(request.getParameter("kwota")).thenReturn("100000");
		when(request.getParameter("iloscRat")).thenReturn("1");
		when(request.getParameter("oprocentowanie")).thenReturn("10");
		when(request.getParameter("oplata")).thenReturn("100");
		when(request.getParameter("typRat")).thenReturn("stala");
		when(request.getParameter("akcja")).thenReturn("wyswietl");
		when(response.getWriter()).thenReturn(writer);
		
		servlet.doPost(request, response);
		verify(writer).println("<table border='1'><tr><th>Rata nr: 1</th><th>Kwota kapitalu: 100000.0</th><th>Kwota odsetek: 935.0</th><th>Oplata stala: 100.0</th><th>Calkowita rata: 100935.0</th></tr></table>");
		
		
	}
	
	@Test
	public void servlet_if_data_is_correct_pdf() throws IOException, ServletException {
		HttpServletRequest request = mock(HttpServletRequest.class);
		HttpServletResponse response = mock(HttpServletResponse.class);
		ServletOutputStream mockOutput = mock(ServletOutputStream.class);
		Calc servlet = new Calc();
		when(request.getParameter("kwota")).thenReturn("100000");
		when(request.getParameter("iloscRat")).thenReturn("1");
		when(request.getParameter("oprocentowanie")).thenReturn("10");
		when(request.getParameter("oplata")).thenReturn("100");
		when(request.getParameter("typRat")).thenReturn("stala");
		when(request.getParameter("akcja")).thenReturn("pdf");
		when(response.getOutputStream()).thenReturn(mockOutput);
		
		servlet.doPost(request, response);
		verify(response).setContentType("application/pdf");
		
		
	}
}
