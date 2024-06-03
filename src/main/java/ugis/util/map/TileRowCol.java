package ugis.util.map;

import java.util.HashMap;
import java.util.Map;

public class TileRowCol {
	private String tilerow;
	private String tilecol;

    public TileRowCol(String tilerow, String tilecol) {
        this.tilerow = tilerow;
        this.tilecol = tilecol;
    }

    public Map<String, String> toMap() {
        Map<String, String> map = new HashMap<>();
        map.put("tilerow", tilerow);
        map.put("tilecol", tilecol);
        return map;
    }

	public String getTilerow() {
		return tilerow;
	}

	public void setTilerow(String tilerow) {
		this.tilerow = tilerow;
	}

	public String getTilecol() {
		return tilecol;
	}

	public void setTilecol(String tilecol) {
		this.tilecol = tilecol;
	}
    
    
}
