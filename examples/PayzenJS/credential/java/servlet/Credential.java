import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Formatter;
import java.util.Map;
import java.util.TreeMap;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;

@WebServlet("/demopayzenjs/credential")
public class Credential extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2833138493954178286L;

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		Gson gson = new Gson();

		BufferedReader br = new BufferedReader(new InputStreamReader(request.getInputStream()));
		String json = "";
		if (br != null) {
			json = br.readLine();
		}
		// Get data from json already sorted alphabetically by key (thanks to
		// treemap)
		Map<String, String> dataToSing = gson.fromJson(json, new TreeMap<String, String>().getClass());

		//Create string with all the fields to sign
		StringBuilder stringToSign = new StringBuilder();
		for (Map.Entry<String, String> entry : dataToSing.entrySet()) {
			stringToSign.append(entry.getValue()).append("+");
		}

		//Add private key (shop certificate)
		stringToSign.append("0000000000000000");

		response.setContentType("application/json");
		PrintWriter out = response.getWriter();
		out.print("{\"signature\":\"" + SHAsum(stringToSign.toString().getBytes("UTF-8")) + "\"}");
		out.flush();
	}

	/*
	 * Creates a sha1 hash
	 */
	private static String SHAsum(byte[] convertme) {
		MessageDigest md = null;
		try {
			md = MessageDigest.getInstance("SHA-1");
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}

		return byteArray2Hex(md.digest(convertme));
	}

	/*
	 * Converts byte array to hexadecimal format
	 */
	private static String byteArray2Hex(final byte[] hash) {
		Formatter formatter = new Formatter();
		for (byte b : hash) {
			formatter.format("%02x", b);
		}
		String result = formatter.toString();
		formatter.close();

		return result;
	}
}