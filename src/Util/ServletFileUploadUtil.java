package Util;

import java.io.File;
import java.io.PrintWriter;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadBase.SizeLimitExceededException;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

/**
 * @date 2016年4月11日 ServletFileUploadUtil.java
 * @author CZP
 * @parameter
 */
public class ServletFileUploadUtil {

	public void upload(HttpServletRequest request, HttpServletResponse response) throws Exception {
		request.setCharacterEncoding("utf-8");
		final long MAX_SIZE = 300 * 1024 * 1024;// 设置上传文件最大值
		// 允许上传文件格式的列表
		final String[] allowedExt = new String[] { "jpg", "jpeg", "gif", "txt", "doc", "mp3", "wma", "m4a", "rar",
				"zip" };
		response.setContentType("text/html");
		// 设置字符编码为UTF-8, 统一编码，处理出现乱码问题
		response.setCharacterEncoding("UTF-8");
		// 实例化一个硬盘文件工厂,用来配置上传组件ServletFileUpload
		DiskFileItemFactory dfif = new DiskFileItemFactory();
		dfif.setSizeThreshold(4096);// 设置上传文件时用于临时存放文件的内存大小,这里是4K.多于的部分将临时存在硬盘
		// dfif.setRepository(new File(request.getRealPath("/") +
		// "ImagesUploadTemp"));
		dfif.setRepository(new File(request.getServletContext().getRealPath("/") + "ImagesUploadTemp"));
		// 用以上工厂实例化上传组件
		ServletFileUpload sfu = new ServletFileUpload(dfif);
		// 设置最大上传大小
		sfu.setSizeMax(MAX_SIZE);
		PrintWriter out = response.getWriter();
		// 从request得到所有上传域的列表
		List fileList = null;
		try {
			fileList = sfu.parseRequest(request);
		} catch (FileUploadException e) {
			// 处理文件尺寸过大异常
			if (e instanceof SizeLimitExceededException) {
				out.println("文件尺寸超过规定大小：" + MAX_SIZE + "字节<p/>");
				out.println("<a href=\"#\" >返回</a>");
				return;
			}
			e.printStackTrace();
		}
		// 没有文件上传
		if (fileList == null || fileList.size() == 0) {
			out.println("请选择上传文件<p/>");
			out.println("<a href=\"#\" >返回</a>");
			return;
		}
		// 得到所有上传的文件
		Iterator fileItr = fileList.iterator();
		// 循环处理所有文件
		while (fileItr.hasNext()) {
			FileItem fileItem = null;
			String path = null;
			long size = 0;
			// 得到当前文件
			fileItem = (FileItem) fileItr.next();
			// 忽略简单form字段而不是上传域的文件域(<input type="text" />等)
			if (fileItem == null || fileItem.isFormField()) {
				continue;
			}
			// 得到文件的完整路径
			path = fileItem.getName();
			// 得到文件的大小
			size = fileItem.getSize();
			if ("".equals(path) || size == 0) {
				out.println("请选择上传文件<p />");
				out.println("<a href=\"#\" >返回</a>");
				return;
			}
			// 得到去除路径的文件名
			String t_name = path.substring(path.lastIndexOf("//") + 1);
			// 得到文件的扩展名(无扩展名时将得到全名)
			String t_ext = t_name.substring(t_name.lastIndexOf(".") + 1);
			// 拒绝接受规定文件格式之外的文件类型
			int allowFlag = 0;
			int allowedExtCount = allowedExt.length;
			for (; allowFlag < allowedExtCount; allowFlag++) {
				if (allowedExt[allowFlag].equals(t_ext))
					break;
			}
			if (allowFlag == allowedExtCount) {
				out.println("请上传以下类型的文件<p />");
				for (allowFlag = 0; allowFlag < allowedExtCount; allowFlag++)
					out.println("*." + allowedExt[allowFlag] + "   ");
				out.println("<p /><a href=\"#\" >返回</a>");
				return;
			}
			// long now = System.currentTimeMillis();
			// 根据系统时间生成上传后保存的文件名
			// String prefix = String.valueOf(now);
			// 保存的最终文件完整路径,保存在web根目录下的ImagesUploaded目录下
			// String u_name = request.getRealPath("/") + "ImagesUploaded/"
			// + prefix + "." + t_ext;
			String filename = t_name;
			System.out.println(t_name);
			try {
				// 保存文件到C://upload目录下
				fileItem.write(new File("C://upload//" + filename));
				// System.out.println(filename);
				out.println("文件上传成功. 已保存为: " + filename + "   文件大小: " + size + "字节<p />");
				out.println("<a href=\"#\" >继续上传</a>");
			} catch (Exception e) {
				e.printStackTrace();
			}

		}
	}

}
