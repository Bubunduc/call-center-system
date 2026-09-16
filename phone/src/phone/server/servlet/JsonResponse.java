package phone.server.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import phone.server.dto.ErrorMessage;
import phone.server.dto.SuccessMessage;

public final class JsonResponse {

	private static final Gson gson = new Gson();
	private static final Gson gsonPretty = new GsonBuilder().setPrettyPrinting().create();
	
	private JsonResponse() {
		
	}
	
	public static void successMessage(HttpServletResponse resp, int status, String message) throws IOException {
		resp.setStatus(status);
		PrintWriter out = resp.getWriter();
		out.print(gson.toJson(new SuccessMessage("success", message)));
	}

	public static void errorMessage(HttpServletResponse resp, int status, String message) throws IOException {
		resp.setStatus(status);
		PrintWriter out = resp.getWriter();
		out.print(gson.toJson(new ErrorMessage(message)));
	}

	public static void successMessageFromList(HttpServletResponse resp, int status, List<?> values) throws IOException {
		resp.setStatus(status);
		PrintWriter out = resp.getWriter();
		out.print(gsonPretty.toJson(values));
	}
}
