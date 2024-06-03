package ugis.service.impl;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import egovframework.rte.fdl.cmmn.EgovAbstractServiceImpl;
import ugis.service.CMSC002Service;
import ugis.service.mapper.CMSC002Mapper;

import java.util.List;

@Service("cmsc002Service")
public class CMSC002ServiceImpl extends EgovAbstractServiceImpl implements CMSC002Service {

	private static final Logger LOGGER = LoggerFactory.getLogger(CMSC002ServiceImpl.class);
	
	//
	@Resource(name = "cmsc002Mapper")
	private CMSC002Mapper cmsc002Mapper;


	@Override
	public List<?> selectSiList() {
		return cmsc002Mapper.selectSiList();
	}


	@Override
	public List<?> selectSggList(String si) {
		// TODO Auto-generated method stub
		return cmsc002Mapper.selectSggList(si);
	}

	@Override
	public List<?> selectEmdList(String sgg) {
		return cmsc002Mapper.selectEmdList(sgg);
	}
}
