package Util;

/**
 * @date 2016年3月1日 NavUtil.java
 * @author CZP
 * @parameter
 */
public class NavUtil {
	// 新闻类别的导航
	public static String genNewsListNav(String typeName, String typeId) {
		StringBuffer navCode = new StringBuffer();
		navCode.append("当前位置：&nbsp;&nbsp;");
		navCode.append("<a href='goIndex'>主页</a>&nbsp;&nbsp;>>&nbsp;&nbsp;");
		navCode.append("<a href='news?action=list&typeId=" + typeId + "'>" + typeName + "</a>");
		return navCode.toString();
	}

	// 具体新闻的导航
	public static String genNewsNav(String typeName, String typeId, String newsName) {
		StringBuffer navCode = new StringBuffer();
		navCode.append("当前位置：&nbsp;&nbsp;");
		navCode.append("<a href='goIndex'>主页</a>&nbsp;&nbsp;>>&nbsp;&nbsp;");
		navCode.append("<a href='news?action=list&typeId=" + typeId + "'>" + typeName + "</a>&nbsp;&nbsp;>>&nbsp;&nbsp;"
				+ newsName);
		return navCode.toString();
	}

	public static String genNewsManageNav(String modeName, String actionName) {
		StringBuffer navCode = new StringBuffer();
		navCode.append("当前位置：&nbsp;&nbsp;");
		navCode.append("主页&nbsp;&nbsp;>>&nbsp;&nbsp;");
		navCode.append(modeName + "&nbsp;&nbsp;>>&nbsp;&nbsp;");
		navCode.append(actionName + "&nbsp;&nbsp;");
		return navCode.toString();
	}

}
