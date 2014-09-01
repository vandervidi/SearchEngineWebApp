package il.ac.shenkar.SearchEngine;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/controller/*")
public class controller extends HttpServlet {
	
	public MysqlConnector ms;
	private static final long serialVersionUID = 1L;
    public controller() {
        super();
        ms = MysqlConnector.getInstance();
    }

    
   
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String str = request.getPathInfo();
		System.out.println(str);
		
		RequestDispatcher dispatcher;
			if (str.equals("/search")){
				
				String searchQuery = request.getParameter("searchQuery");
				System.out.println(searchQuery);
				
				try {
					List<String> splitedQueryList = ms.analyzeQuery(searchQuery);
				
					List<Integer> docNumbers_of_results = ms.getDocNumResults(splitedQueryList);
					
					Iterator a = ms.create_fileDescriptors_list_by_docNumbers(docNumbers_of_results);
					
					request.setAttribute("searchQuery", searchQuery);
					request.setAttribute("result", a);
					request.setAttribute("numberOfSearchResults", docNumbers_of_results.size());
					dispatcher = getServletContext()
							.getRequestDispatcher("/views/searchResults.jsp");
					dispatcher.forward(request, response);
					
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				
			}else if (str.equals("/show_a_result")){
				String filePath = request.getParameter("filePath");
				
				String content = ms.readFileContent(filePath);
				
				request.setAttribute("content", content);
				
				dispatcher = getServletContext()
						.getRequestDispatcher("/views/show_a_result.jsp");
				dispatcher.forward(request, response);
			}
			else{
	            dispatcher = getServletContext()
	            		.getRequestDispatcher("/views/index.jsp"); 
	            dispatcher.forward(request, response);
			}
			
			
	}

}
