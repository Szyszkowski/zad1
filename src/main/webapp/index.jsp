<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html" charset="ISO-8859-1">
<title>Insert title here</title>
</head>
<body>
<form action="hello" method="post">
<table border="1">
<tr><th>Wnioskowana kwota kredytu:</th><th><input type="number" id="kwota" name="kwota"/></th></tr>
<tr><th>Ilosc rat:</th><th><input type="number" id="iloscRat" name="iloscRat"/></th></tr>
<tr><th>Oprocentowanie:</th><th><input type="number" id="oprocentowanie" name="oprocentowanie" step="0.01"/></th></tr>
<tr><th>Oplata stala:</th><th><input type="number" id="oplata" name="oplata"/></th></tr>
<tr><th>Rata:</th><th>
<select id="typRat" name="typRat">
	<option value="stala">stala</option>
	<option value="malejaca">malejaca</option>
</select></th></tr>
<tr><th>Akcja:</th><th>
<select id="akcja" name="akcja">
	<option value="wyswietl">Wyswietl harmonogram</option>
	<option value="pdf">Wygeneruj PDF</option>
</select></th></tr>
</table>
<input type="submit" value="Wyslij"/>
</form>
</body>
</html>