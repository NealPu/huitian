package com.momathink.weixin.service;

import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.jfinal.aop.Before;
import com.jfinal.plugin.activerecord.tx.Tx;
import com.momathink.common.base.BaseService;
import com.momathink.common.base.SplitPage;
import com.momathink.common.tools.ToolString;
import com.momathink.weixin.model.Menu;
import com.momathink.weixin.model.Wpnumber;

public class MenuService extends BaseService {

	private static Logger log = Logger.getLogger(MenuService.class);

	/**
	 * 保存
	 * 
	 * @param
	 * @return
	 */
	@Before(Tx.class)
	public void save(Menu menu) {
		menu.save();
	}

	/**
	 * 更新
	 * 
	 * @param
	 */
	public void update(Menu menu) {
		menu.update();
	}

	/**
	 * 删除
	 * 
	 * @param
	 */
	public void delete(String id) {
		Menu.dao.deleteById(id);
	}

	/**
	 * 分页
	 * 
	 * @param splitPage
	 */
	public void list(SplitPage splitPage) {
		log.debug("微信自定义菜单管理：分页处理");
		String select = " select * ";
		splitPageBase(splitPage, select);
	}

	protected void makeFilter(Map<String, String> queryParam, StringBuilder formSqlSb, List<Object> paramValue) {
		formSqlSb.append(" from wx_menu where 1=1 ");

		if (null == queryParam) {
			return;
		}

		String menuname = queryParam.get("menuname");
		String menutype = queryParam.get("menutype");

		if (null != menuname && !menuname.equals("")) {
			formSqlSb.append(" and menuname like ? ");
			paramValue.add("%" + menuname.trim() + "%");
		}

		if (null != menutype && !menutype.equals("")) {
			formSqlSb.append(" and menutype like ? ");
			paramValue.add("%" + menutype.trim() + "%");
		}
	}

	public String parentNodeData(String menuIds) {
		// 查询一级菜单数据
		List<Menu> menuList = null;
		String sql = null;
		if (null != menuIds) {
			sql = " select ids, menuname from wx_menu where menutype = 0 and ids=? ";
			menuList = Menu.dao.find(sql, menuIds);

		} else {
			sql = " select ids, menuname from wx_menu where menutype = 0 ";
			menuList = Menu.dao.find(sql);
		}

		StringBuilder sb = new StringBuilder();
		sb.append("[");

		int size = menuList.size() - 1;
		for (Menu menu : menuList) {
			sb.append(" { ");
			sb.append(" id : '").append(menu.getStr("ids")).append("', ");
			sb.append(" name : '").append(menu.getStr("menuname")).append("', ");
			sb.append(" font : {'font-weight':'bold'}, ");
			sb.append(" icon : '/jsFile/zTree/css/zTreeStyle/img/diy/5.png' ");
			sb.append(" }");
			if (menuList.indexOf(menu) < size) {
				sb.append(", ");
			}
		}

		sb.append("]");

		return sb.toString();
	}

