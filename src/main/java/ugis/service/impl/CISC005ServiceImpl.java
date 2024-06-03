package ugis.service.impl;


import java.util.HashMap;
import java.util.List;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import egovframework.rte.fdl.cmmn.EgovAbstractServiceImpl;
import ugis.service.CISC005Service;
import ugis.service.mapper.CISC005Mapper;
import ugis.service.vo.CISC005VO;

@Service("cisc005Service")
public class CISC005ServiceImpl extends EgovAbstractServiceImpl implements CISC005Service {

    private static final Logger LOGGER = LoggerFactory.getLogger(CISC005ServiceImpl.class);

    @Resource(name="cisc005Mapper")
    private CISC005Mapper cisc005Mapper;

	@Override
	public List<CISC005VO> selectSoilSatList(HashMap<String, Object> params) {
		// TODO Auto-generated method stub
		return cisc005Mapper.selectSoilSatList(params);
	}
	
	@Override
	public List<CISC005VO> selectEtcSatList(HashMap<String, Object> params) {
		// TODO Auto-generated method stub
		return cisc005Mapper.selectEtcSatList(params);
	}
	
	@Override
	public List<CISC005VO> selectAeroSatList(HashMap<String, Object> params) {
		// TODO Auto-generated method stub
		return cisc005Mapper.selectAeroSatList(params);
	}


}
