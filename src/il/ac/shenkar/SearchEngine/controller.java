package il.ac.shenkar.SearchEngine;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/controller/*")
public class controller extends HttpServlet {
	private static final long serialVersionUID = 1L;
    public controller() {
        super();
        // TODO Auto-generated constructor stub
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String str = request.getPathInfo();
		System.out.println(str);
			if (str.equals("/search")){

				 RequestDispatcher dispatcher = getServletContext()
							.getRequestDispatcher("/views/index.jsp");
				 dispatcher.forward(request, response);	
			}
			else{
	            RequestDispatcher dispatcher = getServletContext()
	            .getRequestDispatcher("/views/index.jsp");
	            dispatcher.forward(request, response);        
			    }
	}

}
