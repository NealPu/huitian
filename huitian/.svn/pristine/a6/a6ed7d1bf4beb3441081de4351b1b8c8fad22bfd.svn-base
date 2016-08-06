package com.momathink.sys.address.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.momathink.common.base.BaseService;
import com.momathink.common.base.SplitPage;
import com.momathink.common.tools.ToolDateTime;
import com.momathink.common.tools.ToolString;
import com.momathink.sys.address.model.IpAddressDetail;
import com.momathink.sys.address.model.IpAddressRecord;

public class IpAddressService extends BaseService{
	private static final Logger logger = Logger.getLogger(IpAddressService.class);

	public void list(SplitPage splitPage) {
		logger.debug("IP管理：分页处理");
		String select = " select ip.*,c.CAMPUS_NAME ";
		splitPageBase(splitPage, select);
	}

	protected void makeFilter(Map<String, String> queryParam, StringBuilder formSqlSb, List<Object> paramValue) {
		formSqlSb.append(" from ipaddress ip left join campus c on ip.campus_id=c.Id where 1=1 ");
		if (null == queryParam) {
			return;
		}else{
			String id = queryParam.get("Id");
			String name=queryParam.get("name");
			if (!ToolString.isNull(id)) {
				formSqlSb.append(" and ip.Id = ? ");
				paramValue.add(Integer.parseInt(id));
			}
			if(!ToolString.isNull(name)){
				formSqlSb.append(" and name like ? ");
				paramValue.add("%"+name+"%");
			}
			
		}
	}

	public void saveIpAddressInfo(IpAddressRecord record, List<IpAddressDetail> detailList) {
		if (record != null) {
			Date date = ToolDateTime.getDate();
			record.set("create_time", date);
			record.save();
			if (detailList != null) {
				Integer recordId = record.getPrimaryKeyValue();
				for (IpAddressDetail detail : detailList) {
					detail.set("recordid", recordId);
					detail.set("createtime", date);
					detail.save();
				}
			}
		}
		logger.info("保存IP成功");
	}

	public void deleteByRecordId(Integer paraToInt) {
		IpAddressDetail.dao.deleteByRecordId(paraToInt);
		IpAddressRecord.dao.deleteById(paraToInt);
	}

}
