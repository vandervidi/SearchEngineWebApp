package il.ac.shenkar.SearchEngine;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import com.mysql.jdbc.PreparedStatement;
import com.mysql.jdbc.Statement;

@WebServlet("/controller/*")
public class controller extends HttpServlet {

	public MysqlConnector ms;
	private static final long serialVersionUID = 1L;

	/**
	 * web application directory.
	 */
	// private static final String SAVE_DIR = "uploadFiles";

	public controller() {
		super();
		ms = MysqlConnector.getInstance();
	}

	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		String str = request.getPathInfo();
		System.out.println("doPost: " + str);

		// Upload
		if (str.equals("/upload")) {
			/* If user send file */
			// String loadFile = request.getParameter("loadFile");
			// if (loadFile != null && loadFile.equals("true")) {

			String realPath = getServletContext().getRealPath(File.separator);
			String[] realPathParts = realPath.split("\\\\");

			String fullPath = realPathParts[0] + "\\" + realPathParts[1] + "\\"
					+ realPathParts[2] + "\\" + realPathParts[3] + "\\"
					+ request.getContextPath().substring(1);

			String fileFullName = "";
			StringBuilder fileName = new StringBuilder();
			String fileExtension = "";
			String tagnames = "";
			long currentTimeMillis = System.currentTimeMillis();
			StringBuilder message = new StringBuilder();
			// process only if its multipart content
			if (ServletFileUpload.isMultipartContent(request)) {
				try {
					List<FileItem> multiparts = new ServletFileUpload(new DiskFileItemFactory()).parseRequest(request);

					for (FileItem item : multiparts) {
						if (!item.isFormField()) {
							
							fileFullName = item.getName();
//							fileNameExtension = new File(item.getName()).getName();
							
							// pure fileName 
							String[] fileFullName_Splited = fileFullName.split("\\.");
							for (int i=0; i<fileFullName_Splited.length-1; i++){
								fileName.append( fileFullName_Splited[i]+"." );
							}
							
							fileExtension = fileFullName.split("\\.")[fileFullName.split("\\.").length-1];
							
							
							if (item.getContentType().contains("image")) {
								// create text file in db-folder
								item.write(new File(fullPath + "\\WebContent\\db\\images\\"+ fileName+currentTimeMillis+"."+fileExtension));
								message.append(fileFullName + " tag as: ");
								System.out.println("create image file Success!");

							} else if (item.getContentType().contains("text")) {
								// create text file in db-folder
								item.write(new File(fullPath + "\\WebContent\\db\\"+ fileName+currentTimeMillis+"."+fileExtension));
								message.append(fileFullName);
								System.out.println("create text file Success!");

							} else {
//								//??
							}
						} else {
							// get tag-names field data
							if (item.getFieldName().equals("pic_tagNames")) {
								tagnames = item.getString();
								
								//Save to another file
								PrintWriter writer = new PrintWriter(fullPath + "\\WebContent\\db\\images\\"+ fileName+currentTimeMillis+".txt", "UTF-8");
								writer.println(tagnames);
								writer.close();
								
								message.append(tagnames);
							}
						}
					}

					if (message.length()==0){
						request.setAttribute("message", "Unsupperted file type: " + fileExtension);
					}else{
					// File uploaded successfully
						request.setAttribute("message", message+ "<br> Uploaded Successfully");
						request.setAttribute("uploadSuccess", true);
					}
				} catch (Exception ex) {
					System.out.println(ex);
					request.setAttribute("message","File Upload Failed due to " + ex);
				}

			} else {
				request.setAttribute("message","Sorry this Servlet only handles file upload request");
			}
			request.getRequestDispatcher("/views/result.jsp").forward(request, response);
		}
	}

	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		RequestDispatcher dispatcher;
		String str = request.getPathInfo();
		System.out.println("doGet: " + str);

		if (str.equals("/upload")) {
			request.getRequestDispatcher("/views/upload.jsp").forward(request, response);
		} 
		
		else if (str.equals("/adminLogin")) {
			request.getRequestDispatcher("/views/adminLogin.html").forward(request, response);
		}
		
		else if (str.equals("/adminMenu")) {
			request.getRequestDispatcher("/views/administration.html").forward(request, response);
		}
		
		else if (str.equals("/getFilesList")) {
			ArrayList<FileSchema> fs = new ArrayList<FileSchema>();
			try {
				ms.statement = ms.connection.createStatement();
				String query = "SELECT *  FROM postingFile WHERE isPicture = 0";
				ResultSet rs = ms.statement.executeQuery(query);
				while (rs.next()) {
					fs.add(new FileSchema(rs.getString("docPath"),rs.getInt("docNumber"),rs.getInt("deleted")));
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			request.setAttribute("files", fs);
			dispatcher = getServletContext().getRequestDispatcher("/views/listOfFiles.jsp");
			dispatcher.forward(request, response);
			
		}else if (str.equals("/authentication")) {
			// Get login fields from the login form page.
			 System.out.println("login");
				String username = request.getParameter("username");
				String password = request.getParameter("password");
				try {
					ms.statement = ms.connection.createStatement();
					String query = "SELECT username,password FROM accounts WHERE username='"+ username + "' AND password='"+ password +"'";
					ResultSet rs = ms.statement.executeQuery(query);
					if (!rs.isBeforeFirst() ) {  
						//in case the login failes-> retry
						dispatcher = getServletContext().getRequestDispatcher("/views/adminLogin.html");
						dispatcher.forward(request, response); 
						} 
					else{
						
						//in case the login is successful -> go to administration page
						dispatcher = getServletContext().getRequestDispatcher("/views/administration.html");
						dispatcher.forward(request, response);
					}
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		}
		else if (str.equals("/search")) {
			String searchQuery = request.getParameter("searchQuery");
			System.out.println(searchQuery);

			try {
				List<String> splitedQueryList = ms.analyzeQuery(searchQuery);
				List<Integer> docNumbers_of_results = ms.getDocNumResults(splitedQueryList);
				List<FileDescriptor> result = ms.create_fileDescriptors_list_by_docNumbers(docNumbers_of_results);
				request.setAttribute("searchQuery", searchQuery);
				request.setAttribute("result", result.iterator());
				request.setAttribute("numberOfSearchResults", result.size());
				
				dispatcher = getServletContext().getRequestDispatcher("/views/searchResults.jsp");
				dispatcher.forward(request, response);

			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else if (str.equals("/enableOrDisable")) {
			String query;
			String docNum= request.getParameter("docNum");
			String action = request.getParameter("action");
			System.out.println(action);
			System.out.println(docNum);
			try {
				ms.statement = ms.connection.createStatement();

				if (action.equals("enable")){
					query = "UPDATE postingfile SET deleted='0' WHERE docNumber='"+docNum+"'";
				}
				else{
					query = "UPDATE postingfile SET deleted='1' WHERE docNumber='"+docNum+"'";
				}
			
				ms.statement.executeUpdate(query);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		    response.sendRedirect("http://localhost:8080/SearchEngineWebApp/controller/getFilesList");

		} else if (str.equals("/displayResult")) {
			String docNum= request.getParameter("docNum");
			System.out.println(docNum);
			
		    response.sendRedirect("http://localhost:8080/SearchEngineWebApp/controller/getFilesList");

		} 
		
		else {
			dispatcher = getServletContext().getRequestDispatcher("/views/index.jsp");
			dispatcher.forward(request, response);
		}

	}

}