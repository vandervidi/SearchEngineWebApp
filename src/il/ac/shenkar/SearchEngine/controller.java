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
        System.out.println("folderScanner CTOR()");
        ms = MysqlConnector.getInstance();
    }

    
   
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String str = request.getPathInfo();
		System.out.println(str);
			if (str.equals("/search")){
				
				String searchQuery = request.getParameter("searchQuery");
				System.out.println(searchQuery);
				
				try {
					List<RowElement> rows = ms.searchWord(searchQuery);
					
					Iterator<RowElement> i = rows.iterator();
					
					while (i.hasNext()){
						//System.out.println( i.toString() );
						//System.out.println("1");
						String filePath = ms.getFilePath_by_docNumber_postingFile( ((RowElement)i).getDocNumber() );
						//System.out.println(s);
//						StringBuilder filePreview = ((RowElement)i).getPreview(   ((RowElement)i).getDocNumber()  );
//						System.out.println(filePreview);
					}
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				 RequestDispatcher dispatcher = getServletContext()
							.getRequestDispatcher("/views/searchResults.jsp");
				 dispatcher.forward(request, response);	
			}
			else{
	            RequestDispatcher dispatcher = getServletContext()
	            .getRequestDispatcher("/views/index.jsp");
	            dispatcher.forward(request, response);        
			}
	}

}
