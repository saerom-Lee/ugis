package ugis.util.map;

import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

public class BaseMap {
	protected String url;
    protected String layer;
    protected String style;
    protected String tilematrixset;
    protected String service;
    protected String request;
    protected String version;
    protected String format;
    protected String tilematrix;
    protected TileRowCol rowCol;
    protected String key;

    public BaseMap(String url, String layer, String style, String tilematrixset, String service, String request,
			String version, String format, String tilematrix, TileRowCol rowCol, String key) {
		super();
		this.url = url;
		this.layer = layer;
		this.style = style;
		this.tilematrixset = tilematrixset;
		this.service = service;
		this.request = request;
		this.version = version;
		this.format = format;
		this.tilematrix = tilematrix;
		this.rowCol = rowCol;
		this.key = key;
	}
    
    public BaseMap(String layer, String style, String tilematrixset, String service, String request,
			String version, String format, String tilematrix, TileRowCol rowCol) {
		super();
		this.layer = layer;
		this.style = style;
		this.tilematrixset = tilematrixset;
		this.service = service;
		this.request = request;
		this.version = version;
		this.format = format;
		this.tilematrix = tilematrix;
		this.rowCol = rowCol;
	}

	public BaseMap() {
        this.url = "";
    }

    public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getLayer() {
		return layer;
	}

	public void setLayer(String layer) {
		this.layer = layer;
	}

	public String getStyle() {
		return style;
	}

	public void setStyle(String style) {
		this.style = style;
	}

	public String getTilematrixset() {
		return tilematrixset;
	}

	public void setTilematrixset(String tilematrixset) {
		this.tilematrixset = tilematrixset;
	}

	public String getService() {
		return service;
	}

	public void setService(String service) {
		this.service = service;
	}

	public String getRequest() {
		return request;
	}

	public void setRequest(String request) {
		this.request = request;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getFormat() {
		return format;
	}

	public void setFormat(String format) {
		this.format = format;
	}

	public String getTilematrix() {
		return tilematrix;
	}

	public void setTilematrix(String tilematrix) {
		this.tilematrix = tilematrix;
	}

	public TileRowCol getRowCol() {
		return rowCol;
	}

	public void setRowCol(TileRowCol rowCol) {
		this.rowCol = rowCol;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}
	
	public String getURL() {
		StringBuilder urlBuilder = new StringBuilder();
		urlBuilder.append(url);
		urlBuilder.append("/openapi/Gettile.do");
		urlBuilder.append("?");
		urlBuilder.append("apikey=");
		urlBuilder.append(key);
		urlBuilder.append("&");
		urlBuilder.append("layer=");
		urlBuilder.append(layer);
		urlBuilder.append("&");
		urlBuilder.append("style=");
		urlBuilder.append(style);
		urlBuilder.append("&");
		urlBuilder.append("tilematrixset=");
		urlBuilder.append(tilematrixset);
		urlBuilder.append("&");
		urlBuilder.append("service=");
		urlBuilder.append(service);
		urlBuilder.append("&");
		urlBuilder.append("request=");
		urlBuilder.append(request);
		urlBuilder.append("&");
		urlBuilder.append("version=");
		urlBuilder.append(version);
		urlBuilder.append("&");
		urlBuilder.append("format=");
		urlBuilder.append(format);
		urlBuilder.append("&");
		urlBuilder.append("tilematrix=");
		urlBuilder.append(tilematrix);
		urlBuilder.append("&");
		urlBuilder.append("tilecol={tilerow}&tilerow={tilecol}"); //행정망
//		urlBuilder.append("tilecol={tilecol}&tilerow={tilerow}");
		
		this.url = urlBuilder.toString();
		
		UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(this.url);
        UriComponents uriComponents = builder.buildAndExpand(this.rowCol.toMap());
		
        return uriComponents.toUriString();
	}
}
