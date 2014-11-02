package il.ac.shenkar.SearchEngine;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

@MultipartConfig
@WebServlet("/con/*")
public class UploadServlet3 extends HttpServlet {
	//private final String UPLOAD_DIRECTORY = "C:/uploads";
	
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	String str = request.getPathInfo();
		System.out.println("doPost: " + str);
    	
        //Part file = request.getPart("upl");
        //String filename = getFilename(file);
        //InputStream filecontent = file.getInputStream();
        //System.out.println("fileName= "+filename+" fileContent= "+filecontent);
        
        String realPath = getServletContext().getRealPath(File.separator);
		String[] realPathParts = realPath.split("\\\\");

		String fullPath = realPathParts[0] + "\\"
				+ realPathParts[1] + "\\" + realPathParts[2] + "\\"
				+ realPathParts[3] + "\\" + request.getContextPath().substring(1);
		
		//process only if its multipart content
        if(ServletFileUpload.isMultipartContent(request)){
            try {
                List<FileItem> multiparts = new ServletFileUpload(
                                         new DiskFileItemFactory()).parseRequest(request);
              
                for(FileItem item : multiparts){
                    if(!item.isFormField()){
                    	FileItem itemTemp = item;
                        String filename = new File(item.getName()).getName();
                        
                        if (item.getContentType().contains("image")){
                			// create text file in db-folder
                			item.write( new File(fullPath+ "\\db\\images\\"+ filename));
                			System.out.println("create image file Success!");
                        	
                        }else if (item.getContentType().contains("text")){
                			// create text file in db-folder
                			item.write( new File(fullPath+ "\\db\\"+ filename));
                			System.out.println("create text file Success!");
                        
                        }else{
                        	response.setContentType("text/plain");
                            response.setCharacterEncoding("UTF-8");
                            response.getWriter().write("Unsupperted file type: " + filename);
                        }
                    }else{
                    	//get tag-names field data
                    	System.out.println( item.getString() );
                    }
                }
           
               //File uploaded successfully
               request.setAttribute("message", "File Uploaded Successfully");
            } catch (Exception ex) {
            	System.out.println("fuck-it");
               request.setAttribute("message", "File Upload Failed due to " + ex);
            }          
         
        }else{
            request.setAttribute("message", "Sorry this Servlet only handles file upload request");
        }
    
        request.getRequestDispatcher("/views/result.jsp").forward(request, response);
    }
    
    
    protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		String str = request.getPathInfo();
		System.out.println("doGet: " + str);

		RequestDispatcher dispatcher;
//		if (str.equals("/upload")) {
//			
			dispatcher = getServletContext().getRequestDispatcher(
					"/views/upload3b.jsp");
			dispatcher.forward(request, response);
//		}
    }

//    private static String getFilename(Part part) {
//        for (String cd : part.getHeader("content-disposition").split(";")) {
//            if (cd.trim().startsWith("filename")) {
//                String filename = cd.substring(cd.indexOf('=') + 1).trim().replace("\"", "");
//                return filename.substring(filename.lastIndexOf('/') + 1).substring(filename.lastIndexOf('\\') + 1); // MSIE fix.
//            }
//        }
//        return null;
//    }
}