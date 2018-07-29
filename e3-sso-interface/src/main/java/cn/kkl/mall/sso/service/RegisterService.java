package cn.kkl.mall.sso.service;

import cn.kkl.mall.pojo.E3Result;
import cn.kkl.mall.pojo.TbUser;

public interface RegisterService {
	
	E3Result checkData(String parameter,Integer type);

	E3Result addUser(TbUser user);

}
