package cn.kkl.mall.sso.service.impl;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import cn.kkl.mall.pojo.TbUser;
import cn.kkl.mall.service.JedisClient;
import cn.kkl.mall.sso.service.TokenService;
import cn.kkl.mall.utils.JsonUtils;

@Service
public class TokenServiceImpl implements TokenService {
	
	@Autowired
	private JedisClient jedisClient;
	
	@Value("${USER_INFO_SESSION_KEY}")
	private String userInfoCacheKey;
	
	@Value("${SESSION_EXPIRE}")
	private Integer expireLimit;

	@Override
	public TbUser getUserByToken(String token) {
		if (StringUtils.isBlank(token)) {
			System.out.println("token is null ,please check");
			return null;
		}
		String jsonStr = jedisClient.get(userInfoCacheKey+token);
		if (StringUtils.isBlank(jsonStr)) {
			return null;
		}
		jedisClient.expire(userInfoCacheKey+token, expireLimit);
		TbUser tbUser = JsonUtils.jsonToPojo(jsonStr, TbUser.class);
		return tbUser;
	}

}
