package ugis.service.impl;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.annotation.Resource;

import com.google.gson.Gson;
import egovframework.rte.psl.dataaccess.util.EgovMap;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.scheduling.Trigger;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Component;
import ugis.service.CTSC001Service;
import ugis.service.vo.CTSC001VO;
import ugis.service.vo.CTSC001_01VO;

/**
 * usgs 사이트 검색 호출.
 */
@Component("ctsc001BatchService")
public class CTSC001BatchServiceImpl {
	@Resource(name="ctsc001Service")
	private CTSC001Service<CTSC001VO> service;
	@Resource(name = "fileProperties")
	private Properties fileProperties;

	private ThreadPoolTaskScheduler scheduler;
	private String cron = "0 0 12 * * ?";

	public void startScheduler() throws Exception {
		scheduler = new ThreadPoolTaskScheduler();
		scheduler.initialize();
		// scheduler setting
		scheduler.schedule(getRunnable(), getTrigger());
	}

	public void changeCronSet(String cron) {
		this.cron = cron;
	}

	public void stopScheduler() {
		scheduler.shutdown();
	}

	private Runnable getRunnable() throws Exception {
		// do something
		return () -> {
			CTSC001VO ctsc001VO = new CTSC001VO();
			CTSC001_01VO ctsc001_01VO = new CTSC001_01VO();

			List<?> select = null;
			try {
				select = service.select(ctsc001VO);
			} catch (Exception e) {
				e.printStackTrace();
			}
			for (int i = 0; i < select.size(); i++) {
				ctsc001VO.setFlag(1);
				ctsc001VO.setVidoId((Integer) ((EgovMap)select.get(i)).get("vidoid"));
				try {
					service.upt(ctsc001VO);
				} catch (Exception e) {
					e.printStackTrace();
				}
				ctsc001_01VO.setPotogrfBeginDt(String.valueOf(((EgovMap)select.get(i)).get("potogrfbegindt"))); //검색 시작일자.
				ctsc001_01VO.setPotogrfEndDt(String.valueOf(((EgovMap)select.get(i)).get("potogrfenddt"))); //검색 종료일자.
				ctsc001_01VO.setPotogrfVidCd((String) ((EgovMap)select.get(i)).get("potogrfVidoNm")); //비디오명 .
				ctsc001_01VO.setLtopCrdntX(Double.parseDouble(String.valueOf(((EgovMap)select.get(i)).get("ulx")))); //좌상단 x
				ctsc001_01VO.setLtopCrdntY(Double.parseDouble(String.valueOf(((EgovMap)select.get(i)).get("uly")))); //좌상단 y
				ctsc001_01VO.setRbtmCrdntX(Double.parseDouble(String.valueOf(((EgovMap)select.get(i)).get("lrx")))); //우하단 x
				ctsc001_01VO.setRbtmCrdntY(Double.parseDouble(String.valueOf(((EgovMap)select.get(i)).get("lry")))); //우하단 y
				ctsc001_01VO.setSatlitVidoInnerPath((String) ((EgovMap)select.get(i)).get("satlitvidoextrlpath"));
				ctsc001_01VO.setVido_id((Integer) ((EgovMap)select.get(i)).get("vidoid"));
				Gson gson = new Gson();
				String file_path = fileProperties.getProperty("external.url");
				HttpPost httpPost = new HttpPost(file_path);
				httpPost.setHeader("Accept", "application/json");
				httpPost.setHeader("Content-Type", "application/json");
				try {
					StringEntity postingString = new StringEntity(gson.toJson(ctsc001_01VO));
					HttpClient httpClient = HttpClientBuilder.create().build();
					httpPost.setEntity(postingString);
					HttpResponse response = httpClient.execute(httpPost);
					
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				} catch (ClientProtocolException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			destroy();
		};
		
	}

	private Trigger getTrigger() {
		// cronSetting
		return new CronTrigger(cron);
	}

	@PostConstruct
	public void init() throws Exception {
		startScheduler();
	}

	@PreDestroy
	public void destroy() {
		stopScheduler();
	}
}

