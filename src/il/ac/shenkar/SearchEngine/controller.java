package il.ac.shenkar.SearchEngine;

import java.awt.image.BufferedImage;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.URL;
import java.sql.SQLException;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import javax.imageio.ImageIO;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import com.sun.xml.internal.messaging.saaj.util.Base64;

@WebServlet("/controller/*")
public class controller extends HttpServlet {

	public MysqlConnector ms;
	private static final long serialVersionUID = 1L;

	/**
	 * Name of the directory where uploaded files will be saved, relative to the
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
								item.write(new File(fullPath + "\\db\\images\\"+ fileName+currentTimeMillis+"."+fileExtension));
								message.append(fileFullName + " tag as: ");
								System.out.println("create image file Success!");

							} else if (item.getContentType().contains("text")) {
								// create text file in db-folder
								item.write(new File(fullPath + "\\db\\"+ fileName+currentTimeMillis+"."+fileExtension));
								message.append(fileFullName);
								System.out.println("create text file Success!");

							} else {
//								response.setContentType("text/plain");
//								response.setCharacterEncoding("UTF-8");
//								response.getWriter().write("Unsupperted file type: " + fileFullName);
							}
						} else {
							// get tag-names field data
							if (item.getFieldName().equals("pic_tagNames")) {
								tagnames = item.getString();
								
								//Save to another file
								PrintWriter writer = new PrintWriter(fullPath + "\\db\\images\\"+ fileName+currentTimeMillis+".txt", "UTF-8");
								writer.println(tagnames);
								writer.close();
								
								message.append(tagnames);
								
								//Save to DB
								//ms.insert_pic_to_db(fullPath + "\\db\\images\\"+ fileName+currentTimeMillis+"."+fileExtension, fileExtension ,currentTimeMillis, tagnames);	
							}
						}
					}

					if (message.length()==0){
						request.setAttribute("message", "Unsupperted file type: " + fileExtension);
					}else{
					// File uploaded successfully
						request.setAttribute("message", message+ "<br> Uploaded Successfully");
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


		} else if (str.equals("/search")) {

			String searchQuery = request.getParameter("searchQuery");
			System.out.println(searchQuery);

			try {
				List<String> splitedQueryList = ms.analyzeQuery(searchQuery);

				List<Integer> docNumbers_of_results = ms
						.getDocNumResults(splitedQueryList);

				Iterator a = ms.create_fileDescriptors_list_by_docNumbers(docNumbers_of_results);

				request.setAttribute("searchQuery", searchQuery);
				request.setAttribute("result", a);
				request.setAttribute("numberOfSearchResults",
						docNumbers_of_results.size());
				dispatcher = getServletContext().getRequestDispatcher(
						"/views/searchResults.jsp");
				dispatcher.forward(request, response);

			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		} else if (str.equals("/show_a_result")) {
			String filePath = request.getParameter("filePath");

			String content = ms.readFileContent(filePath);

			request.setAttribute("content", content);

			dispatcher = getServletContext().getRequestDispatcher(
					"/views/show_a_result.jsp");
			dispatcher.forward(request, response);

		} else {
			dispatcher = getServletContext().getRequestDispatcher(
					"/views/index.jsp");
			dispatcher.forward(request, response);
		}

	}

}

class Test {
	public static String main() throws Exception {
		URL location = Test.class.getProtectionDomain().getCodeSource()
				.getLocation();
		System.out.println(location.getFile());
		return location.getFile();
	}
}