	public String queryAllMenuJSON(String wpnumberid) {
		// 查询一级菜单数据
		List<Menu> menuList = Menu.dao.find("select * from wx_menu where menutype=0 and status=0 and wpnumberid=? order by sortorder",Integer.parseInt(wpnumberid));
		// 查询二级菜单
		List<Menu> subList = Menu.dao.find("select * from wx_menu where menutype=1 and status=0 and wpnumberid=? order by sortorder",Integer.parseInt(wpnumberid));
		Wpnumber wp = Wpnumber.dao.findById(Integer.parseInt(wpnumberid));
		StringBuilder sb = new StringBuilder();
		sb.append("{\"button\":[");
		for (Menu menu : menuList) {// 循环一级菜单
			Integer menuId = menu.getInt("id");// 取一级菜单ID
			String menuName = menu.getStr("menuname");// 取名称
			sb.append("{\"name\":\"").append(menuName).append("\",");// 按照微信接口拼json
			StringBuilder subsb = new StringBuilder("");// 子菜单即一级菜单下的二级菜单
			int size = menuList.size() - 1;
			for (Menu subMenu : subList) {// 循环二级菜单
				Integer parentId = subMenu.getInt("parentid");// 取出二级菜单所在的一级菜单，即二级菜单的父ID
				String isoauth = subMenu.getStr("isoauth");
				String type = subMenu.getStr("type");// 取出二级菜单类型
				if (menuId.equals(parentId)) {// 二级菜单的所在的一级菜单ID与一级菜单ID相比对，如果相同则该二级菜单属于当前一级菜单的子菜单
					subsb.append("{");
					subsb.append("\"name\":\"").append(subMenu.getStr("menuname")).append("\",");
					// 判断是否为oauth认证
					if ("1".equals(isoauth)) {
						subsb.append("\"type\":\"view\",");
						StringBuffer oauthURL = new StringBuffer("https://open.weixin.qq.com/connect/oauth2/authorize?");
						oauthURL.append("appid=").append(wp.getStr("appid")).append("&redirect_uri=").append(ToolString.urlEncode(subMenu.getStr("redirect_uri")))
								.append("&response_type=code&scope=snsapi_userinfo&state=").append(subMenu.getStr("state"))
								.append("#wechat_redirect\"");
						subsb.append("\"url\":\"").append(oauthURL);
						subsb.append("},");
					} else {
						subsb.append("\"type\":\"").append(subMenu.getStr("type")).append("\",");
						if ("click".equals(type))
							subsb.append("\"key\":\"").append(subMenu.getStr("key")).append("\"");
						if ("view".equals(type))
							subsb.append("\"url\":\"").append(subMenu.getStr("url")).append("\"");
						subsb.append("},");
					}
				}
			}
			int sublength = subsb.length();
			if (sublength > 0) {// 如果二级菜单长度大于0，则说明该一级菜单含有二级菜单
				sb.append("\"sub_button\":[").append(subsb.substring(0, sublength - 1)).append("]");
			} else {// 该一级菜单不含有子菜单，则将该一级菜单所具有的click、view拼接到JSON字符串上
				String type = menu.getStr("type");
				String isoauth = menu.getStr("isoauth");
				sb.append("\"type\":\"").append(type).append("\",");
				// 判断是否为oauth认证
				if ("1".equals(isoauth)) {
					subsb.append("\"type\":\"view\",");
					StringBuffer oauthURL = new StringBuffer("https://open.weixin.qq.com/connect/oauth2/authorize?");
					oauthURL.append("appid=").append(wp.getStr("appid")).append("&redirect_uri=").append(ToolString.urlEncode(menu.getStr("redirect_uri")))
							.append("&response_type=code&scope=snsapi_userinfo&state=").append(menu.getStr("state")).append("#wechat_redirect");
					sb.append("\"url\":\"").append(oauthURL);
				} else {
					subsb.append("\"type\":\"").append(menu.getStr("type")).append("\",");
					if ("click".equals(type))
						sb.append("\"key\":\"").append(menu.getStr("key")).append("\"");
					if ("view".equals(type))
						sb.append("\"url\":\"").append(menu.getStr("url")).append("\"");
				}
			}
			sb.append("}");
			if (menuList.indexOf(menu) < size) {
				sb.append(",");
			}
		}
		sb.append("]}");
		return sb.toString();
	}

	/**
	 * 获取所有菜单列表
	 * @param wpnumberid 
	 * @return
	 */
	public List<Menu> getAllMenuList(String wpnumberid) {
		// 查询一级菜单数据
		List<Menu> menuList = Menu.dao.findFirstMenu(wpnumberid);
		for (Menu menu : menuList) {// 循环一级菜单
			List<Menu> subList = Menu.dao.findChildMenuById(menu.getPrimaryKeyValue().toString());
			menu.put("submenus", subList);
			menu.put("subcount", subList.size());
		}
		return menuList;
	}

}
