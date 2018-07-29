package cn.kkl.mall.sso.service;

import cn.kkl.mall.pojo.E3Result;

public interface LoginService {
	
	E3Result userLogin(String username,String password);
	

}
