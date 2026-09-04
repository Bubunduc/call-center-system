package phone.server.servlet;

import java.io.BufferedReader;
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
import phone.server.dto.AnswerCallRequest;
import phone.server.dto.EndCallRequest;
import phone.server.service.TelephonyService;
import phone.shared.exception.InvalidDeviceStateException;
import phone.shared.exception.TelephonyException;

@WebServlet("/api/calls")
public class CallServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	private final TelephonyService service = ApplicationContext.getTelephonyService();

	// Сигнал "кто-то взял трубку аппарата" - ответили на звонок.
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		resp.setContentType("application/json");
		resp.setCharacterEncoding("UTF-8");
		
		String deviceNumber = req.getParameter("deviceNumber");
		String phoneNumber = req.getParameter("phoneNumber");
		
		PrintWriter out = resp.getWriter();
		try (BufferedReader reader = req.getReader()) {
			AnswerCallRequest callRequest = new AnswerCallRequest(deviceNumber,phoneNumber);

			service.answerCall(callRequest);
			resp.setStatus(HttpServletResponse.SC_OK); // 200
			out.print("{\"status\": \"success\", \"message\": \"Звонок принят\"}");

		} catch (TelephonyException e) {
			resp.setStatus(HttpServletResponse.SC_NOT_FOUND); // 404
			resp.getWriter().print("{\"error\": \"" + e.getMessage() + "\"}");

		} catch (InvalidDeviceStateException e) {
			resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			resp.getWriter().print("{\"error\": \"" + e.getMessage() + "\"}");

		} catch (Exception e) {
			resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR); // 500
			resp.getWriter().print("{\"error\": "+e.getMessage()+"}");
		}
	}

	// Сигнал "Звонок окончен" - положили трубку. (Удалить из активных)
	@Override
	protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		resp.setContentType("application/json");
		resp.setCharacterEncoding("UTF-8");

		String deviceNumber = req.getParameter("deviceNumber");
		
		PrintWriter out = resp.getWriter();
		try (BufferedReader reader = req.getReader()) {
			EndCallRequest callRequest = new EndCallRequest(deviceNumber);

			service.endCall(callRequest);
			resp.setStatus(HttpServletResponse.SC_OK); // 200
			out.print("{\"status\": \"success\", \"message\": \"Звонок окончен\"}");

		} catch (TelephonyException e) {
			resp.setStatus(HttpServletResponse.SC_NOT_FOUND); // 404
			resp.getWriter().print("{\"error\": \"" + e.getMessage() + "\"}");

		} catch (InvalidDeviceStateException e) {
			resp.setStatus(HttpServletResponse.SC_BAD_REQUEST); // 400
			resp.getWriter().print("{\"error\": \"" + e.getMessage() + "\"}");

		} catch (Exception e) {
			resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR); // 500
			resp.getWriter().print("{\"error\": \"Внутренняя ошибка сервера\"}");
		}
	}

	// Посмотреть все активные звонки - кто с кем разговаривает.
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		Gson gsonPretty = new GsonBuilder().setPrettyPrinting().create();
		String responseJson = gsonPretty.toJson(service.getActiveCallsList());

		resp.setContentType("application/json");
		resp.setCharacterEncoding("UTF-8");

		try (PrintWriter out = resp.getWriter()) {
			out.print(responseJson);
			out.flush();
		}
	}
}
