package Web;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import Util.DateUtil;
import Util.PropertiesUtil;

/**
 * @date 2016年3月4日 ImageUploadServlet.java
 * @author CZP
 * @parameter
 */
public class ImageUploadServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		this.doPost(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setContentType("text/html;charset=UTF-8");
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
		PrintWriter out = response.getWriter();
		FileItemFactory factory = new DiskFileItemFactory();
		ServletFileUpload upload = new ServletFileUpload(factory);
		try {
			// 获取表单
			List<FileItem> list = upload.parseRequest(request);
			for (FileItem fileItem : list) {
				String imageName = DateUtil.getCurrentDateStr();
				// 实际写入图片的位置(在电脑上的位置路径)
				File file = new File(
						PropertiesUtil.getValue("imagePath") + imageName + "." + fileItem.getName().split("\\.")[1]);
				// 从项目路径中读取的图片路径
				String newPath = PropertiesUtil.getValue("imageFile") + "/" + imageName + "."
						+ fileItem.getName().split("\\.")[1];
				fileItem.write(file);
				// 显示上传图片的回调方法
				String callback = request.getParameter("CKEditorFuncNum");
				out.println("<script type=\"text/javascript\">");
				out.println("window.parent.CKEDITOR.tools.callFunction(" + callback + ",'" + newPath + "',''" + ")");
				out.println("</script>");
				out.flush();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
