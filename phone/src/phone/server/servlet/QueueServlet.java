package phone.server.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("api/queue")
public class QueueServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	// Добавить входящий звонок в очередь (вызов начался, но никто не взял трубку)
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

	}

	// Удалить звонок из очереди (отменить входящий звонок)
	@Override
	protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

	}

	// Посмотреть всю очередь (только входящие, для которых никто не взял трубку)
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

	}

}
