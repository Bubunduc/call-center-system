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
import phone.server.dto.CallRequest;
import phone.server.service.TelephonyService;
import phone.shared.exception.InvalidPhoneFormatException;
import phone.shared.exception.TelephonyException;

@WebServlet("/api/queue")
public class QueueServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	private final TelephonyService service = ApplicationContext.getTelephonyService();

	// Добавить входящий звонок в очередь (вызов начался, но никто не взял трубку)
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		resp.setContentType("application/json");
		resp.setCharacterEncoding("UTF-8");

		String phoneNumber = req.getParameter("phoneNumber");

		PrintWriter out = resp.getWriter();

		try (BufferedReader reader = req.getReader()) {
			CallRequest callRequest = new CallRequest(phoneNumber);

			service.addToQueue(callRequest);
			resp.setStatus(HttpServletResponse.SC_OK); // 200 OK
			out.print("{\"status\": \"success\", \"message\": \"Звонок добавлен в очередь\"}");

		} catch (TelephonyException e) {
			resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			resp.getWriter().print("{\"error\": \"" + e.getMessage() + "\"}");

		} catch (InvalidPhoneFormatException e) {
			resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			resp.getWriter().print("{\"error\": \"" + e.getMessage() + "\"}");
		} catch (Exception e) {
			resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR); // 500
			resp.getWriter().print("{\"error\": \"Внутренняя ошибка сервера\"}");
		}
	}

	// Удалить звонок из очереди (отменить входящий звонок)
	@Override
	protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		resp.setContentType("application/json");
		resp.setCharacterEncoding("UTF-8");

		String phoneNumber = req.getParameter("phoneNumber");
		PrintWriter out = resp.getWriter();
		try (BufferedReader reader = req.getReader()) {
			CallRequest callRequest = new CallRequest(phoneNumber);

			service.removeFromQueue(callRequest);
			resp.setStatus(HttpServletResponse.SC_OK); // 200
			out.print("{\"status\": \"success\", \"message\": \"Звонок удален из очереди\"}");

		} catch (TelephonyException e) {
			resp.setStatus(HttpServletResponse.SC_BAD_REQUEST); // 400
			resp.getWriter().print("{\"error\": \"" + e.getMessage() + "\"}");

		} catch (Exception e) {
			resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR); // 500
			resp.getWriter().print("{\"error\": \"Внутренняя ошибка сервера\"}");
		}
	}

	// Посмотреть всю очередь (только входящие, для которых никто не взял трубку)
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		Gson gsonPretty = new GsonBuilder().setPrettyPrinting().create();
		String responseJson = gsonPretty.toJson(service.getNumsList());

		resp.setContentType("application/json");
		resp.setCharacterEncoding("UTF-8");

		try (PrintWriter out = resp.getWriter()) {
			out.print(responseJson);
			out.flush();
		}
	}
}
