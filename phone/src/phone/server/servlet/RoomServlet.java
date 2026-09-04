package phone.server.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import phone.server.ApplicationContext;
import phone.server.service.TelephonyService;

@WebServlet("/api/rooms")
public class RoomServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	private TelephonyService service = ApplicationContext.getTelephonyService();

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		Gson gsonPretty = new GsonBuilder().setPrettyPrinting().create();
		String responseJson = gsonPretty.toJson(service.getAllRooms());

		resp.setContentType("application/json");
		resp.setCharacterEncoding("UTF-8");

		try (PrintWriter out = resp.getWriter()) {
			out.print(responseJson);
			out.flush();
		}
	}
}
