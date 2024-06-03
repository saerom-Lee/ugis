package ugis.service;

import ugis.service.vo.CISC004VO;

public interface CISC004Service {


	//영상정보
	CISC004VO select(CISC004VO cisc004VO);

	long insert(CISC004VO cisc004VO);



}
