package cn.kkl.mall.sso.service.impl;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import cn.kkl.mall.mapper.TbUserMapper;
import cn.kkl.mall.pojo.E3Result;
import cn.kkl.mall.pojo.TbUser;
import cn.kkl.mall.pojo.TbUserExample;
import cn.kkl.mall.pojo.TbUserExample.Criteria;
import cn.kkl.mall.sso.service.RegisterService;

@Service
public class RegisterServiceImpl implements RegisterService {

	@Autowired
	private TbUserMapper userMapper;
	
	@Override
	public E3Result checkData(String parameter, Integer type) {
		boolean flag=true;
		TbUserExample example=new TbUserExample();
		Criteria criteria = example.createCriteria();
		switch (type) {
		case 1: criteria.andUsernameEqualTo(parameter);
			break;
		case 2: criteria.andPhoneEqualTo(parameter);
			break;
		case 3: criteria.andEmailEqualTo(parameter);
		default: flag=false;
			break;
		}
		List<TbUser> list = userMapper.selectByExample(example);
		if (list != null && list.size()>0) {
			flag=false;
		}
		return E3Result.ok(flag);
	}

	@Override
	public E3Result addUser(TbUser user) {
		//data valid check
		if (StringUtils.isBlank(user.getUsername()) || StringUtils.isBlank(user.getPhone()) || StringUtils.isBlank(user.getPassword())) {
			return E3Result.build(400, "incomplete data,please check");
		}
		if (!(boolean) checkData(user.getUsername(), 1).getData()) {
			return E3Result.build(400, user.getUsername()+" has is already occupied");
		}
		if (!(boolean) checkData(user.getPhone(), 2).getData()) {
			return E3Result.build(400, user.getPhone()+" has is already occupied");
		}
		Date date = new Date();
		user.setCreated(date);
		user.setUpdated(date);
		
		//encryption
		user.setPassword(DigestUtils.md5DigestAsHex(user.getPassword().getBytes()));
		
		userMapper.insertSelective(user);
		return E3Result.ok();
	}

}
