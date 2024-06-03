package ugis.util;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import it.geosolutions.geoserver.rest.GeoServerRESTManager;
import it.geosolutions.geoserver.rest.GeoServerRESTPublisher;
import it.geosolutions.geoserver.rest.GeoServerRESTReader;
import it.geosolutions.geoserver.rest.decoder.RESTFeatureTypeList;
import it.geosolutions.geoserver.rest.decoder.RESTLayerGroupList;
import it.geosolutions.geoserver.rest.decoder.utils.NameLinkElem;

/**
 * {@link GeoServerRESTManager} 상속클래스 - Geoserver API 전반적인 관리 클래스
 */
public class GeoManager extends GeoServerRESTManager {

	/**
	 * Geoserver 생성, 수정, 삭제
	 */
	private final GeoServerRESTPublisher publisher;
	/**
	 * Geoserver 관련 데이터 Read
	 */
	private final GeoServerRESTReader reader;

	/**
	 * Geoserver URL
	 */
	private final String restURL;

	/**
	 * Geoserver ID
	 */
	private final String username;

	/**
	 * Geoserver PW
	 */
	private final String password;

	/**
	 * @author SG.LEE
	 * @param restURL  Geoserver URL
	 * @param username Geoserver ID
	 * @param password Geoserver PW
	 * @throws MalformedURLException {@link MalformedURLException}
	 */
	public GeoManager(String restURL, String username, String password) throws MalformedURLException {
		super(new URL(restURL), username, password);

		// Internal publisher and reader, provide simple access methods.
		publisher = new GeoServerRESTPublisher(restURL.toString(), username, password);
		reader = new GeoServerRESTReader(restURL, username, password);

		this.restURL = restURL;
		this.username = username;
		this.password = password;
	}

//	public List<String> getIntersection4326Layers(String workspace, double[] bbox) throws UnsupportedEncodingException, IOException {
//		
//	}

	public List<String> getIntersectionLayers(String workspace, double[] bbox)
			throws UnsupportedEncodingException, IOException {
		List<String> layers = new ArrayList<String>();
		RESTFeatureTypeList featureTypes = this.reader.getFeatureTypes(workspace);

		if (featureTypes != null) {
			int size = featureTypes.size();
			for (int i = 0; i < size; i++) {
				NameLinkElem featureType = featureTypes.get(i);
				String name = featureType.getName();

				StringBuilder url = new StringBuilder();
				url.append(restURL);
				url.append("wfs?service=wfs&VERSION=1.0.0&REQUEST=GetFeature&typeName=");
				url.append(name);
				url.append("&SRS=EPSG%3A5179&maxFeatures=1&outputFormat=application%2Fjson");
				url.append("&BBOX=");
				url.append(bbox[2] + "," + bbox[1] + "," + bbox[0] + "," + bbox[3]);

				URL jsonUrl = new URL(url.toString());
				InputStreamReader isr = new InputStreamReader(jsonUrl.openConnection().getInputStream(), "UTF-8");
				JSONObject object = (JSONObject) JSONValue.parse(isr);

				long totalFeatures = 0;
				if (object != null) {
					totalFeatures = (long) object.get("totalFeatures");
				}

				if (totalFeatures > 0) {
					layers.add(workspace + ":" + name);
				}
			}
		}
		return layers;
	}

	public List<String> getIntersectionLayersXY(String workspace, double[] xy)
			throws UnsupportedEncodingException, IOException {
		List<String> layers = new ArrayList<String>();
		RESTFeatureTypeList featureTypes = this.reader.getFeatureTypes(workspace);
		if (featureTypes != null) {
			int size = featureTypes.size();
			for (int i = 0; i < size; i++) {
				NameLinkElem featureType = featureTypes.get(i);
				String name = featureType.getName();
				StringBuilder url = new StringBuilder();
				url.append(restURL);
				url.append("wfs?service=wfs&VERSION=1.0.0&REQUEST=GetFeature&typeName=");
				url.append(name);
				url.append("&SRS=EPSG%3A5179&maxFeatures=1&outputFormat=application%2Fjson");
				url.append("&cql_filter=DWithin(geom,%20POINT%20(" + xy[0] + "%20" + xy[1] + "),2000,meters)");
				URL jsonUrl = new URL(url.toString());
				System.out.println("jsonUrl : " + jsonUrl);
				InputStreamReader isr = new InputStreamReader(jsonUrl.openConnection().getInputStream(), "UTF-8");
				JSONObject object = (JSONObject) JSONValue.parse(isr);
				long totalFeatures = 0;
				if (object != null) {
					totalFeatures = (long) object.get("totalFeatures");
				}
				if (totalFeatures > 0) {
					layers.add(workspace + ":" + name);
				}
			}
		}
		return layers;
	}
}
