package ugis.service.vo;

public class CTPageVO<T> {
	private Integer page = 1;
	private Integer size = 20;
	private T data;
	
	public CTPageVO(Integer page, T data) {
		this.page = page;
		this.data = data;
	}
	
	public CTPageVO(Integer page, Integer size, T data) {
		this.page = page;
		this.size = size;
		this.data = data;
	}
	
	public Integer getPage() {
		return page;
	}
	public void setPage(Integer page) {
		this.page = page;
	}
	public Integer getSize() {
		return size;
	}
	public void setSize(Integer size) {
		this.size = size;
	}
	public T getData() {
		return data;
	}
	public void setData(T data) {
		this.data = data;
	}
	
	public Integer getOffset() {
		return (page -1) * size;
	}
	
	@Override
	public String toString() {
		return "PageVO [data=" + data + ", page=" + page + ", size=" + size + "]";
	}
}
