package controller;

import java.io.IOException;
import java.sql.Connection;
import java.util.ArrayList;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import dao.JDBCUserDAOImpl;
import dao.UserDAO;
import model.User;

/**
 * Servlet implementation class LoginServlet
 */
@WebServlet("/LoginServlet")
public class LoginServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public LoginServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession();
		User user = (User) session.getAttribute("user");

		if(user == null) {
			RequestDispatcher view = request.getRequestDispatcher("/WEB-INF/Login.jsp");
			view.forward(request, response);
		} else {
			//response.sendRedirect("notes/MainPageServlet");
			response.sendRedirect(request.getContextPath()+ "/pages/index.html");
		}
		
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		User user = new User();
		user.setUsername(request.getParameter("userNameLogin"));
		user.setEmail(request.getParameter("correoLogin"));
		user.setPassword(request.getParameter("contraseniaLogin"));
		
		Connection conn = (Connection) getServletContext().getAttribute("dbConn");
		UserDAO userDao = new JDBCUserDAOImpl();
		userDao.setConnection(conn);
		
		ArrayList<String> validationMessages = new ArrayList<String>();
		boolean correctUser=true;
		
		User userDB = userDao.get(user.getUsername());
		if(userDB!=null) { //Then the user exists
			if(!userDB.getPassword().equals(user.getPassword())) {
				validationMessages.add("Invalid password");
				correctUser=false;
			}
		}else {
			validationMessages.add("Username does not exists. ");
			correctUser=false;
		}
		
		if(correctUser) {
			HttpSession session = request.getSession();
			session.setAttribute("user", userDB);
			response.sendRedirect("pages/index.html");
		}else {
			request.setAttribute("messages", validationMessages);
			request.setAttribute("user", user);
			RequestDispatcher view = request.getRequestDispatcher("/WEB-INF/Login.jsp");
			view.forward(request, response);
		}
		
	}

}
