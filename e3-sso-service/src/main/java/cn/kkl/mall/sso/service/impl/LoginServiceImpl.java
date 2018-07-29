package cn.kkl.mall.sso.service.impl;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import cn.kkl.mall.mapper.TbUserMapper;
import cn.kkl.mall.pojo.E3Result;
import cn.kkl.mall.pojo.TbUser;
import cn.kkl.mall.pojo.TbUserExample;
import cn.kkl.mall.pojo.TbUserExample.Criteria;
import cn.kkl.mall.service.JedisClient;
import cn.kkl.mall.sso.service.LoginService;
import cn.kkl.mall.utils.JsonUtils;

@Service
public class LoginServiceImpl implements LoginService {
	
	@Autowired
	private JedisClient jedisClient;
	
	@Autowired
	private TbUserMapper userMapper;
	
	@Value("${USER_INFO_SESSION_KEY}")
	private String userInfoCacheKey;
	
	@Value("${SESSION_EXPIRE}")
	private Integer expireLimit;
	
	@Override
	public E3Result userLogin(String username, String password) {
		TbUserExample example=new TbUserExample();
		Criteria criteria = example.createCriteria();
		criteria.andUsernameEqualTo(username);
		List<TbUser> list = userMapper.selectByExample(example);
		if (list==null|| list.size()==0) {
			return E3Result.build(400, "username or password invalid,please check");
		}
		TbUser tbUser = list.get(0);
		if (!DigestUtils.md5DigestAsHex(password.getBytes()).equals(tbUser.getPassword())) {
			return E3Result.build(400, "username or password invalid,please check");
		}
		String token = UUID.randomUUID().toString();
		tbUser.setPassword(null);
		jedisClient.set(userInfoCacheKey+token, JsonUtils.objectToJson(tbUser));
		jedisClient.expire(userInfoCacheKey+token,expireLimit);
		return E3Result.ok(token);
	}

}
