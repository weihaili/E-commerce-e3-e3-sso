package cn.kkl.mall.sso.service;

import cn.kkl.mall.pojo.TbUser;

public interface TokenService {
	
	TbUser getUserByToken(String token);

}
