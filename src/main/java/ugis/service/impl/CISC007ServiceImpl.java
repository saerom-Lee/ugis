package ugis.service.impl;

import java.util.HashMap;
import java.util.List;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import egovframework.rte.fdl.cmmn.EgovAbstractServiceImpl;
import egovframework.rte.psl.dataaccess.util.EgovMap;
import ugis.service.CISC007Service;
import ugis.service.mapper.CISC007Mapper;

@Service("cisc007Service")
public class CISC007ServiceImpl extends EgovAbstractServiceImpl implements CISC007Service {

	private static final Logger LOGGER = LoggerFactory.getLogger(CISC007ServiceImpl.class);

	@Resource(name = "cisc007Mapper")
	private CISC007Mapper cisc007Mapper;

	@Override
	public List<EgovMap> selectAeroList(HashMap<String, Object> params) {
		// TODO Auto-generated method stub
		return cisc007Mapper.selectAeroList(params);
	}

	@Override
	public List<EgovMap> selectUsgsAirList(HashMap<String, Object> params) {
		return cisc007Mapper.selectUsgsAirList(params);
	}

	@Override
	public List<EgovMap> selectEtcList(HashMap<String, Object> params) {
		// TODO Auto-generated method stub
		return cisc007Mapper.selectEtcList(params);
	}

	@Override
	public List<EgovMap> selectUsgsDroneList(HashMap<String, Object> params) {
		return cisc007Mapper.selectUsgsDroneList(params);
	}

	@Override
	public List<EgovMap> selectUsgsList(HashMap<String, Object> params) {
		return cisc007Mapper.selectUsgsList(params);
	}

	@Override
	public List<EgovMap> selectSoilList(HashMap<String, Object> params) {
		return cisc007Mapper.selectSoilList(params);
	}

}
