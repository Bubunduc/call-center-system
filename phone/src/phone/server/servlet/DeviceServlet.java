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
import phone.server.sevice.TelephonyService;
import phone.shared.exception.TelephonyException;

@WebServlet("/api/rooms/devices")
public class DeviceServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private TelephonyService service = ApplicationContext.getTelephonyService();

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		Gson gsonPretty = new GsonBuilder().setPrettyPrinting().create();

		resp.setContentType("application/json");
		resp.setCharacterEncoding("UTF-8");

		String roomIdParam = req.getParameter("roomId");

		if (roomIdParam == null || roomIdParam.isEmpty()) {
			resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			resp.getWriter().write("{\"error\": \"Не указан параметр roomId\"}");
			return;
		}

		Long roomId = Long.parseLong(roomIdParam);
		try {
			String responseJson = gsonPretty.toJson(service.getDevicesStatusByRoom(roomId));

			resp.setContentType("application/json");
			resp.setCharacterEncoding("UTF-8");

			try (PrintWriter out = resp.getWriter()) {
				out.print(responseJson);
				out.flush();
			}
		} catch (TelephonyException e) {
			resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
			resp.getWriter().print("{\"error\": \"" + e.getMessage() + "\"}");

		}
	}

}
